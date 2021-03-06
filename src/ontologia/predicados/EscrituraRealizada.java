package ontologia.predicados;

import ontologia.Predicado;
import ontologia.conceptos.habilidades.*;
import ontologia.conceptos.necesidades.*;

public class EscrituraRealizada extends Predicado {
    private Energia energia;
    private Diversion diversion;
    private Escritura escritura;

    public EscrituraRealizada() {
    }

    public EscrituraRealizada(Energia energia, Diversion diversion, Escritura escritura) {
        this.energia = energia;
        this.diversion = diversion;
        this.escritura = escritura;
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

    public Escritura getEscritura() {
        return escritura;
    }

    public void setEscritura(Escritura escritura) {
        this.escritura = escritura;
    }
}
