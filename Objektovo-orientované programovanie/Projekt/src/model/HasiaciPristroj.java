package model;

public class HasiaciPristroj {
    private ModelHasiacehoPristroja modelHasiacehoPristroja;
    private KategoriaHasiacehoPristroja kategoriaHasiacehoPristroja;
    private Telo telo;
    private int rokVyroby;
    private Hadica hadica;
    private Rucka rucka;
    private Tlakomer tlakomer;
    private boolean kontrola;

    public HasiaciPristroj(ModelHasiacehoPristroja modelHasiacehoPristroja, KategoriaHasiacehoPristroja kategoriaHasiacehoPristroja, int rokVyroby, Telo telo, Hadica hadica, Rucka rucka, Tlakomer tlakomer) {
        this.modelHasiacehoPristroja = modelHasiacehoPristroja;
        this.kategoriaHasiacehoPristroja = kategoriaHasiacehoPristroja;
        this.rokVyroby = rokVyroby;
        this.telo = telo;
        this.hadica = hadica;
        this.rucka = rucka;
        this.tlakomer = tlakomer;
    }

    public ModelHasiacehoPristroja getModelHasiacehoPristroja() {
        return modelHasiacehoPristroja;
    }
    
    public KategoriaHasiacehoPristroja getKategoriaHasiacehoPristroja() {
        return kategoriaHasiacehoPristroja;
    }
    
    
    public Telo getTelo() {
        return telo;
    }
    
    public boolean getKontrola() {
        return kontrola;
    }

    public int getRokVyroby() {
        return rokVyroby;
    }

    public Hadica getHadica() {
        return hadica;
    }

    public Rucka getRucka() {
        return rucka;
    }

    public Tlakomer getTlakomer() {
        return tlakomer;
    }
    
    public void setKontrola(boolean kontrola) {
        this.kontrola = kontrola;
    }
}