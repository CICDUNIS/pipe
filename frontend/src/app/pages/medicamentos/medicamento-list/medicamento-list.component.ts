import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import * as MedicamentoActions from '../state/medicamentos.actions';
import { Medicamento } from '../state/medicamento.model';
import { AppState } from '../../../app.state';
import { MedicamentoService } from '../medicamento.service';
import { AddToCartComponent } from '../../../shared/components/add-to-cart/add-to-cart.component';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-medicamento-list',
  standalone: true,
  templateUrl: './medicamento-list.component.html',
  styleUrls: ['./medicamento-list.component.css'],
  imports: [CommonModule, RouterModule, FormsModule, AddToCartComponent]
})
export class MedicamentoListComponent implements OnInit, OnDestroy {
  medicamentos$: Observable<Medicamento[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  allMedicamentos: Medicamento[] = [];
  filteredMedicamentos: Medicamento[] = [];
  searchTerm: string = '';
  categoryFilter: string = '';
  prescriptionFilter: string = '';
  uniqueCategories: string[] = [];
  isAuthenticated = false;

  private subscription: Subscription = new Subscription();

  constructor(
    private store: Store<AppState>,
    private router: Router,
    private medicamentoService: MedicamentoService,
    private authService: AuthService
  ) {
    this.medicamentos$ = this.store.select(state => state.medicamentos.list);
    this.loading$ = this.store.select(state => state.medicamentos.loading);
    this.error$ = this.store.select(state => state.medicamentos.error);
  }

  ngOnInit(): void {
    this.store.dispatch(MedicamentoActions.loadMedicamentos());

    this.isAuthenticated = this.authService.isLoggedIn();

    this.subscription.add(
      this.medicamentos$.subscribe(medicamentos => {
        console.log('Received medications from store:', medicamentos);
        this.allMedicamentos = medicamentos;
        this.filteredMedicamentos = medicamentos;
        this.extractUniqueCategories();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  goEdit(id: number): void {
    console.log(`Navigating to edit medication with id: ${id}`);
    this.router.navigate(['/pages/medicamentos/edit', id]);
  }

  onDelete(id: number): void {
    if (confirm('¿Está seguro que desea eliminar este medicamento?')) {
      this.store.dispatch(MedicamentoActions.deleteMedicamento({ id }));
    }
  }

  onSearch(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    let filtered = this.allMedicamentos;

    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase().trim();
      filtered = filtered.filter(med =>
        med.nombre?.toLowerCase().includes(term) ||
        med.codigo?.toLowerCase().includes(term) ||
        med.principioActivo?.toLowerCase().includes(term) ||
        med.marca?.toLowerCase().includes(term) ||
        med.categoria?.toLowerCase().includes(term)
      );
    }

    if (this.categoryFilter) {
      filtered = filtered.filter(med => med.categoria === this.categoryFilter);
    }

    if (this.prescriptionFilter !== '') {
      const requiresPrescription = this.prescriptionFilter === 'true';
      filtered = filtered.filter(med => {
        if (requiresPrescription) {
          return med.requiereReceta === true || med.requiereReceta === 'Y' || med.requiereReceta === 'y';
        } else {
          return med.requiereReceta === false || med.requiereReceta === 'N' || med.requiereReceta === 'n';
        }
      });
    }

    this.filteredMedicamentos = filtered;
  }

  private extractUniqueCategories(): void {
    const categories = new Set<string>();
    this.allMedicamentos.forEach(med => {
      if (med.categoria) {
        categories.add(med.categoria);
      }
    });
    this.uniqueCategories = Array.from(categories).sort();
  }
}



