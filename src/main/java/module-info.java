module com.devway {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.devway to javafx.fxml, com.google.gson;
    opens com.devway.model to javafx.fxml, com.google.gson, javafx.base;
    opens com.devway.ui.controllers to javafx.fxml;
    exports com.devway;
    exports com.devway.ui.controllers;
}
