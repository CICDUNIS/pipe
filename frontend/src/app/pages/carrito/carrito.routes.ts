import { Routes } from '@angular/router';

export const CARRITO_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./carrito-page/carrito-page.component').then(m => m.CarritoPageComponent)
  },
  {
    path: 'checkout-success',
    loadComponent: () => import('./checkout-success/checkout-success.component').then(m => m.CheckoutSuccessComponent)
  }
]; 