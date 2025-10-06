import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as RecetasActions from './recetas.actions';
import { mergeMap, map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Receta } from './receta.model';

@Injectable()
export class RecetasEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);

  private baseUrl = `${environment.apiUrl}/api/recetas`;

  loadRecetas$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RecetasActions.loadRecetas),
      mergeMap(() =>
        this.http.get<Receta[]>(this.baseUrl).pipe(
          map(recetas => RecetasActions.loadRecetasSuccess({ recetas })),
          catchError(error => of(RecetasActions.loadRecetasFailure({ error })))
        )
      )
    )
  );

  createReceta$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RecetasActions.createReceta),
      mergeMap(({ receta }) =>
        this.http.post<Receta>(this.baseUrl, receta).pipe(
          map(created => RecetasActions.createRecetaSuccess({ receta: created })),
          catchError(error => of(RecetasActions.createRecetaFailure({ error })))
        )
      )
    )
  );

  updateReceta$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RecetasActions.updateReceta),
      mergeMap(({ id, changes }) =>
        this.http.put<Receta>(`${this.baseUrl}/${id}`, changes).pipe(
          map(updated => RecetasActions.updateRecetaSuccess({ receta: updated })),
          catchError(error => of(RecetasActions.updateRecetaFailure({ error })))
        )
      )
    )
  );

  deleteReceta$ = createEffect(() =>
    this.actions$.pipe(
      ofType(RecetasActions.deleteReceta),
      mergeMap(({ id }) =>
        this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
          map(() => RecetasActions.deleteRecetaSuccess({ id })),
          catchError(error => of(RecetasActions.deleteRecetaFailure({ error })))
        )
      )
    )
  );
}


