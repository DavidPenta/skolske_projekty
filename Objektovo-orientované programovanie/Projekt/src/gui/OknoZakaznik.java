package gui;

import controller.ControllerCustomHp;
import controller.ControllerObserver;
import controller.ControllerZakaznik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import utils.LayoutStyler;
import utils.Okno;
import utils.SaveData;

public class OknoZakaznik extends Stage implements Runnable, Okno{
	private Pane pane = null;
	private Thread t = null;
	private Button Tlacidlo1 = new Button("Model1");
	private Button Tlacidlo2 = new Button("Model2");
	private Button Tlacidlo3 = new Button("Model3");
	private Button Tlacidlo4 = new Button("Custom");
	private Button Spat = new Button("Spat");
	private TextArea vypis = new TextArea();
	private ControllerZakaznik c;
	private int cislo;
	private SaveData s;
	private ControllerObserver cObs;
	private ControllerCustomHp cchp;

	
	public OknoZakaznik(SaveData save, int _cislo) {
		this.cislo = _cislo;
		this.c = save.getCz();
		this.s = save;
		this.cObs = save.getcObs();
		this.cchp = save.getCchp();
		
		
		
		this.pane = new Pane();
		
		this.SetAndInitilize();
		
		this.Tlacidlo1.setOnAction(e -> {
			c.ZakaznikObjednaModel("Model1",cislo);
			c.zakaznikText("Objednany bol Model 1\n", cislo);
		} );
		this.Tlacidlo2.setOnAction(e -> {
			c.ZakaznikObjednaModel("Model2",cislo);
			c.zakaznikText("Objednany bol Model 2\n", cislo);
		} );
		this.Tlacidlo3.setOnAction(e -> {
			c.ZakaznikObjednaModel("Model3",cislo);
			c.zakaznikText("Objednany bol Model 3\n", cislo);
		} );
	
		this.Tlacidlo4.setOnAction(e -> new OknoCustom(cchp,cislo));
		
		this.Spat.setOnAction(e -> spat(this,cObs));

		this.setScene(new Scene(pane, 800, 300));
		this.show();
		
		OknoZakaznik o = this;
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

		String str = c.zakaznikText(cislo);
		Platform.runLater(() -> {
		vypis.clear();
		vypis.appendText(str);
		vypis.setScrollTop(vypis.getLength());
	    });
		
		//System.out.println("Current Thread ID- " + Thread.currentThread().getId() + " For Thread- " + Thread.currentThread().getName()); 
	}
	
	public SaveData getS() {
		return s;
	}

	@Override
	public void SetAndInitilize() {
		this.setTitle("Zakaznik " + cislo);
		LayoutStyler.SetLayout(pane, vypis, 20, 20, 760, 200 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 270, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, Tlacidlo1, 20, 230, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo2, 100, 230, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo3, 180, 230, 100, 30 , true);
		LayoutStyler.SetLayout(pane, Tlacidlo4, 260, 230, 100, 30 , true);
		
	}
}

