import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-success',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './register-success.component.html',
  styleUrls: ['./register-success.component.css']
})
export class RegisterSuccessComponent {
  constructor(private router: Router) {}
  
  navigateToLogin(): void {
    this.router.navigate(['/auth/login']);
  }
} 