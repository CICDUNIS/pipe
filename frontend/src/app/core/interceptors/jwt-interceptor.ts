import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { Router } from "@angular/router";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private publicEndpoints = [
    "/api/medicamentos/search",
    "/api/medicamentos/latest",
    "/api/auth/register",
    "/api/auth/login",
    "/api/auth/verify-email",
  ];

  constructor(private router: Router) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler,
  ): Observable<HttpEvent<unknown>> {
    console.log(`JwtInterceptor: Processing request to ${request.url}`);

    const isPublicEndpoint = this.publicEndpoints.some((endpoint) =>
      request.url.includes(endpoint),
    );

    if (isPublicEndpoint) {
      console.log(
        `JwtInterceptor: Skipping auth for public endpoint: ${request.url}`,
      );
      return next.handle(request);
    }

    const token = localStorage.getItem("token");

    if (request.url.includes("/api/carrito")) {
      console.log(
        "JwtInterceptor: Processing cart request, token exists:",
        !!token,
      );

      if (!token) {
        console.warn("JwtInterceptor: No token found for cart request!");
        return throwError(
          () =>
            new Error(
              "No se encontró un token de autenticación. Por favor, inicia sesión.",
            ),
        );
      }
    }

    if (token) {
      console.log("JwtInterceptor: Adding token to request");

      const authReq = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });

      return next.handle(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401 || error.status === 403) {
            console.error(
              "JwtInterceptor: Authentication error:",
              error.status,
              error.message,
            );

            if (request.url.includes("/api/carrito")) {
              console.warn(
                "JwtInterceptor: Authentication failed for cart request",
              );
              return throwError(
                () =>
                  new Error(
                    "Tu sesión ha expirado. Por favor, inicia sesión nuevamente para continuar con tu compra.",
                  ),
              );
            }

            this.router.navigate(["/auth/login"]);
          }

          return throwError(() => error);
        }),
      );
    }

    console.warn(
      `JwtInterceptor: No token found for authenticated endpoint: ${request.url}`,
    );

    return next.handle(request);
  }
}
