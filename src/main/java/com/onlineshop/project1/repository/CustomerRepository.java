package com.onlineshop.project1.repository;

import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.entity.Product;
import com.onlineshop.project1.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        } finally {
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




    public boolean updateCustomer(int customerId,String customerName, String customerAddress, String customerPhoneNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;

        try {
            conn = DatabaseConnection.getConnection();
            String updateCustomer = "UPDATE customers SET customer_name = ?, customer_address = ?, customer_phone_number = ? WHERE customer_id = ?";

            stmt = conn.prepareStatement(updateCustomer);

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



    public List<String> getAllCustomerPhoneNumbers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<String> phoneNumbers = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            String getPhoneNumbersQuery = "SELECT customer_phone_number FROM customers";
            stmt = conn.prepareStatement(getPhoneNumbersQuery);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                phoneNumbers.add(resultSet.getString("customer_phone_number"));
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
        return phoneNumbers;
    }





}
