package com.onlineshop.project1.repository;

import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.util.DatabaseConnection;

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
            System.err.println("olm bura bak");
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

            int effectedRowCount = stmt.executeUpdate();        //delete update insert islemlerinde executeUpdate kullan,
                                                                // select isleminde executeQuery Kullan.
            if(effectedRowCount == 1) isSuccess = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return isSuccess;
    }




}
