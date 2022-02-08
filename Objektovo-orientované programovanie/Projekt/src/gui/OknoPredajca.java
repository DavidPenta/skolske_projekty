package gui;

import java.util.ArrayList;

import controller.ControllerObserver;
import controller.ControllerPredajca;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.Objednavka;
import utils.LayoutStyler;
import utils.Okno;
import utils.PrazdneExeption;
import utils.SaveData;



public class OknoPredajca extends Stage implements Runnable, Okno {
	private Pane pane = null;
	private Thread t = null;
	private Button Tlacidlo1 = new Button("Prijat");
	private Button Tlacidlo2 = new Button("Zamietnut");
	private Button Tlacidlo3 = new Button("Vypis zakaznikov");
	private Button TlacidloInfo = new Button("Vypis Info");
	private Button Reset = new Button("Reset Info");
	private Button Objednavky = new Button("Vypis vsetky prebiehajuce objednavky");
	private TextArea vypis = new TextArea();
	private ListView<String> listView = new ListView<String>();
	private ControllerPredajca c;
	private SaveData s;
	private Button Spat = new Button("Spat");
	private ControllerObserver cObs;
	
	public OknoPredajca(SaveData save) {
		this.pane = new Pane();
		this.c = save.getCp();
		this.s = save;
		this.cObs = save.getcObs();
		
		
		this.SetAndInitilize();
		
		

		this.Tlacidlo1.setOnAction(e -> {
			try {c.PredajcaModel();} 
			
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
			}
		
		} );
		this.Tlacidlo2.setOnAction(e -> {
			try {c.PredajcaModel_zamietnuta();}
		
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
		}
	
		} );
		
		this.Tlacidlo3.setOnAction(e -> {
			try {c.VypisZakaznikov();}
		
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadny zakaznici neboli vytvoreni.");
				a.showAndWait();
		}
	
		} );
		this.TlacidloInfo.setOnAction(e -> c.predajcaVypisInfo()); 
		this.Spat.setOnAction(e -> spat(this,cObs));
		this.Objednavky.setOnAction(e -> c.vypisObjednavky());
		this.Reset.setOnAction(e -> c.resetInfo());
		
		this.setScene(new Scene(pane, 800, 700));
		this.show();
		
		OknoPredajca o = this;
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
		
		ArrayList<Objednavka> objednavky = c.setPredajca();
		Platform.runLater(() -> {
			listView.getItems().clear();
			for (int i = 0; i < objednavky.size(); i++) {
				listView.getItems().add(objednavky.get(i).getStr() + " Cislo zakaznika: " + objednavky.get(i).getCislo());
			}
			vypis.clear();
			vypis.appendText(c.predajcaText());
			vypis.setScrollTop(vypis.getLength());
		});
		//System.out.println("Current Thread ID- " + Thread.currentThread().getId() + " For Thread- " + Thread.currentThread().getName());   	
		

	}
	public SaveData getS() {
		return s;
	}
	
 	@Override
	public void SetAndInitilize() {
 		this.setTitle("Predajca");
		this.vypis.clear();
		LayoutStyler.SetLayout(pane, listView, 20, 20, 760, 400 , true);
		LayoutStyler.SetLayout(pane, vypis, 20, 460,  760, 200 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 670, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, Tlacidlo1, 20,  428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo2, 80,  428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo3, 340,  428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, TlacidloInfo, 700, 428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Reset, 620, 428, 100, 30 , true);
	}
}