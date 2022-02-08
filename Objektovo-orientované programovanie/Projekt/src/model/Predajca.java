package model;

public class Predajca extends Zamestnanec{
	private int pocetPrijatychObjednavok;
	public Predajca(String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno, Priezvisko, Cislo_Zamestnanca);
	}
	@Override
	public void vypisInfo() {
		super.addText("Pocet prijatych objednavok je " + pocetPrijatychObjednavok + ".\n");
		
	}
	@Override
	public void resetInfo() {
		this.pocetPrijatychObjednavok = 0;
		
	}
	@Override
	public void pridajInfoPocet() {
		this.pocetPrijatychObjednavok++;
		
	}
}

