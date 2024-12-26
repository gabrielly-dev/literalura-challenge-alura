package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonAlias("name") String nomeDoAutor,
                         @JsonAlias("birth_year") int dataDeNascimento,
                         @JsonAlias("death_year") int dataDeFalescimento) {
}
