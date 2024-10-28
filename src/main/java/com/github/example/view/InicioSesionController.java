package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.Sesion;
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

public class InicioSesionController extends Controller implements Initializable {
    @FXML
    Button buttonLogIn;
    @FXML
    Button buttonRegister;
    @FXML
    TextField UserName;
    @FXML
    PasswordField password;


    @FXML
    /**
     * Collects and verifies user login values from the input fields.
     *
     * @return The fully populated User object if credentials are valid, or null if invalid.
     * @throws Exception If an error occurs during the login process.
     */
    private User takeValuesLogin() throws Exception {
        if (UserName == null || password == null) {
            throw new NullPointerException("Campos no inicializados");
        }
        String nickname = UserName.getText();
        String password = this.password.getText();
        User auxUser = new User();
        auxUser.setNickname(nickname);
        auxUser.setPassword(User.hashPassword(password));
        User fullUser = verifyCredentials(auxUser);
        if (fullUser != null) {
            System.out.println("Credenciales correctas");
            Sesion.getInstancia().logIn(fullUser);
            goToMainPage();
        } else {
            AppController.showAlertForLogin();
        }
        return fullUser;
    }

    /**
     * Verifies the provided user credentials against the stored user records.
     *
     * @param tempUser The user object containing the nickname and password to verify.
     * @return The matching User object if credentials are valid, or null if no match is found.
     * @throws Exception If an error occurs while retrieving users or verifying credentials.
     */
    private static User verifyCredentials(User tempUser) throws Exception {
        List<User> users = XMLUser.getUsersFromXml();
        for (User storedUser : users) {
            System.out.println(storedUser);
            if (tempUser.getNickname().equals(storedUser.getNickname()) &&
                    tempUser.getPassword().equals(storedUser.getPassword())) {
                return storedUser;
            }
        }
        return null;
    }


    @Override
    public void onOpen(Object input) throws Exception {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void goToMainPage() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }

    @FXML
    private void goToRegisterPage() throws Exception {
        App.currentController.changeScene(Scenes.REGISTRO, null);
    }
}
