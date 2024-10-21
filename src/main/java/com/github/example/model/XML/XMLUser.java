package com.github.example.model.XML;

import com.github.example.model.Entity.Contacto;
import com.github.example.model.Entity.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUser {
    private static File archivoUsuarios = new File("usuarios.xml");


    public static void agregarUsuario(User user) throws Exception {
        List<User> usuarios = obtenerUsuarios();
        usuarios.add(user);
        guardarUsuarios(usuarios);
    }


    public static List<User> obtenerUsuarios() throws Exception {
        if (archivoUsuarios.exists() && archivoUsuarios.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserWrapper wrapper = (UserWrapper) unmarshaller.unmarshal(archivoUsuarios);
            return wrapper.getUsers();
        } else {
            crearXMLInicial();
            return new ArrayList<>();
        }
    }

    public static List<Contacto> obtenerContactosDeTodosLosUsuarios() throws Exception {
        List<User> usuarios = obtenerUsuarios();
        List<Contacto> todosLosContactos = new ArrayList<>();
        for (User user : usuarios) {
            if (user.getContactos() != null) {
                todosLosContactos.addAll(user.getContactos());
            }
        }
        return todosLosContactos;
    }


    public static List<Contacto> obtenerContactosPorUsuario(User nombreUsuario) throws Exception {
        List<User> usuarios = obtenerUsuarios();
        for (User user : usuarios) {
            if (user.getNickname().equals(nombreUsuario.getNickname())) {
                return user.getContactos();
            }
        }
        return new ArrayList<>();
    }


    public static void guardarUsuarios(List<User> usuarios) throws Exception {
        UserWrapper wrapper = new UserWrapper();
        wrapper.setUsers(usuarios);
        JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, archivoUsuarios);
    }


    private static void crearXMLInicial() throws Exception {
        if (!archivoUsuarios.exists()) {
            archivoUsuarios.createNewFile();
        }
        UserWrapper wrapper = new UserWrapper();
        wrapper.setUsers(new ArrayList<>());
        JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, archivoUsuarios);
    }
}
