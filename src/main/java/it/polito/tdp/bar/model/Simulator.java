package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {
	
	//MODELLO: lista di tavoli
	private List<Tavolo> tavoli;
	//PARAMETRI DELLA SIMULAZIONE (dal testo)
	private int NUM_EVENTI = 2000;
	private int T_ARRIVO_MAX = 10;
	private int NUM_PERSONE_MAX = 10;
	private int DURATA_MIN = 60;
	private int DURATA_MAX = 120;
	private double TOLLERANZA_MAX = 0.9;
	private double OCCUPAZIONE_MAX = 0.5;
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> coda;
	//STATISTICHE
	private Statistiche statistiche;
	
	
	public void creaTavolo(int quantita, int dimensione) {
		for(int i = 0; i<quantita; i++) {
			this.tavoli.add(new Tavolo(dimensione, false));
		}
	}
	public void creaTavoli() {
		creaTavolo(2,10);
		creaTavolo(4,8);
		creaTavolo(4,6);
		creaTavolo(5,4);
		Collections.sort(this.tavoli, new Comparator<Tavolo>() {

			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getPosti()-o2.getPosti();
			}
			
		});
	}
	
	private void creaEventi() {
		Duration arrivo = Duration.ofMinutes(0);
		for(int i = 0; i<this.NUM_EVENTI; i++) {
			int nPersone = (int)(Math.random()*this.NUM_PERSONE_MAX +1);
			Duration durata = Duration.ofMinutes(this.DURATA_MIN + (int) (Math.random()*(this.DURATA_MAX-this.DURATA_MIN+1)));
			double tolleranza = Math.random() * this.TOLLERANZA_MAX;
			Event e = new Event(arrivo, EventType.ARRIVO_GRUPPO_CLIENTI, nPersone, durata, tolleranza, null);
			this.coda.add(e);
			arrivo = arrivo.plusMinutes((int)(Math.random() * this.T_ARRIVO_MAX +1));
		}
	}
	
	//INIZIALIZZAZIONE
	public void init() {
		this.coda = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		
		creaTavoli();
		creaEventi();
	}
	
	//RUN
	public void run() {
		while(!this.coda.isEmpty()) {
			Event e = this.coda.poll();
			processaEvento(e);
		}
	}
	//SIMULAZIONE VERA E PROPRIA
	private void processaEvento(Event e) {
		switch (e.getType()){
		case ARRIVO_GRUPPO_CLIENTI:
			//Conto i clienti totali
			this.statistiche.incrementaClienti(e.getnPersone());
			//cerco un tavolo
			Tavolo tavolo = null;
			for(Tavolo t : this.tavoli) {
				if(!t.isOccupato() && t.getPosti()>=e.getnPersone() && e.getnPersone() >= t.getPosti() * this.OCCUPAZIONE_MAX) {
					tavolo = t;
					break;
				}
			}
			if(tavolo != null) {
				System.out.format("Trovato un tavolo da %d per %d persone", tavolo.getPosti(), e.getnPersone());
				this.statistiche.incrementaSoddisfatti(e.getnPersone());
				tavolo.setOccupato(true); 
				e.setTavolo(tavolo);
				//dopo un po' i clienti si alzeranno
				this.coda.add(new Event(e.getTime().plus(e.getDurata()),EventType.TAVOLO_LIBERATO, e.getnPersone(), e.getDurata(), e.getTolleranza(), tavolo  ));
			} else {
				//c'è solo il balcone ---> capacità illimitata
				double bancone = Math.random();
				if(bancone <= e.getTolleranza()) {
					//clienti si fermano al bancone
					System.out.format("%d persone si fermano al bancone", e.getnPersone());
					this.statistiche.incrementaSoddisfatti(e.getnPersone());
				}else {
					//si va a casa
					System.out.format("%d persone vanno a casa", e.getnPersone());
					this.statistiche.incrementaInsoddisfatti(e.getnPersone());
				}
			}
			
			
			break;
		case TAVOLO_LIBERATO:
			e.getTavolo().setOccupato(false);
			break;
		}
		
	}

}
