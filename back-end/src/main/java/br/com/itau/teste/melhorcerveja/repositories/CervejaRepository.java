package br.com.itau.teste.melhorcerveja.repositories;

import br.com.itau.teste.melhorcerveja.entity.Cerveja;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CervejaRepository extends CrudRepository<Cerveja, String> {

    List<Cerveja> findAll();
    Boolean existsByNome(String nome);
}
