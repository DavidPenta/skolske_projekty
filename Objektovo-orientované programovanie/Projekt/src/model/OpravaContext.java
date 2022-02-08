package model;

/**
 * Trieda <code>OpravaContext</code> reprezentuje context v Strategy navrhovom vzore.
 *
 */

public class OpravaContext {
	private Oprava o;

	public void setOprava(Oprava _o) {
		this.o = _o;
	} 
	
	public void executeOprava(HasiaciPristroj hp) {
		o.opravit(hp);
	}
}
