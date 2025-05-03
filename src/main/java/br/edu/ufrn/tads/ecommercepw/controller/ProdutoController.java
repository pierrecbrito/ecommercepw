package br.edu.ufrn.tads.ecommercepw.controller;

import br.edu.ufrn.tads.ecommercepw.dao.ProdutoDAO;
import br.edu.ufrn.tads.ecommercepw.model.Produto;
import br.edu.ufrn.tads.ecommercepw.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ProdutoController {
    
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @RequestMapping("/produtos")
    public void listarProdutos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Produto> produtos = produtoDAO.listarTodos();
        
        boolean isLojista = false;
        boolean isCliente = false;
        boolean isLogado = false;

        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                isLogado = true;
                isLojista = usuario.isLojista();
                isCliente = usuario.isCliente();
            }
        }
        
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Lista de Produtos</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
            .append("h1 { color: #333; }")
            .append("table { border-collapse: collapse; width: 100%; }")
            .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
            .append("tr:nth-child(even) { background-color: #f2f2f2; }")
            .append("th { background-color: #04AA6D; color: white; }")
            .append(".actions { display: flex; gap: 10px; }")
            .append(".btn { padding: 5px 10px; border-radius: 4px; cursor: pointer; text-decoration: none; color: white; }")
            .append(".btn-add { background-color: #04AA6D; }")
            .append(".btn-edit { background-color: #2196F3; }")
            .append(".btn-delete { background-color: #f44336; }")
            .append(".btn-cart { background-color: #FF9800; }")
            .append(".nav { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }")
            .append(".nav-links { display: flex; gap: 15px; }")
            .append(".nav-link { text-decoration: none; color: #04AA6D; font-weight: bold; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class='nav'>")
            .append("<h1>Lista de Produtos</h1>")
            .append("<div class='nav-links'>");
        
        if (isLogado) {
            if (isCliente) {
                html.append("<a href='/cliente/carrinho' class='nav-link'>Meu Carrinho</a>");
            }
            html.append("<a href='/logout' class='nav-link'>Sair</a>");
        } else {
            html.append("<a href='/login' class='nav-link'>Entrar</a>")
                .append("<a href='/cadastro' class='nav-link'>Cadastrar</a>");
        }
        
        html.append("</div>") 
            .append("</div>"); 
        
        if (isLojista) {
            html.append("<a href='/lojista/novo-produto' class='btn btn-add'>Novo Produto</a>");
        }
        
        html.append("<table style='margin-top: 10px;'>")
            .append("<tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço</th><th>Estoque</th>");
        
        if (isLogado) {
            html.append("<th>Ações</th>");
        }
        
        html.append("</tr>");
        
        for (Produto produto : produtos) {
            html.append("<tr>")
                .append("<td>").append(produto.getId()).append("</td>")
                .append("<td>").append(produto.getNome()).append("</td>")
                .append("<td>").append(produto.getDescricao()).append("</td>")
                .append("<td>R$ ").append(String.format("%.2f", produto.getPreco())).append("</td>")
                .append("<td>").append(produto.getEstoque()).append("</td>");
            

            if (isLogado) {
                html.append("<td class='actions'>");
                
                if (isCliente) {
                    html.append("<a href='/cliente/carrinho/adicionar?quantidade=1&produtoId=").append(produto.getId())
                        .append("' class='btn btn-add'>Adicionar ao carrinho</a>");
                }
                
                if (isLojista) {
                    html.append("<a href='/lojista/editar-produto?id=").append(produto.getId())
                        .append("' class='btn btn-edit'>Editar</a>")
                        .append("<a href='/lojista/excluir-produto?id=").append(produto.getId())
                        .append("' class='btn btn-delete'>Excluir</a>");
                }
                
                html.append("</td>");
            }
            
            html.append("</tr>");
        }
        
        html.append("</table>");

        if(isLogado) {
            html.append("<p>").append("Olá, " + (request.getSession(false).getAttribute("usuarioNome"))).append("</p>");
        }

        html.append("</body>")
        .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/lojista/novo-produto")
    public void novoProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Novo Produto</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
            .append("h1 { color: #333; }")
            .append("form { max-width: 500px; margin: 0 auto; }")
            .append(".form-group { margin-bottom: 15px; }")
            .append("label { display: block; margin-bottom: 5px; }")
            .append("input, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }")
            .append(".btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; }")
            .append(".btn-save { background-color: #04AA6D; }")
            .append(".btn-cancel { background-color: #f44336; margin-left: 10px; text-decoration: none; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<h1>Novo Produto</h1>")
            .append("<form action='/lojista/salvar-produto' method='post'>")
            .append("<div class='form-group'>")
            .append("<label for='nome'>Nome:</label>")
            .append("<input type='text' id='nome' name='nome' required>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='descricao'>Descrição:</label>")
            .append("<textarea id='descricao' name='descricao' rows='3'></textarea>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='preco'>Preço:</label>")
            .append("<input type='number' id='preco' name='preco' step='0.01' min='0' required>")
            .append("</div>")
            .append("<div class='form-group'>")
            .append("<label for='estoque'>Estoque:</label>")
            .append("<input type='number' id='estoque' name='estoque' min='0' required>")
            .append("</div>")
            .append("<button type='submit' class='btn btn-save'>Salvar</button>")
            .append("<a href='/produtos' class='btn btn-cancel'>Cancelar</a>")
            .append("</form>")
            .append("</body>")
            .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/lojista/editar-produto")
    public void editarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("/produtos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idParam);
            Optional<Produto> optProduto = produtoDAO.buscarPorId(id);
            
            if (optProduto.isEmpty()) {
                response.sendRedirect("/produtos");
                return;
            }
            
            Produto produto = optProduto.get();
            
            response.setContentType("text/html;charset=UTF-8");
            StringBuilder html = new StringBuilder();
            
            html.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Editar Produto</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
                .append("h1 { color: #333; }")
                .append("form { max-width: 500px; margin: 0 auto; }")
                .append(".form-group { margin-bottom: 15px; }")
                .append("label { display: block; margin-bottom: 5px; }")
                .append("input, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }")
                .append(".btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; }")
                .append(".btn-save { background-color: #04AA6D; }")
                .append(".btn-cancel { background-color: #f44336; margin-left: 10px; text-decoration: none; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Editar Produto</h1>")
                .append("<form action='/lojista/atualizar-produto' method='post'>")
                .append("<input type='hidden' name='id' value='").append(produto.getId()).append("'>")
                .append("<div class='form-group'>")
                .append("<label for='nome'>Nome:</label>")
                .append("<input type='text' id='nome' name='nome' value='").append(produto.getNome()).append("' required>")
                .append("</div>")
                .append("<div class='form-group'>")
                .append("<label for='descricao'>Descrição:</label>")
                .append("<textarea id='descricao' name='descricao' rows='3'>").append(produto.getDescricao()).append("</textarea>")
                .append("</div>")
                .append("<div class='form-group'>")
                .append("<label for='preco'>Preço:</label>")
                .append("<input type='number' id='preco' name='preco' step='0.01' min='0' value='").append(produto.getPreco()).append("' required>")
                .append("</div>")
                .append("<div class='form-group'>")
                .append("<label for='estoque'>Estoque:</label>")
                .append("<input type='number' id='estoque' name='estoque' min='0' value='").append(produto.getEstoque()).append("' required>")
                .append("</div>")
                .append("<button type='submit' class='btn btn-save'>Atualizar</button>")
                .append("<a href='/produtos' class='btn btn-cancel'>Cancelar</a>")
                .append("</form>")
                .append("</body>")
                .append("</html>");
            
            response.getWriter().write(html.toString());
        } catch (NumberFormatException e) {
            response.sendRedirect("/produtos");
        }
    }
    
    @RequestMapping("/lojista/salvar-produto")
    public void salvarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String precoStr = request.getParameter("preco");
        String estoqueStr = request.getParameter("estoque");
        
        if (nome == null || precoStr == null || estoqueStr == null) {
            response.sendRedirect("/lojista/produtos/novo");
            return;
        }
        
        try {
            double preco = Double.parseDouble(precoStr);
            int estoque = Integer.parseInt(estoqueStr);
            
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            
            produtoDAO.salvar(produto);
            
            response.sendRedirect("/produtos");
        } catch (NumberFormatException e) {
            response.sendRedirect("/lojista/produtos/novo");
        }
    }
    
    @RequestMapping("/lojista/atualizar-produto")
    public void atualizarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String precoStr = request.getParameter("preco");
        String estoqueStr = request.getParameter("estoque");
        
        if (idStr == null || nome == null || precoStr == null || estoqueStr == null) {
            response.sendRedirect("/produtos");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            double preco = Double.parseDouble(precoStr);
            int estoque = Integer.parseInt(estoqueStr);
            
            Produto produto = new Produto();
            produto.setId(id);
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            
            produtoDAO.atualizar(produto);
            
            response.sendRedirect("/produtos");
        } catch (NumberFormatException e) {
            response.sendRedirect("/produtos");
        }
    }
    
    @RequestMapping("/lojista/excluir-produto")
    public void excluirProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                Long id = Long.parseLong(idStr);
                produtoDAO.excluir(id);
            } catch (NumberFormatException e) {
            }
        }
        
        response.sendRedirect("/produtos");
    }
}