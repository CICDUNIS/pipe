import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Medicamento } from './state/medicamento.model';

@Injectable({ providedIn: 'root' })
export class MedicamentoService {
  private baseUrl = `${environment.apiUrl}/api/medicamentos`;

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  findAll(): Observable<Medicamento[]> {
    return this.http.get<Medicamento[]>(this.baseUrl, { headers: this.getAuthHeaders() }).pipe(
      map(medicamentos => medicamentos.map(med => this.transformFromApi(med)))
    );
  }

  create(med: Medicamento): Observable<Medicamento> {
    const apiMed = this.transformToApi(med);
    return this.http.post<Medicamento>(this.baseUrl, apiMed, { headers: this.getAuthHeaders() }).pipe(
      map(response => this.transformFromApi(response))
    );
  }

  update(id: number, med: Medicamento): Observable<Medicamento> {
    const apiMed = this.transformToApi(med);
    return this.http.put<Medicamento>(`${this.baseUrl}/${id}`, apiMed, { headers: this.getAuthHeaders() }).pipe(
      map(response => this.transformFromApi(response))
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  findById(id: number): Observable<Medicamento> {
    console.log(`Fetching medication with ID: ${id}`);
    const headers = this.getAuthHeaders();
    console.log('Request headers:', headers);

    return this.http.get<Medicamento>(`${this.baseUrl}/${id}`, { headers }).pipe(
      map(med => {
        console.log(`Raw medicine data from API:`, med);
        const transformed = this.transformFromApi(med);
        console.log(`Transformed medicine data:`, transformed);
        return transformed;
      })
    );
  }

  search(term: string): Observable<Medicamento[]> {
    return this.http.get<Medicamento[]>(`${this.baseUrl}/search?term=${term}`, { headers: this.getAuthHeaders() }).pipe(
      map(medicamentos => medicamentos.map(med => this.transformFromApi(med)))
    );
  }

  private transformToApi(med: Medicamento): any {
    return {
      ...med,
      requiereReceta: med.requiereReceta === true || med.requiereReceta === 'Y' ? 'Y' : 'N'
    };
  }

  private transformFromApi(med: any): Medicamento {
    return {
      ...med,
      requiereReceta: med.requiereReceta === 'Y' || med.requiereReceta === 'y' || med.requiereReceta === true
    };
  }
}


