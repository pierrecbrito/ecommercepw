package br.edu.ufrn.tads.ecommercepw.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import br.edu.ufrn.tads.ecommercepw.model.Usuario;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@WebFilter(urlPatterns = {"/lojista/*", "/cliente/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("AuthFilter: Executando filtro de autenticação");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        
        boolean isAuthenticated = session != null;
        if(!isAuthenticated) {
            response.sendRedirect("/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if(usuario == null) {
            response.sendRedirect("/login");
            return;
        }
        

        if(requestURI.contains("/lojista/") && !usuario.isLojista()) {
            response.sendRedirect("localhost:8080/acesso-negado");
            return;
        }

        if(requestURI.contains("/cliente/") && usuario.isLojista()) {
            response.sendRedirect("localhost:8080/acesso-negado");
            return;
        }

        chain.doFilter(req, res);
    }
}