package ontologia.conceptos.habilidades;

import jadex.runtime.*;


public class Carisma extends Habilidad{
    private int nivel;
    private int experiencia;

    public Carisma() {
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }
}
