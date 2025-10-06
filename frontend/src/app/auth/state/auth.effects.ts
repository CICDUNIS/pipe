import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as AuthActions from './auth.actions';
import { catchError, map, mergeMap, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthEffects {
  private actions$ = inject(Actions);
  private authService = inject(AuthService);
  private router = inject(Router);

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      tap(action => {
        console.log('Login action dispatched with email:', action.correo);
        if (action.returnUrl) {
          sessionStorage.setItem('returnUrl', action.returnUrl);
        }
      }),
      mergeMap(action =>
        this.authService.login({ correo: action.correo, password: action.password }).pipe(
          tap(response => console.log('Login API response:', { token: response.token ? `${response.token.substring(0, 20)}...` : 'none', user: response.user })),
          map(response =>
            AuthActions.loginSuccess({ token: response.token, user: response.user })
          ),
          catchError(error => {
            console.error('Login API error:', error);
            return of(AuthActions.loginFailure({ error }));
          })
        )
      )
    )
  );

  loginSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginSuccess),
        tap(action => {
          console.log('Login success action dispatched with token:', action.token ? `${action.token.substring(0, 20)}...` : 'none');
          localStorage.setItem('token', action.token);

          if (action.user) {
            console.log('Storing user data:', action.user);
            localStorage.setItem('userData', JSON.stringify(action.user));
          }

          console.log('Token stored in localStorage');

          const storedToken = localStorage.getItem('token');
          console.log('Verified token in localStorage:', storedToken ? `${storedToken.substring(0, 20)}...` : 'none');

          try {
            const headers = {
              'Authorization': `Bearer ${storedToken}`,
              'Content-Type': 'application/json'
            };
            console.log('Headers that will be sent with requests:', headers);
          } catch (e) {
            console.error('Error creating sample headers:', e);
          }

          const returnUrl = sessionStorage.getItem('returnUrl');
          sessionStorage.removeItem('returnUrl');

          if (action.user && action.user.perfilCompleto === 'N') {
            this.router.navigate(['/auth/complete-profile']);
            console.log('Profile not complete. Navigated to /auth/complete-profile');
          } else if (returnUrl) {
            this.router.navigate([returnUrl]);
            console.log('Navigated to returnUrl:', returnUrl);
          } else {
            this.router.navigate(['/pages/medicamentos']);
            console.log('Profile complete. Navigated to /pages/medicamentos');
          }
        })
      ),
    { dispatch: false }
  );

  logout$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logout),
        tap(() => {
          console.log('Logout action dispatched');
          localStorage.removeItem('token');
          console.log('Token removed from localStorage');
          this.router.navigate(['/auth/login']);
          console.log('Navigated to /auth/login');
        })
      ),
    { dispatch: false }
  );

  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      tap(action => console.log('Register action dispatched with email:', action.correo)),
      mergeMap(({ correo, password }) =>
        this.authService.register({ correo, password }).pipe(
          tap(response => console.log('Register API response:', { token: response.token ? `${response.token.substring(0, 20)}...` : 'none', user: response.user })),
          map(response =>
            AuthActions.registerSuccess({ token: response.token, user: response.user })
          ),
          catchError(error => {
            console.error('Register API error:', error);
            return of(AuthActions.registerFailure({ error }));
          })
        )
      )
    )
  );

  registerSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.registerSuccess),
        tap(action => {
          console.log('Register success action dispatched with token:', action.token ? `${action.token.substring(0, 20)}...` : 'none');
          this.router.navigate(['/auth/register-success']);
          console.log('Navigated to /auth/register-success');
        })
      ),
    { dispatch: false }
  );
}




