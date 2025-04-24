package br.edu.ufrn.tads.ecommercepw.repository;

import br.edu.ufrn.tads.ecommercepw.model.Lojista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LojistaRepository extends JpaRepository<Lojista, Long> {
    Optional<Lojista> findByEmail(String email);
}