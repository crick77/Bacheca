package it.dipvvf.abr.app.bacheca.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Interfaccia dei servizi REST per l'indicizzazione
 * 
 * @author riccardo.iovenitti
 *
 */
@Path("index")
@Produces(MediaType.APPLICATION_JSON)
public interface Index {
	@POST
	@Path("{id: \\d+}")
	public Response index(@PathParam("id") int id, String body);
	
	@GET
	@Path("search")
	public Response search(@QueryParam("q") String query);
	
	@DELETE
	@Path("{id: \\d+}")
	public Response delete(@PathParam("id") int id);
}
