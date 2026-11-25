package br.com.brassaria.intranet.model;

import jakarta.persistence.*;

/**
 * Produto do portfólio. Cada bebida pertence a uma {@link Categoria} (N:1)
 * e pode ser servida em vários {@link Evento}s (N:N, lado inverso).
 */
@Entity
@Table(name = "bebidas")
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private boolean alcoolica;

    /** Teor alcoólico em % (0 para bebidas sem álcool). */
    @Column(name = "teor_alcoolico", nullable = false)
    private Double teorAlcoolico;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    /** Calorias por 100 ml — informação nutricional exibida no portfólio. */
    @Column(name = "calorias_100ml")
    private Integer calorias100ml;

    /** Cor do rótulo/líquido, usada na ilustração do site (ex.: #C8811E). */
    @Column(name = "cor_hex", length = 7)
    private String corHex;

    @Column(length = 300)
    private String harmonizacao;

    public Bebida() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public boolean isAlcoolica() { return alcoolica; }
    public void setAlcoolica(boolean alcoolica) { this.alcoolica = alcoolica; }

    public Double getTeorAlcoolico() { return teorAlcoolico; }
    public void setTeorAlcoolico(Double teorAlcoolico) { this.teorAlcoolico = teorAlcoolico; }

    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }

    public Integer getCalorias100ml() { return calorias100ml; }
    public void setCalorias100ml(Integer calorias100ml) { this.calorias100ml = calorias100ml; }

    public String getCorHex() { return corHex; }
    public void setCorHex(String corHex) { this.corHex = corHex; }

    public String getHarmonizacao() { return harmonizacao; }
    public void setHarmonizacao(String harmonizacao) { this.harmonizacao = harmonizacao; }
}
