import { createReducer, on } from '@ngrx/store';
import * as VentasActions from './ventas.actions';

export interface VentasState {
  list: any[];
  selectedVenta?: any;
  loading: boolean;
  error: any;
}

export const initialState: VentasState = {
  list: [],
  selectedVenta: undefined,
  loading: false,
  error: null
};

export const ventasReducer = createReducer(
  initialState,

  on(VentasActions.loadVentas, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(VentasActions.loadVentasSuccess, (state, { ventas }) => ({
    ...state,
    loading: false,
    list: ventas,
    error: null
  })),
  on(VentasActions.loadVentasFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(VentasActions.loadVentaDetail, (state) => ({
    ...state,
    loading: true,
    error: null,
    selectedVenta: undefined
  })),
  on(VentasActions.loadVentaDetailSuccess, (state, { venta }) => ({
    ...state,
    loading: false,
    selectedVenta: venta
  })),
  on(VentasActions.loadVentaDetailFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);



