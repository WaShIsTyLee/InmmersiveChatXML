package com.github.example.Test;

import com.github.example.model.Entity.Contacto;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLUser;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) {
        try {
            XMLUser xmlUserHandler = new XMLUser();

            List<Contacto> contactosJuan = new ArrayList<>();
            Contacto contacto1 = new Contacto("Carlos Lopez", "carlos.lopez@example.com", "carlitos");
            Contacto contacto2 = new Contacto("Ana Garcia", "ana.garcia@example.com", "anita");
            Contacto contacto3 = new Contacto("Pedro Ramirez", "pedro.ramirez@example.com", "pedrito");
            contactosJuan.add(contacto1);
            contactosJuan.add(contacto2);
            contactosJuan.add(contacto3);


            User user1 = new User("Juan Perez", "juan.perez@example.com", "juanito", "password123",contactosJuan);


            List<Contacto> contactosMaria = new ArrayList<>();
            Contacto contacto4 = new Contacto("Luis Fernandez", "luis.fernandez@example.com", "luisito");
            Contacto contacto5 = new Contacto("Elena Gomez", "elena.gomez@example.com", "elenita");
            contactosMaria.add(contacto4);
            contactosMaria.add(contacto5);


            User user2 = new User("Mariaaa", "maria@", "mariquilla", "password123", contactosMaria);


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

