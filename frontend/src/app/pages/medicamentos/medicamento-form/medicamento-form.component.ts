import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { map, take, catchError } from 'rxjs';
import { of } from 'rxjs';
import * as MedicamentoActions from '../state/medicamentos.actions';
import { Medicamento } from '../state/medicamento.model';
import { AppState } from '../../../app.state';
import { MedicamentoService } from '../medicamento.service';

@Component({
  selector: 'app-medicamento-form',
  standalone: true,
  templateUrl: './medicamento-form.component.html',
  styleUrls: ['./medicamento-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class MedicamentoFormComponent implements OnInit {
  medicamentoForm: FormGroup;
  editingId: number | null = null;
  loading = false;
  submitted = false;
  errorMessage: string | null = null;

  constructor(
    private store: Store<AppState>,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private medicamentoService: MedicamentoService
  ) {
    this.medicamentoForm = this.fb.group({
      codigo: ['', [Validators.required]],
      nombre: ['', [Validators.required]],
      categoria: ['', [Validators.required]],
      principioActivo: ['', [Validators.required]],
      descripcion: [''],
      fotoUrl: [''],
      concentracion: ['', [Validators.required]],
      presentacion: ['', [Validators.required]],
      numeroUnidades: [1, [Validators.required, Validators.min(1)]],
      marca: ['', [Validators.required]],
      requiereReceta: [false],
      stock: [0, [Validators.required, Validators.min(0)]],
      precio: [null]
    });
  }

  public get isEdit(): boolean {
    return this.editingId != null;
  }

  ngOnInit(): void {
    console.log('MedicamentoFormComponent initialized');
    this.route.paramMap
      .pipe(map(params => params.get('id')))
      .subscribe(id => {
        if (id) {
          this.editingId = +id;
          console.log(`Preparing to load medication with ID: ${this.editingId}`);
          this.loadMedicamento(this.editingId);
        } else {
          console.log('No ID parameter found, creating new medication');
        }
      });
  }

  loadMedicamento(id: number): void {
    this.loading = true;
    this.errorMessage = null;
    console.log(`Starting to load medication with ID: ${id}`);

    this.medicamentoService.findById(id)
      .pipe(
        take(1),
        catchError(error => {
          console.error('Detailed error when loading medication:', error);
          if (error.status === 403) {
            this.errorMessage = 'No tienes permiso para acceder a este medicamento. Por favor, verifica tu sesión.';
          } else if (error.status === 404) {
            this.errorMessage = 'Medicamento no encontrado.';
          } else {
            this.errorMessage = `Error al cargar el medicamento: ${error.message || 'Error desconocido'}`;
          }
          return of(null);
        })
      )
      .subscribe({
        next: (medicamento) => {
          if (medicamento) {
            console.log('Loaded medicamento from API:', medicamento);
            const medData = {...medicamento};
            if (typeof medData.requiereReceta === 'string') {
              medData.requiereReceta = medData.requiereReceta === 'Y' || medData.requiereReceta === 'y';
            }
            console.log('Prepared data for form:', medData);
            this.medicamentoForm.patchValue(medData);
            console.log('Form after patch:', this.medicamentoForm.value);
          } else {
            console.error('No medication data received');
            if (!this.errorMessage) {
              this.errorMessage = 'No se pudo cargar la información del medicamento.';
            }
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading medicamento:', error);
          this.errorMessage = 'Error al cargar el medicamento: ' + (error.message || 'Error desconocido');
          this.loading = false;
        }
      });
  }

  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = null;

    if (this.medicamentoForm.invalid) {
      console.error('Form is invalid:', this.medicamentoForm.errors);
      this.errorMessage = 'Por favor, completa todos los campos requeridos correctamente.';
      return;
    }

    const formValue: Medicamento = this.medicamentoForm.value;
    console.log('Submitting form with values:', formValue);

    const sanitizedFormValue: any = {
      ...formValue,
      codigo: formValue.codigo || '',
      nombre: formValue.nombre || '',
      categoria: formValue.categoria || '',
      principioActivo: formValue.principioActivo || '',
      descripcion: formValue.descripcion || '',
      fotoUrl: formValue.fotoUrl || '',
      concentracion: formValue.concentracion || '',
      presentacion: formValue.presentacion || '',
      numeroUnidades: formValue.numeroUnidades || 1,
      marca: formValue.marca || '',
      requiereReceta: formValue.requiereReceta ? 'Y' : 'N',
      stock: formValue.stock || 0,
      precio: null
    };

    console.log('Sanitized form values:', sanitizedFormValue);

    if (this.editingId) {
      this.store.dispatch(
        MedicamentoActions.updateMedicamento({
          id: this.editingId,
          changes: sanitizedFormValue
        })
      );
    } else {
      this.store.dispatch(MedicamentoActions.createMedicamento({ medicamento: sanitizedFormValue }));
    }

    this.router.navigate(['/pages/medicamentos']);
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.medicamentoForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched || this.submitted) : false;
  }
}



