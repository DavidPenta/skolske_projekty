package model;

public class CisternaPena extends Cisterna{
	private String penidlo;
	
	public CisternaPena(int i, int o, String p) {
		super(i, o);
		this.penidlo = p;
	}

	@Override
	public String vypisInfo() {
		return "Penidlo: "+ penidlo+"| stav " + getObjem();
	}

	@Override
	public void doplnit(String str) {
		this.penidlo = str;
		super.setObjem(getMax_objem());	
		
	}
}
