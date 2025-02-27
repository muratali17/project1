package com.onlineshop.project1.repository;

import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.entity.Product;
import com.onlineshop.project1.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class  ProductRepository {

    public boolean saveProduct(int productId, String productName, String supplierName, BigDecimal productPrice) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;


        try {
            conn = DatabaseConnection.getConnection();
            String saveProduct = "INSERT INTO products (product_id,product_name, supplier_name, product_price) VALUES (?,?,?,?)";

            stmt = conn.prepareStatement(saveProduct);
            stmt.setInt(1, productId);
            stmt.setString(2, productName);
            stmt.setString(3, supplierName);
            stmt.setBigDecimal(4, productPrice);

            int effectedRowCount = stmt.executeUpdate();

            if(effectedRowCount == 1) isSuccess = true;


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }


        return isSuccess;
    }

    public Product findById(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Product product = null;

        try {
            conn = DatabaseConnection.getConnection();
            String getCustomerQuery = "SELECT * FROM products WHERE product_id = ?";
            stmt = conn.prepareStatement(getCustomerQuery);
            stmt.setInt(1, productId);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                product = new Product();
                product.setProductId(resultSet.getInt(1));
                product.setProductName(resultSet.getString(2));
                product.setSupplierName(resultSet.getString(3));
                product.setProductPrice(resultSet.getBigDecimal(4));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }

        return product;
    }

    public boolean deleteProductById(int productId){
        Connection conn = null;
        PreparedStatement stmt = null;
        Product product = null;
        boolean isSuccess = false;

        try {
            conn = DatabaseConnection.getConnection();
            String deleteCustomerQuery = "DELETE FROM products WHERE product_id = ?";

            stmt = conn.prepareStatement(deleteCustomerQuery);
            stmt.setInt(1,productId);

            int effectedRowCount = stmt.executeUpdate();

            if(effectedRowCount == 1) isSuccess = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
        return isSuccess;
    }

    public boolean updateProduct(int productId, String productName, String productSupplier, BigDecimal productPrice) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;

        try {

            if(!isUpdated(productId, productName, productSupplier, productPrice)){
                throw new RuntimeException("You didnt update the product");
            }

            conn = DatabaseConnection.getConnection();


            String updateProductQuery = "UPDATE products SET product_name = ?, supplier_name = ?, product_price = ? WHERE product_id = ?";

            stmt = conn.prepareStatement(updateProductQuery);



            stmt.setString(1, productName);
            stmt.setString(2, productSupplier);
            stmt.setBigDecimal(3, productPrice);
            stmt.setInt(4, productId);

            int affectedRowCount = stmt.executeUpdate();

            if (affectedRowCount > 0) {
                isSuccess = true;
            }



        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }

        return isSuccess;
    }


    public boolean isUpdated(int productId, String productName, String productSupplier, BigDecimal productPrice ) throws SQLException {

        boolean isChanged = false;

        Product product = findById(productId);

        if(product == null){
            throw new SQLException("There is no product with id " + productId);
        }

        if(!product.getProductName().equals(productName)){
            isChanged = true;
            return isChanged;
        }
        if(!product.getSupplierName().equals(productSupplier)){
            isChanged = true;
            return isChanged;
        }
        if(!product.getProductPrice().equals(productPrice)){
            isChanged = true;
            return isChanged;
        }

        return isChanged;

    }
}
