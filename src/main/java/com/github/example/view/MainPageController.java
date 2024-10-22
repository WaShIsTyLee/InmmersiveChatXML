package com.github.example.view;

import com.github.example.model.Entity.Contacto;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.Sesion;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLMessage;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.xml.bind.JAXBException;
import java.net.URL;
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
    private VBox contactos;
    @FXML
    private VBox mensajes;
    @FXML
    private Button button;
    @FXML
    private TextField textField;

    @FXML
    private ListView<Contacto> contactListView;

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
                textNombreContacto.setText(newValue.getNickname());
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

    private void mostrarMensajes(User user) {
        vBoxMensajes.getChildren().clear();
        try {
            List<Message> mensajes = recogerMensajesdeUsuario(user);
            for (Message message : mensajes) {
                Text mensajeText = new Text(message.getText());
                mensajeText.setStyle("-fx-padding: 5;");
                vBoxMensajes.getChildren().add(mensajeText);
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

    private List<Message> recogerMensajesdeUsuario(User user) throws JAXBException {
        List<Message> mensajes = XMLMessage.recoverMessages();
        List<Message> mensajesFiltrados = new ArrayList<>();
        User usuarioIniciado = Sesion.getInstancia().getUsuarioIniciado();
        for (Message message : mensajes) {
            if ((message.getUserSender().equals(usuarioIniciado) && message.getUserReceiver().equals(user)) ||
                    (message.getUserSender().equals(user) && message.getUserReceiver().equals(usuarioIniciado))) {
                mensajesFiltrados.add(message);
            }
        }
        return mensajesFiltrados;
    }
}