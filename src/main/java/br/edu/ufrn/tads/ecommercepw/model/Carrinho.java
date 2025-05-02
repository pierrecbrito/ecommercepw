package br.edu.ufrn.tads.ecommercepw.model;

import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private Long id;
    private Long usuarioId;
    private Map<Long, Integer> itens; 
    
    public Carrinho() {
        this.itens = new HashMap<>();
    }
    
    public Carrinho(Long id, Long usuarioId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.itens = new HashMap<>();
    }
    
    public void adicionarItem(Long produtoId, int quantidade) {
        itens.put(produtoId, itens.getOrDefault(produtoId, 0) + quantidade);
    }
    
    public void removerItem(Long produtoId) {
        itens.remove(produtoId);
    }
    
    public void atualizarQuantidade(Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            itens.remove(produtoId);
        } else {
            itens.put(produtoId, quantidade);
        }
    }
    
    public void limpar() {
        itens.clear();
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Map<Long, Integer> getItens() {
        return itens;
    }
    
    public void setItens(Map<Long, Integer> itens) {
        this.itens = itens;
    }
}