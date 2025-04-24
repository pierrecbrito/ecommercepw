package br.edu.ufrn.tads.ecommercepw.service;

import br.edu.ufrn.tads.ecommercepw.model.Carrinho;
import br.edu.ufrn.tads.ecommercepw.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarrinhoService {
    @Autowired
    private ProdutoService produtoService;
    
    public double calcularTotal(Carrinho carrinho) {
        double total = 0.0;
        
        for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
            Long produtoId = item.getKey();
            int quantidade = item.getValue();
            
            var produtoOptional = produtoService.buscarPorId(produtoId);
            if (produtoOptional.isPresent()) {
                Produto produto = produtoOptional.get();
                total += produto.getPreco() * quantidade;
            }
        }
        
        return total;
    }
    
    public void finalizarCompra(Carrinho carrinho) {
        for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
            Long produtoId = item.getKey();
            int quantidade = item.getValue();
            
            produtoService.atualizarEstoque(produtoId, quantidade);
        }
        
        carrinho.limpar();
    }
}