package com.onlineshop.project1.controller;

import com.onlineshop.project1.HelloApplication;
import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.repository.CustomerRepository;
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

import java.util.List;


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
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
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
            alert.setContentText("Customer not found!");
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

    /***
     *
     * @param event
     *
     * daha sonra bakılacak validasyon hataları kısmına.
     */
    @FXML
    void onSave(ActionEvent event) {
        String customerName = nameText.getText().trim();
        String customerAddress = addressText.getText().trim();
        String customerPhoneNumber = telephoneText.getText().strip();

        StringBuilder errorMessages = new StringBuilder();

        if (customerName.isEmpty() || !customerName.matches("^[a-zA-Z ]+$")) {
            errorMessages.append("Customer name should only letter and  can not be empty!\n");
        }
        if (customerAddress.isEmpty() || !customerAddress.matches("^[a-zA-Z0-9 ,.\\-]{10,}$") ) {
            errorMessages.append("Address should be least 10 characters and can not be empty!\n");
        }
        if (!customerPhoneNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
            errorMessages.append("Phone Number should only in xxx-xxx-xxxx format and can not be empty!\n");
        }

        if (!errorMessages.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Hata!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessages.toString());
            errorAlert.showAndWait();
            return;
        }

        boolean isSuccess = customerRepository.saveCustomer(customerName,customerAddress,customerPhoneNumber);

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("User has been inserted successfully.");
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }



    }


    /***
     *
     * @param event
     *
     * daha sonra bakılacak validasyon hataları kısmına.
     */
    @FXML
    void onUpdate(ActionEvent event) {
        int customerId = Integer.parseInt(customerIdText.getText());
        String customerName = nameText.getText();
        String customerAddress = addressText.getText();
        String customerPhoneNumber = telephoneText.getText();

        StringBuilder errorMessages = new StringBuilder();

        if (customerName == null || customerName.trim().isEmpty()) {
            errorMessages.append("Customer name can not be empty!\n");
        }
        if (customerAddress == null || customerAddress.trim().isEmpty()) {
            errorMessages.append("Address can not be empty!\n");
        }
        if (customerPhoneNumber == null || !customerPhoneNumber.matches("\\d+")) {
            errorMessages.append("Phone Number should only contain numbers and can not be empty\n\n");
        }

        if (!errorMessages.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessages.toString());
            errorAlert.showAndWait();
            return;
        }

        boolean isSuccess = customerRepository.updateCustomer(customerId,customerName,customerAddress,customerPhoneNumber);

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("User has been inserted successfully.");
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }



    }

}
