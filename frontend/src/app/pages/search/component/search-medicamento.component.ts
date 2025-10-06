import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { FormsModule } from '@angular/forms';
import * as SearchActions from '../state/search.actions';
import { AppState } from '../../../app.state';
import { Observable, of } from 'rxjs';
import { Medicamento } from '../../medicamentos/state/medicamento.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-search-medicamento',
  standalone: true,
  templateUrl: './search-medicamento.component.html',
  styleUrls: ['./search-medicamento.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class SearchMedicamentoComponent implements OnInit {
  term: string = '';
  resultados$: Observable<Medicamento[]> = of([]);
  loading$: Observable<boolean> = of(false);

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.resultados$ = this.store.select(s => s.search.resultados).pipe(
      map(resultados => resultados || [])
    );

    this.loading$ = this.store.select(s => s.search.loading);
  }

  onSearch(): void {
    if (this.term.trim()) {
      this.store.dispatch(SearchActions.searchMedicamentos({ term: this.term.trim() }));
    } else {
      this.store.dispatch(SearchActions.searchMedicamentosSuccess({ resultados: [] }));
    }
  }
}



