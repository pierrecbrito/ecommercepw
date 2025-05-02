package br.edu.ufrn.tads.ecommercepw.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acesso-negado")
public class AcessoNegadoController extends HttpServlet {
    
    @RequestMapping("")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Acesso Negado</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Acesso Negado</h1>");
        out.println("<p>Você não tem permissão para acessar esta página.</p>");
        out.println("<a href='/produtos'>Voltar para a página inicial</a>");
        out.println("</body>");
        out.println("</html>");
    }
}