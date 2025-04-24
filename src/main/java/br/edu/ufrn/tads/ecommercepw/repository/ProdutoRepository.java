package br.edu.ufrn.tads.ecommercepw.repository;

import br.edu.ufrn.tads.ecommercepw.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}