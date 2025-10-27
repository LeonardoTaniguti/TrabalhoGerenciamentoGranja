package br.com.granja.gestao_clientes.service;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    /**
     * Valida as credenciais do usuário (versão simplificada).
     * Em um app real, buscaria no banco e compararia senhas com BCrypt.
     * [cite: 171]
     */
    public boolean validarLogin(String login, String senha) {
        // Usuário "hardcoded" (fixo no código) para este exemplo
        if ("admin".equals(login) && "admin123".equals(senha)) {
            return true;
        }
        return false;
    }
}
