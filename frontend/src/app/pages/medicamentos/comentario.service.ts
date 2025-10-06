import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Comentario } from './state/comentario.model';

@Injectable({ providedIn: 'root' })
export class ComentarioService {
  private baseUrl = `${environment.apiUrl}/api/comentarios`;

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  getByMedicamento(idMedicamento: number): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.baseUrl}/medicamento/${idMedicamento}`);
  }

  create(comentario: Comentario): Observable<Comentario> {
    return this.http.post<Comentario>(this.baseUrl, comentario, { headers: this.getAuthHeaders() });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getAuthHeaders() });
  }
}
