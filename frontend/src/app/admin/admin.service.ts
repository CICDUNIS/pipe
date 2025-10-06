import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(private http: HttpClient) {}
  
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
  
  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/api/admin/users`, {
      headers: this.getHeaders()
    });
  }
  
  getAllRoles(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/api/admin/roles`, {
      headers: this.getHeaders()
    });
  }
  
  filterUsers(email?: string, fromDate?: string | null, toDate?: string | null, role?: string): Observable<any[]> {
    let params = new HttpParams();
    
    if (email) {
      params = params.set('email', email);
    }
    
    if (fromDate) {
      params = params.set('fromDate', fromDate);
    }
    
    if (toDate) {
      params = params.set('toDate', toDate);
    }
    
    if (role) {
      params = params.set('role', role);
    }
    
    return this.http.get<any[]>(`${environment.apiUrl}/api/admin/users/filter`, {
      headers: this.getHeaders(),
      params: params
    });
  }
  
  activateUser(userId: number, roleId: number): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/admin/users/${userId}/activate`,
      { roleId: roleId },
      { headers: this.getHeaders() }
    );
  }
  
  deactivateUser(userId: number): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/admin/users/${userId}/deactivate`,
      {},
      { headers: this.getHeaders() }
    );
  }
  
  assignRole(userId: number, roleIds: number[]): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/api/admin/users/${userId}/roles`,
      { roleIds: roleIds },
      { headers: this.getHeaders() }
    );
  }
  
  removeRole(userId: number, roleId: number): Observable<any> {
    return this.http.delete<any>(
      `${environment.apiUrl}/api/admin/users/${userId}/roles/${roleId}`,
      { headers: this.getHeaders() }
    );
  }
} 