package controller;

import java.util.ArrayList;

import model.Objednavka;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerPlnic</code> sluzi ako controller pre OknoPlnic.
*/

public class ControllerPlnic {
	ControllerObserver cObs;
	Controller c;
	/**
	 * Metoda naplni prvy hasiaci pristroj a posle ho kontrolorovi.
	 * @throws PrazdneExeption plnic nema ziadne hasiace pristroje
	 */
	
	 public void PlnicModel() throws PrazdneExeption{
	    	Objednavka o = c.plnic.getFirstObjednavka();
	    	if ( o != null) {
	    		if (c.plnic.Napln(o.getHp()) == true) {
	    			c.plnic.pridajInfoPocet();
	    			c.plnic.deleteFirstObjednavka();
	    			c.kontrolor.addObjednavka(o);
	    		}
	    		cObs.upovedomSledovatelov();
	    	}
	    	else {
	    		throw new PrazdneExeption();
	    		}
	    }
	    
	    
		public ArrayList<Objednavka> setPlnic() {
			return c.plnic.getObjednavky();
		}
		

		public String plnicText() {
			return c.plnic.getText();
		}
		
		public void plnicText(String s) {
			c.plnic.addText(s);
			cObs.upovedomSledovatelov();
		}
		
		public void doplnSneh(String str) throws PrazdneExeption{
			if (str == null)
				throw new PrazdneExeption();
			c.plnic.doplnSneh(str);
			cObs.upovedomSledovatelov();
		}
		public void doplnPenu(String str) throws PrazdneExeption{
			if (str == null)
				throw new PrazdneExeption();
			c.plnic.doplnPenu(str);
			cObs.upovedomSledovatelov();
		}
		public void doplnPrasok(String str) throws PrazdneExeption{
			if (str == null)
				throw new PrazdneExeption();
			c.plnic.doplnPrasok(str);
			cObs.upovedomSledovatelov();
		}
		
		public void plnicVypisStavyCisterien() {
			c.plnic.vypisStavyCisterien();
			cObs.upovedomSledovatelov();
		}
		
		public void plnicVypisInfo() {
			c.plnic.vypisInfo();
			cObs.upovedomSledovatelov();
		}
		
		public void resetInfo(){
			c.plnic.addText(c.plnic.reset());
			cObs.upovedomSledovatelov();
		}
		
	
	public ControllerPlnic(Controller controller, 	ControllerObserver cObs2) {
		this.c = controller;
		this.cObs = cObs2;
	}
}
