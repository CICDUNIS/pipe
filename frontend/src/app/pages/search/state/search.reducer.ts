import { createReducer, on } from '@ngrx/store';
import * as SearchActions from './search.actions';
import { Medicamento } from '../../medicamentos/state/medicamento.model';

export interface SearchState {
  resultados: Medicamento[];
  loading: boolean;
  error: any;
}

export const initialState: SearchState = {
  resultados: [],
  loading: false,
  error: null
};

export const searchReducer = createReducer(
  initialState,
  on(SearchActions.searchMedicamentos, state => ({
    ...state,
    loading: true,
    error: null
  })),
  on(SearchActions.searchMedicamentosSuccess, (state, { resultados }) => {
    console.log("Result: ", resultados);
    return {
      ...state,
      loading: false,
      resultados
    };
      }),
  on(SearchActions.searchMedicamentosFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);


