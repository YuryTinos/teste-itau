package br.com.itau.teste.melhorcerveja.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {

    private String message;

    public EntidadeNaoEncontradaException() {
        this.message = "Não foram encontradas entidades!";
    }
    public EntidadeNaoEncontradaException(Class clazz) {
        this.message = String.format("Não foram encontrados %ss!",
                clazz.getName().replace(clazz.getPackage().getName() + ".", ""));
    }

    public EntidadeNaoEncontradaException(String message) {
        this.message = String.format("Não foram encontrados %s!", message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
