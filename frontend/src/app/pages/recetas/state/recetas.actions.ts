import { createAction, props } from '@ngrx/store';
import { Receta } from './receta.model';

export const loadRecetas = createAction('[Recetas] Load Recetas');
export const loadRecetasSuccess = createAction(
  '[Recetas] Load Recetas Success',
  props<{ recetas: Receta[] }>()
);
export const loadRecetasFailure = createAction(
  '[Recetas] Load Recetas Failure',
  props<{ error: any }>()
);

export const createReceta = createAction(
  '[Recetas] Create Receta',
  props<{ receta: Receta }>()
);
export const createRecetaSuccess = createAction(
  '[Recetas] Create Receta Success',
  props<{ receta: Receta }>()
);
export const createRecetaFailure = createAction(
  '[Recetas] Create Receta Failure',
  props<{ error: any }>()
);

export const updateReceta = createAction(
  '[Recetas] Update Receta',
  props<{ id: number; changes: Partial<Receta> }>()
);
export const updateRecetaSuccess = createAction(
  '[Recetas] Update Receta Success',
  props<{ receta: Receta }>()
);
export const updateRecetaFailure = createAction(
  '[Recetas] Update Receta Failure',
  props<{ error: any }>()
);

export const deleteReceta = createAction(
  '[Recetas] Delete Receta',
  props<{ id: number }>()
);
export const deleteRecetaSuccess = createAction(
  '[Recetas] Delete Receta Success',
  props<{ id: number }>()
);
export const deleteRecetaFailure = createAction(
  '[Recetas] Delete Receta Failure',
  props<{ error: any }>()
);


