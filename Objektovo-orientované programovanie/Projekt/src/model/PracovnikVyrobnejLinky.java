package model;

/**
 * Trieda <code>PracovnikVyrobnejLinky</code> reprezentuje builder v Builder navrhovom vzore.
 *
 */
public class PracovnikVyrobnejLinky extends Zamestnanec implements VyrobnaLinka {
    private ModelHasiacehoPristroja model;
	private KategoriaHasiacehoPristroja typ;
    private int rok;
    private Telo telo;
    private Hadica hadica;
    private Rucka rucka;
    private Tlakomer ventil;
    private int pocetVyrobenichPristrojov = 0;
    
	public PracovnikVyrobnejLinky(String Meno, String Priezvisko, int Cislo_Zamestnanca) {
		super(Meno, Priezvisko, Cislo_Zamestnanca);
	}
	
    public HasiaciPristroj getResult() {
        return new HasiaciPristroj(model, typ, rok, telo, hadica, rucka, ventil);
    }
	
	@Override
    public void setModel(ModelHasiacehoPristroja model) {
    	this.model = model;
    }
    
	@Override
	public void setKategoria(KategoriaHasiacehoPristroja typ) {
		this.typ = typ;
		
	}
    @Override
    public void setRokVyroby(int rok) {
        this.rok = rok;
    }

    @Override
    public void setTelo(Telo telo) {
        this.telo = telo;
    }

    @Override
    public void setHadica(Hadica hadica) {
        this.hadica = hadica;
    }

    @Override
    public void setRucka(Rucka rucka) {
        this.rucka = rucka;
    }

    @Override
    public void setVentil(Tlakomer ventil) {
        this.ventil = ventil;
    }

	@Override
	public void vypisInfo() {
		super.addText("Pocet vyrobenich pristrojov je " + pocetVyrobenichPristrojov + ".\n");
		
	}

	@Override
	public void resetInfo() {
		this.pocetVyrobenichPristrojov = 0;
		
	}

	@Override
	public void pridajInfoPocet() {
		this.pocetVyrobenichPristrojov++;
	}

}