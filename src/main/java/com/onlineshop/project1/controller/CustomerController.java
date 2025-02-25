package com.onlineshop.project1.controller;

import com.onlineshop.project1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    void onClose(ActionEvent event) {

    }

    @FXML
    void onDelete(ActionEvent event) {

    }

    @FXML
    void onFetch(ActionEvent event) {





    }

    @FXML
    void onProduct(ActionEvent event) throws Exception {
        // 1. Load the FXML file for the new window
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("product-view.fxml"));
        Parent root = loader.load();

        // 2. Create a new Stage (window)
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Product View");

        // 3. Show the new window
        newStage.show();
    }


    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onUpdate(ActionEvent event) {

    }

}
