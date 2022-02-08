package gui;

import java.util.ArrayList;

import controller.ControllerObserver;
import controller.ControllerPlnic;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

public class OknoPlnic extends Stage implements Runnable, Okno {
	private Pane pane = null;
	private Thread t = null;
	private Button Tlacidlo1 = new Button("Nalpnit");
	private Button Tlacidlo2 = new Button("Doplnit cisternu so snehom");
	private Button Tlacidlo3 = new Button("Doplnit cisternu s penou");
	private Button Tlacidlo4 = new Button("Doplnit cisternu s praskom");
	private Button Tlacidlo5 = new Button("Vypis stav cisterien");
	private Button TlacidloInfo = new Button("Vypis Info");
	private Button Reset = new Button("Reset Info");
	private Button Spat = new Button("Spat");
	
	private ChoiceBox<String> cbPena = new ChoiceBox<String>(FXCollections.observableArrayList("AFFF", "AR-AFFF","FFFP"));
	private ChoiceBox<String> cbSneh = new ChoiceBox<String>(FXCollections.observableArrayList("40%", "50%","75%"));
	private ChoiceBox<String> cbPrasok = new ChoiceBox<String>(FXCollections.observableArrayList("Super-D", "M28","Na-X"));
	private TextArea vypis = new TextArea();
	private ListView<String> listView = new ListView<String>();
	private ControllerPlnic c;
	private ControllerObserver cObs;
	int cislo;
	private SaveData s;
	
	public OknoPlnic(SaveData save) {
		this.pane = new Pane();
		this.c = save.getCpl();
		this.s = save;
		this.cObs = save.getcObs();
		
		this.SetAndInitilize();
		
		
		this.Tlacidlo1.setOnAction(e -> {
			try {c.PlnicModel();} 
			
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Ziadne pristoje nie su k dispozicii.");
				a.showAndWait();
			}
		
		} );
		
		this.Tlacidlo2.setOnAction(e -> {
			try {c.doplnSneh(cbSneh.getSelectionModel().selectedItemProperty().getValue());
			} 
		
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Musis zvolit koncentraciu.");
				a.showAndWait();
			}

		} );
		
		this.Tlacidlo3.setOnAction(e -> {
			try {c.doplnPenu(cbPena.getSelectionModel().selectedItemProperty().getValue());
			} 
		
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Musis zvolit penidlo.");
				a.showAndWait();
			}

		} );
		
		this.Tlacidlo4.setOnAction(e -> {
			try {c.doplnPrasok(cbPrasok.getSelectionModel().selectedItemProperty().getValue());
			} 
		
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Musis zvolit druh prasku.");
				a.showAndWait();
			}

		} );
		
		this.Tlacidlo5.setOnAction(e -> c.plnicVypisStavyCisterien());
		
		this.TlacidloInfo.setOnAction(e -> c.plnicVypisInfo());
		
		this.Reset.setOnAction(e -> c.resetInfo());
		
		this.Spat.setOnAction(e -> spat(this,cObs));
		this.setScene(new Scene(pane, 800, 700));
		this.show();
		
		OknoPlnic o = this;
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
		ArrayList<Objednavka> objednavky = c.setPlnic();
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
		vypis.appendText(c.plnicText());
		});
		//System.out.println("Current Thread ID- " + Thread.currentThread().getId() + " For Thread- " + Thread.currentThread().getName());   
	}
	public SaveData getS() {
		return s;
	}
	
 	@Override
	public void SetAndInitilize() {
 		this.setTitle("Plnic");
		this.vypis.clear();
		LayoutStyler.SetLayout(pane, listView, 20, 20, 760, 350 , true);
		LayoutStyler.SetLayout(pane, vypis, 20, 470,  750, 190 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 670, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, Tlacidlo1, 20,  378, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo2, 140,  378, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo3, 140,  408, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo4, 140,  438, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo5, 650, 388, 100, 30,  true);
		LayoutStyler.SetLayout(pane, TlacidloInfo, 700, 428, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, cbSneh, 310,  378, 100, 30 , true);
		LayoutStyler.SetLayout(pane, cbPena, 310,  408, 100, 30 , true);
		LayoutStyler.SetLayout(pane, cbPrasok, 310,  438, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Reset, 620, 428, 100, 30 , true);
		
	}
}