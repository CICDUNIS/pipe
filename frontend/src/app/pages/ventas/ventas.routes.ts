import { Routes } from '@angular/router';
import { VentasListComponent } from './ventas-list/ventas-list.component';
import { VentaDetailComponent } from './venta-detail/venta-detail.component';

export const VENTAS_ROUTES: Routes = [
  { path: '', component: VentasListComponent },
  { path: ':id', component: VentaDetailComponent }
];



