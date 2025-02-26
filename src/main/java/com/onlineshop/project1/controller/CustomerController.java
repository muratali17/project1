package com.onlineshop.project1.controller;

import com.onlineshop.project1.HelloApplication;
import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.repository.CustomerRepository;
import com.onlineshop.project1.util.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class CustomerController {

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressText;

    @FXML
    private Button closeBtn;

    @FXML
    private Label customerIdLabel;

    @FXML
    private TextField customerIdText;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button fetchBtn;

    @FXML
    private Label headingLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameText;

    @FXML
    private Button productBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Label telephoneLabel;

    @FXML
    private TextField telephoneText;

    @FXML
    private Button updateBtn;


    private final CustomerRepository customerRepository = new CustomerRepository();

    @FXML
    void onClose(ActionEvent event) {

    }

    @FXML
    void onDelete(ActionEvent event) {

        if(customerIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to delete customer!");
            alert.show();
            return;
        }
        int customerId;
        try {
            customerId = Integer.parseInt(customerIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Id must be integer!");
            alert.show();
            return;
        }

        Customer customer = customerRepository.findById(customerId);

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(String.format("There is no such a user in the system with the id: %s", customerId));
            alert.show();
            return;
        }

        boolean isSuccess = customerRepository.deleteCustomerById(customerId);

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText(String.format("User with the id: %s has been deleted successfully.",customerId));
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }


    }

    @FXML
    void onFetch(ActionEvent event) {

        if(customerIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to get customer!");
            alert.show();
            return;
        }
        int customerId;
        try {
             customerId = Integer.parseInt(customerIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Id must be integer!");
            alert.show();
            return;
        }

        Customer customer = customerRepository.findById(customerId);

        if(customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Emp not found!");
            alert.show();
        }else{
            nameText.setText(customer.getCustomerName());
            addressText.setText(customer.getCustomerAddress());
            telephoneText.setText(customer.getCustomerPhoneNumber());
        }

    }

    @FXML
    void onProduct(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("product-view.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Product View");

        newStage.show();
    }


    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onUpdate(ActionEvent event) {
        String customerId = customerIdText.getText();
        String customerName = nameText.getText();
        String customerPhoneNumber = telephoneText.getText();
        String customerAddress = addressText.getText();
        Boolean isSuccess = false;

        if (customerId.isEmpty() || customerName.isEmpty() || customerPhoneNumber.isEmpty() || customerAddress.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        String sql = "UPDATE customers SET customer_name = ?, customer_phone_number = ?, customer_address = ? WHERE customer_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, customerName);
            stmt.setString(2, customerPhoneNumber);
            stmt.setString(3, customerAddress);
            stmt.setInt(4, Integer.parseInt(customerId));

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully!");
                isSuccess = true;
            } else {
                System.out.println("No customer found with ID: " + customerId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText(String.format("User with the id: %s has been updated successfully.",customerId));
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }


    }


}
