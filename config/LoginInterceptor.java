package br.com.granja.gestao_clientes.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession();

        // Permite acesso à página de login, ao processo de login e aos recursos estáticos (CSS/JS)
        if (uri.equals("/login") || uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/webjars/") || uri.startsWith("/error")) {
            return true;
        }

        // Verifica se o atributo "usuarioLogado" existe na sessão
        // [cite: 175]
        if (session.getAttribute("usuarioLogado") != null) {
            // Usuário está logado, permite o acesso
            return true;
        }

        // Usuário não está logado, redireciona para a página de login
        response.sendRedirect("/login?error=auth");
        return false;
    }
}