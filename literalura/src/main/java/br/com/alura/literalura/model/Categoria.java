package br.com.alura.literalura.model;

public enum Categoria {

    FICCAO("Fiction", "Ficção"),
    FICCAO_CIENTIFICA("Science Fiction", "Ficção Científica"),
    FANTASIA("Fantasy", "Fantasia"),
    MISTERIO("Mystery", "Mistério"),
    SUSPENSE("Thriller", "Suspense"),
    TERROR("Horror", "Terror"),
    ROMANCE("Romance", "Romance"),
    FICCAO_HISTORICA("Historical Fiction", "Ficção Histórica"),
    JOVEM_ADULTO("Young Adult (YA)", "Jovem Adulto"),
    LITERALURA_INFANTIL("Children’s Literature", "Literatura Infantil"),
    NAO_FICCAO("Non-fiction", "Não ficção"),
    BIOGRAFIA("Biography", "Biografia"),
    AUTOBIOGRAFIA("Autobiography", "Autobiografia"),
    MEMORIAS("Memoir", "Memórias"),
    AUTOAJUDA("Self-help", "Autoajuda"),
    CULINARIA("Cookbooks", "Livros de Culinária"),
    VIAGEM("Travel", "Viagem"),
    CRIMES_REAIS("True Crime", "Crimes Reais"),
    HISTORIA("History", "História"),
    CIENCIAS("Science", "Ciências"),
    FILOSOFIA("Philosophy", "Filosofia"),
    PSICOLOGIA("Psychology", "Psicologia"),
    NEGOCIOS("Business", "Negócios"),
    EDUCACAO("Education", "Educação"),
    POESIA("Poetry", "Poesia"),
    DRAMA("Drama", "Teatro ou Drama"),
    QUADRINHOS("Comics", "Quadrinhos"),
    CONTOS("Short Stories", "Contos"),
    ENSAIOS("Essays", "Ensaios"),
    ESPIRITUALIDADE("Spirituality/Religion", "Espiritualidade/Religião"),
    ARTE_E_FOTOGRAFIA("Art and Photography", "Arte e Fotografia"),
    SAUDE("Health and Wellness", "Saúde e Bem-Estar"),
    POLITICA("Politics", "Política"),
    ECONOMIA("Economics", "Economia"),
    ANTOLOGIA("Anthology", "Antologia"),
    REFERENCIA("Reference", "Referência"),
    PASSATEMPO_E_ARTESANATO("Hobbies and Crafts", "Passatempos e Artesanato"),
    LITERATURA("Literature", "Literatura");

    private String categoriaIngles;
    private String categoriaPortugues;

    Categoria(String categoriaIngles, String categoriaPortugues){
        this.categoriaIngles = categoriaIngles;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaIngles.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        return Categoria.LITERATURA; // Categoria padrão caso nenhuma corresponda
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }


}
