import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';

import { Cerveja } from './cerveja';
import { CervejaService } from '../cerveja.service';
import { MensagemService } from '../mensagem.service';

@Component({
  selector: 'app-cervejas',
  templateUrl: './cervejas.component.html',
  styleUrls: ['./cervejas.component.css']
})
export class CervejasComponent implements OnInit {

  cervejas: Cerveja[] = [];
  formularioCerveja: FormGroup;
  cervejaSelecionada: Cerveja;

  constructor(private formBuilder: FormBuilder,
              private cervejaService: CervejaService,
              private mensagemService: MensagemService) {
    this.formularioCerveja = this.formBuilder.group({
      nome: ''
    });
  }

  ngOnInit(): void {
    this.listarCervejas();
  }


  onAdicionar(cerveja: Cerveja): void {
    if (!cerveja.nome) {
      this.mensagemService.adicionar(`Nome da Cerveja é Obrigatório!`, 'danger');
      return;
    }

    this.cervejaService.adicionarCerveja(cerveja)
      .subscribe((novaCerveja) => {
        this.mensagemService.adicionar(`Cerveja ${cerveja.nome} adicionada com sucesso!`);
        this.listarCervejas();
      });
  }

  onVotar(cerveja: Cerveja): void {
    this.cervejaService.votar(cerveja).subscribe(() => {
      this.mensagemService.adicionar(`Cerveja ${cerveja.nome} foi votada com sucesso!`);
      this.listarCervejas();
    });;
  }

  onDeletar(cerveja: Cerveja): void {
    this.cervejaService.deletar(cerveja).subscribe(() => {
      this.mensagemService.adicionar(`Cerveja ${cerveja.nome} deletada com sucesso!`);
      this.listarCervejas();
    });
  }

  private listarCervejas(): void {
    this.cervejaService.listarCervejas()
      .subscribe(cervejas => {
        this.cervejas = cervejas;
        this.cervejas.sort((a, b) => b.pontuacao - a.pontuacao);
      });
  }
}
