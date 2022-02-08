package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gui.OknoKontrolor;
import gui.OknoOpravar;
import gui.OknoPlnic;
import gui.OknoPracovnik;
import gui.OknoPredajca;
import gui.OknoSkladnik;
import gui.OknoZakaznik;
import javafx.application.Platform;
import model.Zakaznik;
import utils.Okno;

/**
* Trieda <code>ControllerObserver</code> sluzi na upevodovanie okien.
*/

public class ControllerObserver {
	Controller c;
    private List<Runnable> sledovatelia =  new ArrayList<Runnable>();
	
    public void pridajSledovatela(Runnable obs) {
    	sledovatelia.add(obs);
		upovedomSledovatelov();
	}
    
	public void pridajZakaznika(Runnable obs, int i) {
		c.zakaznici.add(new Zakaznik(i,"Z_"+i, "Z"+i));
		if (obs instanceof Okno) {
			((Okno) obs).start();
			sledovatelia.add(obs);
			}
		upovedomSledovatelov();
	} 
	
	public void pridajZakaznika(Runnable obs) {
		if (obs instanceof Okno) {
			((Okno) obs).start();
			sledovatelia.add(obs);
			}
		upovedomSledovatelov();
	}  
	
	public void upovedomSledovatelov() {
		if (sledovatelia.size() > 0) {
			Platform.setImplicitExit(false);
			ExecutorService executor = Executors.newFixedThreadPool(sledovatelia.size());
			sledovatelia.forEach((t) -> executor.execute(t)); // lambda
			executor.shutdown();
		}
		
	}
	
    public void vymazSledovatela(Runnable obs) {
    	sledovatelia.remove(obs);
	}

    public String vypisSledovatelov() {
    	int zakaznici = 0;
    	int predajca= 0;
    	int skladnik = 0;
    	int pracovnik = 0;
    	int plnic = 0;
    	int kontrolor = 0;
    	int opravar = 0;
    	for(Runnable p : sledovatelia) {
    		if (p instanceof Okno) {
    			Okno t = ((Okno) p);
    			if (t instanceof OknoZakaznik){ //RTTI
    				zakaznici++;
    			}	
    			if (t instanceof OknoPredajca){ 
    				predajca++;
    			}	
    			if (t instanceof OknoSkladnik){
    				skladnik++;
    			}	
    			if (t instanceof OknoPracovnik){ 
    				pracovnik++;
    			}	
    			if (t instanceof OknoPlnic){ 
    				plnic++;
    			}	
    			if (t instanceof OknoKontrolor){
    				kontrolor++;
    			}
    			if (t instanceof OknoOpravar){
    				opravar++;
    			}	
    		}
    	}
    	return "Zakaznicke okna- "+ zakaznici+"\n"+
    			"Predajcove okna- "+ predajca+"\n"+
    			"Skladnikove okna- "+ skladnik+"\n"+
    			"Pracovnikove okna- "+ pracovnik+"\n"+
    			"Plnicove okna- "+ plnic+"\n"+
    			"Kontrolorove okna- "+ kontrolor+"\n"+
    			"Opravarove okna- "+ opravar+"\n\n";
	}
    
	public void resetZamestnancov() {                     
		for(int i = 0; i < c.zamestnanci.length; i++) {
			c.zamestnanci[i].addText(c.zamestnanci[i].reset());    // Polymorfia
		}
		upovedomSledovatelov();
	}
    
	
	public ControllerObserver(Controller controller) {
		this.c = controller;
	}
}
