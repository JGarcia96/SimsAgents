package ontologia.predicados;

import ontologia.Predicado;
import ontologia.conceptos.necesidades.*;

public class OrdenadorEstropeadoNavegarInternet extends Predicado {
    private Energia energia;
    private Diversion diversion;

    public OrdenadorEstropeadoNavegarInternet() {
    }

    public Energia getEnergia() {
        return energia;
    }

    public void setEnergia(Energia energia) {
        this.energia = energia;
    }

    public Diversion getDiversion() {
        return diversion;
    }

    public void setDiversion(Diversion diversion) {
        this.diversion = diversion;
    }
}
