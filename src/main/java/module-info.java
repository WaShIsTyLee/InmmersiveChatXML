module com.github.example {
    // Requisitos para JavaFX y JAXB
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;

    // Aperturas necesarias para acceso reflexivo por JavaFX y JAXB
    opens com.github.example to javafx.fxml;  // JavaFX necesita acceso a este paquete
    opens com.github.example.view to javafx.fxml; // Paquete de vista para JavaFX
    opens com.github.example.model.XML to java.xml.bind; // Paquete de XML para JAXB
    opens com.github.example.model.Entity to java.xml.bind; // JAXB necesita acceso a las entidades

    // Exportar paquetes (solo clases públicas serán accesibles desde fuera)
    exports com.github.example;  // Exporta el paquete principal
    exports com.github.example.view;  // Exporta la vista
    exports com.github.example.model.Entity;  // Exporta las entidades
    exports com.github.example.Test;  // Exporta las clases de prueba (si es necesario)

    // Si no es necesario, puedes quitar esta línea, ya que `Test` probablemente no necesite ser abierto
    // opens com.github.example.Test to javafx.fxml;
}
