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

import java.util.*;
import java.util.stream.Collectors;

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
                    1 - Cadastrar um livro no banco de dados
                    2 - Cadastrar autor juntamente com seus livros no banco de dados
                    3 - Buscar livros por linguagem no banco de dados
                    4 - Listar autores vivos em um ano específico no banco de dados
                    5 - Top 10 livros mais populares cadastrados no banco de dados
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();

            switch (opcao) {
                case 1:
                    buscarPorLivro();
                    break;
                case 2:
                    buscarPorAutor();
                    break;
                case 3:
                    buscarPorIdioma();
                    break;
                case 4:
                    listarAutoresVivosPorAno();
                    break;
                case 5:
                    listarTop10Livros();
                    break;

            }

        }
    }

    private void listarTop10Livros() {
        var topLivros = livroRepositorio.findTop10ByOrderByNumeroDeDownloadsDesc();

        if (topLivros.isEmpty()) {
            System.out.println("\nNenhum livro encontrado no banco de dados.");
            return;
        }

        System.out.println("\n=== TOP 10 LIVROS MAIS POPULARES ===");
        int posicao = 1;

        for (Livro livro : topLivros) {
            System.out.printf("""
                
                %dº Lugar:
                Título: %s
                Autor: %s
                Downloads: %,d
                Categoria: %s
                Idioma: %s
                """,
                    posicao,
                    livro.getTituloDoLivro(),
                    livro.getAutor().getNomeAutor(),
                    livro.getNumeroDeDownloads(),
                    livro.getCategoriaDoLivro(),
                    livro.getLinguagem()
            );
            posicao++;
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Digite o ano para verificar quais autores estavam vivos:");
        leitura.nextLine();
        var ano = leitura.nextInt();

        var autoresVivos = autorRepository.findAll()
                .stream()
                .filter(autor -> {
                    boolean nascidoAntesOuNo = autor.getDataDeNascimentoDoAutor() <= ano;
                    boolean ainhaVivo = autor.getDataDeMorteDoAutor() == 0 || autor.getDataDeMorteDoAutor() >= ano;
                    return nascidoAntesOuNo && ainhaVivo;
                })
                .sorted(Comparator.comparing(Autor::getNomeAutor))
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("\nNenhum autor encontrado vivo no ano " + ano);
            return;
        }

        System.out.println("\nAutores vivos no ano " + ano + ":");
        autoresVivos.forEach(autor -> {
            System.out.printf("\nNome: %s (Nascimento: %d%s)",
                    autor.getNomeAutor(),
                    autor.getDataDeNascimentoDoAutor(),
                    autor.getDataDeMorteDoAutor() == 0 ?
                            " - Ainda vivo" :
                            String.format(" - Falecimento: %d", autor.getDataDeMorteDoAutor())
            );
            System.out.println("\nLivros do autor:");
            autor.getLivros().forEach(livro ->
                    System.out.println("  - " + livro.getTituloDoLivro())
            );
        });

        System.out.println("\nTotal de autores encontrados: " + autoresVivos.size());
    }

    private void buscarPorIdioma() {
        System.out.println("Digite o idioma desejado (ex: pt, en, fr):");
        leitura.nextLine();
        var idioma = leitura.nextLine().trim();

        var livrosEncontrados = livroRepositorio.findByLinguagemContainingIgnoreCase(idioma)
                .stream()
                .sorted(Comparator.comparing(Livro::getNumeroDeDownloads).reversed())
                .collect(Collectors.toList());

        if (livrosEncontrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado neste idioma.");
            return;
        }

        System.out.println("\nLivros encontrados no idioma " + idioma + " (ordenados por downloads):");
        livrosEncontrados.forEach(livro -> {
            System.out.println("\n" + livro);
        });

        System.out.println("\nTotal de livros encontrados: " + livrosEncontrados.size());
    }

    private void buscarPorAutor() {
        System.out.println("Digite o nome do autor:");
        leitura.nextLine();
        var nomeAutor = leitura.nextLine();

        var json = consumoAPI.obterDados(ENDERECO + nomeAutor.replace(" ", "%20"));
        ResultadoAPI resultado = conversor.obterDados(json, ResultadoAPI.class);

        if (resultado.results().isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");
            return;
        }

        var livrosDoAutor = resultado.results().stream()
                .filter(livroAPI -> livroAPI.authors().stream()
                        .anyMatch(autor -> autor.nomeDoAutor().toLowerCase()
                                .contains(nomeAutor.toLowerCase())))
                .toList();

        if (livrosDoAutor.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");
            return;
        }

        var primeiroLivro = livrosDoAutor.get(0);
        var dadosAutor = primeiroLivro.authors().get(0);

        var autor = new Autor(dadosAutor);
        autor = autorRepository.save(autor);

        System.out.println("\nCadastrando livros do autor: " + autor.getNomeAutor());

        // Cadastrar cada livro encontrado
        for (DadosLivroAPI livroAPI : livrosDoAutor) {
            var dadosLivro = new DadosLivro(
                    livroAPI.bookshelves().isEmpty() ? "Literature" : livroAPI.bookshelves().get(0),
                    livroAPI.languages().isEmpty() ? "en" : livroAPI.languages().get(0),
                    livroAPI.download_count(),
                    livroAPI.id(),
                    livroAPI.title()
            );

            var livro = new Livro(dadosLivro, autor);
            autor.getLivros().add(livro);
            livroRepositorio.save(livro);

            System.out.println("✓ Livro cadastrado: " + livro.getTituloDoLivro());
        }

        System.out.printf("""
            
            === Cadastro Concluído ===
            Autor: %s
            Total de livros cadastrados: %d
            """,
                autor.getNomeAutor(),
                livrosDoAutor.size()
        );
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

        var autor = new Autor(dadosLivroAPI.authors().get(0));
        autor = autorRepository.save(autor);
        var dadosLivro = new DadosLivro(
                dadosLivroAPI.bookshelves().isEmpty() ? "Literature" : dadosLivroAPI.bookshelves().get(0),
                dadosLivroAPI.languages().isEmpty() ? "en" : dadosLivroAPI.languages().get(0),
                dadosLivroAPI.download_count(),
                dadosLivroAPI.id(),
                dadosLivroAPI.title()
        );

        var livro = new Livro(dadosLivro, autor);
        autor.getLivros().add(livro);
        livroRepositorio.save(livro);

        System.out.println("\n=== Livro salvo com sucesso! ===");
        System.out.println(livro);
        System.out.println("\n=== Dados do Autor ===");
        System.out.println(autor);
    }

    private DadosLivro getDadosLivro() {
        System.out.println("Qual livro gostaria de verificar?");
        var nomeLivro = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }
}
