package br.edu.ufrn.tads.ecommercepw.service;

import br.edu.ufrn.tads.ecommercepw.model.Lojista;
import br.edu.ufrn.tads.ecommercepw.repository.LojistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LojistaService {
    @Autowired
    private LojistaRepository lojistaRepository;
    
    public Lojista salvar(Lojista lojista) {
        return lojistaRepository.save(lojista);
    }
    
    public Optional<Lojista> buscarPorEmail(String email) {
        return lojistaRepository.findByEmail(email);
    }
    
    public boolean autenticar(String email, String senha) {
        Optional<Lojista> lojista = buscarPorEmail(email);
        return lojista.isPresent() && lojista.get().getSenha().equals(senha);
    }
}