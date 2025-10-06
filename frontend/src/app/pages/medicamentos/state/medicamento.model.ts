export interface Medicamento {
  idMedicamento?: number;
  codigo: string;
  nombre: string;
  categoria: string;
  principioActivo: string;
  descripcion: string;
  fotoUrl: string;
  concentracion: string;
  presentacion: string;
  numeroUnidades: number;
  marca: string;
  requiereReceta: boolean | string;
  stock: number;
  precio: number;
}
