package com.musicverse.client.gui;

import com.musicverse.client.sessionManagement.MyLogger;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.musicverse.client.api.ServerAPI;
import lombok.val;
import com.musicverse.client.Localozator;

import java.io.File;
import java.util.regex.Pattern;

public class RegisterScreen {
    final FileChooser fileChooser = new FileChooser();
    private int role;

    @FXML
    private Circle avatarCircle;

    @FXML
    private Label backBtn;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nickNameField;

    @FXML
    private PasswordField pswd2Field;

    @FXML
    private PasswordField pswdField;

    @FXML
    private Button signUpBtn;

    private Alert registerAlert = new Alert(AlertType.NONE);

    @FXML
    void onAvatarClick(MouseEvent event) {
        Stage stage = new Stage();
        final StackPane stac = new StackPane();
        final Button openButton = new Button(Localozator.getResourceBundle().getString("CHOOSE PROFILE PICTURE"));
        final Button saveButton = new Button(Localozator.getResourceBundle().getString("SAVE PROFILE PICTURE"));
        openButton.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image1 = new Image(file.toURI().toString());
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
                BackgroundImage backgroundImage = new BackgroundImage(image1, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                stac.setBackground(new Background(backgroundImage));
            }
        });

        stac.getChildren().add(openButton);

        stage.setScene(new Scene(stac, 500, 500));
        stage.show();

    }

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
    void onSignUpClick(ActionEvent event) {
    	if(!this.correctInputData()) {
             return;
    	}
    	try {
	    	val api = ServerAPI.getInstance();
	    	String nickname = this.nickNameField.getText();
	    	String email = this.emailField.getText();
            String password = this.pswdField.getText();

            Pattern pattern_email = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

            if (!pattern_email.matcher(email).matches()){
                registerAlert.setAlertType(AlertType.ERROR);
                registerAlert.setContentText(Localozator.getResourceBundle().getString("INVALID EMAIL"));
                registerAlert.show();
                return;
            }


            Pattern pattern_passwd = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

            if (!pattern_passwd.matcher(password).matches()){
                registerAlert.setAlertType(AlertType.ERROR);
                registerAlert.setContentText(Localozator.getResourceBundle().getString("INVALID PASSWD"));
                registerAlert.show();
                return;
            }

	    	if (!api.registerUser(email, nickname, password, role)) {
                registerAlert.setAlertType(AlertType.ERROR);
                registerAlert.setContentText(Localozator.getResourceBundle().getString("USERNAME OR EMAIL IS ALREADY IN USE!"));
                registerAlert.show();
                return;
            };

            registerAlert.setAlertType(AlertType.INFORMATION);
            registerAlert.setContentText(Localozator.getResourceBundle().getString("USER HAS BEEN CREATED - WAITING FOR APPROVAL!"));
            registerAlert.show();
            this.goBack(event);

    	}
    	catch(Exception e){
            new MyLogger(e.toString(),"ERROR");
            e.printStackTrace();
    	}

    }

    private boolean correctInputData() {
    	if((this.pswdField.getText().length()<=0) || (this.pswd2Field.getText().length()<=0) ||
    			(this.nickNameField.getText().length()<=0)|| (this.emailField.getText().length()<=0)) {
    		registerAlert.setAlertType(AlertType.ERROR);
   		 	registerAlert.setContentText(Localozator.getResourceBundle().getString("YOU MUST ENTER ALL DATA!"));
            registerAlert.show();
            return false;

    	}
    	if(!this.pswdField.getText().equals(this.pswd2Field.getText())) {
    		registerAlert.setAlertType(AlertType.ERROR);
   		 	registerAlert.setContentText(Localozator.getResourceBundle().getString("PASSWORDS DO NOT MATCH!"));
            registerAlert.show();
    		return false;
    	}

    	return true;
    }



    public void setLabelText(String text, int role){
        this.role = role;
        signUpBtn.setText(text);
    }

}
