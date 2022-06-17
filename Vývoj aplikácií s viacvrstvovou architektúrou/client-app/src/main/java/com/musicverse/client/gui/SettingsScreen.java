package com.musicverse.client.gui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.Localozator;
import com.musicverse.client.api.ServerAPI;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class SettingsScreen {

    @FXML
    private Label backLabel;

    @FXML
    private PasswordField confirmOldPswdField;

    @FXML
    private PasswordField confirmPswdField;

    @FXML
    private Button discardBtn;

    @FXML
    private ChoiceBox<String> languageOption;

    @FXML
    private PasswordField newPswdField;

    @FXML
    private TextField nickField;

    @FXML
    private Label nickLabel;

    @FXML
    private Button saveBtn;

    private ServerAPI api;

    @FXML
    private Label title;

    private String from;

    private Alert loginAlert = new Alert(Alert.AlertType.NONE);

    @FXML
    void onBackLabelClick(MouseEvent event) throws IOException {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        new InitScreensFunctions().initMainScreen(ae);
    }

    @FXML
    void onDiscardBtnClick(ActionEvent event) {

    }

    @FXML
    void onSaveBtnClick(ActionEvent event) throws IOException {
        String nickname = "null", newPswd = "null", confirmPswd = "null", confirmOldPswd = "null";
        nickname = nickField.getText();
        newPswd = newPswdField.getText();
        confirmPswd = confirmPswdField.getText();
        confirmOldPswd = confirmOldPswdField.getText();

        this.api = ServerAPI.getInstance();

        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();

        if (nickname != null || newPswd != null){
            System.out.println("test12");
            if (newPswd.getBytes(StandardCharsets.UTF_8).length > 0){
                System.out.println("test11");
                if (!newPswd.equals(confirmPswd)){
                    loginAlert.setAlertType(Alert.AlertType.ERROR);
                    loginAlert.setContentText(Localozator.getResourceBundle().getString("PASSWORDS DO NOT MATCH!"));
                    loginAlert.show();
                    System.out.println("test10");
                } else {
                    Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)");
                    Matcher matcher = pattern.matcher(newPswd);
                    boolean matchFound = matcher.find();
                    System.out.println("test9");
                    if(matchFound && newPswd.getBytes(StandardCharsets.UTF_8).length > 7) {
                        loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                        loginAlert.setContentText(Localozator.getResourceBundle().getString("PASSWORD IS OK"));
                        loginAlert.show();
                        System.out.println("test8");
                        if (confirmOldPswd.getBytes(StandardCharsets.UTF_8).length > 1) {
                            if (api.updateUser(PreferencesLogin.getPrefs().getId(), newPswd, nickname, confirmOldPswd) == 1){
                                loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                                loginAlert.setContentText(Localozator.getResourceBundle().getString("ACTION WAS SUCCESSFUL"));
                                loginAlert.show();
                                System.out.println("test7");

                                initScreensFunctions.initMainScreen(event);
                            } else {
                                loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                                loginAlert.setContentText(Localozator.getResourceBundle().getString("INCORRECT PASSWORD"));
                                loginAlert.show();
                                System.out.println("test6");
                            }
                        }
                        else{
                            loginAlert.setAlertType(Alert.AlertType.ERROR);
                            loginAlert.setContentText(Localozator.getResourceBundle().getString("ENTER CURRENT PASSWORD"));
                            loginAlert.show();
                            System.out.println("test5");

                        }
                    } else {
                        loginAlert.setAlertType(Alert.AlertType.ERROR);
                        loginAlert.setContentText(Localozator.getResourceBundle().getString("PASSWORD IS TOO WEAK"));
                        loginAlert.show();
                        System.out.println("test4");

                    }
                }
            } else {
                assert nickname != null;
                if (nickname.getBytes(StandardCharsets.UTF_8).length > 0) {
                    if (confirmOldPswd.getBytes(StandardCharsets.UTF_8).length > 1) {
                        if (api.updateUser(PreferencesLogin.getPrefs().getId(), newPswd, nickname, confirmOldPswd) == 1){
                            loginAlert.setAlertType(Alert.AlertType.INFORMATION);
                            loginAlert.setContentText(Localozator.getResourceBundle().getString("ACTION WAS SUCCESSFUL"));
                            loginAlert.show();
                            System.out.println("test3");
                            initScreensFunctions.initMainScreen(event);
                        } else {
                            loginAlert.setAlertType(Alert.AlertType.ERROR);
                            loginAlert.setContentText(Localozator.getResourceBundle().getString("INCORRECT PASSWORD"));
                            loginAlert.show();
                            System.out.println("test2");

                        }
                    } else {
                        loginAlert.setAlertType(Alert.AlertType.ERROR);
                        loginAlert.setContentText(Localozator.getResourceBundle().getString("ENTER CURRENT PASSWORD"));
                        loginAlert.show();
                        System.out.println("test1");
                    }
                }
            }
        }
        if (nickname == null || nickname.equals("") || newPswd == null || newPswd.equals("")) {
            if (languageOption.getValue().equals("English")) {
                Localozator.setLocale(new Locale("en", "US"));
            }
            if (languageOption.getValue().equals("Slovenčina")) {
                Localozator.setLocale(new Locale("sk", "SK"));
            }
            initScreensFunctions.initMainScreen(event);
        }
    }

    public void setFrom(String from){
        this.from = from;
    }

}
