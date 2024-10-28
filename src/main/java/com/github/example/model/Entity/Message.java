package com.github.example.model.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement
    public LocalDateTime date;
    @XmlElement
    public String text;
    @XmlElement
    public Contact senderContact;
    @XmlElement
    public Contact receiverContact;

    public Message(LocalDateTime date, String text, Contact senderContact, Contact receiverContact) {
        this.date = date;
        this.text = text;
        this.senderContact = senderContact;
        this.receiverContact = receiverContact;
    }

    public Message() {}

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Contact getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(Contact senderContact) {
        this.senderContact = senderContact;
    }

    public Contact getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(Contact receiverContact) {
        this.receiverContact = receiverContact;
    }

    @Override
    public String toString() {
        return "Message{" +
                "date=" + date +
                ", text='" + text + '\'' +
                ", senderContact=" + senderContact +
                ", receiverContact=" + receiverContact +
                '}';
    }
}
