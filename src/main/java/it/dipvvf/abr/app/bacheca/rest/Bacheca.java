package it.dipvvf.abr.app.bacheca.rest;



import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Interfaccia dei servizi REST per la gestione della bacheca
 * 
 * @author riccardo.iovenitti
 *
 */
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
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCurrenYearItemStream(@PathParam("tipo") String tipo, @PathParam("id") int id);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}")
	public Response getYearItemDetail(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getYearItemStream(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/attachments")
	public Response getCurrenYearItemDocuments(@PathParam("tipo") String tipo, @PathParam("id") int id, @Context UriInfo info);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/attachments")
	public Response getYearItemDocuments(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id, @Context UriInfo info);

	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}")
	public Response getCurrenYearItemDocumentDetail(@PathParam("tipo") String tipo, @PathParam("id") int id, @PathParam("idall") int idAll);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}")
	public Response getYearItemDocumentDetail(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id, @PathParam("idall") int idAll);

	@POST
	@Path("publish")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response publishNew(@Context HttpServletRequest req, @Context UriInfo info);
	
	@DELETE
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}")
	public Response deleteYearItem(@PathParam("tipo") String tipo, @PathParam("anno") int anno, @PathParam("id") int id);
	
	@DELETE
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}")
	public Response deleteCurrenYearItem(@PathParam("tipo") String tipo, @PathParam("id") int id);
}
