import { createAction, props } from '@ngrx/store';
import { Medicamento } from '../../medicamentos/state/medicamento.model';

export const searchMedicamentos = createAction(
  '[Search] Search Medicamentos',
  props<{ term: string }>()
);

export const searchMedicamentosSuccess = createAction(
  '[Search] Search Medicamentos Success',
  props<{ resultados: Medicamento[] }>()
);

export const searchMedicamentosFailure = createAction(
  '[Search] Search Medicamentos Failure',
  props<{ error: any }>()
);


