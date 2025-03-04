package com.onlineshop.project1.controller;


import com.onlineshop.project1.HelloApplication;
import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.entity.Product;
import com.onlineshop.project1.repository.ProductRepository;
import com.onlineshop.project1.util.ExceptionHandler;
import javafx.application.Platform;
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

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.SQLException;

public class ProductController {

    @FXML
    private Button closeBtn;

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
    private Label priceLabel;

    @FXML
    private TextField priceText;

    @FXML
    private Label productIdLabel;

    @FXML
    private TextField productIdText;

    @FXML
    private Button saveBtn;

    @FXML
    private Label supplierLabel;

    @FXML
    private TextField supplierText;

    @FXML
    private Button updateBtn;

    @FXML
    private Button customerBtn;

    @FXML
    private Button clearBtn;

    private final ProductRepository productRepository = new ProductRepository();

    @FXML
    void onClose(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onDelete(ActionEvent event) {
        if(productIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to delete product!");
            alert.show();
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(productIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Product Id must be integer!");
            alert.show();
            return;
        }

        try {
            Product product = productRepository.findById(productId);

            if (product == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("There is no such a product in the system with the id: %s", productId));
                alert.show();
                return;
            }

            boolean isSuccess = productRepository.deleteProductById(productId);

            if (isSuccess) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText(String.format("Product with the id: %s has been deleted successfully.", productId));
                successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
                successAlert.getDialogPane().setPrefSize(400, 150);
                successAlert.show();
            }
        }catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }


    }

    @FXML
    void onFetch(ActionEvent event) {
        if(productIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter an integer number to product id to get product!");
            alert.show();
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(productIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Product Id must be integer!");
            alert.show();
            return;
        }
        try {
            Product product = productRepository.findById(productId);

            if (product == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Product not found!");
                alert.show();
            } else {
                nameText.setText(product.getProductName());
                priceText.setText(product.getProductPrice().toPlainString());
                supplierText.setText(product.getSupplierName());
            }
        }
        catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }

    }

    @FXML
    void onSave(ActionEvent event) {
        String productId = productIdText.getText();
        String productName = nameText.getText().trim();
        String supplierName = supplierText.getText().trim();
        String productPrice = priceText.getText().strip();


        StringBuilder errorMessages = new StringBuilder();

        if(productId.isBlank() || !productId.matches("\\d+")) {
            errorMessages.append("Product Id can not be empty and it must be an integer!\n");
        }
        if (productName.isBlank()|| !productName.matches("^[a-zA-Z]+(\\s[a-zA-z]+)*$") || productName.length() > 16) {
            errorMessages.append("Product name can not be empty and must be between 2-16 character\n");
        }
        if (supplierName.isBlank() || !supplierName.matches("^[a-zA-Z]+(\\s[a-zA-z]+)*$") || productName.length() > 16) {
            errorMessages.append("Supplier name can not be empty and must be between 2-16 letters\n");
        }
        if (productPrice.isBlank() || !productPrice.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")) {
            errorMessages.append("Product price should be either decimal or integer and can not be empty\n");
        }

        if(!errorMessages.isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessages.toString());
            errorAlert.showAndWait();
            return;
        }

        BigDecimal productPriceBigDecimal = new BigDecimal(productPrice);
        int productIdInt = Integer.parseInt(productId);
        try {
            boolean isSuccess = productRepository.saveProduct(productIdInt, productName, supplierName, productPriceBigDecimal);

            if (isSuccess) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Product has been inserted successfully.");
                successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
                successAlert.getDialogPane().setPrefSize(400, 150);
                successAlert.show();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Something went wrong, Check if there is already a product with the id: " + productId);
                errorAlert.getDialogPane().setPrefSize(450, 150);
                errorAlert.show();
            }
        }
        catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (RuntimeException ex){
            ExceptionHandler.handleException(ex,ex.getMessage());
        }
    }



    @FXML
    void onUpdate(ActionEvent event) {
        String productId = productIdText.getText();
        String productName = nameText.getText();
        String supplierName = supplierText.getText();
        String productPrice = priceText.getText();

        StringBuilder errorMessages = new StringBuilder();

        if(productId.isBlank() || !productId.matches("\\d+")) {
            errorMessages.append("Product Id can not be empty and it must be an integer!\n");
        }
        if (productName.isBlank()|| !productName.matches("^[a-zA-Z]+(\\s[a-zA-z]+)*$") || productName.length() > 16) {
            errorMessages.append("Product name can not be empty and must be between 2-16 character\n");
        }
        if (supplierName.isBlank() || !supplierName.matches("^[a-zA-Z]+(\\s[a-zA-z]+)*$") || productName.length() > 16) {
            errorMessages.append("Supplier name can not be empty and must be between 2-16 letters\n");
        }
        if (productPrice.isBlank() || !productPrice.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")) {
            errorMessages.append("Product price should be either decimal or integer and can not be empty\n");
        }

        if (!errorMessages.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessages.toString());
            errorAlert.showAndWait();
            return;
        }

        BigDecimal productPriceBigDecimal = new BigDecimal(productPrice);
        int productIdInt = Integer.parseInt(productId);


        try{
            boolean isSuccess = productRepository.updateProduct(productIdInt,productName,supplierName,productPriceBigDecimal);

            if(isSuccess){
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Product has been updated successfully.");
                successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
                successAlert.getDialogPane().setPrefSize(400, 150);
                successAlert.show();
            }

            else{
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Something went wrong, Check if there is a product with the id: "+productId);
                errorAlert.getDialogPane().setPrefSize(400, 150);
                errorAlert.show();
            }
        }
        catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (RuntimeException e){
            if(e.getMessage().contains("This Supplier Name and its product already exists with the ProductId")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText(e.getMessage());
                errorAlert.getDialogPane().setPrefSize(400, 150);
                errorAlert.show();
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setContentText(e.getMessage());
                errorAlert.getDialogPane().setPrefSize(400, 150);
                errorAlert.show();
            }
        }

    }
    @FXML
    void onCustomer(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("customer-view.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Customer View");

        newStage.show();
    }

    @FXML
    void onClear(ActionEvent event) {
        productIdText.clear();
        nameText.clear();
        supplierText.clear();
        priceText.clear();
    }




}
