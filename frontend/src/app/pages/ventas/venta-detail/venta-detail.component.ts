import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { AppState } from '../../../app.state';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { of, Observable } from 'rxjs';
import * as VentasActions from '../state/ventas.actions';
import { Venta } from '../state/venta.model';

@Component({
  selector: 'app-venta-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './venta-detail.component.html',
  styleUrls: ['./venta-detail.component.css']
})
export class VentaDetailComponent implements OnInit {

  loading$: Observable<boolean> | undefined;
  error$: Observable<string | null> | undefined;
  venta$: Observable<Venta | null> | undefined;

  constructor(
    private store: Store<AppState>,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loading$ = this.store.select(s => s.ventas.loading);
    this.error$ = this.store.select(s => s.ventas.error);
    this.venta$ = this.store.select(s => s.ventas.selectedVenta);

    this.route.paramMap
      .pipe(
        switchMap(params => {
          const idParam = params.get('id');
          if (!idParam) return of(null);
          const idVenta = +idParam;
          this.store.dispatch(VentasActions.loadVentaDetail({ id: idVenta }));
          return of(null);
        })
      )
      .subscribe();
  }
}



