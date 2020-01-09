package it.dipvvf.abr.app.bacheca.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.dipvvf.abr.app.bacheca.model.User;

/**
 * Interfaccia dei servizi REST di login/logout
 * 
 * @author riccardo.iovenitti
 *
 */
@Path("user")
@Produces(MediaType.TEXT_PLAIN)
public interface Auth {
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(final User accessInfo);
	
	@POST
	@Path("logout")
	public Response logout(@HeaderParam("Authorization") String token);
	
	@GET
	@Path("count")
	public String count();
}
