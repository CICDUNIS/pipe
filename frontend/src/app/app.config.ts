import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {environment} from '../environments/environment';
import {medicamentosReducer} from './pages/medicamentos/state/medicamentos.reducer';
import {MedicamentosEffects} from './pages/medicamentos/state/medicamentos.effects';
import {authReducer} from './auth/state/auth.reducer';
import {AuthEffects} from './auth/state/auth.effects';
import { ventasReducer } from './pages/ventas/state/ventas.reducer';
import { VentasEffects } from './pages/ventas/state/ventas.effects';
import { recetasReducer } from './pages/recetas/state/recetas.reducer';
import {RecetasEffects} from './pages/recetas/state/recetas.effects';
import { searchReducer } from './pages/search/state/search.reducer';
import { SearchEffects } from './pages/search/state/search.effects';
import { profileReducer } from './pages/profile/state/profile.reducer';
import { ProfileEffects } from './pages/profile/state/profile.effects';
import { carritoReducer } from './pages/carrito/state/carrito.reducer';
import { CarritoEffects } from './pages/carrito/state/carrito.effects';
import {provideHttpClient} from '@angular/common/http';
import { AppState } from './app.state';
import { producerAccessed } from '@angular/core/primitives/signals';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './core/interceptors/jwt-interceptor';


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    provideStore<AppState>({
      auth: authReducer, 
      medicamentos: medicamentosReducer, 
      ventas: ventasReducer, 
      recetas: recetasReducer, 
      search: searchReducer, 
      profile: profileReducer,
      carrito: carritoReducer
    }),
    provideEffects([
      AuthEffects, 
      MedicamentosEffects, 
      VentasEffects, 
      RecetasEffects, 
      SearchEffects, 
      ProfileEffects,
      CarritoEffects
    ]),
    !environment.production ? provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }) : []
  ]
};
