package controller;

import java.util.ArrayList;
import java.util.List;

import gui.Launch;
import gui.OknoAdmin;
import gui.OknoKontrolor;
import gui.OknoOpravar;
import gui.OknoPlnic;
import gui.OknoPracovnik;
import gui.OknoPredajca;
import gui.OknoSkladnik;
import gui.OknoZakaznik;
import model.Zakaznik;
import utils.Login;
import utils.SaveData;

/**
* Trieda <code>ControllerLogin</code> sluzi ako controller pre Launch a Launch2.
*/

public class ControllerLogin {
	private SaveData s;
	private Login l;
	private List<Zakaznik> zakaznici = new ArrayList<Zakaznik>();
	
	
	public ControllerLogin(Login log, SaveData sd) {
		this.s = sd;
		this.l = log;
	}
	/**
	 * Metoda pre registraciu zakaznika
	 * @param name zadane meno
	 * @param pass zadane heslo
	 */
	public boolean Register(String name, String pass){
		if(name.equals("")) {
			if(SaveDownCast()) {
				((Launch)l).infoLabel.setText("Zadajte meno.");
				((Launch)l).infoLabel.setVisible(true);
			}
			return false;
		}
		if(pass.equals("")) {
			if(SaveDownCast()) {
				((Launch)l).infoLabel.setText("Zadajte heslo.");
				((Launch)l).infoLabel.setVisible(true);
			}
			return false;
		}
			
		for(Zakaznik z : zakaznici) {
			if(name.equals(z.getMeno())) {
				
				if(SaveDownCast()) {
					((Launch)l).infoLabel.setText("Meno je uz obsadene.");
					((Launch)l).infoLabel.setVisible(true);
				}
				return false;
			}
		
		}
		if(name.equals("skladnik") == false && name.equals("predajca") == false&& name.equals("plnic") == false && name.equals("pracovnik") == false && name.equals("kontrolor") == false && name.equals("opravar") == false && name.equals("admin") == false) {
			Zakaznik zak = new Zakaznik(s.getCisloZakaznika(), name, pass);
			s.getC().zakaznici.add(zak);
			zakaznici.add(zak);
			s.getcObs().pridajZakaznika(new OknoZakaznik(s, zak.getCislo()));
			s.novyZakaznik();
			return true;
			}
		if(SaveDownCast()) {
			((Launch)l).infoLabel.setText("Meno je uz obsadene.");
			((Launch)l).infoLabel.setVisible(true);
		}
		return false;
	}
	
	/**
	 * Metoda pre prihlasenie zamestnanca alebo zakaznika
	 * @param name zadane meno
	 * @param pass zadane heslo
	 * 
	 */
	
	public boolean Login(String name, String pass){
		if(name.equals("skladnik") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoSkladnik(s));
			return true;
		}
		if(name.equals("predajca") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoPredajca(s));
			return true;
		}
		if(name.equals("plnic") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoPlnic(s));
			return true;
		}
		if(name.equals("pracovnik") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoPracovnik(s));
			return true;
		}
		if(name.equals("kontrolor") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoKontrolor(s));
			return true;
		}
		if(name.equals("opravar") && pass.equals("1")){
			s.getcObs().pridajSledovatela(new OknoOpravar(s));
			return true;
		}
		if(name.equals("admin") && pass.equals("1")){
			new OknoAdmin(s);
			return true;
		}
		
		for(Zakaznik z : zakaznici) {
			if(z.getMeno().equals(name) && z.getHeslo().equals(pass)) {
				s.getcObs().pridajSledovatela(new OknoZakaznik(s,z.getCislo()));
				return true;
			}
		}
		
		if(SaveDownCast()) {
			((Launch)l).infoLabel.setText("Neplatne meno alebo heslo.");
			((Launch)l).infoLabel.setVisible(true);
		}
		return false;
	}
	
	private boolean SaveDownCast(){
		return l instanceof Launch;	
	}
	
}