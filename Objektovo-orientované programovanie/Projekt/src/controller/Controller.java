package controller;

import java.util.ArrayList;
import java.util.List;

import model.CisternaPena;
import model.CisternaPrasok;
import model.CisternaSneh;
import model.Kontrolor;
import model.Opravar;
import model.Plnic;
import model.PracovnikVyrobnejLinky;
import model.Predajca;
import model.Sklad;
import model.Skladnik;
import model.Zakaznik;
import model.Zamestnanec;
/**
 * Trieda <code>Controller</code> sluzi na ulozenie vsetkych zamestnancov a zakaznikov.
 * 
 */
public class Controller {	
	protected Sklad sklad = new Sklad();
	protected CisternaPrasok Cpr = new CisternaPrasok(100,50,"Super-D");
    protected CisternaPena Cpe = new CisternaPena(100,50,"AFFF");
    protected CisternaSneh Cs = new CisternaSneh(100,50,"40%");
	
    protected Skladnik skladnik = new Skladnik(sklad, "Marian", "Bakos", 1);
    protected PracovnikVyrobnejLinky builder = new PracovnikVyrobnejLinky("Jozef", "Kovacs", 2);
    protected Predajca predajca = new Predajca("Jan", "Fischer", 3);
    protected Plnic plnic = new Plnic(Cpr, Cpe,Cs, "Matej", "Tal", 4);
    protected Kontrolor kontrolor = new Kontrolor("Alfonz", "Carlsen", 5);
    protected Opravar opravar = new Opravar("Bohus", "Nakamura", 6);
    
	protected List<Zakaznik> zakaznici = new ArrayList<>();
	
	protected Zamestnanec zamestnanci[] = {skladnik, builder, predajca, plnic, kontrolor, opravar};
	

	public Opravar getOpravar() {
		return opravar;
	}

	public void setOpravar(Opravar opravar) {
		this.opravar = opravar;
	}

	public Skladnik getSkladnik() {
		return skladnik;
	}

	public void setSkladnik(Skladnik skladnik) {
		this.skladnik = skladnik;
	}	
}




