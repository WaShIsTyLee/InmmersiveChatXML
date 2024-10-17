package com.github.example.model.Entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@XmlRootElement(name = "message")
public class Message {
    LocalDateTime fecha;
    String text;
    User userSender;
    User userReceiver;

    public Message(LocalDateTime fecha, String text, User userSender, User userReceiver) {
        this.fecha = fecha;
        this.text = text;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
    }
    @XmlElement
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    @XmlElement
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @XmlElement
    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    @XmlElement
    public User getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(User userReceiver) {
        this.userReceiver = userReceiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fecha=" + fecha +
                ", text='" + text + '\'' +
                ", userSender=" + userSender +
                ", userReceiver=" + userReceiver +
                '}';
    }
}
