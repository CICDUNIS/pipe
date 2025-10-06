import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Usuario } from '../models/usuario/usuario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/auth`;
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage(): void {
    const userJson = localStorage.getItem('currentUser');
    if (userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (e) {
        console.error('Error parsing user from localStorage', e);
        localStorage.removeItem('currentUser');
      }
    }
  }

  login(correo: string, password: string): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/login`, { correo, password })
      .pipe(
        tap(user => {
          localStorage.setItem('currentUser', JSON.stringify(user));
          if (typeof user.token === 'string') {
            localStorage.setItem('token', user.token as string);
          }
          this.currentUserSubject.next(user);
        })
      );
  }

  register(nombre: string, correo: string, password: string): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/register`, { nombre, correo, password });
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }

  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  mockLogin(): void {
    const mockUser: Usuario = {
      id: 1,
      nombre: 'Usuario de Prueba',
      correo: 'test@example.com',
      role: 'USER',
      token: 'mock-token'
    };
    localStorage.setItem('currentUser', JSON.stringify(mockUser));
    localStorage.setItem('token', mockUser.token as string);
    this.currentUserSubject.next(mockUser);
  }
}
