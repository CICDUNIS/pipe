export interface Receta {
  idReceta: number;
  codigoReceta: string;
  fecha: Date;
  idUsuario?: number;
  aprobadoSeguro?: string;
  pdfUrl: string;
}
