import { createAction, props } from '@ngrx/store';
import { UserProfile } from './profile.model';

export const loadProfile = createAction('[Profile] Load My Profile');
export const loadProfileSuccess = createAction(
  '[Profile] Load My Profile Success',
  props<{ profile: UserProfile }>()
);
export const loadProfileFailure = createAction(
  '[Profile] Load My Profile Failure',
  props<{ error: any }>()
);

export const updateProfile = createAction(
  '[Profile] Update My Profile',
  props<{ changes: Partial<UserProfile> }>()
);
export const updateProfileSuccess = createAction(
  '[Profile] Update My Profile Success',
  props<{ profile: UserProfile }>()
);
export const updateProfileFailure = createAction(
  '[Profile] Update My Profile Failure',
  props<{ error: any }>()
);


