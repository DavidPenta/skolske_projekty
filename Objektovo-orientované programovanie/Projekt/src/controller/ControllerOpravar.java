package controller;

import java.util.ArrayList;

import model.Objednavka;
import model.OpravaContext;
import model.OpravaHadica;
import model.OpravaRucka;
import model.OpravaTelo;
import model.OpravaTlakomer;
import utils.PrazdneExeption;
/**
* Trieda <code>ControllerOpravar</code> sluzi ako controller pre OknoOpravar.
*/

public class ControllerOpravar{
	private Controller c;
	private ControllerObserver cObs;
	private OpravaContext oc = new OpravaContext();
	
	/**
	 * Metoda odoberie pozadovany komponent prveho hasiacieho pristroja pomocou Strategy navrhoveho vzoru.
	 * @throws PrazdneExeption opravar nema ziadne hasiace pristroje
	 */
	public void vymenit(int i) throws PrazdneExeption {
		Objednavka o = c.getOpravar().getFirstObjednavka();
		if (o == null)
			throw new PrazdneExeption();
		if (i == 0) {
			oc.setOprava(new OpravaTelo(c));
		}
		else if (i == 1) {
			oc.setOprava(new OpravaHadica(c));
		}
		else if (i == 2) {
			oc.setOprava(new OpravaRucka(c));
		}
		else if (i == 3) {
			oc.setOprava(new OpravaTlakomer(c));
		}
		oc.executeOprava(o.getHp());
		cObs.upovedomSledovatelov();
	}
	
	/**
	 * Metoda posle prvy hasiaci pristroj kontrolorovi.
	 * @throws PrazdneExeption opravar nema ziadne hasiace pristroje
	 */
	
    public void opravavene() throws PrazdneExeption{
    	Objednavka o = c.getOpravar().deleteFirstObjednavka();
    	if ( o != null) {
    		c.getOpravar().pridajInfoPocet();
    		c.kontrolor.addObjednavka(o);
    		c.getOpravar().addText("Opravene.\n");

    		cObs.upovedomSledovatelov();
    	}
    	else {
    		throw new PrazdneExeption();
    		}
    }
    
	public ArrayList<Objednavka> setOpravar() {
		return c.getOpravar().getObjednavky();
	}
	
	public String opravarText() {
		return c.getOpravar().getText();

	}
	
	public void opravarText(String s) {
		c.getOpravar().addText(s);
		cObs.upovedomSledovatelov();
	}
	
	public void opravarVypisInfo() {
		c.getOpravar().vypisInfo();
		cObs.upovedomSledovatelov();
	}
	public void resetInfo(){
		c.opravar.addText(c.opravar.reset());
		cObs.upovedomSledovatelov();
	}
	
	
	public ControllerOpravar(Controller controller, ControllerObserver cObs2) {
		this.c = controller;
		this.cObs = cObs2;
	}
	
}
