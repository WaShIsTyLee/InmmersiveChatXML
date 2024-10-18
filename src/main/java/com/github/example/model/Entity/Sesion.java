package com.github.example.model.Entity;

public class Sesion {

    private static Sesion _instance;
    private static User userLoged;

    private Sesion() {
    }

    public static Sesion getInstancia() {
        if (_instance == null) {
            _instance = new Sesion();
            _instance.logIn(userLoged);
        }
        return _instance;
    }

    public void logIn(User user) {
        userLoged = user;
    }

    public User getUsuarioIniciado() {
        return userLoged;
    }

    public void logOut() {
        userLoged = null;
    }
}


