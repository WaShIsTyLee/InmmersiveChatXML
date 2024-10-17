package com.github.example.model.XML;

import com.github.example.model.Entity.Message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "messages")
public class MessageWrapper {
private List<Message> messages = new ArrayList<>();

@XmlElement(name = "message")

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
