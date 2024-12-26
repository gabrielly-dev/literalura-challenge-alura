package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.model.ResultadoAPI;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private List<DadosLivro> dadosLivros = new ArrayList<>();

    @Autowired
    private LivroRepository livroRepositorio;

    @Autowired
    private AutorRepository autorRepository;

    private List<Livro> livros = new ArrayList<>();
    private Optional<Livro> livroBusca;

    public Principal(LivroRepository livroRepositorio, AutorRepository autorRepository) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar livros
                    2 - Buscar autores
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();

            switch (opcao) {
                case 1:
                    buscarPorLivro();
                    break;
                case 2:
                    buscarPorAutor();

            }

        }
    }

    private void buscarPorAutor() {
    }

    private void buscarPorLivro() {
        System.out.println("Qual livro gostaria de verificar?");
        leitura.nextLine();
        var nomeLivro = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        ResultadoAPI resultado = conversor.obterDados(json, ResultadoAPI.class);

        if (resultado.results().isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        var dadosLivroAPI = resultado.results().get(0);

        // Primeiro, cria e salva o autor
        var autor = new Autor(dadosLivroAPI.authors().get(0));
        autor = autorRepository.save(autor);  // Salva o autor primeiro e pega a referência persistida

        var dadosLivro = new DadosLivro(
                dadosLivroAPI.bookshelves().isEmpty() ? "Literature" : dadosLivroAPI.bookshelves().get(0),
                dadosLivroAPI.languages().isEmpty() ? "en" : dadosLivroAPI.languages().get(0),
                dadosLivroAPI.download_count(),
                dadosLivroAPI.id(),
                dadosLivroAPI.title()
        );

        // Depois cria e salva o livro com o autor já persistido
        var livro = new Livro(dadosLivro, autor);
        autor.getLivros().add(livro);
        livroRepositorio.save(livro);

        System.out.println("Livro salvo: " + livro);
    }

    private DadosLivro getDadosLivro() {
        System.out.println("Qual livro gostaria de verificar?");
        var nomeLivro = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }
}
