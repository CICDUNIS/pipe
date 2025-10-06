import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MedicamentoService } from "../../services/medicamento.service";
import { ComentarioService } from "../../services/comentario.service";
import { AuthService } from "../../auth/auth.service";
import { Medicamento } from "../../models/medicamento/medicamento.model";
import { Comentario } from "../../models/comentario/comentario.model";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { Subscription } from "rxjs";
import { Store } from "@ngrx/store";
import { AppState } from "../../app.state";
import * as AuthActions from "../../auth/state/auth.actions";
import { AddToCartComponent } from "../../shared/components/add-to-cart/add-to-cart.component";

@Component({
  selector: "app-medicamento",
  templateUrl: "./medicamento.component.html",
  styleUrls: ["./medicamento.component.css"],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    AddToCartComponent,
  ],
})
export class MedicamentoComponent implements OnInit, OnDestroy {
  medicamento: Medicamento | null = null;
  comentarios: Comentario[] = [];
  isLoading = true;
  error: string | null = null;
  comentarioForm: FormGroup;
  respuestaForm: FormGroup;
  idMedicamento: number = 0;
  mostrarFormRespuesta: number | null = null;
  authSubscription: Subscription | null = null;
  isAuthenticated = false;

  constructor(
    private route: ActivatedRoute,
    private medicamentoService: MedicamentoService,
    private comentarioService: ComentarioService,
    private authService: AuthService,
    private store: Store<AppState>,
    private fb: FormBuilder,
  ) {
    this.comentarioForm = this.fb.group({
      texto: ["", [Validators.required, Validators.minLength(3)]],
    });

    this.respuestaForm = this.fb.group({
      texto: ["", [Validators.required, Validators.minLength(3)]],
    });
  }

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isLoggedIn();
    console.log(
      "MedicamentoComponent: Authentication status on init:",
      this.isAuthenticated,
    );

    this.authSubscription = this.store
      .select((state) => state.auth.user)
      .subscribe((user) => {
        this.isAuthenticated = !!user || this.authService.isLoggedIn();
        console.log(
          "MedicamentoComponent: Authentication status updated:",
          this.isAuthenticated,
        );
      });

    this.route.params.subscribe((params) => {
      this.idMedicamento = +params["id"];
      this.loadMedicamento();
      this.loadComentarios();
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadMedicamento(): void {
    this.isLoading = true;
    this.medicamentoService.getMedicamentoById(this.idMedicamento).subscribe({
      next: (data) => {
        console.log("Medicamento data received:", data);
        this.medicamento = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Error cargando medicamento:", err);
        this.error =
          "Error al cargar el medicamento. Por favor, inténtelo de nuevo.";
        this.isLoading = false;
      },
    });
  }

  loadComentarios(): void {
    console.log(
      "MedicamentoComponent - Starting to load comments for medication ID:",
      this.idMedicamento,
    );

    this.comentarioService
      .getComentariosByMedicamento(this.idMedicamento)
      .subscribe({
        next: (data) => {
          console.log(
            "MedicamentoComponent - Received comments from service:",
            data,
          );
          console.log(
            "MedicamentoComponent - Number of root comments:",
            data.length,
          );

          data.forEach((comment) => {
            console.log(
              `MedicamentoComponent - Root comment ID ${comment.idComentario}:`,
            );
            console.log(`  Text: "${comment.texto}"`);
            console.log(`  Has ${comment.respuestas?.length || 0} replies`);

            if (comment.respuestas && comment.respuestas.length > 0) {
              comment.respuestas.forEach((reply) => {
                console.log(`  - Reply ID ${reply.idComentario}:`);
                console.log(`    Text: "${reply.texto}"`);
                console.log(`    Parent ID: ${reply.parentId}`);
              });
            }
          });

          this.comentarios = data;
          console.log(
            "MedicamentoComponent - Updated comentarios array:",
            this.comentarios,
          );
        },
        error: (err) => {
          console.error("MedicamentoComponent - Error loading comments:", err);
          if (err.status === 401 || err.status === 403) {
            console.log(
              "MedicamentoComponent - Authentication error when loading comments",
            );
          }
        },
      });
  }

  private verificarToken(): boolean {
    const token = localStorage.getItem("token");
    console.log(
      "MedicamentoComponent - Token verification:",
      token ? "token exists" : "no token found",
    );

    if (!token) {
      console.log(
        "MedicamentoComponent - No token found, authentication failed",
      );
      this.isAuthenticated = false;
      return false;
    }

    console.log(
      "MedicamentoComponent - Token is present:",
      token.substring(0, 10) + "...",
    );
    return true;
  }

  agregarComentario(): void {
    if (this.comentarioForm.invalid) return;

    if (!this.verificarToken()) {
      alert("Su sesión ha caducado. Por favor, inicie sesión de nuevo.");
      return;
    }

    const userData = this.authService.getUserData();
    if (!userData) {
      alert("Debe iniciar sesión para comentar");
      return;
    }

    console.log("MedicamentoComponent - Adding comment as user:", userData);
    console.log(
      "MedicamentoComponent - Authentication status:",
      this.authService.isLoggedIn(),
    );
    console.log(
      "MedicamentoComponent - Token available:",
      !!localStorage.getItem("token"),
    );

    const nuevoComentario: Comentario = {
      idMedicamento: this.idMedicamento,
      idUsuario: userData.id,
      texto: this.comentarioForm.value.texto,
      fecha: new Date().toISOString(),
    };

    console.log("MedicamentoComponent - Sending comment:", nuevoComentario);

    this.comentarioService.addComentario(nuevoComentario).subscribe({
      next: (response) => {
        console.log(
          "MedicamentoComponent - Comment added successfully:",
          response,
        );
        this.comentarioForm.reset();
        this.loadComentarios();
      },
      error: (err) => {
        console.error("MedicamentoComponent - Error adding comment:", err);

        if (err.status === 401 || err.status === 403) {
          console.error(
            "MedicamentoComponent - Authentication error. Please log in.",
          );
          alert("Error de autenticación. Asegúrese de estar logueado.");

          localStorage.removeItem("token");
          this.isAuthenticated = false;
          this.store.dispatch(AuthActions.logout());
        } else {
          alert(
            "Error al agregar el comentario. Por favor, inténtelo de nuevo.",
          );
        }
      },
    });
  }

  agregarRespuesta(parentId: number): void {
    if (this.respuestaForm.invalid) return;

    const userData = this.authService.getUserData();
    if (!userData) {
      alert("Debe iniciar sesión para responder");
      return;
    }

    const nuevaRespuesta: Comentario = {
      idMedicamento: this.idMedicamento,
      idUsuario: userData.id,
      texto: this.respuestaForm.value.texto,
      fecha: new Date().toISOString(),
      parentId: parentId,
    };

    this.comentarioService.addComentario(nuevaRespuesta).subscribe({
      next: () => {
        this.respuestaForm.reset();
        this.mostrarFormRespuesta = null;
        this.loadComentarios();
      },
      error: (err) => {
        console.error("Error al agregar respuesta:", err);
        alert("Error al agregar la respuesta. Por favor, inténtelo de nuevo.");
      },
    });
  }

  eliminarComentario(idComentario: number): void {
    if (confirm("¿Estás seguro de que deseas eliminar este comentario?")) {
      this.comentarioService.deleteComentario(idComentario).subscribe({
        next: () => {
          this.loadComentarios();
        },
        error: (err) => {
          console.error("Error al eliminar comentario:", err);
          alert(
            "Error al eliminar el comentario. Por favor, inténtelo de nuevo.",
          );
        },
      });
    }
  }

  mostrarResponder(idComentario: number): void {
    this.mostrarFormRespuesta = idComentario;
    this.respuestaForm.reset();
  }

  ocultarResponder(): void {
    this.mostrarFormRespuesta = null;
  }

  puedeEditar(idUsuario: number): boolean {
    const userData = this.authService.getUserData();
    if (!userData) return false;

    return (
      userData.id === idUsuario ||
      (userData.roles && userData.roles.includes("Administrador"))
    );
  }

  estaLogueado(): boolean {
    return this.isAuthenticated;
  }
}
