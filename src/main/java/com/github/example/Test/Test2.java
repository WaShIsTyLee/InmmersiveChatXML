package com.github.example.Test;

import com.github.example.model.Entity.Contacto;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLMessage;

import java.time.LocalDateTime;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws Exception {
        XMLMessage xmlMessage = new XMLMessage();

        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("juanito");
        User user2 = new User( "mariquilla");

        Contacto contacto = new Contacto();
        Contacto contacto2 = new Contacto();

        Message mensaje1 = new Message(now,"hola guapo", contacto,contacto2);

        xmlMessage.addMessage(mensaje1);

        List<Message> mensagge = xmlMessage.recoverMessages();
        for (Message m : mensagge){
            System.out.println(m);
        }
    }
}
