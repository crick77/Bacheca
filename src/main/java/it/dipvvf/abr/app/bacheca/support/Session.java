package it.dipvvf.abr.app.bacheca.support;

import java.util.LinkedList;
import java.util.List;

/**
 * Storage di sessione. Mantiene l'elenco dei token JWT attivi
 * Implementa pattern singleton
 * 
 * @author riccardo.iovenitti
 *
 */
public final class Session {
	private final List<String> storage;
	private final static Session _instance = new Session();
	
	private Session() {
		storage = new LinkedList<>();
	}
	
	public static Session getSession() {
		return _instance;
	}
	
	public synchronized boolean store(String token) {
		if(!storage.contains(token)) {
			storage.add(token);
			return true;
		}
		return false;
	}
	
	public synchronized boolean isValid(String token) {
		return storage.contains(token);
	}
	
	public synchronized boolean invalidate(String token) {
		if(storage.contains(token)) {
			storage.remove(token);
			return true;
		}
		return false;
	}
	
	public synchronized int activeCount() {
		return storage.size();
	}
}
