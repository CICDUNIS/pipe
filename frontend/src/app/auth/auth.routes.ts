import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { provideHttpClient } from '@angular/common/http';

export const AUTH_ROUTES: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    loadComponent: () => import('./register/register.component').then(c => c.RegisterComponent)
  },
  {
    path: 'register-success',
    loadComponent: () => import('./register-success/register-success.component').then(c => c.RegisterSuccessComponent)
  },
  {
    path: 'verify-email',
    loadComponent: () => import('./verify-email/verify-email.component').then(c => c.VerifyEmailComponent)
  },
  {
    path: 'complete-profile',
    loadComponent: () => import('./complete-profile/complete-profile.component').then(c => c.CompleteProfileComponent)
  },
  {
    path: 'search',
    loadComponent: () => import('../pages/search/component/search-medicamento.component').then(c => c.SearchMedicamentoComponent)
  },
];


