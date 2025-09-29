package br.com.granja.gestao_clientes.controller;

import br.com.granja.gestao_clientes.model.Cliente;
import br.com.granja.gestao_clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Listagem de Registros e Pesquisa
    @GetMapping
    public String listarClientes(@RequestParam(name = "q", required = false) String query, Model model) {
        List<Cliente> clientes;
        if (query != null && !query.trim().isEmpty()) {
            clientes = clienteService.pesquisarPorNome(query);
        } else {
            clientes = clienteService.listarTodos();
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("query", query);
        return "clientes/index";
    }

    // Formulário de Cadastro
    @GetMapping("/novo")
    public String novoClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("pageTitle", "Novo Cliente");
        return "clientes/formulario";
    }

    // Salvar (Inserir ou Atualizar)
    @PostMapping("/salvar")
    public String salvarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", cliente.getId() == null ? "Novo Cliente" : "Editar Cliente");
            return "clientes/formulario";
        }

        try {
            clienteService.salvar(cliente);
            attributes.addFlashAttribute("sucesso", "Cliente salvo com sucesso!");
        } catch (Exception e) {
            // Tratamento genérico para erros, como documento duplicado
            attributes.addFlashAttribute("erro", "Erro ao salvar cliente. Verifique se o CNPJ/CPF já está em uso.");
        }

        return "redirect:/clientes";
    }

    // Formulário de Edição
    @GetMapping("/editar/{id}")
    public String editarClienteForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            model.addAttribute("pageTitle", "Editar Cliente");
            return "clientes/formulario";
        } else {
            attributes.addFlashAttribute("erro", "Cliente não encontrado.");
            return "redirect:/clientes";
        }
    }

    // Exclusão
    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            clienteService.excluir(id);
            attributes.addFlashAttribute("sucesso", "Cliente excluído com sucesso.");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Não foi possível excluir o cliente.");
        }
        return "redirect:/clientes";
    }
}