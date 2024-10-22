package com.github.example.model.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement    public LocalDateTime fecha;
    @XmlElement
    public String text;
    @XmlElement
    public User userSender;
    @XmlElement
    public User userReceiver;

    public Message(LocalDateTime fecha, String text, User userSender, User userReceiver) {
        this.fecha = fecha;
        this.text = text;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
    }

    public Message() {}

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }


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
