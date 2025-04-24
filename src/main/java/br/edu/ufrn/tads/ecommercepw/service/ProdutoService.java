package br.edu.ufrn.tads.ecommercepw.service;

import br.edu.ufrn.tads.ecommercepw.model.Produto;
import br.edu.ufrn.tads.ecommercepw.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }
    
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
    
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    public void atualizarEstoque(Long id, int quantidade) {
        Optional<Produto> optProduto = produtoRepository.findById(id);
        if (optProduto.isPresent()) {
            Produto produto = optProduto.get();
            if (produto.getEstoque() >= quantidade) {
                produto.setEstoque(produto.getEstoque() - quantidade);
                produtoRepository.save(produto);
            }
        }
    }
}