import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { CartWidgetComponent } from '../cart-widget/cart-widget.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, CartWidgetComponent],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  private userData: any = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  private loadUserData(): void {
    const storedData = localStorage.getItem('userData');
    if (storedData) {
      try {
        this.userData = JSON.parse(storedData);
      } catch (error) {
        console.error('Error parsing user data:', error);
        this.userData = null;
      }
    }
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  isAdmin(): boolean {
    this.loadUserData();
    return this.userData?.roles?.includes('Administrador') || false;
  }

  isFarmaceutico(): boolean {
    this.loadUserData();
    return this.userData?.roles?.includes('Farmaceutico') || false;
  }

  isCliente(): boolean {
    this.loadUserData();
    return this.userData?.roles?.includes('Cliente') || false;
  }

  getUserName(): string {
    this.loadUserData();
    return this.userData?.username || 'Usuario';
  }

  getUserRole(): string {
    this.loadUserData();
    if (!this.userData?.roles || this.userData.roles.length === 0) {
      return 'Sin rol asignado';
    }

    return this.userData.roles[0];
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userData');
    this.userData = null;
    this.router.navigate(['/auth/login']);
  }
}

