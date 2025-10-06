import { createReducer, on } from '@ngrx/store';
import * as RecetasActions from './recetas.actions';
import { Receta } from './receta.model';

export interface RecetasState {
  list: Receta[];
  loading: boolean;
  error: any;
}

export const initialState: RecetasState = {
  list: [],
  loading: false,
  error: null
};

export const recetasReducer = createReducer(
  initialState,

  on(RecetasActions.loadRecetas, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(RecetasActions.loadRecetasSuccess, (state, { recetas }) => ({
    ...state,
    list: recetas,
    loading: false
  })),
  on(RecetasActions.loadRecetasFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(RecetasActions.createReceta, (state) => ({
    ...state,
    loading: true
  })),
  on(RecetasActions.createRecetaSuccess, (state, { receta }) => ({
    ...state,
    loading: false,
    list: [...state.list, receta]
  })),
  on(RecetasActions.createRecetaFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(RecetasActions.updateReceta, (state) => ({
    ...state,
    loading: true
  })),
  on(RecetasActions.updateRecetaSuccess, (state, { receta }) => {
    const updatedList = state.list.map(r =>
      r.idReceta === receta.idReceta ? { ...receta } : r
    );
    return {
      ...state,
      loading: false,
      list: updatedList
    };
  }),
  on(RecetasActions.updateRecetaFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(RecetasActions.deleteReceta, (state) => ({
    ...state,
    loading: true
  })),
  on(RecetasActions.deleteRecetaSuccess, (state, { id }) => ({
    ...state,
    loading: false,
    list: state.list.filter(r => r.idReceta !== id)
  })),
  on(RecetasActions.deleteRecetaFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);


