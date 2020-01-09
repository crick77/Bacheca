package it.dipvvf.abr.app.bacheca.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa una versione semplificata di "indice inverso" utilizzato per le ricerche full-text.
 * Il sistema elimina punteggiature e parole inferiori a 3 caratteri per non intasare l'indice.
 * L'implementazione è puramente di test, in produzione verrà utilizzato Apache Solr.
 * 
 * Un indice inverso è costituito da un elenco di parole (token) con associato l'elenco
 * degli id di documento che contengono quel token:
 * 
 * "this" = [1, 2]
 * "is" = [1, 6, 3]
 * "an" = [9, 10]
 * "inverse" = [2]
 * "index" = [8, 5, 2, 3, 7]
 * 
 * Per esempio se cercassi la frase "is inverse index" il sistema ritornerebbe [1, 6, 3, 2, 5, 7, 8] che è
 * l'elenco dei documenti che contengono quelle 3 parole (insieme o separate o combinazione di loro).
 * 
 * La classe è implementata tramite Singleton pattern con accesso sincronizzato allo storage
 * dell'indice per evitare corruzioni di dati in caso di accessi concorrenti.
 * 
 * @author riccardo.iovenitti
 *
 */
public class InverseIndex {
	private final static InverseIndex _instance = new InverseIndex();
	private Map<String, List<Integer>> inverseIndexMap;
	
	private InverseIndex() {
		inverseIndexMap = new HashMap<>();
	}
	
	public static InverseIndex access() {
		return _instance;
	}
	
	/**
	 * Aggiunge un documento all'indice con un id associato
	 * 
	 * @param id
	 * @param text
	 */
	public synchronized void add(int id, String text) {
		String[] tokens = tokenize(text);
		
		// itera tutti i tokens
		for(String token : tokens) {
			// Il token è già indicizzato?
			List<Integer> ids = inverseIndexMap.get(token);
			if(ids==null) {
				// No crea l'indice e associalo al token
				ids = new ArrayList<>();
				ids.add(id);
				inverseIndexMap.put(token, ids);
			}
			else {
				// Si, se l'id non è già censito, aggiungilo.
				if(!ids.contains(id)) {
					ids.add(id);
				}
			}
		}
	}
	
	/**
	 * Effettua una ricarca full-text nell'indice
	 * 
	 * @param text
	 * @return l'elenco degli id trovati
	 */
	public synchronized List<Integer> search(String text) {
		String[] tokens = tokenize(text);
		
		// Inizializza il risultato
		List<Integer> result = new ArrayList<>();
		// Itera i tokens
		for(String token : tokens) {
			// trovato?
			List<Integer> ids = inverseIndexMap.get(token);
			if(ids!=null) {
				// Si, inserisci ciascun id una volta sola (no dup)
				for(int id : ids) {
					if(!result.contains(id)) { 
						result.add(id);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Elimina un documento dall'indice.
	 * 
	 * @param id
	 */
	public synchronized void delete(int id) {
		// elenco dei token vuoti da rimuovere al termine
		List<String> toBeRemoved = new ArrayList<>();
		// itera i token
		for(String token : inverseIndexMap.keySet()) {
			List<Integer> ids = inverseIndexMap.get(token);
			// trovato?
			int idx = ids.indexOf(id);
			if(idx!=-1) {
				ids.remove(idx);
				// nessun documento associato a questo token?
				if(ids.size()==0) {
					// segn come da rimuovere
					toBeRemoved.add(token);
				}
			}
		}
		
		// pulisce l'indice dai token vuoti
		for(String token : toBeRemoved) {
			inverseIndexMap.remove(token);
		}
	}
	
	/**
	 * Tokenizza il testo eliminando segni di punteggiatura, spazi, ritorni
	 * a capo e parole troppo brevi (<3 caratteri)
	 * 
	 * @param text
	 * @return l'elenco dei token
	 */
	private String[] tokenize(String text) {
		// evita null, pulice e rendi maiuscolo
		text = (text!=null) ? text.trim().toUpperCase() : "";
		// Elimina i segni di punteggiatura
		text = text.replaceAll("\\p{Punct}", "").trim();
		// Elimina le parole brevi
		text = text.replaceAll("\\b[\\w']{1,2}\\b", " ").trim();
		// Elimina spazi inutili
		text = text.replaceAll("\\s{2,}", " ").trim();
		// tokenizza tramite spazi
		return text.split("[\\s]+");
	}
}
