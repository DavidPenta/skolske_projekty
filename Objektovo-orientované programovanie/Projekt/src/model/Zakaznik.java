package model;

import java.util.ArrayList;

public class Zakaznik{
	private int cislo;
	private ArrayList<String> pristroje = new ArrayList<String>();
	private String s = ""; 
	private String heslo; 
	private String meno; 
	
	
	public Zakaznik(int _cislo) {
		this.cislo = _cislo;
	}
	public Zakaznik(int _cislo, String _meno, String _heslo) {
		this.meno = _meno;
		this.heslo = _heslo;
		this.cislo = _cislo;
	}
	
	
	public String text() {
		return s;
	}
	
	public String addText(String str) {
		return s = s.concat(str);
	}

	public int getCislo() {
		return cislo;
	}

	public void setCislo(int cislo) {
		this.cislo = cislo;
	}

	public void addPristroje(String pristroj) {
		String a = pristroj;
		pristroje.add(a);
	}

	public String getHeslo() {
		return heslo;
	}
	public String getMeno() {
		return meno;
	}


}
