import { createReducer, on } from '@ngrx/store';
import * as MedicamentoActions from './medicamentos.actions';
import { Medicamento } from './medicamento.model';

export interface MedicamentoState {
  list: Medicamento[];
  loading: boolean;
  error: any;
}

export const initialState: MedicamentoState = {
  list: [],
  loading: false,
  error: null,
};

export const medicamentosReducer = createReducer(
  initialState,
  on(MedicamentoActions.loadMedicamentos, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(MedicamentoActions.loadMedicamentosSuccess, (state, { medicamentos }) => ({
    ...state,
    loading: false,
    list: medicamentos,
  })),
  on(MedicamentoActions.loadMedicamentosFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(MedicamentoActions.createMedicamento, (state) => ({
    ...state,
    loading: true
  })),
  on(MedicamentoActions.createMedicamentoSuccess, (state, { medicamento }) => {
    console.log("este es el med", medicamento);
    return {
      ...state,
      loading: false,
      list: [...state.list, medicamento]
    };
      }),
  on(MedicamentoActions.createMedicamentoFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(MedicamentoActions.updateMedicamento, (state) => ({
    ...state,
    loading: true
  })),
  on(MedicamentoActions.updateMedicamentoSuccess, (state, { medicamento }) => {
    const updatedList = state.list.map(m =>
      m.idMedicamento === medicamento.idMedicamento ? { ...medicamento } : m
    );
    return {
      ...state,
      loading: false,
      list: updatedList
    };
  }),
  on(MedicamentoActions.updateMedicamentoFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(MedicamentoActions.deleteMedicamento, (state) => ({
    ...state,
    loading: true
  })),
  on(MedicamentoActions.deleteMedicamentoSuccess, (state, { id }) => ({
    ...state,
    loading: false,
    list: state.list.filter(m => m.idMedicamento !== id)
  })),
  on(MedicamentoActions.deleteMedicamentoFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);



