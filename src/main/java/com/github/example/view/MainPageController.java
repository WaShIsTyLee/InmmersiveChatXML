package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.Contacto;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.Sesion;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.MessageWrapper;
import com.github.example.model.XML.XMLMessage;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController extends Controller implements Initializable {

    @FXML
    private SplitPane splitpane;
    @FXML
    Text textNombreContacto;
    @FXML
    private ScrollPane scrollPaneMensajes;
    @FXML
    private VBox vBoxMensajes;
    @FXML
    private ImageView imagen;
    @FXML
    private VBox contactos;
    @FXML
    private VBox mensajes;
    @FXML
    private Button buttonAnadirContacto;
    @FXML
    private TextField textField;

    @FXML
    private ListView<Contacto> contactListView;

    @FXML
    TextField mensajeTextField;

    @FXML
    private Button buttonAnadirMensaje;

    private String selectedNickname;



    @FXML
    public void obtenerContactos(User usuario) throws Exception {
        if (usuario.getContactos() == null) {
            usuario.setContactos(new ArrayList<>());
        }
        contactListView.getItems().clear();
        List<Contacto> contactos = XMLUser.obtenerContactosPorUsuario(usuario);
        if (contactos != null && !contactos.isEmpty()) {
            contactListView.getItems().addAll(contactos);
        } else {
            System.out.println("No hay contactos disponibles.");
        }
    }

    public Contacto validarContacto() throws Exception {
        Contacto aux = null;
        String nickname = textField.getText();
        List<User> usuarios = XMLUser.obtenerUsuarios();
        for (User user : usuarios) {
            if (user.getNickname().equals(nickname)) {
                System.out.println("Contacto encontrado: " + user.getNickname());
                aux = new Contacto();
                aux.setEmail(user.getEmail());
                aux.setName(user.getName());
                aux.setNickname(user.getNickname());
                break;
            }
        }
        return aux;
    }

    public void añadirContactos() throws Exception {
        User usuarioIniciado = Sesion.getInstancia().getUsuarioIniciado();
        if (usuarioIniciado == null) {
            System.out.println("No hay un usuario iniciado.");
            return;
        }
        Contacto contacto = validarContacto();
        if (usuarioIniciado.getContactos() == null) {
            usuarioIniciado.setContactos(new ArrayList<>());
        }
        if (contacto == null) {
            System.out.println("El contacto no fue encontrado o es nulo");
            return;
        }
        List<User> usuariosXML = XMLUser.obtenerUsuarios();
        for (User user : usuariosXML) {
            if (user.equals(usuarioIniciado)) {
                usuariosXML.remove(user);
                List<Contacto> contactosUser = usuarioIniciado.getContactos();
                contactosUser.add(contacto);
                usuarioIniciado.setContactos(contactosUser);
                usuariosXML.add(usuarioIniciado);
                XMLUser.guardarUsuarios(usuariosXML);
                obtenerContactos(usuarioIniciado);
                break;
            }
        }
    }

    @Override
    public void onOpen(Object input) throws Exception {
        User usuario = Sesion.getInstancia().getUsuarioIniciado();
        obtenerContactos(usuario);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarListView();
        agregarListenerSeleccion();
    }

    private void configurarListView() {
        contactListView.setCellFactory(param -> new ListCell<Contacto>() {
            @Override
            protected void updateItem(Contacto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNickname());
            }
        });
    }

    private void agregarListenerSeleccion() {
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedNickname = newValue.getNickname();
                textNombreContacto.setText(selectedNickname);
                System.out.println("Contacto seleccionado: " + newValue.getNickname());
                User user = null;
                try {
                    user = buscarUsuarioPorNickname(newValue.getNickname());
                    if (user != null) {
                        System.out.println("Usuario encontrado: " + user.toString());
                        mostrarMensajes(user);
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public String getselectedNickname() {
        return selectedNickname;
    }

    private void mostrarMensajes(User user) {
        vBoxMensajes.getChildren().clear();
        Contacto contacto = new Contacto(user.getEmail(), user.getName(), user.getNickname());

        try {
            List<Message> mensajes = recogerMensajesdeUsuario(contacto);

            for (Message message : mensajes) {
                Label mensajeLabel = new Label(message.getText());
                mensajeLabel.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                mensajeLabel.setWrapText(true);
                mensajeLabel.setMaxWidth(300); // Limitar el ancho de los mensajes

                HBox hbox = new HBox();
                hbox.setPadding(new Insets(5));

                if (message.getContactoEmisor().getNickname().equals(Sesion.getInstancia().getUsuarioIniciado().getNickname())) {
                    mensajeLabel.setStyle("-fx-background-color: #B39DDB; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_RIGHT); // Alinear el mensaje a la derecha
                    hbox.getChildren().add(mensajeLabel);
                } else {
                    mensajeLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_LEFT); // Alinear el mensaje a la izquierda
                    hbox.getChildren().add(mensajeLabel);
                }

                vBoxMensajes.getChildren().add(hbox);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }




    private User buscarUsuarioPorNickname(String nickname) throws Exception {
        List<User> listaUsuarios = XMLUser.obtenerUsuarios();
        User user = new User();
        for (User users : listaUsuarios) {
            if (users.getNickname().equals(nickname)) {
                user = users;
            }
        }
        return user;
    }

    private List<Message> recogerMensajesdeUsuario(Contacto contacto) throws JAXBException {
        List<Message> mensajes = XMLMessage.recoverMessages();
        List<Message> mensajesFiltrados = new ArrayList<>();
        User usuarioIniciado = Sesion.getInstancia().getUsuarioIniciado();

        for (Message message : mensajes) {
            if ((message.getContactoEmisor().getNickname().equals(usuarioIniciado.getNickname()) &&
                    message.getContactoReceptor().getNickname().equals(contacto.getNickname())) ||

                    (message.getContactoEmisor().getNickname().equals(contacto.getNickname()) &&
                            message.getContactoReceptor().getNickname().equals(usuarioIniciado.getNickname()))) {
                mensajesFiltrados.add(message);
            }
        }
        return mensajesFiltrados;
    }


    public User recogerUsuarioPorNickname(String nickname) throws Exception {
        List<User> todosUsuarios= XMLUser.obtenerUsuarios();
        User usuario = null;
        for (User user : todosUsuarios){
            if (user.getNickname().equals(nickname)) {
                usuario = user;
            }
        }
        return usuario;
    }



    public void guardarMensaje() throws Exception {
        String mensajeRecogido = mensajeTextField.getText();
        if (mensajeRecogido == null || mensajeRecogido.trim().isEmpty()) {
            return;
        }

        List<Message> mensajesRecogidos = XMLMessage.recoverMessages();
        LocalDateTime ahora = LocalDateTime.now();
        User user = Sesion.getInstancia().getUsuarioIniciado();

        String nicknameReceptor = getselectedNickname();
        User usuarioReceptor = new User();
        List<User> todosUsuarios = XMLUser.obtenerUsuarios();

        for (User user2 : todosUsuarios) {
            if (user2.getNickname().equals(nicknameReceptor)) {
                usuarioReceptor = user2;
                break;
            }
        }
        Contacto contactoEmisor = new Contacto(user.getEmail(), user.getName(), user.getNickname());
        Contacto contactorReceptor = new Contacto(usuarioReceptor.getEmail(), usuarioReceptor.getName(), usuarioReceptor.getNickname());
        Message message = new Message(ahora, mensajeRecogido, contactoEmisor, contactorReceptor);
        mensajesRecogidos.add(message);
        XMLMessage.saveMessages(mensajesRecogidos);
        mensajeTextField.clear();
        mostrarMensajes(usuarioReceptor);
    }

    @FXML
    public void changeSceneToInicioSesion() throws Exception {
        Sesion.getInstancia().logOut();
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }

}