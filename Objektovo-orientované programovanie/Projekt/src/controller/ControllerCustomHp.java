package controller;

import model.Hadica;
import model.HasiaciPristroj;
import model.KategoriaHasiacehoPristroja;
import model.ModelHasiacehoPristroja;
import model.Objednavka;
import model.Objem;
import model.Rucka;
import model.Telo;
import model.Tlakomer;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerCustomHp</code> sluzi ako controller pre onko na vyrobu vlastneho hasiaceho pristroja.
*/
public class ControllerCustomHp {
	Controller c;
	ControllerZakaznik cz;
	ControllerObserver cObs;
	
	/**
	 * Metoda objedna vlastny hasiaci pristroj. 
	 * 
	 * @param cislo cislo zakaznika
	 * @param k vybrata kategoria hasiaceho pristroja
	 * @param o vybraty objem
	 * @param h vybrata hadica
	 * @param r vybrata rucka
	 * @param t vybraty tlakomer
	 * @throws PrazdneExeption zakaznik nevyplnil policko 
	 */
    public void customHP(int cislo, KategoriaHasiacehoPristroja k, Objem o, Hadica h, Rucka r, Tlakomer t) throws PrazdneExeption{
    	if(k == null || o == null || h == null || r == null || t == null)
    		throw new PrazdneExeption();
    	else {
    		String s = k+"|"+o+"|"+h+"|"+r+"|"+t;
        	Objednavka objednavka = new Objednavka(s, cislo);
        	
        	if(cislo > -1) {
        		c.predajca.addObjednavka(objednavka);
        		cz.zakaznikText("Objednany bol "+ s +"\n", cislo);
        		}
        	else {
        		Telo telo = new Telo(o,true);
        		HasiaciPristroj hp = new HasiaciPristroj(ModelHasiacehoPristroja.CUSTOM, k, 2021, telo, h, r, t);
        		hp.setKontrola(true);
        		objednavka.setHp(hp);
        		c.builder.addObjednavka(objednavka);
        		c.getSkladnik().addText("Objednany bol "+ s +".\n");
        		
        	}
        	
        	cObs.upovedomSledovatelov();
    	}
    }
    
    
	public ControllerCustomHp(Controller controller, ControllerZakaznik cz2, ControllerObserver cObs2) {
		this.c = controller;
		this.cz = cz2;
		this.cObs = cObs2;
	}
	
}
