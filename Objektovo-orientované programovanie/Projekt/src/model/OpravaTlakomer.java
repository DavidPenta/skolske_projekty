package model;

import controller.Controller;

public class OpravaTlakomer implements Oprava{
	private Controller c;
	
	@Override
	public void opravit(HasiaciPristroj hp) {
		Tlakomer t = hp.getTlakomer();
		if(t == Tlakomer.M24BAR) {
			c.getOpravar().addText(c.getSkladnik().pouzi(7));}
		if(t == Tlakomer.M32BAR) {
			c.getOpravar().addText(c.getSkladnik().pouzi(8));}
		if(t == Tlakomer.M34BAR) {
			c.getOpravar().addText(c.getSkladnik().pouzi(9));}
	}
	public OpravaTlakomer(Controller _c) {
		this.c = _c;
	}
}
