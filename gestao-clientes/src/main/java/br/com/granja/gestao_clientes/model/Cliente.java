package br.com.granja.gestao_clientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Campos Obrigatórios ---
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "[a-zA-ZÀ-ú\\s]+", message = "O nome deve conter apenas letras e espaços.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O CNPJ/CPF é obrigatório.")
    @Pattern(regexp = "[0-9]+", message = "O documento deve conter apenas números.")
    @Column(nullable = false, unique = true, length = 18)
    private String documento;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "[0-9]+", message = "O telefone deve conter apenas números.") // Mensagem corrigida
    @Column(nullable = false, length = 20)
    private String telefone;

    // --- Campos Opcionais ---
    @Column(length = 100)
    @Email(message = "Por favor, insira um e-mail válido.") // <-- VALIDAÇÃO CORRETA PARA E-MAIL
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            setDataCadastro(LocalDate.now());
        }
    }
}
