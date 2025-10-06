import { createAction, props } from '@ngrx/store';

export const loadVentas = createAction('[Ventas] Load Ventas');
export const loadVentasSuccess = createAction(
  '[Ventas] Load Ventas Success',
  props<{ ventas: any[] }>()
);
export const loadVentasFailure = createAction(
  '[Ventas] Load Ventas Failure',
  props<{ error: any }>()
);

export const loadVentaDetail = createAction(
  '[Ventas] Load Venta Detail',
  props<{ id: number }>()
);
export const loadVentaDetailSuccess = createAction(
  '[Ventas] Load Venta Detail Success',
  props<{ venta: any }>()
);
export const loadVentaDetailFailure = createAction(
  '[Ventas] Load Venta Detail Failure',
  props<{ error: any }>()
);


