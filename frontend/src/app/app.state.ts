import {AuthState} from './auth/state/auth.reducer';
import { MedicamentoState } from './pages/medicamentos/state/medicamentos.reducer';
import { VentasState } from './pages/ventas/state/ventas.reducer';
import {RecetasState} from './pages/recetas/state/recetas.reducer';
import { SearchState } from './pages/search/state/search.reducer';
import { ProfileState } from './pages/profile/state/profile.reducer';
import { CarritoState } from './pages/carrito/state/carrito.reducer';


export interface AppState {
  auth: AuthState;
  medicamentos: MedicamentoState;
  ventas: VentasState;
  recetas: RecetasState;
  search: SearchState;
  profile: ProfileState;
  carrito: CarritoState;
}

