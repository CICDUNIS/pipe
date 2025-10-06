import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { tap, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(credentials: { correo: string; password: string }): Observable<any> {
    console.log('AuthService: Attempting login for user:', credentials.correo);
    console.log('AuthService: API URL:', `${environment.apiUrl}/api/auth/login`);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<any>(`${environment.apiUrl}/api/auth/login`, credentials, { headers }).pipe(
      tap(
        response => {
          console.log('AuthService: Login successful');
          console.log('AuthService: Token received:', response.token ? `${response.token.substring(0, 20)}...` : 'none');

          if (response.token) {
            localStorage.setItem('token', response.token);
          }

          if (response.user) {
            localStorage.setItem('userData', JSON.stringify({
              id: response.user.id,
              nombre: response.user.nombre,
              correo: response.user.correo,
              roles: response.user.roles || []
            }));
            console.log('AuthService: User data stored in localStorage', response.user);
          }
        },
        error => {
          console.error('AuthService: Login failed', error);
          if (error.error) {
            console.error('AuthService: Error details:', error.error);
          }
        }
      )
    );
  }

  register(data: { correo: string; password: string; nombre?: string }): Observable<any> {
    console.log('AuthService: Attempting registration for user:', data.correo);
    console.log('AuthService: API URL:', `${environment.apiUrl}/api/auth/register`);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<any>(`${environment.apiUrl}/api/auth/register`, data, { headers }).pipe(
      tap(
        response => {
          console.log('AuthService: Registration successful');
          console.log('AuthService: Token received:', response.token ? `${response.token.substring(0, 20)}...` : 'none');

          if (response.token) {
            localStorage.setItem('token', response.token);
          }

          if (response.user) {
            localStorage.setItem('userData', JSON.stringify({
              id: response.user.id,
              nombre: response.user.nombre,
              correo: response.user.correo,
              roles: response.user.roles || []
            }));
          }
        },
        error => {
          console.error('AuthService: Registration failed', error);
          if (error.error) {
            console.error('AuthService: Error details:', error.error);
          }
        }
      )
    );
  }

  verifyEmail(token: string): Observable<any> {
    console.log('AuthService: Verifying email with token');

    return this.http.get<any>(`${environment.apiUrl}/api/auth/verify-email?token=${token}`).pipe(
      tap(
        response => {
          console.log('AuthService: Email verification successful');
        },
        error => {
          console.error('AuthService: Email verification failed', error);
          if (error.error) {
            console.error('AuthService: Error details:', error.error);
          }
        }
      )
    );
  }

  completeProfile(data: any): Observable<any> {
    console.log('AuthService: Completing profile for user');

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.getToken()}`
    });

    return this.http.post<any>(`${environment.apiUrl}/api/auth/complete-profile`, data, { headers }).pipe(
      tap(
        response => {
          console.log('AuthService: Profile completion successful');
          if (response.token) {
            localStorage.setItem('token', response.token);
          }

          if (response.user) {
            localStorage.setItem('userData', JSON.stringify({
              id: response.user.id,
              nombre: response.user.nombre,
              correo: response.user.correo,
              roles: response.user.roles || []
            }));
          }
        },
        error => {
          console.error('AuthService: Profile completion failed', error);
          if (error.error) {
            console.error('AuthService: Error details:', error.error);
          }
        }
      )
    );
  }

  logout(): void {
    console.log('AuthService: Logging out user');
    localStorage.removeItem('token');
    localStorage.removeItem('userData');
    console.log('AuthService: Removed token and userData from localStorage');
    console.log('AuthService: Verification - token exists:', !!localStorage.getItem('token'));
  }

  isLoggedIn(): boolean {
    const hasToken = !!localStorage.getItem('token');
    console.log('AuthService: User is logged in:', hasToken);
    return hasToken;
  }

  getToken(): string | null {
    const token = localStorage.getItem('token');
    console.log('AuthService: Getting token:', token ? `${token.substring(0, 20)}...` : 'none');
    return token;
  }

  getUserData(): any {
    const userData = localStorage.getItem('userData');
    if (userData) {
      return JSON.parse(userData);
    }
    return null;
  }

  hasRole(role: string): boolean {
    const userData = this.getUserData();
    if (userData && userData.roles) {
      return userData.roles.includes(role);
    }
    return false;
  }
}



