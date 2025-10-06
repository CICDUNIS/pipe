export interface Medicamento {
  idMedicamento: number;
  nombre: string;
  principioActivo: string;
  descripcion: string;
  laboratorio: string;
  imagenUrl: string;
  precio: number;
  marca: string;
  stock: number;
  requiereReceta: boolean | string;
}
