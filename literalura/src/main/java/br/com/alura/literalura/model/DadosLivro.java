package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("bookshelves") String genero,
                         @JsonAlias("languages") String linguagem,
                         @JsonAlias("download_count") Long numeroDeDownloads,
                         @JsonAlias("id") Long id,
                         @JsonAlias("title") String tituloDoLivro) {
}
