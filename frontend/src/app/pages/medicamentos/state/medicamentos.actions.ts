import { createAction, props } from '@ngrx/store';
import { Medicamento } from './medicamento.model';

export const loadMedicamentos = createAction('[Medicamentos] Load Medicamentos');
export const loadMedicamentosSuccess = createAction(
  '[Medicamentos] Load Medicamentos Success',
  props<{ medicamentos: Medicamento[] }>()
);
export const loadMedicamentosFailure = createAction(
  '[Medicamentos] Load Medicamentos Failure',
  props<{ error: any }>()
);

export const createMedicamento = createAction(
  '[Medicamentos] Create Medicamento',
  props<{ medicamento: Medicamento }>()
);
export const createMedicamentoSuccess = createAction(
  '[Medicamentos] Create Medicamento Success',
  props<{ medicamento: Medicamento }>()
);
export const createMedicamentoFailure = createAction(
  '[Medicamentos] Create Medicamento Failure',
  props<{ error: any }>()
);

export const updateMedicamento = createAction(
  '[Medicamentos] Update Medicamento',
  props<{ id: number; changes: Partial<Medicamento> }>()
);
export const updateMedicamentoSuccess = createAction(
  '[Medicamentos] Update Medicamento Success',
  props<{ medicamento: Medicamento }>()
);
export const updateMedicamentoFailure = createAction(
  '[Medicamentos] Update Medicamento Failure',
  props<{ error: any }>()
);

export const deleteMedicamento = createAction(
  '[Medicamentos] Delete Medicamento',
  props<{ id: number }>()
);
export const deleteMedicamentoSuccess = createAction(
  '[Medicamentos] Delete Medicamento Success',
  props<{ id: number }>()
);
export const deleteMedicamentoFailure = createAction(
  '[Medicamentos] Delete Medicamento Failure',
  props<{ error: any }>()
);




