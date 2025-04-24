package br.edu.ufrn.tads.ecommercepw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;
    private double preco;
    private int estoque;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
    
    public void incrementaEstoque() {
        this.estoque++;
    }

    public void diminuiEstoque() {
        if(this.estoque > 0) {
            this.estoque--;
        }
    }
    
    public void diminuiEstoque(int quantidade) {
        if(this.estoque >= quantidade) {
            this.estoque -= quantidade;
        }
    }
}