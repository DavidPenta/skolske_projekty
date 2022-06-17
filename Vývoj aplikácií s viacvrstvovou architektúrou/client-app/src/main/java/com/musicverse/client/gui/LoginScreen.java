package com.musicverse.client.gui;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.Localozator;
import com.musicverse.client.api.ServerAPI;

import com.musicverse.client.objects.User;
import com.musicverse.client.sessionManagement.MyLogger;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;

import static com.musicverse.client.collections.Utils.createPlaylist;

public class LoginScreen {

    @FXML
    private Label backBtn;

    @FXML
    private TextField emailField;

    @FXML
    private Button logInBtn;

    @FXML
    private PasswordField pswdField;

    private Alert loginAlert = new Alert(AlertType.NONE);

    @FXML
    void onBackBtnClick(MouseEvent event) {
        this.goBack(event);
    }

    private void goBack(Event event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(WelcomeScreen.getHolder(), 1200, 600);
        window.setTitle("Music Verse");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void onLogInBtnClick(ActionEvent event) {
        try {
            val api = ServerAPI.getInstance();
            String password = this.pswdField.getText();
            String email = this.emailField.getText();

            val userJson = api.authenticate(email, password);
            if(userJson == null) {
                loginAlert.setAlertType(AlertType.ERROR);
                loginAlert.setContentText(Localozator.getResourceBundle().getString("INCORRECT LOGIN DATA"));
                loginAlert.show();
                return;
            }
            else {
                loginAlert.setAlertType(AlertType.INFORMATION);
                loginAlert.setContentText(Localozator.getResourceBundle().getString("LOGGED IN USER") + ": " + userJson.getString("username"));
                loginAlert.show();
                new MyLogger("Prihlaseny pouzivatel: "+userJson.getString("username"),"INFO");

                //Session management
                PreferencesLogin prefLogin = new PreferencesLogin();
                User user = new User(userJson.getString("username"), userJson.getString("email"), userJson.getInt("accessLevel")
                        , userJson.getInt("id"), "000000");
                prefLogin.setPreference(user);
                System.out.println(PreferencesLogin.getPrefs().getEmail());

                //zmena obrazovky
                //val response = api.getUserPlaylists(PreferencesLogin.getPrefs().getId());
                try {
                    new InitScreensFunctions().initMainScreen(event);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    new MyLogger(e.toString(),"ERROR");
                    e.printStackTrace();
                }

            }

            //this.goBack(event);

        }
        catch(Exception e){
            new MyLogger(e.toString(),"ERROR");
            e.printStackTrace();
        }

    }




}
