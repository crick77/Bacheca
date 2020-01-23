package it.dipvvf.abr.app.bacheca.support.soap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import it.dipvvf.abr.app.bacheca.support.sql.Database;
import it.dipvvf.abr.app.mail.soap.MailReturnResponse;

public class MailAsyncHandler implements AsyncHandler<MailReturnResponse> {
	private int publishId;

	public MailAsyncHandler(int publishId) {
		super();
		this.publishId = publishId;
	}

	@Override
	public void handleResponse(Response<MailReturnResponse> res) {
		try {
			// mail inviata?
			String mailStatus = res.get().isStatus() ? "OK" : "FAILED";
			
			try(Connection con = Database.getInstance().getConnection()) {
				String sql = "UPDATE pubblicazione SET mail_status = ? WHERE (id = ?)";
				try(PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1,  mailStatus);
					ps.setInt(2, publishId);
					ps.executeUpdate();
				}
			}
			
			System.out.println("Email "+mailStatus+"...aggiornamento effettuato.");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Errore aggiornamento email: "+ex);
		}
	}

}
