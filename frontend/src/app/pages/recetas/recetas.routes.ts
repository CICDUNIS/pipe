import { Routes } from '@angular/router';
import { RecetaListComponent } from './receta-list/receta-list.component';
import { RecetaFormComponent } from './receta-form/receta-form.component';

export const RECETAS_ROUTES: Routes = [
  { path: '', component: RecetaListComponent },
  { path: 'create', component: RecetaFormComponent },
  { path: 'edit/:id', component: RecetaFormComponent }
];


