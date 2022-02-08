package model;

import controller.Controller;
/**
 * Trieda <code>OpravaTelo</code> reprezentuje strategiu v Strategy navrhovom vzore.
 *
 */

public class OpravaTelo implements Oprava{
	private Controller c;
	
	@Override
	public void opravit(HasiaciPristroj hp) {
		Objem ob = hp.getTelo().getObjem();
		if(ob == Objem.S) {
			c.getOpravar().addText(c.getSkladnik().pouzi(0));
			}
		else if(ob == Objem.M) {
			c.getOpravar().addText(c.getSkladnik().pouzi(1));}
		else if(ob == Objem.L) {
			c.getOpravar().addText(c.getSkladnik().pouzi(2));}
	}
	
	public OpravaTelo(Controller _c) {
		this.c = _c;
	}

}
