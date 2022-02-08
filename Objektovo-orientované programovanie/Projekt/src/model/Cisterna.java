package model;

public abstract class Cisterna {
	private int max_objem;
	private int objem; 

	public Cisterna(int i, int o) {
		this.max_objem = i;
		this.objem = o;
	}

	public int getMax_objem() {
		return max_objem;
	}

	public int getObjem() {
		return objem;
	}
	
	public void setObjem(int i) {
		this.objem = i;
	}
	
	public boolean use(int i) {
		if (objem - i < 0)
			return false;
		else {
			objem -= i;
			return true;
		}	
	}
	
	public String dopln(String str) {
		doplnit(str);
		return "Cisterna bola dolpnena.\n";
		
	}
	
	public abstract void doplnit(String str);
	
	public abstract String vypisInfo();

}
