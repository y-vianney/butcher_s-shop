module com.devway {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.devway to javafx.fxml;
    opens com.devway.ui.controllers to javafx.fxml;
    opens com.devway.model to com.google.gson;
    exports com.devway;
    exports com.devway.ui.controllers;
}
