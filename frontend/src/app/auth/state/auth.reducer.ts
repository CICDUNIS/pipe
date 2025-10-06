import { createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';

export interface AuthState {
  token: string | null;
  user: any | null;
  error: any | null;
}

export const initialAuthState: AuthState = {
  token: null,
  user: null,
  error: null,
};

export const authReducer = createReducer(
  initialAuthState,
  on(AuthActions.loginSuccess, (state, { token, user }) => ({
    ...state,
    token,
    user,
    error: null
  })),
  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(AuthActions.registerSuccess, (state, { token, user }) => ({
    ...state,
    token,
    user,
    error: null
  })),
  on(AuthActions.registerFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(AuthActions.logout, () => initialAuthState)
);



