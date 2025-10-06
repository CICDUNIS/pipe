import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MedicamentoListComponent } from './medicamento-list/medicamento-list.component';
import { MedicamentoFormComponent } from './medicamento-form/medicamento-form.component';
import { AuthGuard } from '../../auth/auth.guard';

const routes: Routes = [
  { path: '', component: MedicamentoListComponent, canActivate: [AuthGuard] },
  { path: 'create', component: MedicamentoFormComponent, canActivate: [AuthGuard] },
  { path: 'edit/:id', component: MedicamentoFormComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MedicamentosRoutingModule { }



