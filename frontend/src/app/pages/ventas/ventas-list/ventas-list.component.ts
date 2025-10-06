import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { AppState } from '../../../app.state';
import * as VentasActions from '../state/ventas.actions';
import { Observable } from 'rxjs';
import { VentasState } from '../state/ventas.reducer';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-ventas-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ventas-list.component.html',
  styleUrls: ['./ventas-list.component.css']
})
export class VentasListComponent implements OnInit {

  loading$: Observable<boolean>;
  error$: Observable<any>;
  ventas$: Observable<any[]>;

  constructor(private store: Store<AppState>, private router: Router) {
    this.loading$ = this.store.select(s => s.ventas.loading);
    this.error$ = this.store.select(s => s.ventas.error);
    this.ventas$ = this.store.select(s => s.ventas.list);
  }

  ngOnInit(): void {
    this.store.dispatch(VentasActions.loadVentas());
  }

  goDetail(idVenta: number): void {
    this.router.navigate(['/pages/ventas', idVenta]);
  }
}



