module com.github.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;

    opens com.github.example to javafx.fxml; // Abre el paquete principal para JavaFX
    opens com.github.example.view to javafx.fxml; // Abre el paquete de la vista para JavaFX
    opens com.github.example.model.XML to java.xml.bind;
    exports com.github.example.model.Entity;

    exports com.github.example; // Exporta el paquete principal
    exports com.github.example.view;
    exports com.github.example.Test;
    opens com.github.example.Test to javafx.fxml; // Exporta el paquete de vista
}
