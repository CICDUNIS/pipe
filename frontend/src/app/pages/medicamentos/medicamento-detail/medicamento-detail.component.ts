import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MedicamentoService } from '../medicamento.service';
import { ComentarioService } from '../comentario.service';
import { AuthService } from '../../../auth/auth.service';
import { Medicamento } from '../state/medicamento.model';
import { Comentario } from '../state/comentario.model';

@Component({
  selector: 'app-medicamento-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './medicamento-detail.component.html',
  styleUrls: ['./medicamento-detail.component.css']
})
export class MedicamentoDetailComponent implements OnInit {
  medicamento: Medicamento | null = null;
  comentarios: Comentario[] = [];
  newComment: string = '';
  replyingTo: number | null = null;
  replyText: string = '';
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private medicamentoService: MedicamentoService,
    private comentarioService: ComentarioService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.loadMedicamento(+id);
        this.loadComentarios(+id);
      } else {
        this.error = 'No se encontró el ID del medicamento';
        this.loading = false;
      }
    });
  }

  loadMedicamento(id: number): void {
    this.loading = true;
    this.medicamentoService.findById(id).subscribe({
      next: (medicamento) => {
        this.medicamento = medicamento;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading medicamento:', err);
        this.error = 'Error al cargar el medicamento. Por favor, intente de nuevo.';
        this.loading = false;
      }
    });
  }

  loadComentarios(idMedicamento: number): void {
    this.comentarioService.getByMedicamento(idMedicamento).subscribe({
      next: (comentarios) => {
        this.comentarios = comentarios;
      },
      error: (err) => {
        console.error('Error loading comentarios:', err);
      }
    });
  }

  isAuthenticated(): boolean {
    return this.authService.isLoggedIn();
  }

  submitComment(): void {
    if (!this.newComment.trim()) return;

    if (!this.isAuthenticated()) {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    const comentario: Comentario = {
      idMedicamento: this.medicamento!.idMedicamento!,
      texto: this.newComment
    };

    this.comentarioService.create(comentario).subscribe({
      next: () => {
        this.newComment = '';
        this.loadComentarios(this.medicamento!.idMedicamento!);
      },
      error: (err) => {
        console.error('Error creating comment:', err);
      }
    });
  }

  startReply(comentarioId: number): void {
    this.replyingTo = comentarioId;
    this.replyText = '';
  }

  cancelReply(): void {
    this.replyingTo = null;
    this.replyText = '';
  }

  submitReply(parentId: number): void {
    if (!this.replyText.trim()) return;

    if (!this.isAuthenticated()) {
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    const comentario: Comentario = {
      idMedicamento: this.medicamento!.idMedicamento!,
      texto: this.replyText,
      parentId: parentId
    };

    this.comentarioService.create(comentario).subscribe({
      next: () => {
        this.replyingTo = null;
        this.replyText = '';
        this.loadComentarios(this.medicamento!.idMedicamento!);
      },
      error: (err) => {
        console.error('Error creating reply:', err);
      }
    });
  }

  deleteComment(id: number): void {
    if (confirm('¿Está seguro de que desea eliminar este comentario?')) {
      this.comentarioService.delete(id).subscribe({
        next: () => {
          this.loadComentarios(this.medicamento!.idMedicamento!);
        },
        error: (err) => {
          console.error('Error deleting comment:', err);
        }
      });
    }
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '';
    return new Date(date).toLocaleString();
  }
}
