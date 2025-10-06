import {
  Component,
  OnInit,
  OnDestroy,
  ElementRef,
  ViewChild,
  Renderer2,
} from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Store } from "@ngrx/store";
import { Observable, map, distinctUntilChanged, Subscription } from "rxjs";
import { AppState } from "../../../app.state";
import {
  CarritoDTO,
  CarritoDetalleDTO,
} from "../../../models/carrito/carrito.model";
import * as CarritoActions from "../../../pages/carrito/state/carrito.actions";

@Component({
  selector: "app-cart-widget",
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: "./cart-widget.component.html",
  styleUrls: ["./cart-widget.component.css"],
})
export class CartWidgetComponent implements OnInit, OnDestroy {
  cart$: Observable<CarritoDTO | null>;
  cartItemCount$: Observable<number>;
  cartTotal$: Observable<number>;
  cartItems$: Observable<CarritoDetalleDTO[]>;

  @ViewChild("badgeElement") badgeElement!: ElementRef;
  private countSubscription: Subscription | null = null;
  private previousCount = 0;

  constructor(
    private store: Store<AppState>,
    private renderer: Renderer2,
  ) {
    this.cart$ = this.store.select((state) => state.carrito.cart);

    this.cartItemCount$ = this.cart$.pipe(
      map((cart) => this.getCartItemCount(cart)),
    );

    this.cartTotal$ = this.cart$.pipe(map((cart) => cart?.total || 0));

    this.cartItems$ = this.cart$.pipe(map((cart) => cart?.items || []));

    console.log("CartWidgetComponent: initialized");
  }

  ngOnInit(): void {
    console.log("CartWidgetComponent: Loading cart");
    this.store.dispatch(CarritoActions.loadCart());

    this.countSubscription = this.cartItemCount$
      .pipe(distinctUntilChanged())
      .subscribe((count) => {
        if (count > this.previousCount && this.previousCount > 0) {
          this.animateBadge();
        }
        this.previousCount = count;
      });
  }

  ngOnDestroy(): void {
    if (this.countSubscription) {
      this.countSubscription.unsubscribe();
    }
  }

  private animateBadge(): void {
    if (this.badgeElement) {
      this.renderer.addClass(this.badgeElement.nativeElement, "badge-shake");

      setTimeout(() => {
        if (this.badgeElement) {
          this.renderer.removeClass(
            this.badgeElement.nativeElement,
            "badge-shake",
          );
        }
      }, 820);
    }
  }

  private getCartItemCount(cart: CarritoDTO | null): number {
    if (!cart || !cart.items) {
      return 0;
    }

    return cart.items.reduce((total, item) => total + item.cantidad, 0);
  }

  removeItem(idMedicamento: number): void {
    this.store.dispatch(CarritoActions.removeItem({ idMedicamento }));
  }
}
