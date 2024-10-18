package com.github.example;

import com.github.example.view.AppController;
import com.github.example.view.Scenes;
import com.github.example.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene,  1920, 1080);
        currentController = (AppController) view.controller;
        currentController.onOpen( null);
        stage.setScene(scene);
        stage.show();

    }
}