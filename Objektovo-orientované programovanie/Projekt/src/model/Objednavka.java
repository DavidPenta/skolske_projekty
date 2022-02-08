package model;

public class Objednavka {
	private HasiaciPristroj hp;
	private String s;
	private int i = -1;
	private boolean nasklade;
	
	public Objednavka(String str) {
		this.s = str;
	}
	
	public Objednavka(String str, int cislo) {
		this.s = str;
		this.i = cislo;
	}
	
	public Objednavka(String str,HasiaciPristroj h, int cislo) {
		this.s = str; 
		this.hp = h;
		this.i = cislo;
	}

	public String getStr() {
		return s;
	}
	
	public int getCislo() {
		return i;
	}

	public boolean isNasklade() {
		return nasklade;
	}

	public void setNasklade(boolean nasklade) {
		this.nasklade = nasklade;
	}

	public HasiaciPristroj getHp() {
		return hp;
	}

	public void setHp(HasiaciPristroj hp) {
		this.hp = hp;
	}
	
}
