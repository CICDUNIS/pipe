import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { FormsModule } from '@angular/forms';
import * as AuthActions from '../state/auth.actions';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  styleUrls: ['./login.component.css'],
  imports: [FormsModule, CommonModule]
})
export class LoginComponent implements OnInit, OnDestroy {
  correo: string = '';
  password: string = '';
  errorMessage: string | null = null;
  loginErrorSubscription: Subscription | null = null;
  returnUrl: string | null = null;

  constructor(
    private store: Store<{ auth: any }>,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.returnUrl = params['returnUrl'] || null;
    });

    this.loginErrorSubscription = this.store.pipe(
      select(state => state.auth.error)
    ).subscribe(error => {
      if (error) {
        if (error.status === 403 && error.error?.error === 'Usuario inactivo') {
          this.errorMessage = 'Tu cuenta aún no ha sido activada. Por favor, espera a que un administrador active tu cuenta.';
        } else if (error.status === 403 && error.error?.requiresAction === 'VERIFY_EMAIL') {
          this.errorMessage = 'Tu correo electrónico no ha sido verificado. Por favor, revisa tu bandeja de entrada.';
        } else if (error.status === 403 && error.error?.requiresAction === 'AWAIT_ROLE_ASSIGNMENT') {
          this.errorMessage = 'Tu cuenta no tiene roles asignados. Por favor, espera a que un administrador te asigne un rol.';
        } else if (error.status === 401) {
          this.errorMessage = 'Credenciales inválidas. Por favor, verifica tu correo y contraseña.';
        } else {
          this.errorMessage = error.error?.error || 'Error al iniciar sesión. Inténtalo de nuevo.';
        }
      } else {
        this.errorMessage = null;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.loginErrorSubscription) {
      this.loginErrorSubscription.unsubscribe();
    }
  }

  onLogin(): void {
    this.errorMessage = null;
    this.store.dispatch(AuthActions.login({
      correo: this.correo,
      password: this.password,
      returnUrl: this.returnUrl
    }));
  }
}



