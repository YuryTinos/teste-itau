package br.com.itau.teste.melhorcerveja.controllers;

import br.com.itau.teste.melhorcerveja.entity.Cerveja;
import br.com.itau.teste.melhorcerveja.exceptions.EntidadeNaoEncontradaException;
import br.com.itau.teste.melhorcerveja.repositories.CervejaRepository;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class CervejaControllerTest {

    private final String NOME_CERVEJA = "Breja";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CervejaRepository repository;

    @Test
    public void deveraRetornarStatus200QuandoBuscarCervejaPorId() throws Exception {

        when(this.repository.findById(anyString()))
                .thenReturn(Optional.of(new Cerveja(NOME_CERVEJA)));

        this.mockMvc.perform(get("/cervejas/<QUALQUE_ID_VALIDO>")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(NOME_CERVEJA)));
    }

    @Test
    public void deveraRetornarStatus201QuandoPassarNomeValido() throws Exception {

        Cerveja cervejaCompleta = new Cerveja(NOME_CERVEJA);
        cervejaCompleta.setId(UUID.randomUUID().toString());

        when(this.repository.save(any(Cerveja.class))).thenReturn(cervejaCompleta);

        this.mockMvc.perform(post("/cervejas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(new Cerveja(NOME_CERVEJA))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("id")))
                .andExpect(jsonPath("$.nome", is(NOME_CERVEJA)))
                .andExpect(jsonPath("$.pontuacao", is(0)));
    }

    @Test
    public void deveraRetornarStatus200QuandoVotarEmCervejaPorId() throws Exception {

        Cerveja cerveja = new Cerveja(NOME_CERVEJA);

        when(this.repository.findById(anyString())).thenReturn(Optional.of(cerveja));
        when(this.repository.save(any(Cerveja.class))).thenReturn(cerveja);

        this.mockMvc.perform(post("/cervejas/<QUALQUE_ID_VALIDO>/votar")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pontuacao", is(1)));
    }

    @Test
    public void deveraRetornarStatus404QuandoNaoEncontrarCervejaPorId() throws Exception {

        when(this.repository.findById(anyString())).thenThrow(EntidadeNaoEncontradaException.class);

        this.mockMvc.perform(get("/cervejas/<não existe>")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveraRetornarStatus400QuandoNaoPassarNome() throws Exception {

        this.mockMvc.perform(post("/cervejas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(new Cerveja(""))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Nome Ã© obrigatÃ³rio!")));
    }

    @Test
    public void deveraRetornarStatus400QuandoTentarCriarCervejaComMesmoNome() throws Exception {

        when(this.repository.existsByNome(anyString())).thenReturn(true);

        this.mockMvc.perform(post("/cervejas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(new Cerveja(NOME_CERVEJA))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cerveja jÃ¡ cadastrada!")));
    }
}
