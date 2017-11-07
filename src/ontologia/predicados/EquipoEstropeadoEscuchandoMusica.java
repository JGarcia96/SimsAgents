package ontologia.predicados;

import ontologia.Predicado;
import ontologia.conceptos.necesidades.*;

public class EquipoEstropeadoEscuchandoMusica extends Predicado {
    private Energia energia;
	private Diversion diversion;
      
	public EquipoEstropeadoEscuchandoMusica()
    {
    }
	   
	public Energia getEnergia() {
        return energia;
    }
    public void setEnergia(Energia e) {
        energia=e;
    }

	public Diversion getDiversion() {
        return diversion;
    }
    public void setDiversion(Diversion d) {
        diversion=d;
    }
}