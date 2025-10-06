import { Routes } from '@angular/router';
import { UserManagementComponent } from './user-management/user-management.component';
import { AdminGuard } from '../auth/admin.guard';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'user-management',
    pathMatch: 'full'
  },
  {
    path: 'user-management',
    component: UserManagementComponent,
    canActivate: [AdminGuard]
  }
]; 