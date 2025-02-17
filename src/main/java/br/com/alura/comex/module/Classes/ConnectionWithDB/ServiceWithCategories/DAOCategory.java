package br.com.alura.comex.module.Classes.ConnectionWithDB.ServiceWithCategories;

import br.com.alura.comex.module.Classes.Category;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DAOCategory {
    private Connection conn;
    DAOCategory(Connection connection){
        this.conn = connection;
    }

    public void save(Category category){
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1,category.getName());
            preparedStatement.setString(2, category.getDescription());

            preparedStatement.execute();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Category> show(){
        PreparedStatement ps;
        ResultSet rs;
        Set<Category> categories = new HashSet<>();
        String sql = "SELECT * from categoria";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);

                categories.add(new Category(id,name,description));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public void alter(String sql, int number, String update) {
        PreparedStatement ps;

        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            ps.setString(1, update);
            ps.setInt(2, number);

            conn.commit();
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            try{
                conn.rollback();
            } catch (SQLException ex){
                throw new RuntimeException(e);
            }
        }
    }

    public Category categoryListener(int idCategory) {
        String sql = "SELECT * FROM conta WHERE nome = " + idCategory;

        Statement ps;
        ResultSet rs;
        Category category = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt(1);
                String nameString = rs.getString(2);
                String description = rs.getString(3);

                category = new Category(id,nameString,description);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    public void delete(int idCategory){
        String sql = "DELETE FROM categoria WHERE nome = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idCategory);

            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
