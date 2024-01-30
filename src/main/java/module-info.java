module org.example.bharatintern {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.net.http;
    requires javafx.graphics;

    opens org.example.bharatintern to javafx.fxml;
    exports org.example.bharatintern;

    exports task02;

    opens task02 to javafx.fxml;
    opens task01 to javafx.graphics;
    exports task03;
//    requires javafx.controls;
//    requires javafx.fxml;
}