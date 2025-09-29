package br.com.granja.gestao_clientes.repository;

import br.com.granja.gestao_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método para a funcionalidade de pesquisa por nome (ignorando maiúsculas/minúsculas)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}