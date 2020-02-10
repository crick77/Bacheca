package it.dipvvf.abr.app.bacheca.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dipvvf.abr.app.bacheca.model.Allegato;

/**
 * Interfaccia dei servizi REST per la gestione della bacheca
 * 
 * @author riccardo.iovenitti
 * @author eugenio.fantaconi
 *
 */
@Path("board")
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(name="bearerAuth",description = "Authenticate to use the service",type=SecuritySchemeType.HTTP,in = SecuritySchemeIn.HEADER,scheme = "bearer",bearerFormat = "JWT")
public interface Board {
	@GET
	@Path("{tipo: [a-zA-Z]+}")
	@Operation(summary = "getElenco",description = "Allowed for all users")
	@Tag(name = "Pubblications",description = "Pubblication services")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getElenco(
			@Parameter(description = "Type of document: ODG or DDS",required = true)
			@PathParam("tipo") String tipo,
			@Parameter(description = "Filter for search",required = false,in = ParameterIn.QUERY,example = "Words to search for")
			@QueryParam("q") String query, 
			@Context UriInfo info);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/years")
	@Operation(summary = "getAnnualità",
				description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "500",description = "Server error")
})
	public Response getAnnualita(
			@Parameter(description = "Type of document : ODG or DDS",required = true)
			@PathParam("tipo") String tipo, @Context UriInfo info);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}")
	@Operation(summary = "getElencoAnno",
				description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getElencoAnno(
			@Parameter(description = "Type of document : ODG or DDS",required = true)
			@PathParam("tipo") String tipo,
			@Parameter(description = "Year of pubblication",required=true,example="2020")
//			@Parameter(in = ParameterIn.PATH, name = "anno",
//   					   required = true, description = "Year of pubblication",
//   					   allowEmptyValue = true, allowReserved = false,
//   					   schema = @Schema(type = "int",format = "yyyy",description = "long year format"))
			@PathParam("anno") int anno,
			@Parameter(description = "filter to research",required = false,in = ParameterIn.QUERY,example = "Chiedere a Riccardo")
			@QueryParam("q") String query, 
			@Context UriInfo info);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}")
	@Operation(summary = "getCurrentYearItemDetail",
				description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
			})
	public Response getCurrenYearItemDetail(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo, 
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "getCurrentYearItemStream",
				description = "Allowed for all users",
				tags = {"Pubblications"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
			})
	public Response getCurrenYearItemStream(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}")
	@Operation(summary = "getCurrentYearItemDetail",
				description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "404",description = "Not found"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getYearItemDetail(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Year of pubblication",required=true,example="2020")
			@PathParam("anno") int anno, 
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "getYearStream",
				description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "404",description = "Not found"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getYearItemStream(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo, 
			@Parameter(description = "Year of pubblication",required=true)
			@PathParam("anno") int anno, 
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/attachments")
	@Operation(summary = "getCurrentYearItemDocuments",
				description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getCurrenYearItemDocuments(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo, 
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id,
			@Context UriInfo info);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/attachments")
	@Operation(summary = "getCurrentYearItemDocumentDetail",
				description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getYearItemDocuments(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Year of pubblication",required=true)
			@PathParam("anno") int anno,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id, 
			@Context UriInfo info);

	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}")
	@Operation(summary = "getCurrentYearItemDetail",
				description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "404",description = "Not found"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getCurrenYearItemDocumentDetail(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id,
			@Parameter(description = "Id of the associated documents",required=true,example = "1")
			@PathParam("idall") int idAll);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}")
	@Operation(summary = "getYearItemDocumentDetail",
				description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value= {
	@ApiResponse(responseCode = "200",description = "OK"),
	@ApiResponse(responseCode = "404",description = "Not found"),
	@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getYearItemDocumentDetail(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo, 
			@Parameter(description = "Year of pubblication",required=true)
			@PathParam("anno") int anno, 
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id, 
			@Parameter(description = "Id of the associated documents",required=true,example = "1")
			@PathParam("idall") int idAll);

	@GET
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "getCurrentYearItemDocumentStream",description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getCurrenYearItemDocumentStream(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id,
			@Parameter(description = "Id of the associated documents",required=true,example = "1")
			@PathParam("idall") int idAll);
	
	@GET
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}/attachments/{idall: \\d+}/stream")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "getYearItemDocumentStream",description = "Allowed for all users",tags = {"Attachment"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "OK"),
			@ApiResponse(responseCode = "404",description = "Not found"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response getYearItemDocumentStream(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo, 
			@Parameter(description = "Year of pubblication",required=true)
			@PathParam("anno") int anno,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id,
			@Parameter(description = "Id of the ",required=true,example = "1")
			@PathParam("idall") int idAll);
	
	@POST
	@Path("publish")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SecurityRequirement(name = "bearerAuth")
	
	@Operation(summary = "publishNew",description = "Need JWT Authorization",tags = {"Pubblications","Attachment"},
			   parameters = {@Parameter(name = "tipo",in = ParameterIn.QUERY,required = true,example = "ODG"),
						 	 @Parameter(name = "numero",in = ParameterIn.QUERY,required = true,example = "1"),
						 	 @Parameter(name = "dataPubblications",in = ParameterIn.QUERY,required = true,example = "2020-01-28"),
						 	 @Parameter(name = "titolo",in = ParameterIn.QUERY,required = true,example = "Test"),
						 	 @Parameter(name = "ufficio",in = ParameterIn.QUERY,required = true,example = "VVF"),
						 	 @Parameter(name = "proprietario",in = ParameterIn.QUERY,required = true,example = "Tonino Dinamitardo")
			   				 ,
			   				 @Parameter(name = "file",style = ParameterStyle.FORM,content = {
			   						 @Content(mediaType = "application/pdf",schema = @Schema(type = "string",format = "binary"))
			   				 })
				},
			   	requestBody = @RequestBody(ref = "Attachment",
			   							   content = @Content(schema = @Schema(implementation = Allegato.class)))
				)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",description = "Created"),
			@ApiResponse(responseCode = "401",description = "Unauthorized"),
			@ApiResponse(responseCode = "409",description = "Conflict"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	public Response publishNew(@Context HttpServletRequest req, @Context UriInfo info);	
	
	
	@DELETE
	@Path("{anno: \\d+}/{tipo: [a-zA-Z]+}/{id: \\d+}")
	@Operation(summary = "deleteYearItem",description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204",description = "No content"),
			@ApiResponse(responseCode = "401",description = "Unauthorized"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	@SecurityRequirement(name = "bearerAuth")
	public Response deleteYearItem(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Year of pubblication",required=true)
			@PathParam("anno") int anno,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	@DELETE
	@Path("{tipo: [a-zA-Z]+}/{id: \\d+}")
	@Operation(summary = "deleteCurrentYearItem",description = "Allowed for all users",tags = {"Pubblications"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204",description = "No content"),
			@ApiResponse(responseCode = "401",description = "Unauthorized"),
			@ApiResponse(responseCode = "500",description = "Server error")
	})
	@SecurityRequirement(name = "bearerAuth")
	public Response deleteCurrenYearItem(
			@Parameter(description = "Type of document : ODG or DDS",required = true,example = "ODG")
			@PathParam("tipo") String tipo,
			@Parameter(description = "Id of pubblication",required=true,example = "1")
			@PathParam("id") int id);
	
	@GET
	@Path("reindex")
	@Operation(summary = "performBoardReindex",description = "Perform a complete board reindexing",tags = {"Pubblications", "Index"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "OK"),
	})
	@SecurityRequirement(name = "bearerAuth")
	public Response performBoardReindex();
}
