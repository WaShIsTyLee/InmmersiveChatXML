package com.github.example.view;

import com.github.example.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController extends Controller implements Initializable {

    @FXML
    Rectangle rectanguloBoton;





    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void changeSceneToInicioSesion() throws Exception {
        App.currentController.changeScene(Scenes.INICIOSESION, null);
        System.out.println("hola");
    }
}
