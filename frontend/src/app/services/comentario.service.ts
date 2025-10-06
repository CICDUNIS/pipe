import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Comentario } from '../models/comentario/comentario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiUrl = `${environment.apiUrl}/api/comentarios`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log('ComentarioService - Creating headers with token:', token ? 'present' : 'not present');

    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
      console.log('ComentarioService - Added Authorization header');
    } else {
      console.log('ComentarioService - No token available for headers');
    }

    return headers;
  }

  getComentariosByMedicamento(idMedicamento: number): Observable<Comentario[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Comentario[]>(`${this.apiUrl}/medicamento/${idMedicamento}`, { headers })
      .pipe(
        tap(response => {
          console.log('ComentarioService - Raw API response:', response);
          console.log('ComentarioService - Number of root comments:', response.length);
          response.forEach(comment => {
            console.log(`ComentarioService - Root comment ID ${comment.idComentario} has ${comment.respuestas?.length || 0} replies`);
            if (comment.respuestas) {
              comment.respuestas.forEach(reply =>
                console.log(`ComentarioService - -- Reply ID ${reply.idComentario} to comment ${comment.idComentario}`)
              );
            }
          });
        })
      );
  }

  addComentario(comentario: Comentario): Observable<Comentario> {
    console.log('ComentarioService - Sending comment to API:', comentario);
    console.log('ComentarioService - API URL:', `${this.apiUrl}`);

    const token = localStorage.getItem('token');
    console.log('ComentarioService - Token available:', !!token);
    if (token) {
      console.log('ComentarioService - Token value:', token.substring(0, 10) + '...');
    }

    const headers = this.getAuthHeaders();
    console.log('ComentarioService - Headers:', headers);

    headers.keys().forEach(key => {
      console.log(`ComentarioService - Header ${key}: ${headers.get(key)}`);
    });

    return this.http.post<Comentario>(
      `${this.apiUrl}`,
      comentario,
      { headers }
    ).pipe(
      tap(
        response => console.log('ComentarioService - Comment added successfully:', response),
        error => console.error('ComentarioService - Error adding comment:', error)
      )
    );
  }

  deleteComentario(idComentario: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.apiUrl}/${idComentario}`, { headers });
  }

  private organizarComentarios(comentarios: Comentario[]): Comentario[] {
    if (!comentarios || comentarios.length === 0) {
      console.log('ComentarioService - No comments to organize');
      return [];
    }

    console.log('ComentarioService - Raw comments from API:', comentarios);

    const comentariosMap = new Map<number, Comentario>();
    comentarios.forEach(comentario => {
      comentario.respuestas = [];
      comentariosMap.set(comentario.idComentario!, comentario);
      console.log(`ComentarioService - Mapped comment ID ${comentario.idComentario}, parentId: ${comentario.parentId || 'null'}`);
    });

    const comentariosPrincipales: Comentario[] = [];

    comentarios.forEach(comentario => {
      if (comentario.parentId) {
        const parentComentario = comentariosMap.get(comentario.parentId);
        if (parentComentario) {
          if (!parentComentario.respuestas) {
            parentComentario.respuestas = [];
          }
          parentComentario.respuestas.push(comentario);
          console.log(`ComentarioService - Added comment ID ${comentario.idComentario} as a reply to parent ID ${comentario.parentId}`);
        } else {
          console.warn(`ComentarioService - Parent comment ID ${comentario.parentId} not found for comment ID ${comentario.idComentario}, adding as top-level`);
          comentariosPrincipales.push(comentario);
        }
      } else {
        comentariosPrincipales.push(comentario);
        console.log(`ComentarioService - Added comment ID ${comentario.idComentario} as top-level comment`);
      }
    });

    comentariosPrincipales.sort((a, b) => {
      if (!a.fecha || !b.fecha) return 0;
      return new Date(b.fecha).getTime() - new Date(a.fecha).getTime();
    });

    console.log('ComentarioService - Final top-level comments count:', comentariosPrincipales.length);
    comentariosPrincipales.forEach(comment => {
      console.log(`ComentarioService - Top comment ID ${comment.idComentario} has ${comment.respuestas?.length || 0} replies`);
    });

    console.log('ComentarioService - First few top-level comments structure:',
      JSON.stringify(comentariosPrincipales.slice(0, 2), null, 2));

    return comentariosPrincipales;
  }
}
