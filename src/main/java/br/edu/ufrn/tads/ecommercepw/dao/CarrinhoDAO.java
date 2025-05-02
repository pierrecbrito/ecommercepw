package br.edu.ufrn.tads.ecommercepw.dao;

import br.edu.ufrn.tads.ecommercepw.connection.DatabaseConnection;
import br.edu.ufrn.tads.ecommercepw.model.Carrinho;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CarrinhoDAO {

    public Optional<Carrinho> buscarPorUsuario(Long usuarioId) {
        String sql = "SELECT c.id, c.usuario_id, ci.produto_id, ci.quantidade " +
                     "FROM carrinhos c LEFT JOIN carrinho_itens ci ON c.id = ci.carrinho_id " +
                     "WHERE c.usuario_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                Carrinho carrinho = null;
                
                while (rs.next()) {
                    if (carrinho == null) {
                        carrinho = new Carrinho();
                        carrinho.setId(rs.getLong("id"));
                        carrinho.setUsuarioId(rs.getLong("usuario_id"));
                    }
                    
                    Long produtoId = rs.getLong("produto_id");
                    if (!rs.wasNull()) {
                        int quantidade = rs.getInt("quantidade");
                        carrinho.adicionarItem(produtoId, quantidade);
                    }
                }
                
                if (carrinho != null) {
                    return Optional.of(carrinho);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    public boolean salvar(Carrinho carrinho) {
        String sqlCarrinho = "INSERT INTO carrinhos (usuario_id) VALUES (?) ON CONFLICT (usuario_id) DO UPDATE SET usuario_id = EXCLUDED.usuario_id RETURNING id";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlCarrinho)) {
                stmt.setLong(1, carrinho.getUsuarioId());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Long carrinhoId = rs.getLong("id");
                        carrinho.setId(carrinhoId);
                        
                        // Delete existing items
                        String sqlDeleteItens = "DELETE FROM carrinho_itens WHERE carrinho_id = ?";
                        try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDeleteItens)) {
                            stmtDelete.setLong(1, carrinhoId);
                            stmtDelete.executeUpdate();
                        }
                        
                        // Insert new items
                        String sqlInsertItem = "INSERT INTO carrinho_itens (carrinho_id, produto_id, quantidade) VALUES (?, ?, ?)";
                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertItem)) {
                            for (Map.Entry<Long, Integer> item : carrinho.getItens().entrySet()) {
                                stmtInsert.setLong(1, carrinhoId);
                                stmtInsert.setLong(2, item.getKey());
                                stmtInsert.setInt(3, item.getValue());
                                stmtInsert.addBatch();
                            }
                            stmtInsert.executeBatch();
                        }
                        
                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean limparCarrinho(Long usuarioId) {
        String sql = "DELETE FROM carrinho_itens WHERE carrinho_id = (SELECT id FROM carrinhos WHERE usuario_id = ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, usuarioId);
            
            return stmt.executeUpdate() >= 0;  // Success even if no rows deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}