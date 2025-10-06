import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { register } from '../state/auth.actions';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  correo: string = '';
  password: string = '';

  constructor(private store: Store) {}

  onRegister(): void {
    this.store.dispatch(register({ correo: this.correo, password: this.password }));
  }
}



