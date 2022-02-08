package model;

public class Plnic extends Zamestnanec {
	private int pocetPlneni = 0;
    private CisternaPrasok Cpr;
    private CisternaPena Cpe;
    private CisternaSneh Csn;
    
	public Plnic(CisternaPrasok Cpra, CisternaPena Cpen, CisternaSneh Cs, String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno, Priezvisko, Cislo_Zamestnanca);
		this.Cpr = Cpra;
		this.Cpe = Cpen;
		this.Csn = Cs;
		
	}
	
	private boolean PouziCisternu(HasiaciPristroj hp) {
		int i = 0;
		KategoriaHasiacehoPristroja k = hp.getKategoriaHasiacehoPristroja();
		Telo t = hp.getTelo();
		Objem objem = t.getObjem();
		
		
		if (objem == Objem.S)
			i = 3;
		else if (objem == Objem.M)
			i = 5;
		else if (objem == Objem.L)
			i = 8;
		
		if(k == KategoriaHasiacehoPristroja.VODNY) {
			return true;
		}
		else if(k == KategoriaHasiacehoPristroja.PRASKOVY) {
			return Cpr.use(i);
		}
		else if(k == KategoriaHasiacehoPristroja.PENOVY) {
			return Cpe.use(i);
		}
		else if(k == KategoriaHasiacehoPristroja.SNEHOVY) {
			return Csn.use(i);
		}
		return false;
	} 
	
	public boolean Napln(HasiaciPristroj hp){
		if (PouziCisternu(hp) == true) {
			hp.getTelo().setStav(true);
			addText("Objednavka na " + hp.getModelHasiacehoPristroja() + " bola naplnena.\n"); 
			return true;
		}
		else {
			if(hp.getKategoriaHasiacehoPristroja() == KategoriaHasiacehoPristroja.PRASKOVY) {
				addText("Nedostatok prasku\n");
			}
			if(hp.getKategoriaHasiacehoPristroja() == KategoriaHasiacehoPristroja.PENOVY) {
				addText("Nedostatok peny\n");
			}
			if(hp.getKategoriaHasiacehoPristroja() == KategoriaHasiacehoPristroja.SNEHOVY) {
				addText("Nedostatok snehu\n");
			}
			return false;
		}
			
	}
	
	public void doplnSneh(String str) {
		addText(Csn.dopln(str));
	}
	public void doplnPenu(String str) {	
		addText(Cpe.dopln(str));
	}
	public void doplnPrasok(String str) {	
		addText(Cpr.dopln(str));
	}
	
	public void vypisStavyCisterien(){	
		super.addText(Csn.vypisInfo()+ "%\n" + Cpe.vypisInfo()+ "%\n" + Cpr.vypisInfo()+ "%\n");
	}
	
	@Override
	public void vypisInfo(){	
		super.addText("Pocet plneni je " +pocetPlneni+".\n");
	}

	@Override
	public void resetInfo() {
		this.pocetPlneni = 0;
		
	}

	@Override
	public void pridajInfoPocet() {
		this.pocetPlneni++;
	}
	
} 