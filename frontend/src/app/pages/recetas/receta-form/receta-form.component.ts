import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { Receta } from '../state/receta.model';
import * as RecetasActions from '../state/recetas.actions';
import { AppState } from '../../../app.state';

@Component({
  selector: 'app-receta-form',
  standalone: true,
  templateUrl: './receta-form.component.html',
  styleUrls: ['./receta-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class RecetaFormComponent implements OnInit {
  recetaForm: FormGroup = new FormGroup({
    codigoReceta: new FormControl(''),
    fecha: new FormControl(''),
    aprobadoSeguro: new FormControl(false),
    pdfUrl: new FormControl('')
  });
  editingId: number | null = null;

  constructor(
    private store: Store<AppState>,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(map(params => params.get('id'))).subscribe(id => {
      if (id) {
        this.editingId = +id;
        this.store.select(s => s.recetas.list).subscribe(list => {
          const found = list.find(r => r.idReceta === this.editingId);
          if (found) {
            this.recetaForm.patchValue(found);
          }
        });
      }
    });
  }

  onSubmit(): void {
    const formValue: Receta = this.recetaForm.value;

    if (this.editingId) {
      this.store.dispatch(
        RecetasActions.updateReceta({ id: this.editingId, changes: formValue })
      );
    } else {
      this.store.dispatch(
        RecetasActions.createReceta({ receta: formValue })
      );
    }
    this.router.navigate(['/pages/recetas']);
  }
}


