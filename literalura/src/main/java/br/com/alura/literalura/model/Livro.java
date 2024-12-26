package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    private Long id;

    private String tituloDoLivro;

    @Enumerated(EnumType.STRING)
    private Categoria categoriaDoLivro;

    private String linguagem;
    private Long numeroDeDownloads;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Livro(DadosLivro dadosLivro, Autor autor) {
        this.id = dadosLivro.id();
        this.tituloDoLivro = dadosLivro.tituloDoLivro();


        String genero = dadosLivro.genero();
        this.categoriaDoLivro = (genero != null && !genero.isEmpty()) ?
                Categoria.fromString(genero.split(",")[0].trim()) :
                Categoria.LITERATURA;

        this.linguagem = dadosLivro.linguagem();
        this.numeroDeDownloads = dadosLivro.numeroDeDownloads();
        this.autor = autor;
    }

    public Livro() {
    }

    public String getTituloDoLivro() {
        return tituloDoLivro;
    }

    public void setTituloDoLivro(String tituloDoLivro) {
        this.tituloDoLivro = tituloDoLivro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoriaDoLivro() {
        return categoriaDoLivro;
    }

    public void setCategoriaDoLivro(Categoria categoriaDoLivro) {
        this.categoriaDoLivro = categoriaDoLivro;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public Long getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(Long numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }


    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", categoriaDoLivro=" + categoriaDoLivro +
                ", linguagem='" + linguagem + '\'' +
                ", numeroDeDownloads=" + numeroDeDownloads +
                ", autor='" + autor;
    }
}
