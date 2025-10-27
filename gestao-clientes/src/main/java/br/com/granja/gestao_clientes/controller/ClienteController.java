package br.com.granja.gestao_clientes.controller;

import br.com.granja.gestao_clientes.model.Cliente;
import br.com.granja.gestao_clientes.service.ClienteService;
import jakarta.servlet.http.HttpSession; // Importação do Spring Boot 3
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

    // --- LISTAGEM E PESQUISA (MODIFICADO PARA USAR SESSÃO) ---
    @GetMapping
    public String listarClientes(@RequestParam(name = "q", required = false) String query,
                                 Model model,
                                 HttpSession session) { // [cite: 100]

        String filtroAtivo = null;

        if (query != null) {
            // 1. Se uma nova pesquisa foi enviada (query não é nula)
            filtroAtivo = query;
            session.setAttribute("filtroClientes", filtroAtivo); // Salva na sessão [cite: 102]
        } else {
            // 2. Se nenhuma pesquisa foi enviada, busca o filtro salvo na sessão
            filtroAtivo = (String) session.getAttribute("filtroClientes"); // [cite: 112]
        }

        List<Cliente> clientes;
        if (filtroAtivo != null && !filtroAtivo.trim().isEmpty()) {
            clientes = clienteService.pesquisarPorNome(filtroAtivo); // [cite: 115]
        } else {
            clientes = clienteService.listarTodos(); // [cite: 119]
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("query", filtroAtivo); // Envia o filtro ativo (da sessão ou da request)
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado")); // Para o header [cite: 209]

        return "clientes/index"; // [cite: 120]
    }

    // --- NOVO MÉTODO PARA LIMPAR O FILTRO ---
    @GetMapping("/limpar-filtro")
    public String limparFiltro(HttpSession session) {
        // Remove apenas o atributo do filtro, mantendo o usuário logado
        // [cite: 238]
        session.removeAttribute("filtroClientes");
        return "redirect:/clientes";
    }


    // --- CADASTRO (EXIBIR FORMULÁRIO) ---
    @GetMapping("/novo")
    public String novoClienteForm(Model model, HttpSession session) { // Adiciona HttpSession
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("pageTitle", "Novo Cliente");
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado")); // Para o header
        return "clientes/formulario";
    }

    // --- EDIÇÃO (EXIBIR FORMULÁRIO) ---
    @GetMapping("/editar/{id}")
    public String editarClienteForm(@PathVariable Long id, Model model, RedirectAttributes attributes, HttpSession session) { // Adiciona HttpSession
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            model.addAttribute("pageTitle", "Editar Cliente");
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado")); // Para o header
            return "clientes/formulario";
        } else {
            attributes.addFlashAttribute("erro", "Cliente não encontrado.");
            return "redirect:/clientes";
        }
    }

    // --- SALVAR (CRIAÇÃO OU ATUALIZAÇÃO) ---
    @PostMapping("/salvar")
    public String salvarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, RedirectAttributes attributes, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", cliente.getId() == null ? "Novo Cliente" : "Editar Cliente");
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado")); // Para o header
            return "clientes/formulario";
        }

        try {
            clienteService.salvar(cliente);
            attributes.addFlashAttribute("sucesso", "Cliente salvo com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro ao salvar cliente. Verifique se o CNPJ/CPF já está em uso.");
        }

        return "redirect:/clientes";
    }

    // --- EXCLUSÃO (PASSO 1: MOSTRAR PÁGINA DE CONFIRMAÇÃO) ---
    @GetMapping("/excluir/confirmar/{id}")
    public String confirmarExclusao(@PathVariable Long id, Model model, RedirectAttributes attributes, HttpSession session) { // Adiciona HttpSession
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isEmpty()) {
            attributes.addFlashAttribute("erro", "Cliente não encontrado.");
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", clienteOpt.get());
        model.addAttribute("pageTitle", "Confirmar Exclusão");
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado")); // Para o header
        return "clientes/confirmar-exclusao";
    }

    // --- EXCLUSÃO (PASSO 2: EXECUTAR A EXCLUSÃO) ---
    @PostMapping("/excluir/{id}")
    public String executarExclusao(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            clienteService.excluir(id);
            attributes.addFlashAttribute("sucesso", "Cliente excluído com sucesso.");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Não foi possível excluir o cliente.");
        }
        return "redirect:/clientes";
    }
}
