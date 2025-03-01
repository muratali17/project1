module com.onlineshop.project1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.management;


    opens com.onlineshop.project1 to javafx.fxml;
    exports com.onlineshop.project1;
    exports com.onlineshop.project1.controller;
    opens com.onlineshop.project1.controller to javafx.fxml;
}