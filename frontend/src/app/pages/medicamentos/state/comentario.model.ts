export interface Comentario {
  idComentario?: number;
  idMedicamento: number;
  idUsuario?: number;
  texto: string;
  fecha?: Date;
  parentId?: number;
  nombreUsuario?: string;
  respuestas?: Comentario[];
} 