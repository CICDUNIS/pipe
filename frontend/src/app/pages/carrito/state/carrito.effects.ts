import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import * as CarritoActions from './carrito.actions';
import { CarritoService } from '../../../services/carrito.service';
import { Router } from '@angular/router';

@Injectable()
export class CarritoEffects {
  private actions$ = inject(Actions);
  private carritoService = inject(CarritoService);
  private router = inject(Router);

  loadCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.loadCart),
      switchMap(() => {
        console.log('CarritoEffects: Loading cart');
        return this.carritoService.getActiveCart().pipe(
          map(cart => {
            console.log('CarritoEffects: Cart loaded successfully', cart);
            return CarritoActions.loadCartSuccess({ cart });
          }),
          catchError(error => {
            console.error('CarritoEffects: Error loading cart', error);
            return of(CarritoActions.loadCartFailure({ error: error.message }));
          })
        );
      })
    )
  );

  addItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.addItem),
      mergeMap(({ idMedicamento, cantidad }) => {
        console.log(`CarritoEffects: Adding item - medicamento ID: ${idMedicamento}, cantidad: ${cantidad}`);
        return this.carritoService.addItem(idMedicamento, cantidad).pipe(
          map(cart => {
            console.log('CarritoEffects: Item added successfully', cart);
            return CarritoActions.addItemSuccess({ cart });
          }),
          catchError(error => {
            console.error('CarritoEffects: Error adding item', error);
            return of(CarritoActions.addItemFailure({ error: error.message }));
          })
        );
      })
    )
  );

  updateItemQuantity$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.updateItemQuantity),
      mergeMap(({ idMedicamento, cantidad }) => {
        console.log(`CarritoEffects: Updating item quantity - medicamento ID: ${idMedicamento}, cantidad: ${cantidad}`);
        return this.carritoService.updateItemQuantity(idMedicamento, cantidad).pipe(
          map(cart => {
            console.log('CarritoEffects: Item quantity updated successfully', cart);
            return CarritoActions.updateItemQuantitySuccess({ cart });
          }),
          catchError(error => {
            console.error('CarritoEffects: Error updating item quantity', error);
            return of(CarritoActions.updateItemQuantityFailure({ error: error.message }));
          })
        );
      })
    )
  );

  removeItem$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.removeItem),
      mergeMap(({ idMedicamento }) => {
        console.log(`CarritoEffects: Removing item - medicamento ID: ${idMedicamento}`);
        return this.carritoService.removeItem(idMedicamento).pipe(
          map(cart => {
            console.log('CarritoEffects: Item removed successfully', cart);
            return CarritoActions.removeItemSuccess({ cart });
          }),
          catchError(error => {
            console.error('CarritoEffects: Error removing item', error);
            return of(CarritoActions.removeItemFailure({ error: error.message }));
          })
        );
      })
    )
  );

  checkout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.checkout),
      switchMap(() => {
        console.log('CarritoEffects: Checking out cart');
        return this.carritoService.checkout().pipe(
          map(cart => {
            console.log('CarritoEffects: Checkout successful', cart);
            return CarritoActions.checkoutSuccess({ cart });
          }),
          catchError(error => {
            console.error('CarritoEffects: Checkout failed', error);
            return of(CarritoActions.checkoutFailure({ error: error.message }));
          })
        );
      })
    )
  );

  checkoutSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(CarritoActions.checkoutSuccess),
        tap(() => {
          console.log('CarritoEffects: Navigating to checkout success page');
          this.router.navigate(['/checkout-success']);
        })
      ),
    { dispatch: false }
  );

  clearCart$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CarritoActions.clearCart),
      switchMap(() => {
        console.log('CarritoEffects: Clearing cart');
        return this.carritoService.clearCart().pipe(
          map(() => {
            console.log('CarritoEffects: Cart cleared successfully');
            return CarritoActions.clearCartSuccess();
          }),
          catchError(error => {
            console.error('CarritoEffects: Error clearing cart', error);
            return of(CarritoActions.clearCartFailure({ error: error.message }));
          })
        );
      })
    )
  );
} 