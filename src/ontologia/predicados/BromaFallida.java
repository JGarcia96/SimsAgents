package ontologia.predicados;

import ontologia.Predicado;
import ontologia.conceptos.necesidades.*;

public class BromaFallida extends Predicado {

	private Energia energia;
	private Diversion diversion;

	public BromaFallida() {
		;
	}

	public BromaFallida(Energia energia, Diversion diversion) {
		this.energia = energia;
		this.diversion = diversion;
	}

	public Energia getEnergia() {
		return energia;
	}

	public void setEnergia(Energia e) {
		energia = e;
	}

	public Diversion getDiversion() {
		return diversion;
	}

	public void setDiversion(Diversion d) {
		diversion = d;
	}

}
