package br.com.granja.gestao_clientes.model;

// Imports para Spring Boot 3 (pacote jakarta)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome é obrigatório.")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
    @Column(nullable = false)
    private String nome;

    @NotEmpty(message = "O CPF/CNPJ é obrigatório.")
    @Column(unique = true, nullable = false)
    private String cpfCnpj;

    @NotEmpty(message = "O telefone é obrigatório.")
    @Column(nullable = false)
    private String telefone;

    @NotEmpty(message = "O endereço é obrigatório.")
    private String endereco;

    // Campos Opcionais
    private String email;
    private String inscricaoEstadual;
    private String nomeContato;
    private String observacoes;

    // --- GETTERS E SETTERS ---
    // (A causa do erro estava aqui)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // --- CORREÇÃO AQUI ---
    // Métodos Getter e Setter para o campo 'cpfCnpj'
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
    // --- FIM DA CORREÇÃO ---

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}

