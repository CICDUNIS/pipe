export interface Comentario {
  idComentario?: number;
  idMedicamento: number;
  idUsuario: number;
  nombreUsuario?: string;
  texto: string;
  fecha: string;
  parentId?: number;
  respuestas?: Comentario[];
}
