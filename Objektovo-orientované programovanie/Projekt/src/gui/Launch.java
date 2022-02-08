package gui;

import controller.Controller;
import controller.ControllerCustomHp;
import controller.ControllerKontrolor;
import controller.ControllerLogin;
import controller.ControllerObserver;
import controller.ControllerOpravar;
import controller.ControllerPlnic;
import controller.ControllerPracovnik;
import controller.ControllerPredajca;
import controller.ControllerSkladnik;
import controller.ControllerZakaznik;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.stage.*;
import utils.LayoutStyler;
import utils.Login;
import utils.SaveData;

public class Launch extends Application implements Login{
	
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
	
		
	public void start(Stage hlavneOkno) {
		
		try {
			pageStage = hlavneOkno;
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			pageStage.setScene(scene);
			SetAndInitilize();
			InitializeController();
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

	public static void main(String[] args) {
		Application.launch(args);
	
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

	
	public void InitializeController() {
		Controller c = new Controller();
		ControllerObserver cObs = new ControllerObserver(c);
		ControllerOpravar co = new ControllerOpravar(c,cObs);
		ControllerPredajca cp = new ControllerPredajca(c,cObs); 
		ControllerSkladnik cs = new ControllerSkladnik(c,cObs); 
		ControllerKontrolor ck = new ControllerKontrolor(c,cObs);
		ControllerPracovnik cpr = new ControllerPracovnik(c,cObs);
		ControllerPlnic cpl = new ControllerPlnic(c,cObs);
		ControllerZakaznik cz = new ControllerZakaznik(c,cObs);
		ControllerCustomHp cchp = new ControllerCustomHp(c,cz,cObs); 
		this.s = new SaveData(0, c, cObs, co, cp, cs, ck, cpr, cpl, cz, cchp);
		ctrl = new ControllerLogin(this,s);	
		s.setCtrl(ctrl);
	}
}