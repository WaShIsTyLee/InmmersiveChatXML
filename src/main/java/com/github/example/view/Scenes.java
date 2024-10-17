package com.github.example.view;

public enum Scenes {

    ROOT("view/Layout.fxml"),
    MAINPAGE("view/mainPage.fxml");


    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
