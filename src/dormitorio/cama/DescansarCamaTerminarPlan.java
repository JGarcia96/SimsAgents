/**
 * Lizaveta Mishkinitse		NIA: 100317944
 * Raul Escabia				NIA: 100315903
 */


package dormitorio.cama;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.*;
import jadex.runtime.impl.RMessageEvent;
import ontologia.acciones.Descansar;
import ontologia.conceptos.necesidades.Energia;
import ontologia.conceptos.necesidades.Necesidad;
import ontologia.predicados.HasDescansado;


public class DescansarCamaTerminarPlan extends Plan {
    public void body() {

        getGoalbase().getGoal("terminar_descansar_cama").drop();
        getBeliefbase().getBelief("tiempo_fin_descansar_cama").setFact(new Integer (0));

        RMessageEvent peticion= (RMessageEvent)getBeliefbase().getBelief("mensaje_descansar_cama").getFact();
        Descansar contenido = (Descansar) peticion.getContent();


        Energia e  = contenido.getEnergia();
        e.setGrado(e.getGrado()+ Necesidad.NC_MUCHO);

        HasDescansado response = new HasDescansado(e);

        IMessageEvent inform = createMessageEvent("has_descansado");
        inform.getParameterSet(SFipa.RECEIVERS).addValue(peticion.getParameterSet(SFipa.SENDER).getValues());
        inform.setContent(response);
        sendMessage(inform);

        getBeliefbase().getBelief("ocupado").setFact(Boolean.FALSE);
        getBeliefbase().getBelief("cama_hecha").setFact(Boolean.FALSE);
    }

}
