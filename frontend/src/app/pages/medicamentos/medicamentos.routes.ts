import { Routes } from '@angular/router';
import { MedicamentoListComponent } from './medicamento-list/medicamento-list.component';
import { MedicamentoFormComponent } from './medicamento-form/medicamento-form.component';

export const MEDICAMENTOS_ROUTES: Routes = [
  { path: '', component: MedicamentoListComponent },
  { path: 'create', component: MedicamentoFormComponent },
  { path: 'edit/:id', component: MedicamentoFormComponent }
];



