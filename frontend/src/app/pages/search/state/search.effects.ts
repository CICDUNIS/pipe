import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as SearchActions from './search.actions';
import { HttpClient } from '@angular/common/http';
import { mergeMap, map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Medicamento } from '../../medicamentos/state/medicamento.model';

@Injectable()
export class SearchEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/medicamentos/search`;

  searchMedicamentos$ = createEffect(() =>
    this.actions$.pipe(
      ofType(SearchActions.searchMedicamentos),
      mergeMap(({ term }) =>
        this.http.get<Medicamento[]>(`${this.baseUrl}?term=${term}`).pipe(
          map(resultados =>
            SearchActions.searchMedicamentosSuccess({ resultados })
          ),
          catchError(error =>
            of(SearchActions.searchMedicamentosFailure({ error }))
          )
        )
      )
    )
  );
}


