import { createReducer, on } from '@ngrx/store';
import * as ProfileActions from './profile.actions';
import { UserProfile } from './profile.model';

export interface ProfileState {
  user?: UserProfile;
  loading: boolean;
  error: any;
}

export const initialState: ProfileState = {
  user: undefined,
  loading: false,
  error: null
};

export const profileReducer = createReducer(
  initialState,
  on(ProfileActions.loadProfile, state => ({
    ...state,
    loading: true,
    error: null
  })),
  on(ProfileActions.loadProfileSuccess, (state, { profile }) => ({
    ...state,
    user: profile,
    loading: false
  })),
  on(ProfileActions.loadProfileFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  on(ProfileActions.updateProfile, state => ({
    ...state,
    loading: true
  })),
  on(ProfileActions.updateProfileSuccess, (state, { profile }) => ({
    ...state,
    user: profile,
    loading: false
  })),
  on(ProfileActions.updateProfileFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);


