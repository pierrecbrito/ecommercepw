package br.edu.ufrn.tads.ecommercepw.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Carrinho implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<Long, Integer> itens;
    
    public Carrinho() {
        itens = new HashMap<>();
    }
    
    public void adicionarProduto(Long produtoId) {
        itens.put(produtoId, itens.getOrDefault(produtoId, 0) + 1);
    }
    
    public void removerProduto(Long produtoId) {
        if (itens.containsKey(produtoId)) {
            int quantidade = itens.get(produtoId);
            if (quantidade > 1) {
                itens.put(produtoId, quantidade - 1);
            } else {
                itens.remove(produtoId);
            }
        }
    }
    
    public Map<Long, Integer> getItens() {
        return itens;
    }
    
    public boolean isEmpty() {
        return itens.isEmpty();
    }
    
    public void limpar() {
        itens.clear();
    }
}
