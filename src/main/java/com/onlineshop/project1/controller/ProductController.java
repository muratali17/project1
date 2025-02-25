package com.onlineshop.project1.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    void onClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onDelete(ActionEvent event) {

    }

    @FXML
    void onFetch(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onUpdate(ActionEvent event) {

    }

}
