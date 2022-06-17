package com.musicverse.client;

import com.musicverse.client.gui.WelcomeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(WelcomeScreen.getHolder(), 1200, 600);
        stage.setTitle("Music Verse");
        stage.setScene(scene);
        stage.show();
    }
}
