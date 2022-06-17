package com.musicverse.client.gui;

import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.sessionManagement.MyLogger;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import com.musicverse.client.Localozator;

public class WelcomeScreen {

    private static Stage primaryStage;
    private static BorderPane rootLayout;

    public static VBox getHolder(){

        Button registerBtn, loginBtn, registerArtistBtn, guestBtn;

        // Requesting the Java versions used by the application
        //(top/right/bottom/left)

        final String versionJava = System.getProperty("java.version");
        final String versionJavaFX = System.getProperty("javafx.version");

        GridPane buttons = new GridPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(50));
        buttons.setHgap(10);
        buttons.setVgap(30);

        Label title = new Label(Localozator.getResourceBundle().getString("WELCOME"));
        title.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
        title.setAlignment(Pos.TOP_LEFT);
        title.setPadding(new Insets(40,10, 10, 40));

        registerBtn = new Button(Localozator.getResourceBundle().getString("CREATE AN ACCOUNT"));
        registerBtn.setStyle("-fx-cursor: hand; -fx-background-color: #58A034; -fx-font-size: 18px;-fx-text-fill: white; -fx-pref-width: 300px;");
        registerBtn.setAlignment(Pos.CENTER);

        loginBtn = new Button(Localozator.getResourceBundle().getString("I ALREADY HAVE AN ACCOUNT"));
        loginBtn.setStyle("-fx-cursor: hand; -fx-background-color: #58A034; -fx-font-size: 18px;-fx-text-fill: white; -fx-pref-width: 300px;");

        registerArtistBtn = new Button(Localozator.getResourceBundle().getString("CREATE AN ARTIST ACCOUNT"));
        registerArtistBtn.setStyle("-fx-cursor: hand; -fx-background-color: #58A034; -fx-font-size: 18px; -fx-text-fill: white; -fx-pref-width: 300px;");
        registerArtistBtn.setAlignment(Pos.CENTER);

        guestBtn = new Button(Localozator.getResourceBundle().getString("CONTINUE WITHOUT REGISTRATION"));
        if (PreferencesLogin.getPrefs().getId() > 0)
            guestBtn.setText(Localozator.getResourceBundle().getString("CONTINUE WITH SAVED ACCOUNT"));
        guestBtn.setStyle("-fx-cursor: hand; -fx-background-color: #58A034; -fx-font-size: 18px; -fx-text-fill: white; -fx-pref-width: 300px;");
        guestBtn.setMaxWidth(Double.MAX_VALUE);
        guestBtn.setAlignment(Pos.CENTER);
        guestBtn.setUnderline(true);

        Label motto = new Label(Localozator.getResourceBundle().getString("WHERE ALL THE MUSIC EXPERIENCE IS INDESCRIBABLY UNEARTHLY"));
        motto.setStyle("-fx-text-fill: white; -fx-font-size: 18px; ");
        motto.setMaxWidth(Double.MAX_VALUE);
        motto.setAlignment(Pos.BOTTOM_RIGHT);
        motto.setPadding(new Insets(0,40, 10, 0));


        buttons.add(registerBtn, 0, 0);
        buttons.add(loginBtn, 0, 1);
        buttons.add(registerArtistBtn, 0, 2);
        buttons.add(guestBtn, 0, 3);

        VBox holder = new VBox(title, buttons, motto);
        holder.setSpacing(20);
        holder.setStyle("-fx-background-color: #3D3F3C");

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
					new InitScreensFunctions().initLoginScreen(actionEvent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
                    new MyLogger(e.toString(),"ERROR");
					e.printStackTrace();
				}
            }
        });

        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
					new InitScreensFunctions().initRegistrationScreen(actionEvent, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
                    new MyLogger(e.toString(),"ERROR");
					e.printStackTrace();
				}
            }
        });

        registerArtistBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
					new InitScreensFunctions().initRegistrationScreen(actionEvent, 2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
                    new MyLogger(e.toString(),"ERROR");
					e.printStackTrace();
				}
            }
        });
        guestBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                try {
					new InitScreensFunctions().initMainScreen(event);
				} catch (IOException e) {
                    new MyLogger(e.toString(),"ERROR");
					e.printStackTrace();
				}
            }
        });

        return holder;
    }

}
