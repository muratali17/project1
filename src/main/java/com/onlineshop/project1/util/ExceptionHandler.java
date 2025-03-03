package com.onlineshop.project1.util;

import javafx.scene.control.Alert;

public class ExceptionHandler {

    public static void handleException(Exception e,String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();

    }
}
