package model;

public class Kontrolor extends Zamestnanec {
    private int pocetKontrol = 0;
    
	public Kontrolor(String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno, Priezvisko, Cislo_Zamestnanca);
	}
	
	@Override
	public void vypisInfo() {
		super.addText("Pocet vykonanych kontrol je " + pocetKontrol + ".\n");
		
	}
	@Override
	public void resetInfo() {
		this.pocetKontrol = 0;
		
	}

	@Override
	public void pridajInfoPocet() {
		this.pocetKontrol++;
		
	}
}