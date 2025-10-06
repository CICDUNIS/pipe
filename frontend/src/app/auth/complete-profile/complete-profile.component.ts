import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-complete-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './complete-profile.component.html',
  styleUrls: ['./complete-profile.component.css']
})
export class CompleteProfileComponent implements OnInit {
  userData: any = {
    correo: '',
    nombre: ''
  };

  isLoading: boolean = false;
  error: string | null = null;
  isPrimerLogin: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userDataString = localStorage.getItem('userData');
    if (userDataString) {
      const storedUserData = JSON.parse(userDataString);
      this.userData.correo = storedUserData.correo;
      this.userData.nombre = storedUserData.nombre || '';
      this.isPrimerLogin = storedUserData.primerLogin === 'Y';
    } else {
      console.error('No user data found in localStorage');
      this.authService.logout();
      this.router.navigate(['/auth/login']);
    }
  }

  onSubmit(): void {
    this.isLoading = true;
    this.error = null;

    const profileData = {
      correo: this.userData.correo,
      nombre: this.userData.nombre
    };

    this.authService.completeProfile(profileData).subscribe({
      next: (response) => {
        this.isLoading = false;

        if (response.user) {
          localStorage.setItem('userData', JSON.stringify(response.user));
        }

        this.router.navigate(['/pages/medicamentos']);
      },
      error: (error) => {
        this.isLoading = false;
        this.error = error.error?.error || 'Error al completar el perfil. Intente nuevamente.';
        console.error('Error completing profile:', error);
      }
    });
  }
}
