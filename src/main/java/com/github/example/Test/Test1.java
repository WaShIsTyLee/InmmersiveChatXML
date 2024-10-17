package com.github.example.Test;

import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLUser;

import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        try {
            XMLUser xmlUserHandler = new XMLUser();


            User user1 = new User("Juan Perez", "juan.perez@example.com", "password123", "juanito");
            User user2 = new User("Mariaaa", "maria@", "password123", "mariquilla");

            xmlUserHandler.agregarUsuario(user1);
            xmlUserHandler.agregarUsuario(user2);

            List<User> usuariosGuardados = xmlUserHandler.obtenerUsuarios();
            for (User user : usuariosGuardados) {
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


