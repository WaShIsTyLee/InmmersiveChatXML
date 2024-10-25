package com.github.example.model.Entity;

import javax.xml.bind.annotation.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)

public class User  {
    @XmlElement
    public String name;
    @XmlElement
    public String email;
    @XmlElement
    public String nickname;
    @XmlElement
    public String password;
    @XmlElement
    public List<Contacto> contactos;


    public User(String name, String email, String nickname, String password, List<Contacto> contactos) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.contactos = contactos;
    }
    public User(String name, String email, String nickname, String password) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public User() {
    }

    public User(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getNickname(), user.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public static String hashPassword(String pass) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(pass.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    public static boolean validatePassword(String password) {
        boolean result = false;
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!.#_()%?&])[A-Za-z\\d@$!.#_()%?&]{8,}$");
        Matcher contrasenaMatcher = passwordPattern.matcher(password);
        if (contrasenaMatcher.matches()) {
            result = true;
        }
        return result;
    }

    public static boolean validateEmail(String mail) {
        boolean result = false;
        Pattern mailPattern = Pattern.compile("[A-Za-z0-9]+@+(gmail|outlook|hotmail)\\.(com|es)");
        Matcher mailMatcher = mailPattern.matcher(mail);
        if (mailMatcher.matches()) {
            result = true;
        }
        return result;
    }

}
