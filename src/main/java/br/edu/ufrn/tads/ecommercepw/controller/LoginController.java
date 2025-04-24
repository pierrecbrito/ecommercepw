package br.edu.ufrn.tads.ecommercepw.controller;

import br.edu.ufrn.tads.ecommercepw.model.Cliente;
import br.edu.ufrn.tads.ecommercepw.model.Lojista;
import br.edu.ufrn.tads.ecommercepw.service.ClienteService;
import br.edu.ufrn.tads.ecommercepw.service.LojistaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private LojistaService lojistaService;
    
    @GetMapping("/login")
    public void mostrarLogin(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>Login</title></head>");
        out.println("<body>");
        out.println("<h1>Login</h1>");
        out.println("<form method='post' action='/login'>");
        out.println("Email: <input type='email' name='email' required><br>");
        out.println("Senha: <input type='password' name='senha' required><br>");
        out.println("<button type='submit'>Entrar</button>");
        out.println("</form>");
        out.println("<p><a href='/cadastro'>Cadastrar-se</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @PostMapping("/login")
    public void realizarLogin(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        if (cliente.isPresent() && cliente.get().getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", cliente.get());
            session.setAttribute("tipoUsuario", "cliente");
            response.sendRedirect("/produtos");
            return;
        }
        
        Optional<Lojista> lojista = lojistaService.buscarPorEmail(email);
        if (lojista.isPresent() && lojista.get().getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", lojista.get());
            session.setAttribute("tipoUsuario", "lojista");
            response.sendRedirect("/produtos");
            return;
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>Login Inválido</title></head>");
        out.println("<body>");
        out.println("<h1>Login Inválido</h1>");
        out.println("<p>Email ou senha incorretos.</p>");
        out.println("<p><a href='/login'>Tentar novamente</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/login");
    }
}