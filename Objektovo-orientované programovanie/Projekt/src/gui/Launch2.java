package gui;

import controller.ControllerLogin;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.LayoutStyler;
import utils.Login;
import utils.SaveData;

public class Launch2 extends Stage implements Login{

	public Stage pageStage = null;
	public ControllerLogin ctrl = null;
	public Button loginBtn = new Button("Prihlasit sa");
	public Button registerBtn = new Button("Zaregistrovat sa");
	public TextField nameField = new TextField(); 
	public PasswordField passField = new PasswordField(); 
	public Label nameLabel = new Label("Meno:");
	public Label passLabel = new Label("Heslo:");
	public Label infoLabel = new Label("");
	SaveData s;
	
	public Launch2(SaveData save) {
		ctrl = save.getCtrl();
		try {
			pageStage = this;
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			pageStage.setScene(scene);
			SetAndInitilize();
			pageStage.show();
		}catch(Exception e){
			e.printStackTrace();
		}

		
		loginBtn.setOnAction( e -> { 
			if (ctrl.Login(nameField.getText(), passField.getText()))
				pageStage.close();
			});
		
		registerBtn.setOnAction( e -> { 	
			if (ctrl.Register(nameField.getText(), passField.getText()))
				pageStage.close();
			});
	}


	@Override
	public void SetAndInitilize() {
		this.pageStage.setTitle("Login");
		LayoutStyler.SetLayout(this, loginBtn, 332, 300, 100, 30 , true);
		LayoutStyler.SetLayout(this, registerBtn, 205, 300, 100, 30 , true);
		LayoutStyler.SetLayout(this, nameField, 205, 240, 200, 25 , true);
		LayoutStyler.SetLayout(this, passField, 205, 270, 200, 25 , true);
		LayoutStyler.SetLayout(this, nameLabel, 140, 240, 100, 30 , true);
		LayoutStyler.SetLayout(this, passLabel, 140, 270, 100, 30 , true);
		LayoutStyler.SetLayout(this, infoLabel, 205, 330, 100, 30 , false);
	}
}