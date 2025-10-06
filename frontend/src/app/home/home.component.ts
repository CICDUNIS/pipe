import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Medicamento } from '../pages/medicamentos/state/medicamento.model';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [CommonModule, RouterModule]
})
export class HomeComponent implements OnInit {
  medicamentos$: Observable<Medicamento[]> = of([]);
  loading$: Observable<boolean> = of(true);
  error: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadLatestMedicamentos();
  }

  private loadLatestMedicamentos(): void {
    this.medicamentos$ = this.http.get<Medicamento[]>(`${environment.apiUrl}/api/medicamentos/latest`).pipe(
      map(medicamentos => {
        console.log('Latest medications loaded:', medicamentos);
        return medicamentos.map(med => this.transformFromApi(med));
      }),
      catchError(error => {
        console.error('Error loading latest medications:', error);
        this.error = 'No se pudieron cargar los medicamentos. Por favor, intente más tarde.';
        return of([]);
      })
    );

    setTimeout(() => {
      this.loading$ = of(false);
    }, 500);
  }

  private transformFromApi(med: any): Medicamento {
    return {
      ...med,
      requiereReceta: med.requiereReceta === 'Y' || med.requiereReceta === 'y' || med.requiereReceta === true
    };
  }
}



