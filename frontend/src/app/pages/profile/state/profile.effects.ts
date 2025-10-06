import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as ProfileActions from './profile.actions';
import { mergeMap, map, catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { UserProfile } from './profile.model';

@Injectable()
export class ProfileEffects {
  private actions$ = inject(Actions);
  private http = inject(HttpClient);

  private baseUrl = `${environment.apiUrl}/api/users/me`;

  loadProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProfileActions.loadProfile),
      tap(() => console.log('Profile Effects - Loading profile')),
      mergeMap(() => {
        const token = localStorage.getItem('token');
        console.log('Profile Effects - Token from localStorage:', token ? `${token.substring(0, 20)}...` : 'none');

        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        });

        console.log('Profile Effects - Request headers:', headers);

        return this.http.get<UserProfile>(this.baseUrl, { headers }).pipe(
          tap(profile => console.log('Profile Effects - Profile loaded successfully:', profile)),
          map(profile => ProfileActions.loadProfileSuccess({ profile })),
          catchError(error => {
            console.error('Profile Effects - Error loading profile:', error);
            return of(ProfileActions.loadProfileFailure({ error }));
          })
        );
      })
    )
  );

  updateProfile$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ProfileActions.updateProfile),
      tap(({ changes }) => console.log('Profile Effects - Updating profile with changes:', changes)),
      mergeMap(({ changes }) => {
        const token = localStorage.getItem('token');
        console.log('Profile Effects - Token from localStorage:', token ? `${token.substring(0, 20)}...` : 'none');

        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        });

        console.log('Profile Effects - Request headers:', headers);

        return this.http.put<UserProfile>(this.baseUrl, changes, { headers }).pipe(
          tap(profile => console.log('Profile Effects - Profile updated successfully:', profile)),
          map(profile => ProfileActions.updateProfileSuccess({ profile })),
          catchError(error => {
            console.error('Profile Effects - Error updating profile:', error);
            return of(ProfileActions.updateProfileFailure({ error }));
          })
        );
      })
    )
  );
}


