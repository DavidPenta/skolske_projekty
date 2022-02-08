package model;

/**
 * Trieda <code>PracovnikVyrobnejLinky</code> reprezentuje director v Builder navrhovom vzore.
 *
 */
public class Skladnik extends Zamestnanec{  //director
	private int pocetPristrojovNaSklade;
	Sklad sklad;

	public Skladnik (Sklad sklad, String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno,Priezvisko, Cislo_Zamestnanca);
		this.sklad = sklad;
		this.pocetPristrojovNaSklade = sklad.zistiPocet();
	}
	
	public void ulozit_do_skladu(HasiaciPristroj pristroj) { 
		pridajInfoPocet();
		sklad.uloz(pristroj);
	}
	
	public void vymaz_zo_skladu(HasiaciPristroj pristroj) { 
		this.pocetPristrojovNaSklade--;
		sklad.vymaz(pristroj);
	}
	
	public boolean najdi_v_sklade(HasiaciPristroj pristroj){ 
		return sklad.najdi(pristroj);
	}
	
	public void checkObjednavka(Objednavka objednavka, HasiaciPristroj hp) {
		hp.setKontrola(true);
		if (sklad.najdi(hp))
			objednavka.setNasklade(true);
		else
			objednavka.setNasklade(false);	
	}
	
	public String dajKomponenty(String model) {
		return sklad.vymazKomponenty(model);
		}	
	
	
	public void kup(int i) {
		sklad.kup(i);
	}

	
	public String pouzi(int i){
		String str = sklad.pouzi(i);
		addText(str);
		return str;
	}
	
    public void constructModel1(VyrobnaLinka builder) {
        builder.setModel(ModelHasiacehoPristroja.Model1);
        builder.setKategoria(KategoriaHasiacehoPristroja.PENOVY);
        builder.setRokVyroby(2021);
        builder.setTelo(new Telo(Objem.L,false));
        builder.setHadica(Hadica.LUXHOSE);
        builder.setRucka(Rucka.KOVOVA);
        builder.setVentil(Tlakomer.M24BAR);
    }

    public void constructModel2(VyrobnaLinka builder) {
        builder.setModel(ModelHasiacehoPristroja.Model2);
        builder.setKategoria(KategoriaHasiacehoPristroja.SNEHOVY);
        builder.setRokVyroby(2021);
        builder.setTelo(new Telo(Objem.M,false));
        builder.setHadica(Hadica.OPTIHOSE);
        builder.setRucka(Rucka.PLASTOVA);
        builder.setVentil(Tlakomer.M32BAR);
    }

    public void constructModel3(VyrobnaLinka builder) {
        builder.setModel(ModelHasiacehoPristroja.Model3);
        builder.setKategoria(KategoriaHasiacehoPristroja.VODNY);
        builder.setRokVyroby(2021);
        builder.setTelo(new Telo(Objem.S,false));
        builder.setHadica(Hadica.LUXHOSE);
        builder.setRucka(Rucka.KOVOVA);
        builder.setVentil(Tlakomer.M34BAR);
    }

	@Override
	public void vypisInfo() {
		super.addText("Pocet pristrojov na sklade je "+pocetPristrojovNaSklade+".\n");
		
	}

	@Override
	public void resetInfo() {
	}

	@Override
	public void pridajInfoPocet() {
		this.pocetPristrojovNaSklade++;
		
	}

	public HasiaciPristroj posliDoOpravy() {
		HasiaciPristroj hp = sklad.posliDoOpravy();
		if (hp != null) {
		   String s =  
				hp.getKategoriaHasiacehoPristroja() + "|" + 
				hp.getTelo().getObjem() + "|" + 
				hp.getTelo().getStav() + "|" + 
				hp.getRokVyroby() + "|" + 
				hp.getHadica() + "|" + 
				hp.getRucka() + "|" + 
				hp.getTlakomer() + "|" + 
				hp.getKontrola();
		    addText("Hasiaci pristroj "+ s +" bol poslani do oravy.\n");
			return hp;
		}
		else {
			super.addText("Sklad je prazdny.\n");
			return null;
			}
	}
	
}