package br.edu.ufrn.tads.ecommercepw.service;

import br.edu.ufrn.tads.ecommercepw.model.Cliente;
import br.edu.ufrn.tads.ecommercepw.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public boolean autenticar(String email, String senha) {
        Optional<Cliente> cliente = buscarPorEmail(email);
        return cliente.isPresent() && cliente.get().getSenha().equals(senha);
    }
}
