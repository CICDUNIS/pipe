export interface CarritoDetalleDTO {
  idCartItem?: number;
  idMedicamento: number;
  nombreMedicamento: string;
  cantidad: number;
  precioUnitario: number;
  total: number;
  requiereReceta: string;
}

export interface CarritoDTO {
  idCart?: number;
  idUsuario?: number;
  status: string;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  items: CarritoDetalleDTO[];
  total: number;
} 