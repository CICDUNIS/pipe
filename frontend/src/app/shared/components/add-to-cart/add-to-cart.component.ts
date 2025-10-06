import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../../app.state';
import * as CarritoActions from '../../../pages/carrito/state/carrito.actions';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-add-to-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './add-to-cart.component.html',
  styleUrls: ['./add-to-cart.component.css']
})
export class AddToCartComponent implements OnInit {
  @Input() medicamentoId: number = 0;
  @Input() requiereReceta: string | boolean = false;
  @Input() inStock: boolean = true;
  @Input() stock: number = 0;
  @Input() showQuantity: boolean = true;
  @Input() buttonClass: string = 'btn-primary';
  @Input() buttonText: string = 'Agregar al Carrito';
  @Input() buttonSize: string = '';

  cantidad: number = 1;
  loading = false;
  error: string | null = null;
  success = false;
  isAuthenticated = false;
  authError = false;

  constructor(
    private store: Store<AppState>,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isLoggedIn();
    console.log('AddToCartComponent: Authentication status:', this.isAuthenticated);

    this.store.select(state => state.carrito.loading).subscribe(loading => {
      this.loading = loading;
    });

    this.store.select(state => state.carrito.error).subscribe(error => {
      this.error = error;
      
      if (error) {
        this.authError = error.includes('sesión') || 
                         error.includes('autorizado') || 
                         error.includes('acceso denegado') ||
                         error.toLowerCase().includes('auth');
        
        console.log('AddToCartComponent: Error detected:', error);
        console.log('AddToCartComponent: Is auth error:', this.authError);
        
        setTimeout(() => {
          this.error = null;
          this.authError = false;
        }, 5000);
      }
    });
  }

  decrementQuantity(): void {
    if (this.cantidad > 1) {
      this.cantidad--;
    }
  }

  incrementQuantity(): void {
    if (this.cantidad < this.stock) {
      this.cantidad++;
    }
  }

  addToCart(): void {
    if (!this.isAuthenticated) {
      this.error = 'Debes iniciar sesión para agregar productos al carrito.';
      this.authError = true;
      return;
    }

    if (this.requiereReceta === 'Y' || this.requiereReceta === true) {
      this.error = 'Este medicamento requiere receta médica y no puede ser comprado directamente.';
      return;
    }

    if (this.cantidad <= 0) {
      this.error = 'La cantidad debe ser mayor a 0.';
      return;
    }

    if (this.cantidad > this.stock) {
      this.error = `Solo hay ${this.stock} unidades disponibles.`;
      return;
    }

    console.log(`Adding medicamento ID ${this.medicamentoId} to cart with quantity ${this.cantidad}`);
    
    const token = localStorage.getItem('token');
    if (!token) {
      this.error = 'No se pudo verificar tu sesión. Por favor, inicia sesión nuevamente.';
      this.authError = true;
      return;
    }
    
    this.store.dispatch(CarritoActions.addItem({ 
      idMedicamento: this.medicamentoId, 
      cantidad: this.cantidad 
    }));

    this.success = true;
    setTimeout(() => {
      this.success = false;
    }, 3000);
  }
} 