package gui;

import java.util.ArrayList;

import controller.ControllerCustomHp;
import controller.ControllerObserver;
import controller.ControllerSkladnik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Objednavka;
import utils.LayoutStyler;
import utils.Okno;
import utils.PrazdneExeption;
import utils.SaveData;

public class OknoSkladnik extends Stage implements Runnable, Okno{
	private Pane pane = null;
	private Thread t = null;
	private Button pTlacidlo = new Button("Schvalit");
	private Button vyrobit1Tlacidlo = new Button("Model1");
	private Button vyrobit2Tlacidlo = new Button("Model2");
	private Button vyrobit3Tlacidlo = new Button("Model3");
	private Button vyrobitCustomTlacidlo = new Button("Custom");
	private Button tlacidloDoOpravy = new Button("Poslat do opravy");
	private TextArea vypis = new TextArea();
	ListView<String> listView = new ListView<String>();
	private Button Tlacidlo0 = new Button("Kup telo s");
	private Button Tlacidlo1 = new Button("Kup telo m");
	private Button Tlacidlo2 = new Button("Kup telo l");
	private Button Tlacidlo3 = new Button("Kup luxhose");
	private Button Tlacidlo4 = new Button("Kup optihose");
	private Button Tlacidlo5 = new Button("Kup kovove rucky");
	private Button Tlacidlo6 = new Button("Kup plastove rucky");
	private Button Tlacidlo7 = new Button("Kup M24BAR");
	private Button Tlacidlo8 = new Button("Kup M32BAR");
	private Button Tlacidlo9 = new Button("Kup M34BAR");
	private Button TlacidloInfo = new Button("Vypis Info");
	private ControllerSkladnik c;
	private SaveData s;
	private Button Spat = new Button("Spat");
	private ControllerObserver cObs;
	private ControllerCustomHp cch;

	public OknoSkladnik(SaveData save) {
		this.pane = new Pane();
		this.c = save.getCs();
		this.s = save;
		this.cObs = save.getcObs();
		this.cch = save.getCchp();
		
		this.setTitle("Skladnik");
		this.SetAndInitilize();

		

		
		this.pTlacidlo.setOnAction(e -> {
			try {c.odstran_model1();} 
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
			}}); 
		
		this.vyrobit1Tlacidlo.setOnAction(e -> c.vyrob_model1());
		this.vyrobit2Tlacidlo.setOnAction(e -> c.vyrob_model2());
		this.vyrobit3Tlacidlo.setOnAction(e -> c.vyrob_model3());
		this.vyrobitCustomTlacidlo.setOnAction(e -> new OknoCustom(cch,-1));
		this.Tlacidlo0.setOnAction(e -> c.kup_telo_s());
		this.Tlacidlo1.setOnAction(e -> c.kup_telo_m());
		this.Tlacidlo2.setOnAction(e -> c.kup_telo_l());
		this.Tlacidlo3.setOnAction(e -> c.kup_luxhose());
		this.Tlacidlo4.setOnAction(e -> c.kup_optihose());
		this.Tlacidlo5.setOnAction(e -> c.kup_kovovu_rucku());
		this.Tlacidlo6.setOnAction(e -> c.kup_plastovu_rucku());
		this.Tlacidlo7.setOnAction(e -> c.kup_M24BAR()); 
		this.Tlacidlo8.setOnAction(e -> c.kup_M32BAR()); 
		this.Tlacidlo9.setOnAction(e -> c.kup_M34BAR());
		this.TlacidloInfo.setOnAction(e -> c.skladnikVypisInfo()); 
		
		this.tlacidloDoOpravy.setOnAction(e -> {
			try {c.skladnikPosliDoOpravy();} 
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su na sklade.");
				a.showAndWait();
			}}); 
		
		this.Spat.setOnAction(e -> spat(this,cObs));

		this.setScene(new Scene(pane, 800, 700));
		this.show();
		
		OknoSkladnik o = this;
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	   zatvor(o,cObs);
	        	   o.close();
	          }
	      });
		
	}
	
	public void start() {
		t = new Thread(this);
		t.start();			
	}
	@Override
	public void run() {
		ArrayList<Objednavka> objednavky = c.getSkladnikObjednavky();
		Platform.runLater(() -> {
			this.listView.getItems().clear();
	
			for (int i = 0; i < objednavky.size(); i++) {
				listView.getItems().add(c.strPreSkladnika(i,objednavky));
				}
		
			vypis.clear();
			vypis.appendText(c.skladnikText());
			vypis.setScrollTop(vypis.getLength());
		});
	    //System.out.println("Current Thread ID- " + Thread.currentThread().getId() + " For Thread- " + Thread.currentThread().getName());
	    
	}
	public SaveData getS() {
		return this.s;
	}

	@Override
	public void SetAndInitilize() {
		this.vypis.clear();
		
		LayoutStyler.SetLayout(pane, listView, 20, 20, 760, 400 , true);
		LayoutStyler.SetLayout(pane, vypis, 20, 460,  460, 200 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 670, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, pTlacidlo, 20, 428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, vyrobit1Tlacidlo, 80, 428, 100, 30  , true);
		LayoutStyler.SetLayout(pane, vyrobit2Tlacidlo, 140, 428, 100, 30  , true);
		LayoutStyler.SetLayout(pane, vyrobit3Tlacidlo, 200, 428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, vyrobitCustomTlacidlo, 260, 428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, tlacidloDoOpravy, 320, 428, 100, 30  , true);
		LayoutStyler.SetLayout(pane, TlacidloInfo, 430, 428, 100, 30 , true);

		LayoutStyler.SetLayout(pane, Tlacidlo0, 500, 468, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo1, 600, 468, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo2, 700, 468, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo3, 500, 508, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo4, 600, 508, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo5, 500, 548, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo6, 620, 548, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo7, 500, 588, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo8, 600, 588, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo9, 700, 588, 100, 30 , true);				
	}	
}





