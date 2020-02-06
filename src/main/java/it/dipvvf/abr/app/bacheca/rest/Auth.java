package it.dipvvf.abr.app.bacheca.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import it.dipvvf.abr.app.bacheca.model.User;

/**
 * Interfaccia dei servizi REST di login/logout
 * 
 * @author riccardo.iovenitti
 *
 */
@Path("auth")
@Produces(MediaType.TEXT_PLAIN)
@SecurityScheme(name="bearerAuth",description = "Authenticate to use the service",type=SecuritySchemeType.HTTP,in = SecuritySchemeIn.HEADER,scheme = "bearer",bearerFormat = "JWT")
public interface Auth {
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(
				summary = "login",
				tags = {"User"},
				description = "Login authentication", 
				responses = {
					   @ApiResponse(responseCode = "200",description = "OK"),
					   @ApiResponse(responseCode = "304",description = "Not modified"),
					   @ApiResponse(responseCode = "401",description = "Unauthorized")
				}
	)
	public Response login(
			@Parameter(description="Object 'User' with credentials",required=true) final User accessInfo);
	
	
	@POST
	@Path("logout")
	@SecurityRequirement(name = "bearerAuth")
//	public Response logout(@HeaderParam("Authorization") 
//	 	@Parameter (description = "Token used for the login to verify that the session is correct",required=true) String token);
	@Operation(
			summary = "logout",
			tags = {"User"},
			description = "Logout authentication", 
					responses = {
							   @ApiResponse(responseCode = "200",description = "OK"),
							   @ApiResponse(responseCode = "304",description = "Not modified")
						})
	public Response logout(@Parameter(description = "JWT Token",required = true)String token);
	@GET
	@Path("count")
	@Operation(
			summary = "count",
			tags = {"User"},
			description = "return the number of logged users", 
					responses = {
							   @ApiResponse(responseCode = "200",description = "OK")
			}
	)
	public String count();
}
