package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivroAPI(
        Long id,
        String title,
        List<DadosAutor> authors,
        List<String> bookshelves,
        List<String> languages,
        Long download_count
) {}
