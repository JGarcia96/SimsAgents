package dormitorio.caballete;

import jadex.adapter.fipa.SFipa;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import ontologia.Accion;
import ontologia.acciones.PintarNuevoCuadro;
import ontologia.conceptos.Cuadro;

public class PintarNuevoCuadroPreguntaPlan extends Plan {

    public PintarNuevoCuadroPreguntaPlan() {
    }

    @Override
    public void body() {
        IMessageEvent peticion = ((IMessageEvent) getInitialEvent());
        PintarNuevoCuadro content = (PintarNuevoCuadro) peticion.getContent();

        Cuadro cuadro = content.getCuadro();

        if (getBeliefbase().getBelief("cuadro_instalado").getFact() != null) {
            System.out.println("No es posible pintar un cuadro nuevo ya que hay un cuadro instalado en el caballete.");
            IMessageEvent refuse = createMessageEvent("refuse_caballete");
            refuse.getParameterSet(SFipa.RECEIVERS).addValue(peticion.getParameterSet(SFipa.SENDER).getValues());
            sendMessage(refuse);
        } else {
            getBeliefbase().getBelief("cuadro_instalado").setFact(cuadro);
            getBeliefbase().getBelief("ocupado_caballete").setFact(Boolean.TRUE);
            getBeliefbase().getBelief("mensaje_caballete").setFact(peticion);
            int tiempo = (int) getBeliefbase().getBelief("tiempo_caballete").getFact();
            getBeliefbase().getBelief("tiempo_fin_caballete").setFact(tiempo + Accion.TIEMPO_MEDIO);

            IMessageEvent agree = createMessageEvent("agree_caballete");
            agree.getParameterSet(SFipa.RECEIVERS).addValue(peticion.getParameterSet(SFipa.SENDER).getValues());
            sendMessage(agree);

            IGoal goal = createGoal("pintar_nuevo_cuadro_tiempo_superado");
            dispatchTopLevelGoal(goal);
        }
    }
}
