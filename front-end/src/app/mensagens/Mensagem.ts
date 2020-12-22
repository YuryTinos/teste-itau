export interface Mensagem {
  tipo: string;
  texto: string;

  getAlert(): string;
}
