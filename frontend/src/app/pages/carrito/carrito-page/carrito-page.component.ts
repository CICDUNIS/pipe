import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AppState } from '../../../app.state';
import { CarritoDTO, CarritoDetalleDTO } from '../../../models/carrito/carrito.model';
import * as CarritoActions from '../state/carrito.actions';

@Component({
  selector: 'app-carrito-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule],
  templateUrl: './carrito-page.component.html',
  styleUrls: ['./carrito-page.component.css']
})
export class CarritoPageComponent implements OnInit {
  cart$: Observable<CarritoDTO | null>;
  loading$: Observable<boolean>;
  error$: Observable<any>;
  checkoutSuccess$: Observable<boolean>;

  constructor(private store: Store<AppState>) {
    this.cart$ = this.store.select(state => state.carrito.cart);
    this.loading$ = this.store.select(state => state.carrito.loading);
    this.error$ = this.store.select(state => state.carrito.error);
    this.checkoutSuccess$ = this.store.select(state => state.carrito.checkoutSuccess);

    console.log('CarritoPageComponent: initialized');
  }

  ngOnInit(): void {
    console.log('CarritoPageComponent: Loading cart');
    this.store.dispatch(CarritoActions.loadCart());
  }

  updateQuantity(item: CarritoDetalleDTO, cantidad: number): void {
    console.log(`CarritoPageComponent: Updating quantity for ${item.nombreMedicamento} to ${cantidad}`);
    this.store.dispatch(
      CarritoActions.updateItemQuantity({
        idMedicamento: item.idMedicamento,
        cantidad: cantidad
      })
    );
  }

  removeItem(item: CarritoDetalleDTO): void {
    console.log(`CarritoPageComponent: Removing item ${item.nombreMedicamento}`);
    this.store.dispatch(
      CarritoActions.removeItem({ idMedicamento: item.idMedicamento })
    );
  }

  clearCart(): void {
    console.log('CarritoPageComponent: Clearing cart');
    this.store.dispatch(CarritoActions.clearCart());
  }

  checkout(): void {
    console.log('CarritoPageComponent: Checking out');
    this.store.dispatch(CarritoActions.checkout());
  }
} 