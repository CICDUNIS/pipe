import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError, switchMap, of, map } from 'rxjs';
import { tap } from 'rxjs/operators';
import { CarritoDTO } from '../models/carrito/carrito.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private apiUrl = `${environment.apiUrl}/api/carrito`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log('CarritoService: Getting auth headers', token ? 'Token exists' : 'No token found');

    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    } else {
      console.warn('CarritoService: No token found in localStorage');
    }

    return headers;
  }

  getJdbcCart(): Observable<CarritoDTO> {
    console.log('CarritoService: Getting JDBC cart');
    const url = `${this.apiUrl}/jdbc-cart`;
    const headers = this.getAuthHeaders();

    return this.http.get<any>(url, { headers }).pipe(
      map(response => {
        const cartDto: CarritoDTO = {
          idCart: response.idCart,
          idUsuario: response.idUsuario,
          status: response.status,
          fechaCreacion: response.fechaCreacion,
          fechaActualizacion: response.fechaActualizacion,
          items: response.items ? response.items.map((item: any) => ({
            idCartItem: item.idCartItem,
            idMedicamento: item.idMedicamento,
            nombreMedicamento: item.nombreMedicamento,
            cantidad: item.cantidad,
            precioUnitario: item.precioUnitario,
            total: item.total,
            requiereReceta: item.requiereReceta
          })) : [],
          total: response.total || 0
        };

        console.log('CarritoService: JDBC cart loaded', cartDto);
        return cartDto;
      }),
      catchError(error => {
        console.log('CarritoService: Note - Unable to get JDBC cart, returning empty cart', error);
        return of({
          status: "A",
          items: [],
          total: 0
        } as CarritoDTO);
      })
    );
  }

  jdbcAddItem(idMedicamento: number, cantidad: number): Observable<CarritoDTO> {
    console.log(`CarritoService: JDBC adding item - medicamento ID: ${idMedicamento}, cantidad: ${cantidad}`);
    const url = `${this.apiUrl}/jdbc-add-item`;
    const params = { idMedicamento, cantidad };
    const headers = this.getAuthHeaders();

    return this.http.post<any>(url, null, { params: params, headers }).pipe(
      switchMap(response => {
        if (response && response.status === "ERROR") {
          console.log('CarritoService: Server returned error but with HTTP 200, treating as success');
          return this.getJdbcCart();
        }

        const cartDto: CarritoDTO = {
          idCart: response.idCart,
          idUsuario: response.idUsuario,
          status: response.status,
          fechaCreacion: response.fechaCreacion,
          fechaActualizacion: response.fechaActualizacion,
          items: response.items ? response.items.map((item: any) => ({
            idCartItem: item.idCartItem,
            idMedicamento: item.idMedicamento,
            nombreMedicamento: item.nombreMedicamento,
            cantidad: item.cantidad,
            precioUnitario: item.precioUnitario,
            total: item.total,
            requiereReceta: item.requiereReceta
          })) : [],
          total: response.total || 0
        };

        console.log('CarritoService: JDBC item added', cartDto);
        return of(cartDto);
      }),
      catchError(error => {
        if (error.status === 400 && error.error && error.error.message) {
          console.warn('CarritoService: Validation error in JDBC add item', error.error.message);
          return throwError(() => new Error(error.error.message));
        }
        else {
          console.warn('CarritoService: Possible error in JDBC add item, but product might have been added', error);

          return this.getJdbcCart().pipe(
            tap(cart => {
              console.log('CarritoService: Retrieved cart after potential error', cart);
              const itemFound = cart.items && cart.items.find(item => item.idMedicamento === idMedicamento);
              if (itemFound) {
                console.log('CarritoService: Item was actually added successfully', itemFound);
              }
            })
          );
        }
      })
    );
  }

  getActiveCart(): Observable<CarritoDTO> {
    console.log('CarritoService: Loading active cart');

    return this.getJdbcCart();

  }

  addItem(idMedicamento: number, cantidad: number): Observable<CarritoDTO> {
    console.log(`CarritoService: Adding item to cart - medicamento ID: ${idMedicamento}, cantidad: ${cantidad}`);

    return this.jdbcAddItem(idMedicamento, cantidad);

  }

  updateItemQuantity(idMedicamento: number, cantidad: number): Observable<CarritoDTO> {
    console.log(`CarritoService: Updating item quantity - medicamento ID: ${idMedicamento}, cantidad: ${cantidad}`);
    const url = `${this.apiUrl}/items/${idMedicamento}`;
    const params = { cantidad };
    const headers = this.getAuthHeaders();

    return this.http.put<CarritoDTO>(url, null, { params: params, headers }).pipe(
      tap(cart => console.log('CarritoService: Item quantity updated', cart)),
      catchError(error => {
        console.error('CarritoService: Error updating item quantity', error);
        return throwError(() => new Error(this.getErrorMessage(error)));
      })
    );
  }

  removeItem(idMedicamento: number): Observable<CarritoDTO> {
    console.log(`CarritoService: Removing item from cart - medicamento ID: ${idMedicamento}`);
    const url = `${this.apiUrl}/items/${idMedicamento}`;
    const headers = this.getAuthHeaders();

    return this.http.delete<CarritoDTO>(url, { headers }).pipe(
      tap(cart => console.log('CarritoService: Item removed from cart', cart)),
      catchError(error => {
        console.error('CarritoService: Error removing item from cart', error);
        return throwError(() => new Error('Error al eliminar el producto del carrito'));
      })
    );
  }

  checkout(): Observable<CarritoDTO> {
    console.log('CarritoService: Checking out cart');
    const url = `${this.apiUrl}/checkout`;
    const headers = this.getAuthHeaders();

    return this.http.post<CarritoDTO>(url, null, { headers }).pipe(
      tap(cart => console.log('CarritoService: Cart checked out', cart)),
      catchError(error => {
        console.error('CarritoService: Error checking out cart', error);
        return throwError(() => new Error(this.getErrorMessage(error)));
      })
    );
  }

  clearCart(): Observable<void> {
    console.log('CarritoService: Clearing cart');
    const headers = this.getAuthHeaders();

    return this.http.delete<void>(this.apiUrl, { headers }).pipe(
      tap(() => console.log('CarritoService: Cart cleared')),
      catchError(error => {
        console.error('CarritoService: Error clearing cart', error);
        return throwError(() => new Error('Error al vaciar el carrito'));
      })
    );
  }

  private getErrorMessage(error: any): string {
    if (error.error && error.error.message) {
      return error.error.message;
    } else if (error.status === 400) {
      return 'Error en la solicitud. Verifique los datos.';
    } else if (error.status === 401) {
      return 'No autorizado. Inicie sesión para continuar.';
    } else if (error.status === 403) {
      return 'Acceso denegado.';
    } else if (error.status === 404) {
      return 'Recurso no encontrado.';
    } else if (error.status === 0) {
      return 'Error de conexión. Verifique su conexión a internet.';
    } else {
      return 'Ocurrió un error. Intente nuevamente más tarde.';
    }
  }
}
