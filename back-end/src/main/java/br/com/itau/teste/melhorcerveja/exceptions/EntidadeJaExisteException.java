package br.com.itau.teste.melhorcerveja.exceptions;

public class EntidadeJaExisteException extends RuntimeException {

    private String message;

    public EntidadeJaExisteException() {
        this.message = "Entidade já cadastrada!";
    }
    public EntidadeJaExisteException(Class clazz) {
        this.message = String.format("%s já cadastrada!",
                clazz.getName().replace(clazz.getPackage().getName() + ".", ""));
    }
    public EntidadeJaExisteException(String message) {
        this.message = String.format("%s já cadastrada!", message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
