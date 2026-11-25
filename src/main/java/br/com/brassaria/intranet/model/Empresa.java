package br.com.brassaria.intranet.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Dados institucionais: história, missão e visão.
 * Relacionamento 1:N com {@link Valor} — os valores são gravados em cascata
 * junto com a empresa e removidos quando saem da lista (orphanRemoval).
 */
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "ano_fundacao", nullable = false)
    private Integer anoFundacao;

    @Column(length = 200)
    private String slogan;

    @Column(length = 3000)
    private String historia;

    @Column(nullable = false, length = 1500)
    private String missao;

    @Column(nullable = false, length = 1500)
    private String visao;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    private List<Valor> valores = new ArrayList<>();

    public Empresa() {
    }

    /** Mantém os dois lados do relacionamento sincronizados. */
    public void adicionarValor(Valor valor) {
        valor.setEmpresa(this);
        valor.setOrdem(valores.size() + 1);
        valores.add(valor);
    }

    public void limparValores() {
        valores.forEach(v -> v.setEmpresa(null));
        valores.clear();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getAnoFundacao() { return anoFundacao; }
    public void setAnoFundacao(Integer anoFundacao) { this.anoFundacao = anoFundacao; }

    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }

    public String getHistoria() { return historia; }
    public void setHistoria(String historia) { this.historia = historia; }

    public String getMissao() { return missao; }
    public void setMissao(String missao) { this.missao = missao; }

    public String getVisao() { return visao; }
    public void setVisao(String visao) { this.visao = visao; }

    public List<Valor> getValores() { return valores; }
}
