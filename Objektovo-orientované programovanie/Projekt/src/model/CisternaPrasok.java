package model;

public class CisternaPrasok extends Cisterna{
	private String prasok;
	
	public CisternaPrasok(int i, int o, String p) {
		super(i, o);
		this.prasok = p;
	}

	@Override
	public String vypisInfo() {
		return "Prasok: "+prasok+"| stav " + getObjem();
	}

	@Override
	public void doplnit(String str) {
		this.prasok = str;
		super.setObjem(getMax_objem());	
	}
}