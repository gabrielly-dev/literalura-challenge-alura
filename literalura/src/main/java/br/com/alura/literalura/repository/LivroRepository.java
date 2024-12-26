package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Categoria;
import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloDoLivroContainingIgnoreCase(String nomeLivro);

    List<Livro> findTop5ByOrderByNumeroDeDownloadsDesc();
}
