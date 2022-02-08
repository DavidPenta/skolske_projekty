package utils;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public interface Login {
	public Pane pane = new Pane();
	public Scene scene = new Scene(pane,600,600);
	
	void SetAndInitilize();
}