package model;

public class CisternaSneh extends Cisterna{
	private String koncentracia;
	
	public CisternaSneh(int i, int o, String k) {
		super(i, o);
		this.koncentracia = k;
	}

	@Override
	public String vypisInfo() {
		return "Koncentracia: "+koncentracia+"| stav " + getObjem();
	}

	@Override
	public void doplnit(String str) {
		this.koncentracia = str;
		super.setObjem(getMax_objem());	
	}
}
