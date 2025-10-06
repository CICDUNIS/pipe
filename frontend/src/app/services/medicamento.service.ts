import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Medicamento } from '../models/medicamento/medicamento.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MedicamentoService {
  private apiUrl = `${environment.apiUrl}/api/medicamentos`;

  constructor(private http: HttpClient) { }

  getMedicamentos(): Observable<Medicamento[]> {
    return this.http.get<Medicamento[]>(`${this.apiUrl}`);
  }

  getMedicamentoById(id: number): Observable<Medicamento> {
    return this.http.get<Medicamento>(`${this.apiUrl}/${id}`);
  }

  searchMedicamentos(query: string): Observable<Medicamento[]> {
    return this.http.get<Medicamento[]>(`${this.apiUrl}/search?q=${query}`);
  }
}
