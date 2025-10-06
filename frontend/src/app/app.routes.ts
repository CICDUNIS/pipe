import { Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component').then(c => c.HomeComponent),
    pathMatch: 'full'
  },
  {
    path: 'search',
    loadComponent: () => import('./pages/search/component/search-medicamento.component').then(c => c.SearchMedicamentoComponent),
  },
  {
    path: 'medicamento/:id',
    loadComponent: () => import('./pages/medicamento/medicamento.component').then(c => c.MedicamentoComponent),
  },
  {
    path: 'auth',
    loadChildren: () =>
      import('./auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'admin',
    loadChildren: () =>
      import('./admin/admin.routes').then(m => m.ADMIN_ROUTES),
    canActivate: [AuthGuard]
  },
  {
    path: 'pages',
    loadChildren: () =>
      import('./pages/pages.routes').then(m => m.PAGES_ROUTES),
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    redirectTo: '/'
  }
];



