package br.com.brassaria.intranet.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Notícia de um evento promovido para os clientes.
 * Relacionamento N:N com {@link Bebida} (bebidas servidas no evento),
 * mapeado pela tabela de junção "evento_bebidas".
 */
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 300)
    private String resumo;

    @Column(nullable = false, length = 5000)
    private String conteudo;

    // "local" é palavra reservada no MySQL, por isso o nome da coluna é diferente.
    @Column(name = "local_evento", nullable = false, length = 150)
    private String local;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    /** Preenchida automaticamente; define quais são os "últimos eventos cadastrados". */
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @ManyToMany
    @JoinTable(
            name = "evento_bebidas",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "bebida_id"))
    @OrderBy("nome ASC")
    private Set<Bebida> bebidasServidas = new HashSet<>();

    @PrePersist
    void aoCadastrar() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Evento() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }

    public Set<Bebida> getBebidasServidas() { return bebidasServidas; }
    public void setBebidasServidas(Set<Bebida> bebidasServidas) { this.bebidasServidas = bebidasServidas; }
}
