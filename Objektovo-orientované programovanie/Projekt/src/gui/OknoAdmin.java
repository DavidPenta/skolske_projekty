package gui;

import controller.ControllerObserver;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.LayoutStyler;
import utils.SaveData;

public class OknoAdmin extends Stage {
	private Pane pane = null;
	private Button skladnik = new Button("Skladnik");
	private Button pracovnik = new Button("Pracovnik vyrobnej linky");
	private Button zakaznik = new Button("Zakaznik");
	private Button kontrolor = new Button("Kontrolor");
	private Button plnic = new Button("Plnic");
	private Button predajca = new Button("Predajca");
	private Button opravar = new Button("Opravar");
	private Button okna = new Button("Vypis otvorene okna");
	private Button reset = new Button("Reset");
	private TextArea vypis = new TextArea();
	private Button Spat = new Button("Spat");
	private int a = 0;

	private ControllerObserver cObs;
	private SaveData s;
	
	public OknoAdmin(SaveData save) {
		this.pane = new Pane();
		this.s = save;
		this.cObs = save.getcObs();
		this.a = save.getCisloZakaznika();

		this.SetAndInitilize();
		

		
		this.zakaznik.setOnAction(e -> {cObs.pridajZakaznika(new OknoZakaznik(s,a),a); a++; s.novyZakaznik(); });
		this.skladnik.setOnAction(e -> {cObs.pridajSledovatela(new OknoSkladnik(s));});
		this.pracovnik.setOnAction(e -> {cObs.pridajSledovatela(new OknoPracovnik(s));});
		this.predajca.setOnAction(e -> {cObs.pridajSledovatela(new OknoPredajca(s));});
		this.plnic.setOnAction(e -> {cObs.pridajSledovatela(new OknoPlnic(s));});
		this.kontrolor.setOnAction(e -> {cObs.pridajSledovatela(new OknoKontrolor(s));});
		this.opravar.setOnAction(e -> {cObs.pridajSledovatela(new OknoOpravar(s));});
		this.okna.setOnAction(e -> vypis.appendText(cObs.vypisSledovatelov()));
		this.Spat.setOnAction(e -> {this.close(); new Launch2(s);});
		this.reset.setOnAction(e -> cObs.resetZamestnancov());
		
		this.setScene(new Scene(pane, 800, 700));
		this.show();
	}
	
	public void SetAndInitilize() {
		this.setTitle("Admin");
		LayoutStyler.SetLayout(pane, vypis, 20, 260,  760, 400 , true);
		vypis.setEditable(false);
		LayoutStyler.SetLayout(pane, Spat, 20, 670, 100, 30 , true);

		LayoutStyler.SetLayout(pane, zakaznik, 20, 120, 100, 30 , true);
		LayoutStyler.SetLayout(pane, predajca, 100, 120, 100, 30 , true);
		LayoutStyler.SetLayout(pane, skladnik, 180, 120, 100, 30 , true);
		LayoutStyler.SetLayout(pane, pracovnik, 260, 120, 100, 30 , true);
		LayoutStyler.SetLayout(pane, plnic, 20, 180, 100, 30 , true);
		LayoutStyler.SetLayout(pane, kontrolor, 100, 180, 100, 30 , true);
		LayoutStyler.SetLayout(pane, opravar, 180, 180, 100, 30 , true);
		LayoutStyler.SetLayout(pane, okna, 260, 180, 100, 30 , true);
		LayoutStyler.SetLayout(pane, reset, 400, 180, 100, 30 , true);
	}

	
}