module com.republic.library {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.republic.library.controllers to javafx.fxml;
    exports com.republic.library;
}