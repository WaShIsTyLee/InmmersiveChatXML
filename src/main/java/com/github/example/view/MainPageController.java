package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.Contact;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.Sesion;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLMessage;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.example.model.XML.XMLMessage.txtFile;

public class MainPageController extends Controller implements Initializable {
    @FXML
    ImageView summaryImage;
    @FXML
    private SplitPane splitpane;
    @FXML
    Text textNameContact;
    @FXML
    private ScrollPane scrollPaneMessages;
    @FXML
    private VBox vBoxMessages;
    @FXML
    private ImageView image;
    @FXML
    private VBox contacts;
    @FXML
    private VBox messages;
    @FXML
    private Button buttonAddContact;
    @FXML
    private TextField textField;

    @FXML
    private ListView<Contact> contactListView;

    @FXML
    TextField messageTextField;

    @FXML
    private Button buttonAddMessage;

    private String selectedNickname;


    @FXML
    /**
     * Retrieves and displays the contacts for the specified user.
     *
     * @param user The user for whom to retrieve contacts.
     * @throws Exception If an error occurs while retrieving contacts or updating the UI.
     */
    public void getContacts(User user) throws Exception {
        if (user.getContacts() == null) {
            user.setContacts(new ArrayList<>());
        }
        contactListView.getItems().clear();
        List<Contact> contacts = XMLUser.getContactsByUser(user);
        if (contacts != null && !contacts.isEmpty()) {
            contactListView.getItems().addAll(contacts);
        } else {
            System.out.println("No hay contactos disponibles.");
        }
    }

    /**
     * Validates and retrieves a contact based on the nickname entered in the text field.
     *
     * @return A Contacto object if a matching user is found, or null if no match is found.
     * @throws Exception If an error occurs while retrieving users.
     */
    public Contact verifyContact() throws Exception {
        Contact aux = null;
        String nickname = textField.getText();
        List<User> users = XMLUser.getUsersFromXml();
        for (User user : users) {
            if (user.getNickname().equals(nickname)) {
                System.out.println("Contacto encontrado: " + user.getNickname());
                aux = new Contact();
                aux.setEmail(user.getEmail());
                aux.setName(user.getName());
                aux.setNickname(user.getNickname());
                break;
            }
        }
        return aux;
    }

    /**
     * Adds a new contact to the currently logged-in user's contact list.
     *
     * @throws Exception If an error occurs while adding the contact or retrieving user data.
     */
    public void addContacts() throws Exception {
        User userLoged = Sesion.getInstancia().getUserLoged();
        Contact contact = verifyContact();
        if (userLoged.getContacts() == null) {
            userLoged.setContacts(new ArrayList<>());
        }
        if (contact == null) {
            AppController.showAlertForAddContact();
            return;
        }
        List<User> usersXML = XMLUser.getUsersFromXml();
        for (User user : usersXML) {
            if (user.equals(userLoged)) {
                usersXML.remove(user);
                List<Contact> contactsUser = userLoged.getContacts();
                for (Contact c : contactsUser) {
                    if (c.getNickname().equals(contact.getNickname())) {
                        AppController.showAlertForContactRepetido();
                        return;
                    }
                }
                contactsUser.add(contact);
                userLoged.setContacts(contactsUser);
                usersXML.add(userLoged);
                XMLUser.saveUsersInXML(usersXML);
                getContacts(userLoged);
                break;
            }
        }
    }

    @Override
    public void onOpen(Object input) throws Exception {
        User usuario = Sesion.getInstancia().getUserLoged();
        getContacts(usuario);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configListView();
        addListenerSelection();
    }

    /**
     * Configures the ListView to display contacts.
     * It sets a custom cell factory to define how each item (contact) is displayed.
     */
    private void configListView() {
        contactListView.setCellFactory(param -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNickname());
            }
        });
    }
    /**
     * Adds a listener to the contact list view to handle selection events.
     * When a contact is selected, it updates the selected nickname and displays the corresponding user's messages.
     */
    private void addListenerSelection() {
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedNickname = newValue.getNickname();
                textNameContact.setText(selectedNickname);
                System.out.println("Contacto seleccionado: " + newValue.getNickname());
                User user = null;
                try {
                    user = searchUserByNickname(newValue.getNickname());
                    if (user != null) {
                        System.out.println("Usuario encontrado: " + user.toString());
                        showMessages(user);
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
    /**
     * Displays the messages exchanged with a specific user.
     * It clears the current message view and retrieves messages to show them in a chat format.
     *
     * @param user The user whose messages will be displayed.
     */
    private void showMessages(User user) {
        vBoxMessages.getChildren().clear();
        Contact contact = new Contact(user.getEmail(), user.getName(), user.getNickname());
        try {
            List<Message> messages = getMessagesOfUser(contact);

            for (Message message : messages) {
                Label messageLabel = new Label(message.getText());
                messageLabel.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                messageLabel.setWrapText(true);
                messageLabel.setMaxWidth(300);
                HBox hbox = new HBox();
                hbox.setPadding(new Insets(5));
                if (message.getSenderContact().getNickname().equals(Sesion.getInstancia().getUserLoged().getNickname())) {
                    messageLabel.setStyle("-fx-background-color: #B39DDB; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.getChildren().add(messageLabel);
                } else {
                    messageLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.getChildren().add(messageLabel);
                }
                vBoxMessages.getChildren().add(hbox);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for a user by their nickname in the list of users.
     *
     * @param nickname The nickname of the user to search for.
     * @return The User object if found, or a new User object if not found.
     * @throws Exception if an error occurs while retrieving the user list.
     */
    private User searchUserByNickname(String nickname) throws Exception {
        List<User> usersList = XMLUser.getUsersFromXml();
        User user = new User();
        for (User users : usersList) {
            if (users.getNickname().equals(nickname)) {
                user = users;
            }
        }
        return user;
    }

    /**
     * Recovers messages exchanged between the currently logged-in user and a specified contact.
     *
     * @param contact The contact whose messages with the logged-in user are to be retrieved.
     * @return A list of messages exchanged between the logged-in user and the specified contact.
     * @throws JAXBException if an error occurs while retrieving messages from the XML.
     */
    private List<Message> getMessagesOfUser(Contact contact) throws JAXBException {
        List<Message> messages = XMLMessage.recoverMessages();
        List<Message> filterMessages = new ArrayList<>();
        User userLoged = Sesion.getInstancia().getUserLoged();

        for (Message message : messages) {
            if ((message.getSenderContact().getNickname().equals(userLoged.getNickname()) &&
                    message.getReceiverContact().getNickname().equals(contact.getNickname())) ||

                    (message.getSenderContact().getNickname().equals(contact.getNickname()) &&
                            message.getReceiverContact().getNickname().equals(userLoged.getNickname()))) {
                filterMessages.add(message);
            }
        }
        return filterMessages;
    }

    /**
     * Saves a message sent by the currently connected user and stores it in the system.
     *
     * @throws Exception if an error occurs while retrieving or saving messages.
     */
    public void saveMessage() throws Exception {
        String getMessage = messageTextField.getText();
        if (getMessage == null || getMessage.trim().isEmpty()) {
            return;
        }

        List<Message> getMessageList = XMLMessage.recoverMessages();
        LocalDateTime now = LocalDateTime.now();
        User user = Sesion.getInstancia().getUserLoged();
        String nicknameReceptor = getselectedNickname();
        if (nicknameReceptor == null || nicknameReceptor.isEmpty()) {
            AppController.showAlertForContactSelected();
            return;

        }
        User userReceiver = new User();
        List<User> allUsers = XMLUser.getUsersFromXml();

        for (User user2 : allUsers) {
            if (user2.getNickname().equals(nicknameReceptor)) {
                userReceiver = user2;
                break;
            }
        }
        Contact contactSender = new Contact(user.getEmail(), user.getName(), user.getNickname());
        Contact contactorReceiver = new Contact(userReceiver.getEmail(), userReceiver.getName(), userReceiver.getNickname());
        Message message = new Message(now, getMessage, contactSender, contactorReceiver);
        getMessageList.add(message);
        XMLMessage.saveMessages(getMessageList);
        messageTextField.clear();
        showMessages(userReceiver);
    }

    @FXML
    public void changeSceneToInicioSesion() throws Exception {
        Sesion.getInstancia().logOut();
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }

    @FXML
    /**
     * Exports the messages between the currently connected user and a selected contact to a text file.
     *
     * @throws Exception if an error occurs while retrieving messages or saving the text file.
     */
    public void txtMessages() throws Exception {
        String nickname = getselectedNickname();
        User user = searchUserByNickname(nickname);
        List<Message> messagesTxt = XMLMessage.recoverMessages();
        List<Message> messagesFilter = new ArrayList<>();

        User userLoged = Sesion.getInstancia().getUserLoged();
        String userLogedNickname = userLoged.getNickname();


        for (Message message : messagesTxt) {
            String SenderNickname = message.getSenderContact().getNickname();
            String receiverNickname = message.getReceiverContact().getNickname();

            if ((SenderNickname.equals(user.getNickname()) && receiverNickname.equals(userLogedNickname)) ||
                    (receiverNickname.equals(user.getNickname()) && SenderNickname.equals(userLogedNickname))) {
                messagesFilter.add(message);
            }
        }
        if (!messagesFilter.isEmpty()) {
            XMLMessage.convertXMLToTXT(txtFile, messagesFilter);
        } else {
            System.out.println("No hay mensajes entre estos usuarios.");
        }
    }


}