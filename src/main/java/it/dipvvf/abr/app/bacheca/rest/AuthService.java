package it.dipvvf.abr.app.bacheca.rest;

import javax.ws.rs.core.Response;

import it.dipvvf.abr.app.auth.soap.AuthSOAP;
import it.dipvvf.abr.app.auth.soap.AuthSOAPServiceService;
import it.dipvvf.abr.app.bacheca.model.User;
import it.dipvvf.abr.app.bacheca.support.Session;
import it.dipvvf.abr.app.bacheca.support.Utils;

public class AuthService implements Auth {
	public final static String ISSUER = "Bacheca-VVF";
	public final static String APP_ROLE = "GBacheca";
	private final static String BEARER = "Bearer ";
	
	@Override
	public Response login(User accessInfo) {
		// Chiama il servizio SOAP (per ora simulato)
		AuthSOAP authService = new AuthSOAPServiceService().getAuthSOAPServicePort();
		
		if(authService.authenticate(accessInfo.getUsername(), accessInfo.getPassword())) {
			if(authService.hasRole(accessInfo.getUsername(), APP_ROLE)) {
			String token = Utils.createToken(accessInfo.getUsername(), ISSUER, -1);
				if(Session.getSession().store(token)) {
					return Response.ok(token).build();
				}
				else {
					return Response.status(Response.Status.NOT_MODIFIED).build();
				}
			}
			else {
				System.out.println("Utente ["+accessInfo.getUsername()+"] valido ma ruolo non presente.");
			}
		}
		
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@Override
	public Response logout(String token) {
		if(token==null || token.length()<1) return Response.status(Response.Status.NOT_MODIFIED).build();
		
		// Rimuove il Bearer se presente
		token = token.replace(BEARER, "").trim();
		
		if(Session.getSession().invalidate(token)) {
			return Response.ok().build();
		}
		else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}

	@Override
	public String count() {
		return String.valueOf(Session.getSession().activeCount());
	}
}
