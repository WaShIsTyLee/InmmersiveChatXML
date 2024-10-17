package com.github.example.model.XML;

import com.github.example.model.Entity.User;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUser {
    private File archivoUsuarios = new File("usuarios.xml");


    public void agregarUsuario(User user) throws Exception {
        List<User> usuarios = obtenerUsuarios();
        usuarios.add(user);
        guardarUsuarios(usuarios);
    }


    public List<User> obtenerUsuarios() throws Exception {
        if (archivoUsuarios.exists() && archivoUsuarios.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserWrapper wrapper = (UserWrapper) unmarshaller.unmarshal(archivoUsuarios);
            return wrapper.getUsers();
        } else {
            return new ArrayList<>();
        }
    }


    private void guardarUsuarios(List<User> usuarios) throws Exception {
        UserWrapper wrapper = new UserWrapper();
        wrapper.setUsers(usuarios); // Establecer la lista de usuarios en el wrapper

        JAXBContext context = JAXBContext.newInstance(UserWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, archivoUsuarios); // Guardar el wrapper en el archivo XML
    }
}
