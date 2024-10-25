package com.github.example.view;

import com.github.example.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {


    @FXML
    private BorderPane bp;
    private Controller centerController;
    @FXML
    static Alert alertError = new Alert(Alert.AlertType.ERROR);

    @FXML
    public static void showAlertForLogin() {
        alertError.setTitle("❌ Error al registrarse ❌");
        alertError.setHeaderText(null);
        alertError.setContentText("La contraseña o el Email no coinciden, inténtelo de nuevo.");
        alertError.showAndWait();
    }

    @FXML
    public static void showAlertForContactSelected() {
        alertError.setTitle("❌ Error al seleccionar contacto ❌");
        alertError.setHeaderText(null);
        alertError.setContentText("No has seleccionado ningún contacto.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForPassword() {
        alertError.setTitle("❌ Error en el campo contraseña ❌");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- La contraseña debe contener al menos 8 caracteres y debe incluir al menos una letra mayúscula, \n " +
                "una letra minúscula, un número y un carácter especial.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForEmail() {
        alertError.setTitle("❌ Error en el email ❌");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- El correo electrónico debe ser una dirección válida de Gmail, Outlook o Hotmail. \n" +
                "- El correo introducido ya existe, por favor elija otro.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForAddContact() {
        alertError.setTitle("❌ Error al añadir contacto ❌");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- Ese contacto no existe en nuestra base de datos, por favor elija otro.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForContactRepetido() {
        alertError.setTitle("❌ Error al añadir contacto ❌");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- Ese contacto ya existe en tu lista.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForNickname() {
        alertError.setTitle("❌ Error al registrarse ❌");
        alertError.setHeaderText(null);
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setContentText("Por favor, revise la información proporcionada, y vuelva a intentarlo:\n\n" +
                "- El nombre de usuario puede estar en uso, por favor elija otro.");
        alertError.showAndWait();
    }


    @Override
    public void onOpen(Object input) throws Exception {
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }


    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }

    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL();
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }


    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene);
        bp.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }


    public void openModal(Scenes scene, String title, Controller parent, Object data) throws Exception {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }


}



