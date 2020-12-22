package br.com.itau.teste.melhorcerveja.controllers;

import br.com.itau.teste.melhorcerveja.entity.Cerveja;
import br.com.itau.teste.melhorcerveja.exceptions.EntidadeJaExisteException;
import br.com.itau.teste.melhorcerveja.exceptions.EntidadeNaoEncontradaException;
import br.com.itau.teste.melhorcerveja.repositories.CervejaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/cervejas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CervejaController {

    private final CervejaRepository cervejaRepository;

    @Autowired
    public CervejaController(CervejaRepository cervejaRepository) {
        this.cervejaRepository = cervejaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cerveja>> listarCervejas() {
        return ResponseEntity.ok(cervejaRepository.findAll());
    }

    @GetMapping("/{idCerveja}")
    public ResponseEntity<Cerveja> buscarCervejaPorId(@PathVariable(value = "idCerveja") String idCerveja) {
        return ResponseEntity.ok(
                cervejaRepository.findById(idCerveja)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Cervejas")));
    }

    @PostMapping
    public ResponseEntity<Cerveja> criarCerveja(@RequestBody @Valid Cerveja cerveja) {

        if (cervejaRepository.existsByNome(cerveja.getNome())) {
            throw new EntidadeJaExisteException(Cerveja.class);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cervejaRepository.save(cerveja));
    }

    @PostMapping("/{idCerveja}/votar")
    public ResponseEntity<Cerveja> votar(@PathVariable(value = "idCerveja") String idCerveja) {

        Cerveja cerveja = cervejaRepository.findById(idCerveja)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cervejas"));

        cerveja.votar();

        return ResponseEntity
                .ok(cervejaRepository.save(cerveja));
    }

    @DeleteMapping("/{idCerveja}")
    public ResponseEntity<Void> deletarCerveja(@PathVariable(value = "idCerveja") String idCerveja) {
        this.cervejaRepository.deleteById(idCerveja);
        return ResponseEntity.noContent().build();
    }
}
