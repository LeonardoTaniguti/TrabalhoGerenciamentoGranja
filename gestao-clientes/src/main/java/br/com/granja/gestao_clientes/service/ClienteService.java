package br.com.granja.gestao_clientes.service;

import br.com.granja.gestao_clientes.model.Cliente;
import br.com.granja.gestao_clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public void salvar(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> pesquisarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
}