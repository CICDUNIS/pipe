import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as MedicamentoActions from './medicamentos.actions';
import { mergeMap, map, catchError, tap, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Medicamento } from './medicamento.model';
import { MedicamentoService } from '../medicamento.service';
import { Router } from '@angular/router';

@Injectable()
export class MedicamentosEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);
  private medicamentoService = inject(MedicamentoService);
  private router = inject(Router);
  private baseUrl = `${environment.apiUrl}/api/medicamentos`;

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  loadMedicamentos$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MedicamentoActions.loadMedicamentos),
      tap(() => console.log('Medicamentos Effects - Loading medicamentos')),
      mergeMap(() => {
        const headers = this.getAuthHeaders();
        console.log('Medicamentos Effects - Request headers:', headers);

        return this.http.get<Medicamento[]>(`${environment.apiUrl}/api/medicamentos/latest`, { headers }).pipe(
          tap(medicamentos => console.log('Medicamentos Effects - Medicamentos loaded successfully:', medicamentos.length)),
          map(medicamentos =>
            MedicamentoActions.loadMedicamentosSuccess({ medicamentos })
          ),
          catchError(error => {
            console.error('Medicamentos Effects - Error loading medicamentos:', error);
            if (error.status === 403) {
              this.router.navigate(['/login']);
            }
            return of(MedicamentoActions.loadMedicamentosFailure({ error: error.message || 'Error loading medications' }));
          })
        );
      })
    )
  );

  createMedicamento$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MedicamentoActions.createMedicamento),
      tap(({ medicamento }) => console.log('Medicamentos Effects - Creating medicamento:', medicamento)),
      mergeMap(({ medicamento }) => {
        const headers = this.getAuthHeaders();
        console.log('Medicamentos Effects - Request headers:', headers);

        return this.medicamentoService.create(medicamento).pipe(
          tap(created => console.log('Medicamentos Effects - Medicamento created successfully:', created)),
          map(created =>
            MedicamentoActions.createMedicamentoSuccess({ medicamento: created })
          ),
          catchError(error => {
            console.error('Medicamentos Effects - Error creating medicamento:', error);
            if (error.status === 403) {
              this.router.navigate(['/login']);
            }
            return of(MedicamentoActions.createMedicamentoFailure({ error: error.message || 'Error creating medication' }));
          })
        );
      })
    )
  );

  updateMedicamento$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MedicamentoActions.updateMedicamento),
      tap(({ id, changes }) => console.log('Medicamentos Effects - Updating medicamento:', id, changes)),
      mergeMap(({ id, changes }) => {
        const headers = this.getAuthHeaders();
        console.log('Medicamentos Effects - Request headers:', headers);

        return this.medicamentoService.update(id, changes as Medicamento).pipe(
          tap(updated => console.log('Medicamentos Effects - Medicamento updated successfully:', updated)),
          map(updated =>
            MedicamentoActions.updateMedicamentoSuccess({ medicamento: updated })
          ),
          catchError(error => {
            console.error('Medicamentos Effects - Error updating medicamento:', error);
            if (error.status === 403) {
              this.router.navigate(['/login']);
            }
            return of(MedicamentoActions.updateMedicamentoFailure({ error: error.message || 'Error updating medication' }));
          })
        );
      })
    )
  );

  deleteMedicamento$ = createEffect(() =>
    this.actions$.pipe(
      ofType(MedicamentoActions.deleteMedicamento),
      tap(({ id }) => console.log('Medicamentos Effects - Deleting medicamento:', id)),
      mergeMap(({ id }) => {
        const headers = this.getAuthHeaders();
        console.log('Medicamentos Effects - Request headers:', headers);

        return this.medicamentoService.delete(id).pipe(
          tap(() => console.log('Medicamentos Effects - Medicamento deleted successfully:', id)),
          map(() => MedicamentoActions.deleteMedicamentoSuccess({ id })),
          catchError(error => {
            console.error('Medicamentos Effects - Error deleting medicamento:', error);
            if (error.status === 403) {
              this.router.navigate(['/login']);
            }
            return of(MedicamentoActions.deleteMedicamentoFailure({ error: error.message || 'Error deleting medication' }));
          })
        );
      })
    )
  );
}



