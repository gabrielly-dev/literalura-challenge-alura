package br.com.alura.literalura.model;

import br.com.alura.literalura.model.DadosLivroAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadoAPI(
        int count,
        String next,
        String previous,
        List<DadosLivroAPI> results
) {}