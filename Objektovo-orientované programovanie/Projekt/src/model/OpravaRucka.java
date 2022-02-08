package model;

import controller.Controller;
/**
 * Trieda <code>OpravaRucka</code> reprezentuje strategiu v Strategy navrhovom vzore.
 *
 */
public class OpravaRucka implements Oprava{
	private Controller c;
	
	@Override
	public void opravit(HasiaciPristroj hp) {
		Rucka r = hp.getRucka();
		if(r == Rucka.KOVOVA) {
			c.getOpravar().addText(c.getSkladnik().pouzi(5));}
		if(r == Rucka.PLASTOVA) {
			c.getOpravar().addText(c.getSkladnik().pouzi(6));}
		
	}
	public OpravaRucka(Controller _c) {
		this.c = _c;
	}
}
