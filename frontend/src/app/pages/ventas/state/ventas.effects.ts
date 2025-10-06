import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as VentasActions from './ventas.actions';
import { mergeMap, map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Injectable()
export class VentasEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);

  private baseUrl = `${environment.apiUrl}/api/ventas`;

  loadVentas$ = createEffect(() =>
    this.actions$.pipe(
      ofType(VentasActions.loadVentas),
      mergeMap(() =>
        this.http.get<any[]>(this.baseUrl).pipe(
          map(ventas => VentasActions.loadVentasSuccess({ ventas })),
          catchError(error => of(VentasActions.loadVentasFailure({ error })))
        )
      )
    )
  );

  loadVentaDetail$ = createEffect(() =>
    this.actions$.pipe(
      ofType(VentasActions.loadVentaDetail),
      mergeMap(({ id }) =>
        this.http.get<any>(`${this.baseUrl}/${id}`).pipe(
          map(venta => VentasActions.loadVentaDetailSuccess({ venta })),
          catchError(error => of(VentasActions.loadVentaDetailFailure({ error })))
        )
      )
    )
  );
}



