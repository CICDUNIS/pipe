import { createReducer, on } from "@ngrx/store";
import * as CarritoActions from "./carrito.actions";
import { CarritoDTO } from "../../../models/carrito/carrito.model";

export interface CarritoState {
  cart: CarritoDTO | null;
  loading: boolean;
  error: any;
  checkoutSuccess: boolean;
}

const initialState: CarritoState = {
  cart: null,
  loading: false,
  error: null,
  checkoutSuccess: false,
};

export const carritoReducer = createReducer(
  initialState,
  on(CarritoActions.loadCart, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(CarritoActions.loadCartSuccess, (state, { cart }) => ({
    ...state,
    cart,
    loading: false,
    error: null,
  })),
  on(CarritoActions.loadCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  on(CarritoActions.addItem, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(CarritoActions.addItemSuccess, (state, { cart }) => ({
    ...state,
    cart,
    loading: false,
    error: null,
  })),
  on(CarritoActions.addItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  on(CarritoActions.updateItemQuantity, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(CarritoActions.updateItemQuantitySuccess, (state, { cart }) => ({
    ...state,
    cart,
    loading: false,
    error: null,
  })),
  on(CarritoActions.updateItemQuantityFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  on(CarritoActions.removeItem, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(CarritoActions.removeItemSuccess, (state, { cart }) => ({
    ...state,
    cart,
    loading: false,
    error: null,
  })),
  on(CarritoActions.removeItemFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),

  on(CarritoActions.checkout, (state) => ({
    ...state,
    loading: true,
    error: null,
    checkoutSuccess: false,
  })),
  on(CarritoActions.checkoutSuccess, (state, { cart }) => ({
    ...state,
    cart,
    loading: false,
    error: null,
    checkoutSuccess: true,
  })),
  on(CarritoActions.checkoutFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
    checkoutSuccess: false,
  })),

  on(CarritoActions.clearCart, (state) => ({
    ...state,
    loading: true,
    error: null,
  })),
  on(CarritoActions.clearCartSuccess, (state) => ({
    ...state,
    cart: null,
    loading: false,
    error: null,
  })),
  on(CarritoActions.clearCartFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error,
  })),
);
