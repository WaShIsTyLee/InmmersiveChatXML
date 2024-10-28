package com.github.example.model.XML;

import com.github.example.model.Entity.Contact;
import com.github.example.model.Entity.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUser {
    private static File UsersFile = new File("usuarios.xml");

    /**
     * Adds a new user to the list of users and saves the updated list.
     *
     * @param user The user to be added to the list.
     * @throws Exception If an error occurs while retrieving or saving the user list.
     */
    public static void addUserXML(User user) throws Exception {
        List<User> users = getUsersFromXml();
        users.add(user);
        saveUsersInXML(users);
    }

    /**
     * Retrieves a list of users from an XML file. If the file does not exist or is empty,
     * it creates an initial XML file and returns an empty list.
     *
     * @return A list of users retrieved from the XML file.
     * @throws Exception If an error occurs while reading or unmarshalling the XML file.
     */
    public static List<User> getUsersFromXml() throws Exception {
        if (UsersFile.exists() && UsersFile.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserWrapper wrapper = (UserWrapper) unmarshaller.unmarshal(UsersFile);
            return wrapper.getUsers();
        } else {
            createInitialXML();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the list of contacts associated with a specific user.
     *
     * @param UserName The user whose contacts are to be retrieved.
     * @return A list of contacts associated with the specified user, or an empty list if the user is not found.
     * @throws Exception If an error occurs while retrieving the user list.
     */
    public static List<Contact> getContactsByUser(User UserName) throws Exception {
        List<User> users = getUsersFromXml();
        for (User user : users) {
            if (user.getNickname().equals(UserName.getNickname())) {
                return user.getContacts();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Saves the list of users to an XML file.
     *
     * @param users The list of users to be saved.
     * @throws Exception If an error occurs while saving the user list to the XML file.
     */
    public static void saveUsersInXML(List<User> users) throws Exception {
        UserWrapper wrapper = new UserWrapper();
        wrapper.setUsers(users);
        JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, UsersFile);
    }

    /**
     * Creates an initial XML file to store user data if it does not already exist.
     *
     * @throws Exception If an error occurs while creating the XML file or marshalling the data.
     */
    private static void createInitialXML() throws Exception {
        if (!UsersFile.exists()) {
            UsersFile.createNewFile();
        }
        UserWrapper wrapper = new UserWrapper();
        wrapper.setUsers(new ArrayList<>());
        JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, UsersFile);
    }
}
