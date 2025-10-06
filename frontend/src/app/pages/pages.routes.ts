import { Routes } from '@angular/router';

export const PAGES_ROUTES: Routes = [
  {
    path: 'medicamentos',
    loadChildren: () =>
      import('./medicamentos/medicamentos.routes').then(m => m.MEDICAMENTOS_ROUTES)
  },
  {
    path: 'ventas',
    loadChildren: () =>
      import('./ventas/ventas.routes').then(m => m.VENTAS_ROUTES)
  },
  {
    path: 'recetas',
    loadChildren: () =>
      import('./recetas/recetas.routes').then(m => m.RECETAS_ROUTES)
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('./profile/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: 'carrito',
    loadChildren: () =>
      import('./carrito/carrito.routes').then(m => m.CARRITO_ROUTES)
  }
//  {
//    path: 'search',
//    loadComponent: () =>
//      import('./search/component/search-medicamento.component').then(c => c.SearchMedicamentoComponent)
//
//  }
];


