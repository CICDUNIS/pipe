import { createAction, props } from '@ngrx/store';

export const login = createAction(
  '[Auth] Login',
  props<{ correo: string; password: string; returnUrl?: string | null }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ token: string; user?: any }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: any }>()
);

export const logout = createAction('[Auth] Logout');

export const register = createAction(
  '[Auth] Register',
  props<{ correo: string; password: string; nombre?: string }>()
);

export const registerSuccess = createAction(
'[Auth] Register Success',
  props<{ token: string, user?: any }>()
);

export const registerFailure = createAction(
'[Auth] Register Failure',
  props<{ error: any }>()
);



