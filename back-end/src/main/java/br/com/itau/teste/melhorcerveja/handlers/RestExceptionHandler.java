package br.com.itau.teste.melhorcerveja.handlers;

import br.com.itau.teste.melhorcerveja.entity.Erro;
import br.com.itau.teste.melhorcerveja.exceptions.EntidadeJaExisteException;
import br.com.itau.teste.melhorcerveja.exceptions.EntidadeNaoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(Erro.builder()
                        .codigo(1000)
                        .mensagem(ex.getBindingResult()
                                    .getFieldErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .collect(Collectors.joining(", ")))
                        .build());
    }

    @ExceptionHandler(value = EntidadeJaExisteException.class)
    public  ResponseEntity<Object> handleEntidadeJaExisteException(
            EntidadeJaExisteException ex, WebRequest request) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(Erro.builder()
                        .codigo(1001)
                        .mensagem(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = EntidadeNaoEncontradaException.class)
    public  ResponseEntity<Object> handleEntidadeNaoEncontradaException(
            EntidadeNaoEncontradaException ex, WebRequest request) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Erro.builder()
                        .codigo(1002)
                        .mensagem(ex.getMessage())
                        .build());
    }
}
