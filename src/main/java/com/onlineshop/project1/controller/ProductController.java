package com.onlineshop.project1.controller;


import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.entity.Product;
import com.onlineshop.project1.repository.ProductRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

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

    private final ProductRepository productRepository = new ProductRepository();

    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onDelete(ActionEvent event) {
        if(productIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to delete customer!");
            alert.show();
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(productIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Id must be integer!");
            alert.show();
            return;
        }

        Product product = productRepository.findById(productId);

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(String.format("There is no such a user in the system with the id: %s", productId));
            alert.show();
            return;
        }

        boolean isSuccess = productRepository.deleteProductById(productId);

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText(String.format("Product with the id: %s has been deleted successfully.",productId));
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }


    }

    @FXML
    void onFetch(ActionEvent event) {
        if(productIdText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to get customer!");
            alert.show();
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(productIdText.getText());
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Id must be integer!");
            alert.show();
            return;
        }

        Product product = productRepository.findById(productId);

        if(product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Product not found!");
            alert.show();
        }else{
            nameText.setText(product.getProductName());
            priceText.setText(product.getProductPrice().toPlainString());
            supplierText.setText(product.getSupplierName());
        }

    }

    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onUpdate(ActionEvent event) {
        int productId = Integer.parseInt(productIdText.getText());
        String productName = nameText.getText();
        String supplierName = supplierText.getText();
        String productPrice = priceText.getText();

        StringBuilder errorMessages = new StringBuilder();

        if (productName == null || productName.trim().isEmpty()) {
            errorMessages.append("Product name can not be empty\n");
        }
        if (supplierName == null || supplierName.trim().isEmpty()) {
            errorMessages.append("Supplier name can not be empty\n");
        }
        if (productPrice == null || !productPrice.matches("\\d+")) {
            errorMessages.append("Product price should only contain numbers and can not be empty\n");
        }

        if (!errorMessages.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessages.toString());
            errorAlert.showAndWait();
            return;
        }

        boolean isSuccess = productRepository.updateProduct(productId,productName,supplierName,productPrice);

        if(isSuccess){
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setContentText("User has been inserted successfully.");
            successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
            successAlert.getDialogPane().setPrefSize(400, 150);
            successAlert.show();
        }

    }

}
