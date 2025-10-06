export interface Venta {
  idVenta?: number;
  idUsuario?: number;
  idReceta?: number;
  fechaVenta: Date;
  total?: number;
  impuesto?: number
  descuento?: number
  montoPagado?: number
  detalles?: VentaDetalle[];
}

export interface VentaDetalle {
  idVentaDetalle: number;
  idMedicamento: number;
  cantidad: number;
  precioUnitario: number;
  totalLinea: number;
}

