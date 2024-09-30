module com.devway {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.logging;
    requires org.apache.pdfbox;

    opens com.devway to javafx.fxml, com.google.gson;
    opens com.devway.model to javafx.fxml, com.google.gson, javafx.base;
    opens com.devway.ui.controllers to javafx.fxml, com.google.gson;
    exports com.devway;
    exports com.devway.ui.controllers;
}
