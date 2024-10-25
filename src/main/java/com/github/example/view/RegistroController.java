package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroController extends Controller implements Initializable {

    @FXML
    TextField nombre;

    @FXML
    TextField nickname;
    @FXML
    TextField email;
    @FXML
    PasswordField contrasena;
    @FXML
    Button registrar;

    @FXML
    private User takeValuesRegister() throws Exception {
        User nuevoUsuario = getValuesFromRegister();
        if (nuevoUsuario == null) {
            return null;
        }
        registerUser(nuevoUsuario);
        return nuevoUsuario;
    }

    private User getValuesFromRegister() {
        if (nombre == null || contrasena == null || email == null || nickname == null) {
            throw new NullPointerException("Campos no inicializados");
        }
        String nombreText = nombre.getText();
        String password = contrasena.getText();
        if (!User.validatePassword(password)) {
            AppController.showAlertForPassword();
            password = "";
        }
        String emailText = email.getText();
        if (!User.validateEmail(emailText)) {
            AppController.showAlertForEmail();
            emailText= "";
        }
        String nicknameText = nickname.getText();
        if (nombreText.isEmpty() || emailText.isEmpty() || nicknameText.isEmpty() || password.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return null;
        }
        return new User(nombreText, emailText, nicknameText, password);
    }

    private void registerUser(User nuevoUsuario) throws Exception {
        List<User> todosUsuarios = XMLUser.obtenerUsuarios();
        if (todosUsuarios.isEmpty()) {
            nuevoUsuario.setPassword(User.hashPassword(nuevoUsuario.getPassword()));
            XMLUser.agregarUsuario(nuevoUsuario);
            changeSceneToInicioSesion();
        } else {
            boolean nicknameExistente = false;
            boolean emailExistente = false;
            for (User user : todosUsuarios) {
                if (nuevoUsuario.getNickname().equals(user.getNickname())) {
                    nicknameExistente = true;
                }
                if (nuevoUsuario.getEmail().equals(user.getEmail())) {
                    emailExistente = true;
                }
                if (nicknameExistente || emailExistente) {
                    break;
                }
            }
            if (nicknameExistente) {
                AppController.showAlertForNickname();
            } else if (emailExistente) {
                AppController.showAlertForEmail();
            } else {
                nuevoUsuario.setPassword(User.hashPassword(nuevoUsuario.getPassword()));
                XMLUser.agregarUsuario(nuevoUsuario);
                changeSceneToInicioSesion();
            }
        }
}

    @Override
    public void onOpen(Object input) throws Exception {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void changeSceneToInicioSesion() throws Exception {
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }
}