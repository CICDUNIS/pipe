import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const userData = localStorage.getItem('userData');

    if (userData) {
      const user = JSON.parse(userData);

      if (user.roles && user.roles.includes('Administrador')) {
        return true;
      }
    }

    this.router.navigate(['/pages/medicamentos']);
    return false;
  }
}
