package br.edu.ufrn.tads.ecommercepw.controller;

import br.edu.ufrn.tads.ecommercepw.dao.CarrinhoDAO;
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
    
    private final CarrinhoDAO carrinhoDAO = new CarrinhoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @RequestMapping("/cliente/carrinho")
    public void visualizarCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Long usuarioId = (Long) usuario.getId();
        Optional<Carrinho> optCarrinho = carrinhoDAO.buscarPorUsuario(usuarioId);
        Carrinho carrinho = optCarrinho.orElseGet(() -> {
            Carrinho novoCarrinho = new Carrinho();
            novoCarrinho.setUsuarioId(usuarioId);
            return novoCarrinho;
        });
        
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
                .append("<a href='/cliente/carrinho/finalizar' class='btn btn-checkout'>Finalizar Compra</a>");
        }
        
        html.append("<p><a href='/produtos' class='btn btn-shop'>Continuar Comprando</a></p>")
            .append("</body>")
            .append("</html>");
        
        response.getWriter().write(html.toString());
    }
    
    @RequestMapping("/cliente/carrinho/adicionar")
    public void adicionarItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String produtoIdStr = request.getParameter("produtoId");
        String quantidadeStr = request.getParameter("quantidade");
        
        if (produtoIdStr == null || quantidadeStr == null) {
            response.sendRedirect("/produtos");
            return;
        }
        
        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            Long usuarioId = (Long) ((Usuario) session.getAttribute("usuario")).getId();
            
            if (quantidade <= 0) {
                response.sendRedirect("/produtos");
                return;
            }
            
            // Verificar estoque
            Optional<Produto> optProduto = produtoDAO.buscarPorId(produtoId);
            if (optProduto.isEmpty() || optProduto.get().getEstoque() < quantidade) {
                response.sendRedirect("/produtos");
                return;
            }
            
            // Buscar ou criar carrinho
            Optional<Carrinho> optCarrinho = carrinhoDAO.buscarPorUsuario(usuarioId);
            Carrinho carrinho = optCarrinho.orElseGet(() -> {
                Carrinho novoCarrinho = new Carrinho();
                novoCarrinho.setUsuarioId(usuarioId);
                return novoCarrinho;
            });
            
            // Adicionar item
            carrinho.adicionarItem(produtoId, quantidade);
            
            // Salvar carrinho
            carrinhoDAO.salvar(carrinho);
            
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
        
        if (produtoIdStr == null || quantidadeStr == null) {
            response.sendRedirect("/cliente/carrinho");
            return;
        }
        
        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            Long usuarioId = (Long) ((Usuario) session.getAttribute("usuario")).getId();
            
            if (quantidade <= 0) {
                response.sendRedirect("/cliente/carrinho/remover?produtoId=" + produtoId);
                return;
            }
            
            // Verificar estoque
            Optional<Produto> optProduto = produtoDAO.buscarPorId(produtoId);
            if (optProduto.isEmpty() || optProduto.get().getEstoque() < quantidade) {
                response.sendRedirect("/cliente/carrinho");
                return;
            }
            
            // Buscar carrinho
            Optional<Carrinho> optCarrinho = carrinhoDAO.buscarPorUsuario(usuarioId);
            if (optCarrinho.isPresent()) {
                Carrinho carrinho = optCarrinho.get();
                carrinho.atualizarQuantidade(produtoId, quantidade);
                carrinhoDAO.salvar(carrinho);
            }
            
            response.sendRedirect("/cliente/carrinho");
        } catch (NumberFormatException e) {
            response.sendRedirect("/cliente/carrinho");
        }
    }
    
    @RequestMapping("/cliente/carrinho/remover")
    public void removerItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        String produtoIdStr = request.getParameter("produtoId");
        
        if (produtoIdStr == null) {
            response.sendRedirect("/cliente/carrinho");
            return;
        }
        
        try {
            Long produtoId = Long.parseLong(produtoIdStr);
            Long usuarioId = (Long) ((Usuario) session.getAttribute("usuario")).getId();
            
            Optional<Carrinho> optCarrinho = carrinhoDAO.buscarPorUsuario(usuarioId);
            if (optCarrinho.isPresent()) {
                Carrinho carrinho = optCarrinho.get();
                carrinho.removerItem(produtoId);
                carrinhoDAO.salvar(carrinho);
            }
            
            response.sendRedirect("/cliente/carrinho");
        } catch (NumberFormatException e) {
            response.sendRedirect("/cliente/carrinho");
        }
    }
    
    @RequestMapping("/cliente/carrinho/finalizar")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        
        Long usuarioId = (Long) ((Usuario) session.getAttribute("usuario")).getId();
        
        Optional<Carrinho> optCarrinho = carrinhoDAO.buscarPorUsuario(usuarioId);
        if (optCarrinho.isPresent()) {
            Carrinho carrinho = optCarrinho.get();
            
            if (carrinho.getItens().isEmpty()) {
                response.sendRedirect("/carrinho");
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
                carrinhoDAO.limparCarrinho(usuarioId);
                
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
                    .append(".btn-home { background-color: #2196F3; }")
                    .append("</style>")
                    .append("</head>")
                    .append("<body>")
                    .append("<h1>Compra Finalizada com Sucesso!</h1>")
                    .append("<p class='message'>Sua compra foi processada com sucesso. Obrigado por comprar conosco!</p>")
                    .append("<a href='/produtos' class='btn btn-home'>Voltar para a Loja</a>")
                    .append("</body>")
                    .append("</html>");
                
                response.getWriter().write(html.toString());
                return;
            }
        }
        
        // Em caso de erro
        response.sendRedirect("/cliente/carrinho?error=1");
    }
}