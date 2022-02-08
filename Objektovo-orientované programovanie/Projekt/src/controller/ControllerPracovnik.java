package controller;

import java.util.ArrayList;

import model.HasiaciPristroj;
import model.Objednavka;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerPracovnik</code> sluzi ako controller pre OknoPracovnik.
*/

public class ControllerPracovnik {
	Controller c;
	ControllerObserver cObs;
	
	/**
	 * Metoda vyrobi pozadovany hasiaci pristroj- prvy v zozname.
	 * @throws PrazdneExeption v zozname nie su ziadne hasiace pristroje
	 */
	 public void vyrob() throws PrazdneExeption{
	    	Objednavka o = c.builder.getFirstObjednavka();
	    	if (o == null)
	    		throw new PrazdneExeption();
	    	
	    	String sprava = c.getSkladnik().dajKomponenty(o.getStr());
	    	
	    	if (sprava == "Pristroj bol vyrobeny.\n") {
	    		c.builder.deleteFirstObjednavka();
	    		c.builder.pridajInfoPocet();
	    		if (o.getStr() == "Model1") { 
	    			c.getSkladnik().constructModel1(c.builder);
	        		HasiaciPristroj hp= c.builder.getResult();
	        	    hp.setKontrola(false);
	        	    o.setHp(hp);
	    			}
	    		if (o.getStr() == "Model2") {
	    			c.getSkladnik().constructModel2(c.builder);
	        		HasiaciPristroj hp= c.builder.getResult();
	        	    hp.setKontrola(false);
	        	    o.setHp(hp);
	    			}
	    		if (o.getStr() == "Model3") {
	    			c.getSkladnik().constructModel3(c.builder);
	        		HasiaciPristroj hp= c.builder.getResult();
	        	    hp.setKontrola(false);
	        	    o.setHp(hp);
	    			}
	    		
	    		c.plnic.addObjednavka(o);	
	    		c.getSkladnik().addText("Komponenty pre typ "+ o.getStr()+ "boli odobrate.\n");
	    		c.builder.addText("Pristroj typu "+ o.getStr()+" bol vyrobeni.\n");
	    	}
	    	else{
	    		c.getSkladnik().addText(sprava);
	    		c.builder.addText(sprava);
	    	}

	    	cObs.upovedomSledovatelov();
	    }
	    
	    
		public ArrayList<Objednavka> setPracovnik() {
			return c.builder.getObjednavky();
		}
		
		
		public String pracovnikText() {
			return c.builder.getText();
		}
		
		public void pracovnikVypisInfo(){
			c.builder.vypisInfo();
			cObs.upovedomSledovatelov();
		}
		public void resetInfo(){
			c.builder.addText(c.builder.reset());
			cObs.upovedomSledovatelov();
		}
		
		public ControllerPracovnik (Controller controller, ControllerObserver cObs2) {
			this.c = controller;
			this.cObs = cObs2;
		}
}
