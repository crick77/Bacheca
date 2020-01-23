package it.dipvvf.abr.app.bacheca.support.soap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.ws.rs.client.InvocationCallback;

import it.dipvvf.abr.app.bacheca.support.sql.Database;

public class IndexInvocationCallback implements InvocationCallback<String> {
	private int publishId;
	
	public IndexInvocationCallback(int publishId) {
		super();
		this.publishId = publishId;
	}

	@Override
	public void completed(String response) {
		saveStatus("OK");
		
	}

	@Override
	public void failed(Throwable throwable) {
		saveStatus("FAILED");
	}
	
	private void saveStatus(String status) {
		try(Connection con = Database.getInstance().getConnection()) {
			String sql = "UPDATE pubblicazione SET indexing_status = ? WHERE (id = ?)";
			try(PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1,  status);
				ps.setInt(2, publishId);
				ps.executeUpdate();
				
				System.out.println("Indexing "+status+"...aggiornamento effettuato.");
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Errore aggiornamento Indexing: "+ex);
		}
	}
}


