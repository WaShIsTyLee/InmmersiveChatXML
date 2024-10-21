module com.github.example {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.desktop;
    opens com.github.example to javafx.fxml;
    opens com.github.example.view to javafx.fxml;
    opens com.github.example.model.XML to java.xml.bind;
    opens com.github.example.model.Entity to java.xml.bind;
    exports com.github.example;
    exports com.github.example.view;
    exports com.github.example.model.Entity;
    exports com.github.example.Test;

}
