package utils;

import controller.ControllerObserver;
import gui.Launch2;
import javafx.stage.Stage;
/**
* Rozhranie  <code>Okno</code> sluzi na spravu okien.
*/
public interface Okno{
	
	/**
	 * Implicitne implementovana metoda na odstranenie okna z observera a vratenie sa na login.
	 * @param o okno ktore sa ma zatvorit
	 * @param cObs ControllerObserver
	 */
	public default void spat(Stage o, ControllerObserver cObs) {
		if(o instanceof Okno) {
			SaveData s = ((Okno) o ).getS(); 
			if(o instanceof Runnable) {
				Runnable r = ((Runnable) o);
				cObs.vymazSledovatela(r);
		
				o.close();
				new Launch2(s);
			}
		}
	}
	/**
	 * Implicitne implementovana metoda na odstranenie okna z observera.
	 * @param o okno ktore sa ma zatvorit
	 * @param cObs ControllerObserver
	 */
	public default void zatvor(Stage o, ControllerObserver cObs) {
		Runnable r = ((Runnable) o);
		cObs.vymazSledovatela(r);
	}
	public void start();
	public SaveData getS();
	void SetAndInitilize();
}