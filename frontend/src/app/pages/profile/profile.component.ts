import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { AppState } from '../../app.state';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { UserProfile } from './state/profile.model';
import * as ProfileActions from './state/profile.actions';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  loading$: Observable<boolean>;
  error$: Observable<any>;

  constructor(private store: Store<AppState>) {
    this.profileForm = new FormGroup({
      nombre: new FormControl(''),
      correo: new FormControl(''),
      password: new FormControl('')
    });

    this.loading$ = this.store.select(s => s.profile.loading);
    this.error$ = this.store.select(s => s.profile.error);
  }

  ngOnInit(): void {
    this.store.dispatch(ProfileActions.loadProfile());

    this.store.select(s => s.profile.user)
      .subscribe(user => {
        if (user) {
          this.profileForm.patchValue({
            nombre: user.nombre,
            correo: user.correo
          });
        }
      });
  }

  onUpdate(): void {
    const changes: Partial<UserProfile> = this.profileForm.value;
    this.store.dispatch(ProfileActions.updateProfile({ changes }));
  }
}



