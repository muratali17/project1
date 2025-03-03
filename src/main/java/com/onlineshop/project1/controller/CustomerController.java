package com.onlineshop.project1.controller;

import com.onlineshop.project1.HelloApplication;
import com.onlineshop.project1.entity.Customer;
import com.onlineshop.project1.repository.CustomerRepository;
import com.onlineshop.project1.util.ExceptionHandler;
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

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


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


    /***
     * Bu fonksiyon, kullanıcı pencereyi kapatma butonuna bastığında çağrılır ve
     * mevcut pencereyi kapatır.
     */
    @FXML
    void onClose(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    /***
     * Kullanıcıyı ID ile siler.
     * Bu fonksiyon, müşteri ID'sini alır, geçerliliğini kontrol eder ve eğer müşteri bulunursa
     * o müşteri veritabanından silinir. Başarı durumu bir bilgi mesajı ile kullanıcıya bildirilir.
     */
    @FXML
    void onDelete(ActionEvent event) {

        String customerId = customerIdText.getText().strip();

        if (customerId.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to delete customer!");
            alert.show();
            return;
        }
        Optional<Integer> customerIdInt = convertCustomerIdToInteger(customerId);

        if(customerIdInt.isEmpty()){    // stop if NumberOfException is thrown inside the convertCustomerIdToInteger function.
            return;
        }

        try{
            Customer customer = customerRepository.findById(customerIdInt.get());



            if (customer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("There is no such a user in the system with the id: %s", customerId));
                alert.show();
                return;
            }

            boolean isSuccess = customerRepository.deleteCustomerById(customerIdInt.get());

            if (isSuccess) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText(String.format("User with the id: %s has been deleted successfully.", customerId));
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


    /***
     * Kullanıcıdan alınan ID ile veritabanında arama yapılır ve müşteri bilgileri,
     * formdaki ilgili alanlara doldurulur. Eğer müşteri bulunamazsa, hata mesajı gösterilir.
     */
    @FXML
    void onFetch(ActionEvent event) {

        String customerId = customerIdText.getText().strip();

        if (customerId.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Enter a integer number to get customer!");
            alert.show();
            return;
        }
        Optional<Integer> customerIdInt = convertCustomerIdToInteger(customerId);

        if(customerIdInt.isEmpty()){    // it is gonna stop if NumberOfException is thrown inside the convertCustomerIdToInteger function.
            return;
        }
        try{
            Customer customer = customerRepository.findById(customerIdInt.get());

            if (customer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("There is no customer with the id: %s.",customerId));
                alert.show();
            } else {
                nameText.setText(customer.getCustomerName());
                addressText.setText(customer.getCustomerAddress());
                telephoneText.setText(customer.getCustomerPhoneNumber());
            }
        }catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }

    }


    /***
     * Bu fonksiyon, kullanıcı "Product" butonuna bastığında çağrılır ve yeni bir pencere açarak
     * ürün bilgileri sayfasına yönlendirir.
     */
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
     * İlk olarak, kullanıcıdan alınan müşteri ID'si bir tamsayıya dönüştürülür. Eğer bu dönüşüm başarısız olursa işlem sonlanır.(Harf iceriyor ise NumberFormatException aliriz.)
     * Ardından, bu ID'ye sahip bir müşteri veritabanında var mı diye kontrol edilir.
     * Eğer varsa, kullanıcıya hata mesajı gösterilir.
     * Eğer müşteri yoksa, kullanıcıdan alınan tüm diğer bilgiler doğrulandıktan sonra müşteri kaydedilir.(Bu asamada da eger hata cikar ise kullanici bildirilir ve save islemi durur.)
     * Başarıyla kaydedilirse, başarılı işlem mesajı görüntülenir.
     */
    @FXML
    void onSave(ActionEvent event) {
        String customerId = customerIdText.getText().strip();
        String customerName = nameText.getText().trim();
        String customerAddress = addressText.getText().trim();
        String customerPhoneNumber = telephoneText.getText().strip();

        Optional<Integer> customerIdInt = convertCustomerIdToInteger(customerId);

        if(customerIdInt.isEmpty()){    // stop if NumberOfException is thrown inside the convertCustomerIdToInteger function.
            return;
        }

        try {
            Customer existsCustomer = customerRepository.findById(customerIdInt.get());

            if (existsCustomer != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("There is already a customer with the Id: %s in the system.", customerId));
                alert.show();
                return;
            }

            boolean isValid = validateInputValues(
                    customerId,
                    customerName,
                    customerAddress,
                    customerPhoneNumber,
                    false
            );
            if (!isValid) return;    //If validation exception occurred then stop.

            boolean isSuccess = customerRepository
                    .saveCustomer(
                            customerIdInt.get(),
                            customerName,
                            customerAddress,
                            customerPhoneNumber
                    );

            if (isSuccess) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("User has been updated successfully.");
                successAlert.getDialogPane().setStyle("-fx-background-color: #06b306;");
                successAlert.getDialogPane().setPrefSize(400, 150);
                successAlert.show();
            }
        }
        catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }

    }


    /***
     * İlk olarak, kullanıcıdan alınan müşteri ID'si bir tamsayıya dönüştürülür. Eğer bu dönüşüm başarısız olursa işlem sonlanır.(Harf iceriyor ise NumberFormatException aliriz.)
     * Ardından, bu ID'ye sahip müşteri veritabanında aranır. Eğer müşteri bulunmazsa, kullanıcıya hata mesajı gösterilir.
     * Eğer müşteri varsa, yeni bilgiler doğrulanır. (Bu asamada da eger hata cikar ise kullanici bildirilir ve update islemi durur.)
     * Mevcut bilgilerle yeni bilgiler karşılaştırılır ve değişiklik varsa güncelleme yapılır.
     * Güncelleme işlemi başarılıysa, kullanıcıya bilgi mesajı gösterilir.
     */
    @FXML
    void onUpdate(ActionEvent event) {
        String customerId= customerIdText.getText().strip();
        String newCustomerName = nameText.getText().trim();
        String newCustomerAddress = addressText.getText().trim();
        String newCustomerPhoneNumber = telephoneText.getText().strip();

        Optional<Integer> customerIdInt = convertCustomerIdToInteger(customerId);

        if(customerIdInt.isEmpty()){    // stop if NumberOfException is thrown inside the convertCustomerIdToInteger function.
            return;
        }
        try {
            Customer existsCustomer = customerRepository.findById(customerIdInt.get());

            if (existsCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("There is no such a customer with the Id: %s in the system.", customerId));
                alert.show();
                return;
            }

            boolean isValid = validateInputValues(
                    customerId,
                    newCustomerName,
                    newCustomerAddress,
                    newCustomerPhoneNumber,
                    true
            );
            if (!isValid) return;    // If validation exception occurred then stop.


            boolean isChanged = hasCustomerDetailsChanged(
                    existsCustomer,
                    newCustomerName,
                    newCustomerAddress,
                    newCustomerPhoneNumber
            );

            if (!isChanged) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("User is already Up-to-date. Please enter new information.");
                successAlert.getDialogPane().setStyle("-fx-background-color: #0859c5;");
                successAlert.getDialogPane().setPrefSize(400, 150);
                successAlert.show();
                return;
            }


            boolean isSuccess = customerRepository
                    .updateCustomer(
                            customerIdInt.get(),
                            newCustomerName,
                            newCustomerAddress,
                            newCustomerPhoneNumber
                    );

            if (isSuccess) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("User has been updated successfully.");
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


    /***
     * Kullanıcı girdiği bilgileri doğrular ve hata mesajlarını toplar.
     * Ayrıca telefon numarasının veritabanında mevcut olup olmadığı da kontrol edilir.
     * onUpdate ve onSave islemleri icin farkli davranis gösterir.
     * Boilerplate kod yazilmasini engeller.
     */
    private boolean validateInputValues(String customerId, String customerName, String customerAddress, String customerPhoneNumber, boolean isUpdate) {
        StringBuilder errorMessages = new StringBuilder();

        if (customerId.isBlank() || !customerId.matches("\\d+")) {
            errorMessages.append("Customer Id cannot be blank and it must be an integer!\n");
        }
        if (customerName.isBlank() || !customerName.matches("^[A-Za-z]+( [A-Za-z]+)*$") || customerName.length() > 16) {
            errorMessages.append("Customer name should only contain English letters and be at most 16 characters!\n");
        }
        if (customerAddress.isBlank() || !customerAddress.matches("^[A-Za-z0-9]+([ ,.\\-][A-Za-z0-9]+)*$") || customerAddress.length() > 32) {
            errorMessages.append("Address cannot be empty and must be at most 32 characters, only English letters, numbers, space, comma, dot, and dash allowed!\n");
        }
        if (customerPhoneNumber.isBlank() || !customerPhoneNumber.matches("^\\+90-\\d{3}-\\d{3}-\\d{4}$")) {
            errorMessages.append("Phone Number should be in +90-xxx-xxx-xxxx and cannot be empty!\n");
        }

        try {

            List<String> existsPhoneNumbers = customerRepository.getAllCustomerPhoneNumbers();

            if (isUpdate) {
                int customerIdInt = convertCustomerIdToInteger(customerId).get();
                Customer repoCustomer = customerRepository.findById(customerIdInt);
                existsPhoneNumbers.remove(repoCustomer.getCustomerPhoneNumber());
            }

            boolean phoneExists = existsPhoneNumbers.stream()
                    .anyMatch(phone -> phone.equals(customerPhoneNumber));

            if (phoneExists) {
                errorMessages.append("Phone Number already exists in the database!\n");
            }

            if (!errorMessages.isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setContentText(errorMessages.toString());
                errorAlert.showAndWait();
                return false;
            }
        }catch (SQLException e){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        catch (ConnectException e ){
            ExceptionHandler.handleException(e,e.getMessage());
        }
        return true;
    }



    /***
     * Müşteri bilgileriyle yeni girilen değerleri karşılaştırarak değişiklik olup olmadığını kontrol eder.
     * Boilerplate kod yazilmasini engeller.
     */
    private boolean hasCustomerDetailsChanged(Customer customer,String newCustomerName, String newCustomerAddress, String newCustomerPhoneNumber ) {
        boolean isChanged = false;

        if(!customer.getCustomerName().equals(newCustomerName)){
            isChanged = true;
            return isChanged;
        }
        if(!customer.getCustomerAddress().equals(newCustomerAddress)){
            isChanged = true;
            return isChanged;
        }
        if(!customer.getCustomerPhoneNumber().equals(newCustomerPhoneNumber)){
            isChanged = true;
            return isChanged;
        }
        return isChanged;

    }


    /***
     * Müşteri ID'sini String'den Integer'a dönüştürür.
     * NumberFormatException hatasi cikar ise burda handle edilir.
     * Boilerplate kod yazilmasini engeller.
     */
    private Optional<Integer> convertCustomerIdToInteger(String customerIdString) {
        try {
            return Optional.of(Integer.parseInt(customerIdString));
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer Id must be integer!");
            alert.show();
            return Optional.empty();
        }
    }


}
