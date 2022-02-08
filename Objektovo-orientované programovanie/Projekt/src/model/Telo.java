package model;

public class Telo {
    private Objem objem;
    private boolean plny;

    public Telo(Objem objem, boolean plny) {
        this.objem = objem;
        this.plny = plny;
    }

    public void naplnit() {
        plny = true;
    }

    public void vyprazdnit() {
        plny = false;
    }

    public boolean getStav() {
        return plny;
    }

    public Objem getObjem() {
        return objem;
    }

    public void setStav(boolean stav) {
    	this.plny = stav;
    }
}
