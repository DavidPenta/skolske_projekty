package gui; 

import java.util.ArrayList;


import controller.ControllerKontrolor;
import controller.ControllerObserver;
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

public class OknoKontrolor extends Stage implements Runnable, Okno {
	private Pane pane = null;
	private Thread t = null;
	private Button Tlacidlo1 = new Button("Skontrolovane");
	private Button Tlacidlo2 = new Button("Poslat do opravy");
	private Button TlacidloInfo = new Button("Vypis Info");
	private Button Spat = new Button("Spat");
	private Button Reset = new Button("Reset Info");
	private TextArea vypis = new TextArea();
	private ListView<String> listView = new ListView<String>();
	private ControllerKontrolor c;
	private ControllerObserver cObs;
	int cislo;
	private SaveData s;
	
	public OknoKontrolor(SaveData save) {
		this.pane = new Pane();
		this.s = save;
		this.c = save.getCk();
		this.cObs = save.getcObs();

		this.SetAndInitilize();

		this.Tlacidlo1.setOnAction(e -> {
			try {c.Skontrolovane();} 
			
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
			}
		
		} );
		
		this.Tlacidlo2.setOnAction(e -> {
			try {c.DoOpravy();} 
			
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
			}
		
		} );
		this.TlacidloInfo.setOnAction(e -> c.kontrolorVypisInfo()); 
		this.Spat.setOnAction(e -> spat(this,cObs));
		this.Reset.setOnAction(e -> c.resetInfo());
		this.setScene(new Scene(pane, 800, 700));
		this.show();
		
		OknoKontrolor o = this;
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
		
		ArrayList<Objednavka> objednavky = c.setKontrolor();
		Platform.runLater(() -> {
		listView.getItems().clear();
		for (int i = 0; i < objednavky.size(); i++) {
			cislo = objednavky.get(i).getCislo();
			if (cislo == -1)
				listView.getItems().add(objednavky.get(i).getStr() + " pre skladnika");
			else
				listView.getItems().add(objednavky.get(i).getStr() + " Cislo zakaznika: " + cislo);
			}
		vypis.clear();
		vypis.appendText(c.kontrolorText());
		vypis.setScrollTop(vypis.getLength());
		});
		//System.out.println("Current Thread ID- " + Thread.currentThread().getId() + " For Thread- " + Thread.currentThread().getName());   
	}
	public SaveData getS() {
		return s;
	}
 	@Override
	public void SetAndInitilize() {
 		this.setTitle("Kontrolor");
		LayoutStyler.SetLayout(pane, listView, 20, 20, 760, 400 , true);
		LayoutStyler.SetLayout(pane, vypis, 20, 460,  760, 200 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 670, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, Tlacidlo1, 20,  428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo2, 140,  428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, TlacidloInfo, 700, 428, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Reset, 620, 428, 100, 30 , true);
	}
 	
}
