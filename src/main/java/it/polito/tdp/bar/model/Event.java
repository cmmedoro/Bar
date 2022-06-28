package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event implements Comparable<Event>{
	
	//due eventi: arrivo dei clienti e quando se ne vanno
		public enum EventType{
			ARRIVO_GRUPPO_CLIENTI,
			TAVOLO_LIBERATO
		}
		
		//attributi
		//supponiamo che il tempo parte da 0 e che sia un numero che va avanti
		private Duration time; //in alternativa posso usare un intero
		private EventType type; 
		private int nPersone;
		private Duration durata; //quanto sta il gruppo di utenti al tavolo
		private double tolleranza; //è una probabilità
		private Tavolo tavolo;
		public Event(Duration time, EventType type, int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
			super();
			this.time = time;
			this.type = type;
			this.nPersone = nPersone;
			this.durata = durata;
			this.tolleranza = tolleranza;
			this.tavolo = tavolo;
		}
		public Duration getTime() {
			return time;
		}
		public void setTime(Duration time) {
			this.time = time;
		}
		public EventType getType() {
			return type;
		}
		public void setType(EventType type) {
			this.type = type;
		}
		public int getnPersone() {
			return nPersone;
		}
		public void setnPersone(int nPersone) {
			this.nPersone = nPersone;
		}
		public Duration getDurata() {
			return durata;
		}
		public void setDurata(Duration durata) {
			this.durata = durata;
		}
		public double getTolleranza() {
			return tolleranza;
		}
		public void setTolleranza(double tolleranza) {
			this.tolleranza = tolleranza;
		}
		public Tavolo getTavolo() {
			return tavolo;
		}
		public void setTavolo(Tavolo tavolo) {
			this.tavolo = tavolo;
		}
		@Override
		public int compareTo(Event o) {
			return this.time.compareTo(o.getTime());
		}
		

}
