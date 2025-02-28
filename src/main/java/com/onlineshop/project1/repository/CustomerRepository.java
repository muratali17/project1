package com.onlineshop.project1.repository;

import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.entity.Product;
import com.onlineshop.project1.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {

    public Customer findById(int customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Customer customer = null;

        try {
            conn = DatabaseConnection.getConnection();
            String getCustomerQuery = "SELECT * FROM customers WHERE customer_id = ?";
            stmt = conn.prepareStatement(getCustomerQuery);
            stmt.setInt(1, customerId);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                customer = new Customer();
                customer.setCustomerId(resultSet.getInt(1));
                customer.setCustomerName(resultSet.getString(2));
                customer.setCustomerAddress(resultSet.getString(3));
                customer.setCustomerPhoneNumber(resultSet.getString(4));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally { // her halükarda finally blogu calisir ve kaynaklar kapanir.
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }

        return customer;
    }



    public boolean deleteCustomerById(int customerId){
        Connection conn = null;
        PreparedStatement stmt = null;
        Customer customer = null;
        boolean isSuccess = false;

        try {
            conn = DatabaseConnection.getConnection();
            String deleteCustomerQuery = "DELETE FROM customers WHERE customer_id = ?";

            stmt = conn.prepareStatement(deleteCustomerQuery);
            stmt.setInt(1,customerId);

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

    public boolean saveCustomer(int customerId, String customerName, String customerAddress, String customerPhoneNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;


        try {
            conn = DatabaseConnection.getConnection();
            String saveCustomer = "INSERT INTO customers (customer_id,customer_name, customer_address, customer_phone_number) VALUES (?,?,?,?)";

            stmt = conn.prepareStatement(saveCustomer);
            stmt.setInt(1, customerId);
            stmt.setString(2, customerName);
            stmt.setString(3, customerAddress);
            stmt.setString(4, customerPhoneNumber);

            int effectedRowCount = stmt.executeUpdate();

            if(effectedRowCount == 1) isSuccess = true;


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }


        return isSuccess;
    }

    public boolean updateCustomer(int customerId,String customerName, String customerAddress, String customerPhoneNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;

        try {

            if(!isUpdated(customerId, customerName, customerAddress, customerPhoneNumber)) {
                throw new RuntimeException("You didnt update the product");
            }

            conn = DatabaseConnection.getConnection();
            String updateCustomer = "UPDATE customers SET customer_name = ?, customer_address = ?, customer_phone_number = ? WHERE customer_id = ?";

            stmt = conn.prepareStatement(updateCustomer);

            // Parametreleri PreparedStatement ile ayarlıyoruz
            stmt.setString(1, customerName);
            stmt.setString(2, customerAddress);
            stmt.setString(3, customerPhoneNumber);
            stmt.setInt(4, customerId);


            int effectedRowCount = stmt.executeUpdate();

            if(effectedRowCount == 1) isSuccess = true;

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }


        return isSuccess;

    }

    public boolean isUpdated(int customerId, String customerName, String customerAddress, String customerPhoneNumber ) throws SQLException {

        boolean isChanged = false;

        Customer customer = findById(customerId);

        if(customer == null){
            throw new SQLException("There is no customer with id " + customerId);
        }

        if(!customer.getCustomerName().equals(customerName)){
            isChanged = true;
            return isChanged;
        }
        if(!customer.getCustomerAddress().equals(customerAddress)){
            isChanged = true;
            return isChanged;
        }
        if(!customer.getCustomerPhoneNumber().equals(customerPhoneNumber)){
            isChanged = true;
            return isChanged;
        }

        return isChanged;

    }

}
