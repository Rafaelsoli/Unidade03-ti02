package dao;

import model.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends DAO {
    public ProdutoDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(Produto produto) {
        boolean status = false;
        try {
            String sql = "INSERT INTO produto (Nome, Marca, preco, quantidade, dataLancamento) "
                       + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, produto.getNome());
            st.setString(2, produto.getMarca());
            st.setFloat(3, produto.getPreco());
            st.setInt(4, produto.getQuantidade());
            st.setTimestamp(5, Timestamp.valueOf(produto.getDataLancamento()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    

    public Produto get(int id) {
        Produto produto = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM animacao WHERE id=" + id;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                produto = new Produto(rs.getInt("id"), rs.getString("Nome"), rs.getString("Marca"), rs.getFloat("preco"),
                        rs.getInt("quantidade"), rs.getTimestamp("dataLancamento").toLocalDateTime());
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return produto;
    }


    public List<Produto> get() {
        return get("");
    }

    public List<Produto> getOrderByID() {
        return get("id");
    }

    public List<Produto> getOrderByMarca() {
        return get("marca");
    }
    
    public List<Produto> getOrderByNome() {
        return get("nome");
    }

    public List<Produto> getOrderByPreco() {
        return get("preco");
    }

    private List<Produto> get(String orderBy) {
        List<Produto> produtos = new ArrayList<Produto>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM produto" + ((orderBy.trim().isEmpty()) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Produto p = new Produto(rs.getInt("id"), rs.getString("Nome"), rs.getString("Marca"),
                        rs.getFloat("preco"), rs.getInt("quantidade"),
                        rs.getTimestamp("dataLancamento").toLocalDateTime());
                produtos.add(p);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return produtos;
    }

    public boolean update(Produto produto) {
        boolean status = false;
        try {
            String sql = "UPDATE produto SET Nome = ?, Marca = ?, preco = ?, quantidade = ?, dataLancamento = ? WHERE id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, produto.getNome());
            st.setString(2, produto.getMarca());
            st.setFloat(3, produto.getPreco());
            st.setInt(4, produto.getQuantidade());
            st.setTimestamp(5, Timestamp.valueOf(produto.getDataLancamento()));
            st.setInt(6, produto.getId());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }


    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM produto WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
}

