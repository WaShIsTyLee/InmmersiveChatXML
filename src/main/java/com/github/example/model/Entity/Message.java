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
    public Contacto contactoEmisor;
    @XmlElement
    public Contacto contactoReceptor;

    public Message(LocalDateTime fecha, String text, Contacto contactoEmisor, Contacto contactoReceptor) {
        this.fecha = fecha;
        this.text = text;
        this.contactoEmisor = contactoEmisor;
        this.contactoReceptor = contactoReceptor;
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

    public Contacto getContactoEmisor() {
        return contactoEmisor;
    }

    public void setContactoEmisor(Contacto contactoEmisor) {
        this.contactoEmisor = contactoEmisor;
    }

    public Contacto getContactoReceptor() {
        return contactoReceptor;
    }

    public void setContactoReceptor(Contacto contactoReceptor) {
        this.contactoReceptor = contactoReceptor;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fecha=" + fecha +
                ", text='" + text + '\'' +
                ", contactoEmisor=" + contactoEmisor +
                ", contactoReceptor=" + contactoReceptor +
                '}';
    }
}
