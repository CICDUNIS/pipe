import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../admin.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: any[] = [];
  roles: any[] = [];
  isLoading: boolean = true;
  error: string | null = null;

  filters = {
    email: '',
    fromDate: '',
    toDate: '',
    role: ''
  };

  selectedUser: any = null;
  selectedRoleId: number | null = null;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadRoles();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.adminService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al cargar usuarios';
        this.isLoading = false;
      }
    });
  }

  loadRoles(): void {
    this.adminService.getAllRoles().subscribe({
      next: (data) => {
        this.roles = data;
      },
      error: (error) => {
        console.error('Error loading roles:', error);
      }
    });
  }

  applyFilters(): void {
    this.isLoading = true;
    this.adminService.filterUsers(
      this.filters.email,
      this.filters.fromDate ? new Date(this.filters.fromDate).toISOString() : null,
      this.filters.toDate ? new Date(this.filters.toDate).toISOString() : null,
      this.filters.role
    ).subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al filtrar usuarios';
        this.isLoading = false;
      }
    });
  }

  resetFilters(): void {
    this.filters = {
      email: '',
      fromDate: '',
      toDate: '',
      role: ''
    };
    this.loadUsers();
  }

  activateUser(user: any): void {
    if (!this.selectedRoleId) {
      this.error = 'Debe seleccionar un rol para activar al usuario';
      return;
    }

    this.adminService.activateUser(user.id, this.selectedRoleId).subscribe({
      next: () => {
        this.loadUsers();
        this.selectedUser = null;
        this.selectedRoleId = null;
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al activar usuario';
      }
    });
  }

  deactivateUser(user: any): void {
    this.adminService.deactivateUser(user.id).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al desactivar usuario';
      }
    });
  }

  assignRole(user: any): void {
    if (!this.selectedRoleId) {
      this.error = 'Debe seleccionar un rol para asignar al usuario';
      return;
    }

    this.adminService.assignRole(user.id, [this.selectedRoleId]).subscribe({
      next: () => {
        this.loadUsers();
        this.selectedUser = null;
        this.selectedRoleId = null;
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al asignar rol';
      }
    });
  }

  removeRole(user: any, roleId: number | null): void {
    if (roleId === null) {
      this.error = 'No se pudo encontrar el ID del rol';
      return;
    }

    this.adminService.removeRole(user.id, roleId).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (error) => {
        this.error = error.error?.error || 'Error al eliminar rol';
      }
    });
  }

  selectUser(user: any): void {
    this.selectedUser = user;
    this.selectedRoleId = null;
  }

  getRoleName(roleId: number): string {
    const role = this.roles.find(r => r.idRol === roleId);
    return role ? role.nombreRol : 'Desconocido';
  }

  getRoleIdByName(roleName: string): number | null {
    const role = this.roles.find(r => r.nombreRol === roleName);
    return role ? role.idRol : null;
  }
}
