package controller;

import java.util.ArrayList;

import model.HasiaciPristroj;
import model.Objednavka;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerKontrolor</code> sluzi ako controller pre OknoKontrolor.
*/

public class ControllerKontrolor {
	Controller c;
	ControllerObserver cObs;
	
	/**
	 * Metoda posle prvy hasiaci pristroj skladnikovi.
	 * @throws PrazdneExeption kontrolor nema ziadne hasiace pristroje
	 */
	 public void Skontrolovane() throws PrazdneExeption{
	    	Objednavka o = c.kontrolor.deleteFirstObjednavka();
	    	if ( o != null) {
	    		HasiaciPristroj hp = o.getHp();
	    		hp.setKontrola(true);
	    		c.kontrolor.pridajInfoPocet();
	    		
	    		if (o.getCislo() > -1)
	    			c.zakaznici.get(o.getCislo()).addText("Objednavka na " + o.getStr() + " bola skontrolovana a je pripravena na vyzdvihnutie.\n");
	    		else {
	    			c.getSkladnik().addText("Objednavka na " + o.getStr() + " bola skontrolovana a uskladnena.\n");
	    			c.getSkladnik().ulozit_do_skladu(o.getHp());
	    		}
	    		
	    		c.kontrolor.addText("Objednavka na " + o.getStr() + " bola skontrolovana.\n");
	    		cObs.upovedomSledovatelov();
	    		
	    	}
	    	else {
	    		throw new PrazdneExeption();
	    		}
	    }
	    
		/**
		 * Metoda posle prvy hasiaci pristroj opravarovi.
		 * @throws PrazdneExeption kontrolor nema ziadne hasiace pristroje
		 */
	 
	    public void DoOpravy() throws PrazdneExeption{
	    	Objednavka o = c.kontrolor.deleteFirstObjednavka();
	    	if ( o != null) {
	    		c.getOpravar().addObjednavka(o);
	    		c.kontrolor.addText("Objednavka na " + o.getStr() + " bola poslana do opravy.\n");
	    		cObs.upovedomSledovatelov();
	    		
	    	}
	    	else {
	    		throw new PrazdneExeption();
	    		}
	    }
		
		public ArrayList<Objednavka> setKontrolor() {
			return c.kontrolor.getObjednavky();
		}
		
		public String kontrolorText() {
			return c.kontrolor.getText();
		}
		
		public void kontrolorVypisInfo() {
			c.kontrolor.vypisInfo();
			cObs.upovedomSledovatelov();
		}
		
		public void resetInfo(){
			c.kontrolor.addText(c.kontrolor.reset());
			cObs.upovedomSledovatelov();
		}
		public ControllerKontrolor(Controller controller, ControllerObserver cObs2) {
			this.c = controller;
			this.cObs = cObs2;
		}
		

}
