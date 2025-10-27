package br.com.granja.gestao_clientes.controller;

import br.com.granja.gestao_clientes.service.UsuarioService;
import jakarta.servlet.http.HttpSession; // Importação do Spring Boot 3
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Exibe a página de login.
     *
     */
    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model, @RequestParam(required = false) String error) {
        // Se já está logado, redireciona para a home
        if (session.getAttribute("usuarioLogado") != null) {
            return "redirect:/clientes";
        }
        // Adiciona mensagem de erro se o interceptor redirecionou
        if (error != null) {
            model.addAttribute("erro", "Você precisa estar logado para acessar esta página.");
        }
        return "login"; // Nome da view: login.html
    }

    /**
     * Processa a tentativa de login.
     * [cite: 178]
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String login,
                               @RequestParam String senha,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        if (usuarioService.validarLogin(login, senha)) {
            // Sucesso: Armazena o usuário na sessão
            // [cite: 183]
            session.setAttribute("usuarioLogado", login);
            return "redirect:/clientes"; // [cite: 184]
        } else {
            // Falha: Retorna ao login com mensagem de erro
            redirectAttributes.addFlashAttribute("erro", "Usuário ou senha inválidos."); // [cite: 186]
            return "redirect:/login"; // [cite: 187]
        }
    }

    /**
     * Processa o logout do usuário.
     * [cite: 193]
     */
    @GetMapping("/logout")
    public String processLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Invalida a sessão (remove todos os atributos)
        session.invalidate(); // [cite: 195]
        redirectAttributes.addFlashAttribute("sucesso", "Logout realizado com sucesso.");
        return "redirect:/login"; // [cite: 196]
    }
}
