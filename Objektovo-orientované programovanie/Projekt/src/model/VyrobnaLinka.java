package model;

public interface VyrobnaLinka {
    void setModel(ModelHasiacehoPristroja model);
    void setKategoria(KategoriaHasiacehoPristroja typ);
    void setRokVyroby(int rok);
    void setTelo(Telo telo);
    void setHadica(Hadica hadica);
    void setRucka(Rucka rucka);
    void setVentil(Tlakomer ventil);
}
