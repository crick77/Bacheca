package it.dipvvf.abr.app.bacheca.support;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Classe di utilità generatica (membri statici).
 * Non è una best-practice...ma va bene.
 * 
 * @author riccardo.iovenitti
 *
 */
public final class Utils {
	private final static String SECRET = "ToBeChanged!";
	
	private Utils() {
	}
	
	/**
	 * Converte un oggetto (in formato stringa) in una URI relaiva
	 * al path del contesto. 
	 * 
	 * @param uriInfo
	 * @param resource
	 * @return
	 */
	public static URI resourceToURI(UriInfo uriInfo, Object resource) {                       
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(String.valueOf(resource));
        return builder.build();
    }
	
	/**
	 * Converte un elenco di ogggetti (rappresentati tramite stringa)
	 * in un elenco di URI relative al path del contesto.
	 * 
	 * @param uriInfo
	 * @param resources
	 * @return
	 */
	public static List<String> resourcesToURI(UriInfo uriInfo, List<? extends Object> resources) {
        List<String> uris = new ArrayList<>(resources!=null ? resources.size() : 1);
        
        if(resources!=null) {                
            resources.stream().map((res) -> {
                UriBuilder builder = uriInfo.getAbsolutePathBuilder();
                builder.path(String.valueOf(res));
                return builder;
            }).forEachOrdered((builder) -> {
                uris.add(builder.build().toASCIIString());
            });
        }
        
        return uris;
    }
	
	/**
	 * Effettua un parse di una data in formato ISO (yyyy-mm-dd)
	 * 
	 * @param isoDate
	 * @return
	 */
	public static LocalDate parseISODate(String isoDate) {
		try {
			return LocalDate.parse(isoDate);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("invalid date", e);
		}
		
	}
	
	/**
	 * Ritorna nell'header il content-disposition per il download nel body.
	 * 
	 * @param fileName nome del file
	 * @param data dati del file
	 * @return
	 */
	public static Response downloadFile(String fileName, byte[] data) {
		return Response.ok(data).header("Content-Disposition", "attachment; filename=\""+fileName+"\"").build();
	}
	
	/**
	 * Crea un token JWT. Se expire è <1 allora il token non ha scadenza,
	 * altrimenti scadrà esattamente dopo expire secondi
	 * 
	 * @param userId
	 * @param issuer
	 * @param expire secondi
	 * @return
	 */
	public static String createToken(String userId, String issuer, long expire) {
		Date issueDate = new Date();
		JWTCreator.Builder token = JWT.create().withSubject(userId).withIssuer(issuer).withIssuedAt(issueDate);
		if(expire>0) {
			token.withExpiresAt(new Date(issueDate.getTime()+(expire*1000)));
		}
		
		return token.sign(Algorithm.HMAC512(SECRET.getBytes()));
	}
	
	/**
	 * Verifica un token JWT con l'issuer indicato e restituisce
	 * la sua versione decodificata se tutto è ok. Verrà restituito
	 * null altrimenti.
	 * 
	 * @param token
	 * @param issuer
	 * @return
	 */
	public static DecodedJWT verify(String token, String issuer) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
									.withIssuer(issuer)
									.build();
			return verifier.verify(token);
		}
		catch(Exception e) {
			return null;
		}
	}
}
