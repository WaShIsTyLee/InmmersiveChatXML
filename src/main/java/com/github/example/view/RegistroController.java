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
    TextField name;

    @FXML
    TextField nickname;
    @FXML
    TextField email;
    @FXML
    PasswordField password;
    @FXML
    Button register;

    @FXML
    /**
     * Obtains and validates the registration values for a new user.
     *
     * @return User object of the new user if all fields are valid, or null if there are errors.
     * @throws NullPointerException if any input field is not initialized.
     */
    private User takeValuesRegister() throws Exception {
        User newUser = getValuesFromRegister();
        if (newUser == null) {
            return null;
        }
        registerUser(newUser);
        return newUser;
    }

    /**
     * Retrieves user input values from the registration form fields, validates them,
     * and returns a new User instance if all fields are correctly filled and validated.
     *
     * @return a User instance with the provided registration data, or null if any field is invalid.
     * @throws NullPointerException if any of the form fields (nombre, contrasena, email, nickname) are not initialized.
     */
    private User getValuesFromRegister() {
        if (name == null || password == null || email == null || nickname == null) {
            throw new NullPointerException("Campos no inicializados");
        }
        String nameText = name.getText();
        String password = this.password.getText();
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
        if (nameText.isEmpty() || emailText.isEmpty() || nicknameText.isEmpty() || password.isEmpty()) {
            System.out.println("Error: Todos los campos son obligatorios.");
            return null;
        }
        return new User(nameText, emailText, nicknameText, password);
    }
    /**
     * Registers a new user by verifying if the nickname and email are unique among all users.
     * If valid, the user is added to the system with a hashed password, and the scene changes to the login screen.
     * Alerts are displayed if the nickname or email is already taken.
     *
     * @param newUser the User instance containing the new user’s registration data
     * @throws Exception if there is an error obtaining or saving user data
     */
    private void registerUser(User newUser) throws Exception {
        List<User> allUsers = XMLUser.getUsersFromXml();
        if (allUsers.isEmpty()) {
            newUser.setPassword(User.hashPassword(newUser.getPassword()));
            XMLUser.addUserXML(newUser);
            changeSceneToInicioSesion();
        } else {
            boolean ExistNickname = false;
            boolean ExistEmail = false;
            for (User user : allUsers) {
                if (newUser.getNickname().equals(user.getNickname())) {
                    ExistNickname = true;
                }
                if (newUser.getEmail().equals(user.getEmail())) {
                    ExistEmail = true;
                }
                if (ExistNickname || ExistEmail) {
                    break;
                }
            }
            if (ExistNickname) {
                AppController.showAlertForNickname();
            } else if (ExistEmail) {
                AppController.showAlertForEmail();
            } else {
                newUser.setPassword(User.hashPassword(newUser.getPassword()));
                XMLUser.addUserXML(newUser);
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