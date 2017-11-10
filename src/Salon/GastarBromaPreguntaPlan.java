package Salon.Telefono;

import java.util.Random;

import jadex.adapter.fipa.SFipa;
import jadex.runtime.*;
import jadex.runtime.impl.RBelief;
import jadex.runtime.impl.RBeliefbase;
import ontologia.Accion;
import ontologia.acciones.GastarBromaTel;
import ontologia.acciones.LlamarSim;
import ontologia.conceptos.habilidades.Carisma;
import ontologia.conceptos.necesidades.Diversion;
import ontologia.conceptos.necesidades.Energia;
import ontologia.conceptos.necesidades.InteraccionSocial;
import ontologia.predicados.BromaFallida;
import ontologia.predicados.LlamadaFallida;

public class GastarBromaPreguntaPlan extends Plan {

	/**
	 * Plan body.
	 */
	public void body() {
		/* Obtenci�n del request que inicia el plan */
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		GastarBromaTel content = (GastarBromaTel) request.getContent();
		Energia energia = content.getEnergia();
		Diversion diversion = content.getDiversion();

		/* Obtenci�n de las creencias del agente */
		RBeliefbase bb;
		bb = (RBeliefbase) getBeliefbase();
		RBelief creenciaOcupado = (RBelief) bb.getBelief("telefono_ocupado");
		RBelief creenciaMensaje = (RBelief) bb.getBelief("mensaje");
		RBelief creenciaTiempoFin = (RBelief) bb.getBelief("tiempo_fin_broma");
		Boolean ocupado = (Boolean) creenciaOcupado.getFact();

		/* Si el tel�fono est� ocupado se rechaza la petici�n, si no se acepta */
		if (ocupado) {
			/* Env�o del refuse */
			IMessageEvent refuse = createMessageEvent("peticion_rechazada");
			refuse.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
			sendMessage(refuse);
		} else {
			/* Env�o del agree */
			IMessageEvent agree = createMessageEvent("peticion_aceptada");
			agree.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
			sendMessage(agree);

			/* Probabilidad de que la broma falle */
			Boolean falloBroma = new Random().nextInt(20) == 0;

			/*
			 * Si la llamada falla se env�a un failure. Si no, pasar� a ejecutarse el plan
			 * de GastarBromaRespuestaPlan
			 */
			if (falloBroma) {
				IMessageEvent failure = createMessageEvent("broma_fallida");
				BromaFallida bromaFallida = new BromaFallida(energia, diversion);
				failure.setContent(bromaFallida);
				failure.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
				sendMessage(failure);
			} else {
				/* Se guarda el contenido del request en una creencia para que el segundo plan pueda acceder a dicho contenido */
				creenciaMensaje.setFact(request);
				
				/* Ahora el tel�fono est� ocupado, se actualiza la creencia*/
				creenciaOcupado.setFact(true);
				
				/* Obtenci�n del tiempo que lleva ejecut�ndose el agente */
				int tiempo=(int) getBeliefbase().getBelief("tiempo_actual").getFact();
				
				/* Se guarda en una creencia el tiempo en el que se va a terminar la broma*/
				creenciaTiempoFin.setFact(tiempo + Accion.TIEMPO_CORTO);

				/* Lanzamiento del objetivo */
				IGoal goal = createGoal("gastar_broma_tiempo_superado");
				dispatchTopLevelGoal(goal);
			}
		}
	}
}