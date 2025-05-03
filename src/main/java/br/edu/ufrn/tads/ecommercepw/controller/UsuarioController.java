package br.edu.ufrn.tads.ecommercepw.controller;

import br.edu.ufrn.tads.ecommercepw.dao.UsuarioDAO;
import br.edu.ufrn.tads.ecommercepw.model.Carrinho;
import br.edu.ufrn.tads.ecommercepw.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Controller
public class UsuarioController {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @RequestMapping("/login")
    public void loginForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        
        String error = request.getParameter("error");
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Login</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
            .append("h1 { color: #333; }")
            .append("form { max-width: 400px; margin: 0 auto; }")
            .append(".form-group { margin-bottom: 15px; }")
            .append("label { display: block; margin-bottom: 5px; }")
            .append("input { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }")
            .append(".btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; }")
            .append(".btn-login { background-color: #04AA6D; }")
            .append(".register-link { display: block; margin-top: 15px; text-align: center; }")
            .append(".error { color: red; margin-bottom: 15px; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<h1>Login</h1>");
        
        if (error != null && error.equals("1")) {
            html.append("<p class='error'>Email ou senha inválidos</p>");
        }
        
        html.append("<form action='/autenticar' method='post'>")
            .append("<div class='form-group'>")
            .append("<label for='email'>Email:</label>")
            .append("<input type='email' id='email' name='email' required>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='senha'>Senha:</label>")
            .append("<input type='password' id='senha' name='senha' required>")
            .append("</div>")
            .append("<button type='submit' class='btn btn-login'>Entrar</button>")
            .append("</form>")
            .append("<a href='/cadastro' class='register-link'>Não tem conta? Cadastre-se</a>")
            .append("</body>")
            .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/cadastro")
    public void cadastroForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        
        String error = request.getParameter("error");
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Cadastro</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
            .append("h1 { color: #333; }")
            .append("form { max-width: 400px; margin: 0 auto; }")
            .append(".form-group { margin-bottom: 15px; }")
            .append("label { display: block; margin-bottom: 5px; }")
            .append("input { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }")
            .append(".btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; }")
            .append(".btn-register { background-color: #04AA6D; }")
            .append(".login-link { display: block; margin-top: 15px; text-align: center; }")
            .append(".error { color: red; margin-bottom: 15px; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<h1>Cadastro</h1>");
        
        if (error != null && error.equals("1")) {
            html.append("<p class='error'>Email já cadastrado</p>");
        }
        /*
         * 
         */
        html.append("<form action='/registrar' method='post'>")
            .append("<div class='form-group'>")
            .append("<label for='nome'>Nome:</label>")
            .append("<input type='text' id='nome' name='nome' required>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='email'>Email:</label>")
            .append("<input type='email' id='email' name='email' required>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='senha'>Senha:</label>")
            .append("<input type='password' id='senha' name='senha' required>")
            .append("</div>")
            .append("<button type='submit' class='btn btn-register'>Cadastrar</button>")
            .append("</form>")
            .append("<a href='/login' class='login-link'>Já tem conta? Faça login</a>")
            .append("</body>")
            .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/autenticar")
    public void autenticar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        
        Optional<Usuario> optUsuario = usuarioDAO.buscarPorEmail(email);
        
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            
            if (usuario.getSenha().equals(senha)) {
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(20*60);
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioNome", usuario.getNome());
                Carrinho carrinho = new Carrinho();
                session.setAttribute("carrinho", carrinho);
                
                response.sendRedirect("/produtos");
                return;
            }
        }
        
        response.sendRedirect("/login?error=1");
    }
    
    @RequestMapping("/registrar")
    public void registrar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipo = "CLIENTE";
        
        Optional<Usuario> optUsuario = usuarioDAO.buscarPorEmail(email);
        
        if (optUsuario.isPresent()) {
            response.sendRedirect("/cadastro?error=1");
            return;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipo(tipo);
        
        if (usuarioDAO.salvar(usuario)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNome", usuario.getNome());
            
            response.sendRedirect("/produtos");
        } else {
            response.sendRedirect("/cadastro");
        }
    }
    
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect("/login");
    }
}