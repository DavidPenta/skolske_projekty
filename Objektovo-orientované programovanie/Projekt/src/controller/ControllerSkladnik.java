package controller;

import java.util.ArrayList;

import model.HasiaciPristroj;
import model.Objednavka;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerSkladnik</code> sluzi ako controller pre OknoSkladnik.
*/

public class ControllerSkladnik {
	Controller c;
	ControllerObserver cObs;
	
	
	public ArrayList<Objednavka> getSkladnikObjednavky() {
		return c.getSkladnik().getObjednavky();
	}
	
    public String strPreSkladnika(int i, ArrayList<Objednavka> objednavky) {
    	String nasklade;
    	c.getSkladnik().checkObjednavka(objednavky.get(i), objednavky.get(i).getHp());
    	if (objednavky.get(i).isNasklade())
    		nasklade = " | je na sklade";
    	else 
    		nasklade = " | nie je na sklade";
    	return objednavky.get(i).getStr() + " | Cislo zakaznika: " + objednavky.get(i).getCislo() + nasklade;
    }
    
	public String skladnikText() {
		return c.getSkladnik().getText();
	}
	
    
    
	
	 public void vyrob_model1(){
		 c.builder.addObjednavka("Model1");
		 c.getSkladnik().addText("Objednany bol Model1.\n");
		 cObs.upovedomSledovatelov();
	 }
	 public void vyrob_model2(){
		 c.builder.addObjednavka("Model2");
		 c.getSkladnik().addText("Objednany bol Model2.\n");
		 cObs.upovedomSledovatelov();
	 }
	 public void vyrob_model3(){
		 c.builder.addObjednavka("Model3");
		 c.getSkladnik().addText("Objednany bol Model3.\n");
		 cObs.upovedomSledovatelov();
	 }

/**
 * Medoda, ak je hasiaci pristroj na sklade, posle spravu spravnemu zakaznikovy. Ak na sklade nie je tak ho posle pracovnikovi vyrobnej linky.  
 * @throws PrazdneExeption v zozname nie su ziadne hasiace pristroje 
 */
    public void odstran_model1() throws PrazdneExeption {
    	Objednavka o = c.getSkladnik().getFirstObjednavka();
    	if ( o != null) {
    		if (o.isNasklade()) {
    			c.getSkladnik().deleteFirstObjednavka();
    			c.getSkladnik().vymaz_zo_skladu(o.getHp());
    			c.zakaznici.get(o.getCislo()).addText("Objednavka na " + o.getStr() + " je pripravena na vyzdvihnutie.\n");
    			c.getSkladnik().addText("Objednavka na " + o.getStr() + " bola pripravena na vyzdvihnutie.\n");
    			cObs.upovedomSledovatelov();
    		}
    		else {
    			c.getSkladnik().deleteFirstObjednavka();
    			c.builder.addObjednavka(o);
    			c.zakaznici.get(o.getCislo()).addText("Objednavka na " + o.getStr() + " nie je na sklade a bola odoslana na vyrobu\n");
    			c.getSkladnik().addText(o.getStr() + " nie je na sklade a bol odoslany na vyrobu.\n");
    			cObs.upovedomSledovatelov();
    			}
        }
    	else
    		throw new PrazdneExeption();
    		
    }
    
    

    

    

	public void skladnikText(String s) {
		c.getSkladnik().addText(s);
		cObs.upovedomSledovatelov();
	}
	
	
	
	/**
	 * Metoda posle prvy hasiaci pristroj opravarovi
	 * @throws PrazdneExeption sklad je prazny
	 */
	public void skladnikPosliDoOpravy() throws PrazdneExeption{
		HasiaciPristroj hp = c.getSkladnik().posliDoOpravy();
		if (hp != null) {
			String s = hp.getModelHasiacehoPristroja().name();
			if(s.equals("CUSTOM")) {
				s = 
	  			 hp.getKategoriaHasiacehoPristroja() + "|" + 
	  			 hp.getTelo().getObjem() + "|" + 
	  			 hp.getTelo().getStav() + "|" + 
	  			 hp.getRokVyroby() + "|" + 
	  			 hp.getHadica() + "|" + 
	  			 hp.getRucka() + "|" + 
	  			 hp.getTlakomer() + "|" + 
	  			 hp.getKontrola();
			}
			Objednavka o = new Objednavka(s ,hp, -1);
			c.getOpravar().addObjednavka(o);
			cObs.upovedomSledovatelov();
		}
		else 
			throw new PrazdneExeption();
	}
	
	
	public void kup_telo_s() {
		c.getSkladnik().kup(0);
		c.getSkladnik().addText("Bolo kupenych 10x telo_s.\n"); 
		cObs.upovedomSledovatelov();
	}
	public void kup_telo_m(){
		c.getSkladnik().kup(1);
		c.getSkladnik().addText("Bolo kupenych 10x telo_m.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_telo_l(){
		c.getSkladnik().kup(2);
		c.getSkladnik().addText("Bolo kupenych 10x telo_l.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_luxhose(){
		c.getSkladnik().kup(3);
		c.getSkladnik().addText("Bolo kupenych 10x luxhose.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_optihose(){
		c.getSkladnik().kup(4);
		c.getSkladnik().addText("Bolo kupenych 10x optihose.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_kovovu_rucku(){
		c.getSkladnik().kup(5);
		c.getSkladnik().addText("Bolo kupenych 10 kovovych ruciek.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_plastovu_rucku(){
		c.getSkladnik().kup(6);
		c.getSkladnik().addText("Bolo kupenych 10 plastovych ruciek.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_M24BAR(){
		c.getSkladnik().kup(7);
		c.getSkladnik().addText("Bolo kupenych 10x M24BAR.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_M32BAR(){
		c.getSkladnik().kup(8);
		c.getSkladnik().addText("Bolo kupenych 10x M32BAR.\n");
		cObs.upovedomSledovatelov();
	}
	public void kup_M34BAR(){
		c.getSkladnik().kup(9);
		c.getSkladnik().addText("Bolo kupenych 10x M34BAR.\n");
		cObs.upovedomSledovatelov();
	}
	
	public void skladnikVypisInfo(){
		c.getSkladnik().vypisInfo();
		cObs.upovedomSledovatelov();
	}
	
	public ControllerSkladnik(Controller controller, ControllerObserver cObs2) {
		this.c = controller;
		this.cObs = cObs2;
	}
	
	
}
