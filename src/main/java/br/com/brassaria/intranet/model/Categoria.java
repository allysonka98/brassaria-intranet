package br.com.brassaria.intranet.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Linha do portfólio (ex.: Lagers, Ales, Sem álcool).
 * Relacionamento 1:N com {@link Bebida}.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @OneToMany(mappedBy = "categoria")
    @OrderBy("nome ASC")
    private List<Bebida> bebidas = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Bebida> getBebidas() { return bebidas; }
}
