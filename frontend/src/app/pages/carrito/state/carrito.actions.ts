import { createAction, props } from '@ngrx/store';
import { CarritoDTO } from '../../../models/carrito/carrito.model';

export const loadCart = createAction('[Carrito] Load Cart');
export const loadCartSuccess = createAction(
  '[Carrito] Load Cart Success',
  props<{ cart: CarritoDTO }>()
);
export const loadCartFailure = createAction(
  '[Carrito] Load Cart Failure',
  props<{ error: any }>()
);

export const addItem = createAction(
  '[Carrito] Add Item',
  props<{ idMedicamento: number; cantidad: number }>()
);
export const addItemSuccess = createAction(
  '[Carrito] Add Item Success',
  props<{ cart: CarritoDTO }>()
);
export const addItemFailure = createAction(
  '[Carrito] Add Item Failure',
  props<{ error: any }>()
);

export const updateItemQuantity = createAction(
  '[Carrito] Update Item Quantity',
  props<{ idMedicamento: number; cantidad: number }>()
);
export const updateItemQuantitySuccess = createAction(
  '[Carrito] Update Item Quantity Success',
  props<{ cart: CarritoDTO }>()
);
export const updateItemQuantityFailure = createAction(
  '[Carrito] Update Item Quantity Failure',
  props<{ error: any }>()
);

export const removeItem = createAction(
  '[Carrito] Remove Item',
  props<{ idMedicamento: number }>()
);
export const removeItemSuccess = createAction(
  '[Carrito] Remove Item Success',
  props<{ cart: CarritoDTO }>()
);
export const removeItemFailure = createAction(
  '[Carrito] Remove Item Failure',
  props<{ error: any }>()
);

export const checkout = createAction('[Carrito] Checkout');
export const checkoutSuccess = createAction(
  '[Carrito] Checkout Success',
  props<{ cart: CarritoDTO }>()
);
export const checkoutFailure = createAction(
  '[Carrito] Checkout Failure',
  props<{ error: any }>()
);

export const clearCart = createAction('[Carrito] Clear Cart');
export const clearCartSuccess = createAction('[Carrito] Clear Cart Success');
export const clearCartFailure = createAction(
  '[Carrito] Clear Cart Failure',
  props<{ error: any }>()
);
