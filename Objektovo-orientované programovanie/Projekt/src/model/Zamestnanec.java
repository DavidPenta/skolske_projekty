package model;

import java.util.ArrayList;

public abstract class Zamestnanec {
	private String Meno;
	private String Priezvisko;
	private int Cislo_Zamestnanca; 
	private String text = "";
	private ArrayList<Objednavka> objednavky = new ArrayList<Objednavka>();

	public Zamestnanec (String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		this.setMeno(Meno);
		this.setPriezvisko(Priezvisko);
		this.Cislo_Zamestnanca = Cislo_Zamestnanca;	}
	
	
	public String getMeno() {
		return Meno;
	}
	public void setMeno(String meno) {
		this.Meno = meno;
	}
	public String getPriezvisko() {
		return Priezvisko;
	}
	public void setPriezvisko(String priezvisko) {
		this.Priezvisko = priezvisko;
	}
	
	public int getCislo_Zamestnanca() {
		return Cislo_Zamestnanca;
	}

	public synchronized ArrayList<Objednavka> getObjednavky() {
		synchronized (this) {
			return objednavky;
		}
	}
	
	public void addObjednavka(Objednavka objednavka) {
		objednavky.add(objednavka);
	}
	
	public void addObjednavka(String str) {
		Objednavka o = new Objednavka(str);
		objednavky.add(o);
	}
	
	public Objednavka deleteFirstObjednavka() {
		if (!objednavky.isEmpty())
			return objednavky.remove(0);
		else
			return null;
	}

	public Objednavka getFirstObjednavka() {
		if (!objednavky.isEmpty())
			return objednavky.get(0);
		else
			return null;		
	}
	
	public String getText() {
		return text;
	}
	
	
	public String addText(String str) {
		return text = text.concat(str);
	}
	
	public String reset() {
		resetInfo();
		return "Reset informacie bol uspesny.\n";
		
	}
	
	public abstract void vypisInfo();
	public abstract void pridajInfoPocet();
	public abstract void resetInfo();



}
