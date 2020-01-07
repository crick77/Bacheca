package it.dipvvf.abr.app.bacheca.rest;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("bacheca")
@Produces(MediaType.APPLICATION_JSON)
public interface Bacheca {
	@GET
	@Path("{tipo: [a-zA-Z]+}")
	public Response getElenco(@PathParam("tipo") String tipo, @Context UriInfo info);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}")
	public Response getElencoAnno(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @Context UriInfo info);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}")
	public Response getCurrenYearItemDetail(@PathParam("tipo") String tipo, @PathParam("id") int id);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}")
	public Response getYearItemDetail(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/documents")
	public Response getCurrenYearItemDocuments(@PathParam("tipo") String tipo, @PathParam("id") int id, @Context UriInfo info);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/documents")
	public Response getYearItemDocuments(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id, @Context UriInfo info);

	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/documents/{iddoc: \\d+}")
	public Response getCurrenYearItemDocumentDetail(@PathParam("tipo") String tipo, @PathParam("id") int id, @PathParam("iddoc") int idDoc);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/documents/{iddoc: \\d+}")
	public Response getYearItemDocumentDetail(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id, @PathParam("iddoc") int idDoc);
}
