import { Injectable } from '@angular/core';
import { Mensagem } from './mensagens/Mensagem';

@Injectable({
  providedIn: 'root'
})
export class MensagemService {

  mensagens: Mensagem[] = [];

  adicionar(mensagem: string, tipo?: string): void {

    this.mensagens.push({
      getAlert(): string {
        return `alert alert-${tipo == null ? 'primary' : tipo}`;
      }, texto: mensagem, tipo });
  }

  clear(): void {
    this.mensagens = [];
  }
}
