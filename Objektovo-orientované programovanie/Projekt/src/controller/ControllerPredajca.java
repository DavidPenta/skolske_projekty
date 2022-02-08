package controller;

import java.util.ArrayList;

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
* Trieda <code>ControllerPredajca</code> sluzi ako controller pre OknoPredajca.
*/

public class ControllerPredajca {
	Controller c;
	ControllerObserver cObs;
	
	
	public void VypisZakaznikov() throws PrazdneExeption{
		if (c.zakaznici.isEmpty())
			throw new PrazdneExeption();
		c.zakaznici.forEach((z) -> predajcaText(z.getCislo() +"- "+ z.getMeno()+ "\n"));
	}
	
	public void PredajcaModel() throws PrazdneExeption{
    	Objednavka o = c.predajca.deleteFirstObjednavka();
    	if ( o != null) {
    		HasiaciPristroj hp = StringToModel(o.getStr());
    		o.setHp(hp);
    		c.getSkladnik().addObjednavka(o);
    		c.zakaznici.get(o.getCislo()).addText("Objednavka na " + o.getStr() + " bola prijata.\n");
    		predajcaText("Objednavka na " + o.getStr() + " bola prijata.\n");
    		c.predajca.pridajInfoPocet();
    		cObs.upovedomSledovatelov();
    	}
    	else {
    		throw new PrazdneExeption();
    		}
    }
    /**
     * Metoda vyrobi pozadovany hasiaci pristroj
     * @param s pozadovany hasiaci pristroj
     * 
     */
    public HasiaciPristroj StringToModel(String s){
    	HasiaciPristroj hp = null;
    	if (s == "Model1") {
    		c.getSkladnik().constructModel1(c.builder);
			hp = c.builder.getResult();
			hp.setKontrola(true);
			hp.getTelo().setStav(true);
			}
    	else if (s == "Model2") {
    		c.getSkladnik().constructModel2(c.builder);
			hp = c.builder.getResult();
			hp.setKontrola(true);
			hp.getTelo().setStav(true);
			}
    	else if (s == "Model3") {
    		c.getSkladnik().constructModel3(c.builder);
			hp = c.builder.getResult();
			hp.setKontrola(true);
			hp.getTelo().setStav(true);
			}
    	else {
    		String[] items = s.split("\\|");
    		KategoriaHasiacehoPristroja k = KategoriaHasiacehoPristroja.valueOf(items[0]) ;
    		Objem o = Objem.valueOf(items[1]);
    		Hadica h = Hadica.valueOf(items[2]);
    		Rucka r =  Rucka.valueOf(items[3]);
    		Tlakomer t = Tlakomer.valueOf(items[4]);
    		Telo telo = new Telo(o,true);
    		hp = new HasiaciPristroj(ModelHasiacehoPristroja.CUSTOM, k, 2021, telo, h, r, t);
    		hp.setKontrola(true);
    	}
		return hp; 
		}
    
    public void PredajcaModel_zamietnuta() throws PrazdneExeption {
    	Objednavka o = c.predajca.deleteFirstObjednavka();
    	if ( o != null) {
    		c.zakaznici.get(o.getCislo()).addText("Objednavka na " + o.getStr() + " bola zamietnuta.\n");
    		predajcaText("Objednavka na " + o.getStr() + " bola zamietnuta.\n");
    		cObs.upovedomSledovatelov();
    		
    	}
    	else
    		throw new PrazdneExeption();
    }
        
	public ArrayList<Objednavka> setPredajca() {
		return c.predajca.getObjednavky();
	}
	
	public ArrayList<Objednavka> getSkladnikObjednavky() {
		return c.getSkladnik().getObjednavky();
	}
	
	public String predajcaText() {
		return c.predajca.getText();
	}
	
	public void predajcaText(String s) {
		c.predajca.addText(s);
		cObs.upovedomSledovatelov();
	}
	
	public void predajcaVypisInfo(){
		c.predajca.vypisInfo();
		cObs.upovedomSledovatelov();
	}
	
	/**
	 * Metoda vypise vsetky aktualne objednavky pre vsetkych zamestnancov.
	 */
	
	public void vypisObjednavky() {
		c.predajca.addText("Vsetky prebiehajuce objednavky su:\n");
		c.predajca.getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Predajca-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Predajca-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();
		
		c.getSkladnik().getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Skladnik-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Skladnik-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();
		
		c.builder.getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Pracovnik vyrobnej linky-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Pracovnik vyrobnej linky-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();
		
		c.plnic.getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Plnic-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Plnic-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();
		
		c.kontrolor.getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Kontrolor-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Kontrolor-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();
		
		c.getOpravar().getObjednavky().forEach((t) -> {
			if (t.getCislo() == -1) {
				c.predajca.addText("Opravar-"+t.getStr() + " pre skladnika.\n");
			}
			else
			    c.predajca.addText("Opravar-"+t.getStr() + " pre zakaznika cislo " + t.getCislo()+"\n");
			});
		cObs.upovedomSledovatelov();

	}
	
	public void resetInfo(){
		c.predajca.addText(c.predajca.reset());
		cObs.upovedomSledovatelov();
	}
	
	public ControllerPredajca(Controller controller, ControllerObserver cObs2) {
		this.c = controller;
		this.cObs = cObs2;
	}
	
}
