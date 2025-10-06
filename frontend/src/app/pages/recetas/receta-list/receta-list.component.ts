import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Receta } from '../state/receta.model';
import { AppState } from '../../../app.state';
import * as RecetasActions from '../state/recetas.actions';

@Component({
  selector: 'app-receta-list',
  standalone: true,
  templateUrl: './receta-list.component.html',
  styleUrls: ['./receta-list.component.css'],
  imports: [CommonModule, RouterModule]
})
export class RecetaListComponent implements OnInit {
  recetas$: Observable<Receta[]>;
  loading$: Observable<boolean>;

  constructor(private store: Store<AppState>, private router: Router) {
    this.recetas$ = this.store.select(state => state.recetas.list);
    this.loading$ = this.store.select(state => state.recetas.loading);
  }

  ngOnInit(): void {
    this.store.dispatch(RecetasActions.loadRecetas());
  }

  onDelete(id: number): void {
    if (confirm('¿Eliminar esta receta?')) {
      this.store.dispatch(RecetasActions.deleteReceta({ id }));
    }
  }

  goEdit(id: number) {
    this.router.navigate(['/pages/recetas/edit', id]);
  }
}


