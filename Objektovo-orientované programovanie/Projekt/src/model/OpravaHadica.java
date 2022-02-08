package model;

import controller.Controller;
/**
 * Trieda <code>OpravaHAdica</code> reprezentuje strategiu v Strategy navrhovom vzore.
 *
 */
public class OpravaHadica implements Oprava{
	private Controller c;
	
	@Override
	public void opravit(HasiaciPristroj hp) {
		Hadica h = hp.getHadica();
		if(h == Hadica.LUXHOSE) {
			c.getOpravar().addText(c.getSkladnik().pouzi(3));}
		else if(h == Hadica.OPTIHOSE) {
			c.getOpravar().addText(c.getSkladnik().pouzi(4));}
		
	}
	public OpravaHadica(Controller _c) {
		this.c = _c;
	}

}
