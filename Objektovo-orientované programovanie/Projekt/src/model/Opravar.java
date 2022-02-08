package model;

public class Opravar extends Zamestnanec {
    private int pocetOprav = 0;
    
	public Opravar(String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno, Priezvisko, Cislo_Zamestnanca);
	}

	@Override
	public void vypisInfo() {
		super.addText("Pocet opravenych pristrojov je " + pocetOprav + ".\n");
		
	}

	@Override
	public void resetInfo() {
		this.pocetOprav = 0;
	}

	@Override
	public void pridajInfoPocet() {
		this.pocetOprav++;
		
	}
	
}