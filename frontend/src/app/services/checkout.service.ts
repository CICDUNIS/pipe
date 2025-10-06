import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, catchError, throwError, map, timeout } from "rxjs";
import { tap } from "rxjs/operators";
import { environment } from "../../environments/environment";
import { AuthService } from "../auth/auth.service";

interface CheckoutPayload {
  idCart?: number;
  descuento?: number;
  email?: string;
}

interface FacturaDTO {
  idFactura: number;
  idVenta: number;
  fechaFactura: string;
  totalFactura: number;
  pdfUrl: string;
  venta?: any;
}

@Injectable({
  providedIn: "root",
})
export class CheckoutService {
  private apiUrl = `${environment.apiUrl}/api/checkout`;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set("Authorization", `Bearer ${token}`);
  }

  checkout(descuento?: number, email?: string): Observable<FacturaDTO> {
    console.log("CheckoutService: Processing checkout");
    const payload: CheckoutPayload = {
      descuento: descuento || 0,
      email: email,
    };

    const headers = this.getAuthHeaders();

    const timeoutMs = 30000;

    return this.http
      .post<FacturaDTO>(this.apiUrl, payload, {
        headers,
        observe: "response",
      })
      .pipe(
        map((response) => {
          console.log(
            "CheckoutService: Checkout HTTP status:",
            response.status,
          );
          return response.body as FacturaDTO;
        }),
        timeout(timeoutMs),
        tap((response) =>
          console.log("CheckoutService: Checkout successful", response),
        ),
        catchError((error) => {
          console.error("CheckoutService: Checkout failed", error);

          if (error.name === "TimeoutError") {
            return throwError(
              () =>
                new Error(
                  "La operación ha tardado demasiado tiempo. Por favor intente nuevamente.",
                ),
            );
          }

          const errorMsg = this.getErrorMessage(error);
          console.error("CheckoutService: Error message extracted:", errorMsg);

          if (
            errorMsg.includes("DbAction.UpdateRoot") ||
            errorMsg.includes("Carrito")
          ) {
            return throwError(
              () =>
                new Error(
                  "Error al procesar el carrito. Por favor, intente nuevamente o contacte a soporte técnico si el problema persiste.",
                ),
            );
          } else if (errorMsg.includes("Stock insuficiente")) {
            return throwError(
              () =>
                new Error(
                  "No hay suficiente inventario para completar su pedido. Revise su carrito.",
                ),
            );
          } else if (error.status === 500) {
            return throwError(
              () =>
                new Error(
                  "Error interno del servidor. Su compra puede haber sido procesada parcialmente. Por favor, verifique su historial de compras o contacte a soporte técnico.",
                ),
            );
          } else if (error.status === 0 || error.status === 504) {
            return throwError(
              () =>
                new Error(
                  "No se pudo conectar con el servidor. Por favor, verifique su conexión a internet e intente nuevamente.",
                ),
            );
          }

          return throwError(
            () => new Error("Error al procesar el pedido: " + errorMsg),
          );
        }),
      );
  }

  getInvoicePdfUrl(idFactura: number): string {
    return `${this.apiUrl}/${idFactura}/pdf`;
  }

  sendInvoiceEmail(idFactura: number, email: string): Observable<void> {
    const url = `${this.apiUrl}/${idFactura}/email`;
    const headers = this.getAuthHeaders();
    const params = { email };

    return this.http.post<void>(url, null, { headers, params }).pipe(
      tap(() => console.log("CheckoutService: Invoice email sent")),
      catchError((error) => {
        console.error("CheckoutService: Error sending invoice email", error);
        return throwError(
          () =>
            new Error(
              "Error al enviar la factura por email: " +
                this.getErrorMessage(error),
            ),
        );
      }),
    );
  }

  private getErrorMessage(error: any): string {
    if (error.error && error.error.message) {
      return error.error.message;
    }
    if (error.error && typeof error.error === "string") {
      return error.error;
    }
    if (error.message) {
      return error.message;
    }
    return "Error desconocido";
  }
}
