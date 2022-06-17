package com.musicverse.client.gui;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.api.ServerAPI;
import com.musicverse.client.collections.Controller;
import com.musicverse.client.objects.Album;
import com.musicverse.client.objects.Request;
import com.musicverse.client.objects.User;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import lombok.SneakyThrows;
import lombok.val;

import java.util.ArrayList;

public class AdminSectionScreen {

    public AdminSectionScreen(){

    }

    @FXML
    private Label NickNameValueLabel;

    @FXML
    private Label backLabel;

    @FXML
    private Button blockUserBtn;

    @FXML
    private Label dateOfRegistrationLabel;

    @FXML
    private Label dateOfRegistrationValueLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label emailValueLabel;

    @FXML
    private Rectangle lab;

    @FXML
    private MenuBar menuBarSettings;

    @FXML
    private Label nickLabel;

    @FXML
    private Label nickNameLabel;

    @FXML
    private ChoiceBox<Integer> roleIDchoiceBox;

    @FXML
    private Label roleIDlabel;

    @FXML
    private Button safeButton;

    @FXML
    private TextField searchuserField;

    @FXML
    private Button showAccountBtn;

    @FXML
    private Label title;

    @FXML
    private TableView<Request> tableOfRequests;


    @FXML
    private TableView<User> searchedUsersTable;

    private ServerAPI api;
    @FXML
    private AnchorPane adminPane;

    private JsonNode searchedUsers;

    @SneakyThrows
    @FXML
    void backLabelClick(MouseEvent event) {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initMainScreen(ae);
    }

    @FXML
    void onBlockUserBtnClick(ActionEvent event) {

    }

    @FXML
    void onShowAccountBtnClick(ActionEvent event) {
        NickNameValueLabel.setText(searchedUsersTable.getSelectionModel().getSelectedItem().getNickName());
        emailValueLabel.setText(searchedUsersTable.getSelectionModel().getSelectedItem().getEmail());
        this.NickNameValueLabel.setVisible(true);
        this.nickNameLabel.setVisible(true);
        this.emailLabel.setVisible(true);
        this.emailValueLabel.setVisible(true);
        this.roleIDlabel.setVisible(true);
        this.roleIDchoiceBox.setVisible(true);
        Integer[] st = { 1, 2, 3};
        // create a choiceBox
        roleIDchoiceBox.setItems(FXCollections.observableArrayList(st));
        roleIDchoiceBox.getSelectionModel().select(searchedUsersTable.getSelectionModel().getSelectedItem().getRole() - 1);

    }

    private String from;

    public void setSettings(String from){
        this.NickNameValueLabel.setVisible(false);
        this.nickNameLabel.setVisible(false);
        this.emailLabel.setVisible(false);
        this.emailValueLabel.setVisible(false);
        this.roleIDlabel.setVisible(false);
        this.roleIDchoiceBox.setVisible(false);
        this.dateOfRegistrationValueLabel.setVisible(false);
        this.dateOfRegistrationLabel.setVisible(false);
        nickLabel.setText(PreferencesLogin.getPrefs().getNickName());

        this.api = ServerAPI.getInstance();
        JsonNode requests = api.getRequests();

        ArrayList<Request> requestsArrayList = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++){
            requestsArrayList.add(
                    new Request(
                            requests.get(i).getInt("id"),
                            requests.get(i).getString("name")
                    )

            );
        }
        Controller<Request> controller = new Controller<>();
        tableOfRequests.getItems().clear();
        tableOfRequests = controller.initialize(requestsArrayList, new String[]{"id", "artistName"}, tableOfRequests, 4 , adminPane);
        tableOfRequests.setVisible(true);
        tableOfRequests.refresh();
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        String search = searchuserField.getText();
        this.api = ServerAPI.getInstance();
        searchedUsers = api.searchUser(search);

        ArrayList<User> usersArrayList = new ArrayList<>();
        for (int i = 0; i < searchedUsers.size(); i++){
            usersArrayList.add(
                    new User(
                            searchedUsers.get(i).getString("nick_name"),
                            searchedUsers.get(i).getString("email"),
                            searchedUsers.get(i).getInt("access_level"),
                            searchedUsers.get(i).getInt("id"),
                            "0000"
                    )

            );
        }
        Controller<User> controller = new Controller<>();
        searchedUsersTable.getItems().clear();
        searchedUsersTable = controller.initialize(usersArrayList, new String[]{"nickName", "email", "access_level", "id"}, searchedUsersTable, 3 , adminPane);
        searchedUsersTable.setVisible(true);
        searchedUsersTable.refresh();

    }

    @SneakyThrows
    @FXML
    void onSafeBtnClick(ActionEvent event) {
        api.updateUser(searchedUsersTable.getSelectionModel().getSelectedItem().getId(), roleIDchoiceBox.getSelectionModel().getSelectedItem(), 0);
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initSettingsScreen("Admin", "/AdminSectionScreen.fxml", adminPane, 0, 0);
    }

    @SneakyThrows
    @FXML
    void onDeclineBtnClick(ActionEvent event) {
        if (tableOfRequests.getSelectionModel().getSelectedItem() != null){
            api.updateUser(searchedUsersTable.getSelectionModel().getSelectedItem().getId(), 2, 1);
            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
            initScreensFunctions.initSettingsScreen("Admin", "/AdminSectionScreen.fxml", adminPane, 0, 0);
        }
    }

    @SneakyThrows
    @FXML
    void onAcceptBtnClick(ActionEvent event) {

        if (tableOfRequests.getSelectionModel().getSelectedItem() != null){
            api.updateUser(tableOfRequests.getSelectionModel().getSelectedItem().getId(), 1, 1);
            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
            initScreensFunctions.initSettingsScreen("Admin", "/AdminSectionScreen.fxml", adminPane, 0, 0);
        }

    }


}
