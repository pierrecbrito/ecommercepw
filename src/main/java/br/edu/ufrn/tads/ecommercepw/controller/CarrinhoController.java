package br.edu.ufrn.tads.ecommercepw.controller;

import br.edu.ufrn.tads.ecommercepw.dao.ProdutoDAO;
import br.edu.ufrn.tads.ecommercepw.model.Carrinho;
import br.edu.ufrn.tads.ecommercepw.model.Produto;
import br.edu.ufrn.tads.ecommercepw.model.Usuario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class CarrinhoController {
    
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @RequestMapping("/cliente/carrinho")
    public void visualizarCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<title>Carrinho de Compras</title>")
            .append("<style>")
            .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }")
            .append("h1 { color: #333; }")
            .append("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }")
            .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
            .append("tr:nth-child(even) { background-color: #f2f2f2; }")
            .append("th { background-color: #04AA6D; color: white; }")
            .append(".actions { display: flex; gap: 10px; }")
            .append(".btn { padding: 8px 15px; border-radius: 4px; cursor: pointer; text-decoration: none; color: white; }")
            .append(".btn-remove { background-color: #f44336; }")
            .append(".btn-shop { background-color: #2196F3; }")
            .append(".btn-checkout { background-color: #04AA6D; }")
            .append(".total { font-weight: bold; margin: 20px 0; font-size: 18px; }")
            .append(".empty-cart { margin: 20px 0; color: #777; }")
            .append(".quantity-input { width: 50px; text-align: center; }")
            .append(".alert { padding: 15px; margin-bottom: 20px; border-radius: 4px; }")
            .append(".alert-success { background-color: #dff0d8; color: #3c763d; }")
            .append(".alert-danger { background-color: #f2dede; color: #a94442; }")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<h1>Carrinho de Compras</h1>");
        
    
        
        if (carrinho.getItens().isEmpty()) {
            html.append("<p class='empty-cart'>Seu carrinho está vazio!</p>");
        } else {
            html.append("<table>")
                .append("<tr><th>Produto</th><th>Preço</th><th>Quantidade</th><th>Subtotal</th><th>Ações</th></tr>");
            
            double totalCarrinho = 0.0;
            
            for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
                Long produtoId = item.getKey();
                int quantidade = item.getValue();
                
                Optional<Produto> optProduto = produtoDAO.buscarPorId(produtoId);
                
                if (optProduto.isPresent()) {
                    Produto produto = optProduto.get();
                    double subtotal = produto.getPreco() * quantidade;
                    totalCarrinho += subtotal;
                    
                    html.append("<tr>")
                        .append("<td>").append(produto.getNome()).append("</td>")
                        .append("<td>R$ ").append(String.format("%.2f", produto.getPreco())).append("</td>")
                        .append("<td>")
                        .append("<form action='/cliente/carrinho/atualizar' method='post' style='margin:0;'>")
                        .append("<input type='hidden' name='produtoId' value='").append(produtoId).append("'>")
                        .append("<input type='number' name='quantidade' value='").append(quantidade).append("' min='1' max='").append(produto.getEstoque()).append("' class='quantity-input'>")
                        .append("<button type='submit' style='margin-left:5px;'>Atualizar</button>")
                        .append("</form>")
                        .append("</td>")
                        .append("<td>R$ ").append(String.format("%.2f", subtotal)).append("</td>")
                        .append("<td><a href='/cliente/carrinho/remover?produtoId=").append(produtoId).append("' class='btn btn-remove'>Remover</a></td>")
                        .append("</tr>");
                }
            }
            
            html.append("</table>")
                .append("<p class='total'>Total: R$ ").append(String.format("%.2f", totalCarrinho)).append("</p>")
                .append("<form action='/cliente/carrinho/finalizar' method='post' style='margin-bottom: 20px;'>")
                .append("<button type='submit' class='btn btn-checkout'>Finalizar Compra</button>")
                .append("</form>");
        }
        
        html.append("<a href='/produtos' class='btn btn-shop'>Continuar Comprando</a>")
            .append("</body>")
            .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/cliente/carrinho/adicionar")
    public void adicionarItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String produtoIdStr = request.getParameter("produtoId");
        String quantidadeStr = request.getParameter("quantidade");
        
        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            
            HttpSession session = request.getSession(true);
            
            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new Carrinho();
                if (session.getAttribute("usuario") != null) {
                    Usuario usuario = (Usuario) session.getAttribute("usuario");
                    carrinho.setUsuarioId(usuario.getId());
                }
            }
            
            if (carrinho.getItens().containsKey(produtoId)) {
                int quantidadeAtual = carrinho.getItens().get(produtoId);
                int novaQuantidade = quantidadeAtual + quantidade;
                
                carrinho.atualizarQuantidade(produtoId, novaQuantidade);
            } else {
                carrinho.adicionarItem(produtoId, quantidade);
            }
            
            session.setAttribute("carrinho", carrinho);
            
            response.sendRedirect("/cliente/carrinho");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("/produtos");
        }
    }
    
    @RequestMapping("/cliente/carrinho/atualizar")
    public void atualizarItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        String produtoIdStr = request.getParameter("produtoId");
        String quantidadeStr = request.getParameter("quantidade");

        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            
            if (quantidade <= 0) {
                response.sendRedirect("/cliente/carrinho/remover?produtoId=" + produtoId);
                return;
            }
            
            Optional<Produto> optProduto = produtoDAO.buscarPorId(produtoId);
            if (optProduto.isEmpty() || optProduto.get().getEstoque() < quantidade) {
                response.sendRedirect("/cliente/carrinho");
                return;
            }
    
            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
            carrinho.atualizarQuantidade(produtoId, quantidade);
            session.setAttribute("carrinho", carrinho);
            
            response.sendRedirect("/cliente/carrinho");
        } catch (NumberFormatException e) {
            response.sendRedirect("/cliente/carrinho");
        }
    }
    
    @RequestMapping("/cliente/carrinho/remover")
    public void removerItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        String produtoIdStr = request.getParameter("produtoId");
        
        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            
            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
            carrinho.removerItem(produtoId);
            session.setAttribute("carrinho", carrinho);
            
            response.sendRedirect("/cliente/carrinho");
        } catch (NumberFormatException e) {
            response.sendRedirect("/cliente/carrinho");
        }
    }
    
    @RequestMapping("/cliente/carrinho/finalizar")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        
        if (carrinho.getItens().isEmpty()) {
            response.sendRedirect("/cliente/carrinho");
            return;
        }
        
        boolean estoqueDisponivel = true;
        StringBuilder mensagemErro = new StringBuilder("Produtos sem estoque suficiente: ");
        boolean temErro = false;
        
        for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
            Long produtoId = item.getKey();
            int quantidade = item.getValue();
            
            Optional<Produto> optProduto = produtoDAO.buscarPorId(produtoId);
            if (optProduto.isEmpty() || optProduto.get().getEstoque() < quantidade) {
                estoqueDisponivel = false;
                if (optProduto.isPresent()) {
                    if (temErro) mensagemErro.append(", ");
                    mensagemErro.append(optProduto.get().getNome());
                    temErro = true;
                }
            }
        }
        
        if (!estoqueDisponivel) {
            response.sendRedirect("/cliente/carrinho");
            return;
        }
        
        boolean sucesso = true;
        for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
            Long produtoId = item.getKey();
            int quantidade = item.getValue();
            
            if (!produtoDAO.atualizarEstoque(produtoId, quantidade)) {
                sucesso = false;
                break;
            }
        }
        
        if (sucesso) {
            carrinho.limpar();
            session.setAttribute("carrinho", carrinho);
            
            response.setContentType("text/html;charset=UTF-8");
            StringBuilder html = new StringBuilder();
            
            html.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Compra Finalizada</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; text-align: center; }")
                .append("h1 { color: #04AA6D; }")
                .append(".message { margin: 20px 0; }")
                .append(".btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; text-decoration: none; }")
                .append(".btn-home { background-color: #2196F3; display: inline-block; margin-top: 20px; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Compra Finalizada com Sucesso!</h1>")
                .append("<p class='message'>Sua compra foi processada com sucesso. Obrigado por comprar conosco!</p>")
                .append("<a href='/produtos' class='btn btn-home'>Voltar para a Loja</a>")
                .append("</body>")
                .append("</html>");
            
            response.getWriter().write(html.toString());
        } else {
            response.sendRedirect("/cliente/carrinho");
        }
    }
}