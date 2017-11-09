package Salon;

import jadex.adapter.fipa.SFipa;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import jadex.runtime.impl.RBelief;
import jadex.runtime.impl.RBeliefbase;
import ontologia.Accion;


public class HacerCrucigramaPreguntaPlan extends Plan {

    public void body()
    {
		/* Creencias */
        RBeliefbase bb;
        bb=(RBeliefbase) getBeliefbase();
        RBelief creenciaOcupado=(RBelief) bb.getBelief("ocupado_periodico");
        Boolean ocupado= (Boolean)creenciaOcupado.getFact();
        IMessageEvent request	= (IMessageEvent)getInitialEvent();

        if(ocupado){
            IMessageEvent refuse = createMessageEvent("periodico_ocupado");
            refuse.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
            sendMessage(refuse);
        }else{
            RBelief creenciaMensaje=(RBelief) bb.getBelief("mensaje_periodico");
            RBelief creenciaTiempoFinPeriodico=(RBelief) bb.getBelief("tiempo_fin_periodico");
            RBelief creenciaTiempo=(RBelief) bb.getBelief("tiempo_periodico");
            Integer tiempo= (Integer)creenciaTiempo.getFact();

            creenciaMensaje.setFact(request);
            creenciaTiempoFinPeriodico.setFact(tiempo + Accion.TIEMPO_CORTO);
            creenciaOcupado.setFact(true);

            IMessageEvent agree = createMessageEvent("periodico_no_ocupado");
            agree.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
            sendMessage(agree);

            IGoal goal= createGoal("crucigrama_periodico_tiempo_superado");
            dispatchSubgoal(goal);

        }
    }
}