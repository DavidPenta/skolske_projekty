package controller;

import model.Objednavka;
/**
* Trieda <code>ControllerZakaznik</code> sluzi ako controller pre OknoZakaznik.
*/

public class ControllerZakaznik {
	Controller c;
	ControllerObserver cObs;
	
/**
 * Metoda opjedna hasiaci pristroj.
 * @param s model objednaneho hasiaceho pristroja
 * @param cislo cislo zakaznika
 */
    public void ZakaznikObjednaModel(String s, int cislo){
    	Objednavka objednavka = new Objednavka(s, cislo);
    	c.predajca.addObjednavka(objednavka);
    	cObs.upovedomSledovatelov();
    }
    

    
	public String zakaznikText(int i) {
		return c.zakaznici.get(i).text();
	}
	public void zakaznikText(String s, int i) {
		c.zakaznici.get(i).addText(s);
		cObs.upovedomSledovatelov();
		
	}
	
	public ControllerZakaznik(Controller controller, 	ControllerObserver cObs2) {
		this.c = controller;
		this.cObs = cObs2;
	}

}
