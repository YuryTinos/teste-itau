package br.com.itau.teste.melhorcerveja.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Erro {

    private int codigo;
    private String mensagem;
}
