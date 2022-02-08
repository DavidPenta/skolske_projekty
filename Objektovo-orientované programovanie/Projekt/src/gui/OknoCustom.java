package gui;

import controller.ControllerCustomHp;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Hadica;
import model.KategoriaHasiacehoPristroja;
import model.Objem;
import model.Rucka;
import model.Tlakomer;
import utils.LayoutStyler;
import utils.PrazdneExeption;

public class OknoCustom extends Stage {
	private Pane pane = null;
	private Text t0 = new Text("Kategoria");
	private ChoiceBox<KategoriaHasiacehoPristroja> cb0 = new ChoiceBox<KategoriaHasiacehoPristroja>(FXCollections.observableArrayList( KategoriaHasiacehoPristroja.SNEHOVY, KategoriaHasiacehoPristroja.PRASKOVY, KategoriaHasiacehoPristroja.VODNY, KategoriaHasiacehoPristroja.PENOVY));
	private Text t1 = new Text("Telo");
	private ChoiceBox<Objem> cb1 = new ChoiceBox<Objem>(FXCollections.observableArrayList( Objem.S, Objem.M, Objem.L));
	private Text t2 = new Text("Hadica");
	private ChoiceBox<Hadica> cb2 = new ChoiceBox<Hadica>(FXCollections.observableArrayList(Hadica.LUXHOSE, Hadica.OPTIHOSE));
	private Text t3 = new Text("Rucka");
	private ChoiceBox<Rucka> cb3 = new ChoiceBox<Rucka>(FXCollections.observableArrayList(Rucka.KOVOVA, Rucka.PLASTOVA));
	private Text t4 = new Text("Tlakomer");
	private ChoiceBox<Tlakomer> cb4 = new ChoiceBox<Tlakomer>(FXCollections.observableArrayList(Tlakomer.M24BAR, Tlakomer.M32BAR, Tlakomer.M34BAR));
	private Button Tlacidlo1 = new Button("Objednat");

	
	
	
	public OknoCustom(ControllerCustomHp c, int i) {
		this.pane = new Pane();
		
		this.SetAndInitilize();
		
		
		this.Tlacidlo1.setOnAction(e -> {
			try{c.customHP(i, cb0.getSelectionModel().selectedItemProperty().getValue(), cb1.getSelectionModel().selectedItemProperty().getValue(), cb2.getSelectionModel().selectedItemProperty().getValue(), cb3.getSelectionModel().selectedItemProperty().getValue(), cb4.getSelectionModel().selectedItemProperty().getValue());
			}
			catch(PrazdneExeption ex) {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error.");
				a.setContentText("Musite zadat vsetky suciastky.");
				a.showAndWait();
			}
		});
		
		this.setScene(new Scene(pane, 300, 300));
		this.show();
	}
	
	public void SetAndInitilize() {
		this.setTitle("Custom Hasiaci pristroj");
		LayoutStyler.SetLayout(pane, t0, 20, 35, 760, 200 , true);
		LayoutStyler.SetLayout(pane, cb0, 80, 20, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, t1, 20, 75, 760, 200 , true);
		LayoutStyler.SetLayout(pane, cb1, 80, 60, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, t2, 20, 115, 760, 200 , true);
		LayoutStyler.SetLayout(pane, cb2, 80, 100, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, t3, 20, 155, 760, 200 , true);
		LayoutStyler.SetLayout(pane, cb3, 80, 140, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, t4, 20, 195, 760, 200 , true);
		LayoutStyler.SetLayout(pane, cb4, 80, 180, 100, 30 , true);
		
		LayoutStyler.SetLayout(pane, Tlacidlo1, 20, 250, 100, 30 , true);
	}
		
	
	
}
