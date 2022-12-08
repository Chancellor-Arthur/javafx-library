module com.republic.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.republic.library.controllers to javafx.fxml;
    opens com.republic.library.entities to javafx.base;
    exports com.republic.library;
    opens com.republic.library.utils to javafx.base;
}