package utils;

import controller.Controller;
import controller.ControllerCustomHp;
import controller.ControllerKontrolor;
import controller.ControllerLogin;
import controller.ControllerObserver;
import controller.ControllerOpravar;
import controller.ControllerPlnic;
import controller.ControllerPracovnik;
import controller.ControllerPredajca;
import controller.ControllerSkladnik;
import controller.ControllerZakaznik;
/**
* Trieda <code>SaveData</code> sluzi na ulozenie vsetkych controllerov.
* 
*
*/
public class SaveData {
	private Controller c;
	private ControllerObserver cObs;
	private ControllerOpravar co;
	private ControllerPredajca cp;
	private ControllerSkladnik cs;
	private ControllerKontrolor ck;
	private ControllerPracovnik cpr;
	private ControllerPlnic cpl;
	private ControllerZakaznik cz;
	private ControllerCustomHp cchp;
	private ControllerLogin ctrl;
	private int cisloZakaznika;
	
	public SaveData(int i, Controller c1, ControllerObserver cObs1, ControllerOpravar co1, ControllerPredajca cp1, ControllerSkladnik cs1, ControllerKontrolor ck1, ControllerPracovnik cpr1, ControllerPlnic cpl1, ControllerZakaznik cz1, ControllerCustomHp cchp1) {
		cisloZakaznika = i;
		c = c1;
		cObs = cObs1;
		co = co1;
		cp = cp1; 
		cs = cs1; 
		ck = ck1;
		cpr = cpr1;
		cpl = cpl1;
		cz = cz1;
		cchp = cchp1; 
	}
	public void novyZakaznik() {
		cisloZakaznika++;
	}
	
	public int getCisloZakaznika() {
		return cisloZakaznika;
	}
	
	public ControllerObserver getcObs() {
		return cObs;
	}
	public ControllerOpravar getCo() {
		return co;
	}
	public ControllerPredajca getCp() {
		return cp;
	}
	public ControllerSkladnik getCs() {
		return cs;
	}
	public ControllerPracovnik getCpr() {
		return cpr;
	}
	public ControllerKontrolor getCk() {
		return ck;
	}
	public ControllerPlnic getCpl() {
		return cpl;
	}
	public ControllerZakaznik getCz() {
		return cz;
	}
	public ControllerCustomHp getCchp() {
		return cchp;
	}
	public Controller getC() {
		return c;
	}
	public ControllerLogin getCtrl() {
		return ctrl;
	}
	public void setCtrl(ControllerLogin _ctrl) {
		this.ctrl = _ctrl;
	}
}
