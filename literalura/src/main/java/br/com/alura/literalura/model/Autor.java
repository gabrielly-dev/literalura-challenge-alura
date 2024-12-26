package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeAutor;

    private int dataDeNascimentoDoAutor;
    private int dataDeMorteDoAutor;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DadosAutor dadosAutor) {
        this.nomeAutor = dadosAutor.nomeDoAutor();
        this.dataDeNascimentoDoAutor = dadosAutor.dataDeNascimento();
        this.dataDeMorteDoAutor = dadosAutor.dataDeFalescimento();
        this.livros = new ArrayList<>(); // Adiciona inicialização correta da lista de livros
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public int getDataDeNascimentoDoAutor() {
        return dataDeNascimentoDoAutor;
    }

    public void setDataDeNascimentoDoAutor(int dataDeNascimentoDoAutor) {
        this.dataDeNascimentoDoAutor = dataDeNascimentoDoAutor;
    }

    public int getDataDeMorteDoAutor() {
        return dataDeMorteDoAutor;
    }

    public void setDataDeMorteDoAutor(int dataDeMorteDoAutor) {
        this.dataDeMorteDoAutor = dataDeMorteDoAutor;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", nomeAutor='" + nomeAutor + '\'' +
                ", dataDeNascimentoDoAutor=" + dataDeNascimentoDoAutor +
                ", dataDeMorteDoAutor=" + dataDeMorteDoAutor +
                ", livros=" + livros;
    }
}
