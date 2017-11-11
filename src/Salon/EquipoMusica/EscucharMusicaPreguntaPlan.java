package Salon.EquipoMusica;

import java.util.ArrayList;

import jadex.adapter.fipa.SFipa;
import jadex.runtime.*;
import jadex.runtime.impl.RBelief;
import ontologia.Accion;
import ontologia.acciones.EscucharMusica;
import ontologia.conceptos.Musica;
import ontologia.conceptos.necesidades.Diversion;
import ontologia.conceptos.necesidades.Energia;
import ontologia.predicados.EquipoEstropeadoEscuchandoMusica;

public class EscucharMusicaPreguntaPlan extends Plan {

	public EscucharMusicaPreguntaPlan() {
	}

	public void body() {
		/* Obtenci�n del request que inicia el plan */
		IMessageEvent request = (IMessageEvent) getInitialEvent();
		EscucharMusica content = (EscucharMusica) request.getContent();
		Diversion diversion = content.getDiversion();
		Energia energia = content.getEnergia();

		/* Obtenci�n de las creencias del agente */
		RBelief creenciaMusicaSonando = (RBelief) getBeliefbase().getBelief("musica_sonando"); // El tipo de m�sica que
		Musica musicaSonando = (Musica) creenciaMusicaSonando.getFact();						// est� sonando
		RBelief creenciaObsolescencia = (RBelief) getBeliefbase().getBelief("obsolescencia");// Si el equipo est�
		int obsolescencia = (int) creenciaObsolescencia.getFact(); 								// estropeado

		/* Al escuchar m�sica siempre se aceptan las peticiones */
		IMessageEvent agree = createMessageEvent("peticion_aceptada");
		agree.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
		sendMessage(agree);

		/* Se comprueba si el equipo de m�sica est� roto */
		if (obsolescencia <= 0) {
			/* La m�sica deja de sonar */
			if (!musicaSonando.equals(null)) {
				creenciaMusicaSonando.setFact(null);
			}
			/* Mensaje de failure enviado con el predicado correspondiente */
			IMessageEvent failure = createMessageEvent("equipo_estropeado_escuchando_mmusica");
			EquipoEstropeadoEscuchandoMusica equipoEstropeado = new EquipoEstropeadoEscuchandoMusica(energia, diversion);
			failure.setContent(equipoEstropeado);
			failure.getParameterSet(SFipa.RECEIVERS).addValue(request.getParameterSet(SFipa.SENDER).getValues());
			sendMessage(failure);
		} else {
			/*
			 * Se modifican las creencias del agente sobre su obsolescencia.
			 */
			obsolescencia--;
			creenciaObsolescencia.setFact(obsolescencia);

			/*
			 * Se obtienen los arrays que contienen los tiempos y mensajes de los sims que
			 * est�n a la espera de que se modifiquen sus recursos
			 */

			RBelief creenciaMensajes = (RBelief) getBeliefbase().getBelief("mensajes_escuchar_musica");
			@SuppressWarnings("unchecked")
			ArrayList<IMessageEvent> arrayMensajes = (ArrayList<IMessageEvent>) creenciaMensajes.getFact();
			RBelief creenciaTiemposFin = (RBelief) getBeliefbase().getBelief("tiempos_escuchar_musica");
			@SuppressWarnings("unchecked")
			ArrayList<Integer> arrayTiempos = (ArrayList<Integer>) creenciaTiemposFin.getFact();

			/*
			 * Se actualiza el Array de Mensajes a�adiendo el mensaje del sim actual en la
			 * �ltima posicion
			 */
			arrayMensajes.add(request);
			creenciaMensajes.setFact(arrayMensajes);

			/*
			 * Se actualiza el array de tiempos de finalizaci�n a�adiendo el tiempo de
			 * finalizaci�n de la acci�n para el Sim actual a la �ltima posici�n
			 */
			RBelief creenciaTiempo = (RBelief) getBeliefbase().getBelief("tiempo_actual");
			Integer tiempo = (Integer) creenciaTiempo.getFact();
			arrayTiempos.add(tiempo + Accion.TIEMPO_CORTO);
			creenciaTiemposFin.setFact(arrayTiempos);

			/* Si es el primero en escuchar m�sica se lanza el objetivo */
			if (arrayTiempos.size() == 1) {
				IGoal goal = createGoal("escuchar_musica_tiempo_superado");
				dispatchTopLevelGoal(goal);
			}
		}
	}
}