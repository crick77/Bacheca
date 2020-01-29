package it.dipvvf.abr.app.bacheca.rest.security;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.auth0.jwt.interfaces.DecodedJWT;

import it.dipvvf.abr.app.bacheca.support.Utils;

public class SecurityInterceptor extends AbstractPhaseInterceptor<Message> {
	private final static String BEARER = "Bearer";
	
	public SecurityInterceptor() {
		super(Phase.USER_LOGICAL);
		System.out.println(getClass().getName()+": Interceptor registrato.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message message) {
		Map<String, List<String>> headers = CastUtils.cast((Map<String, List<String>>)message.get(Message.PROTOCOL_HEADERS));
    	     
    	OperationResourceInfo ori = (OperationResourceInfo)message.getExchange().get(OperationResourceInfo.class);
    	Method m = ori.getMethodToInvoke();
    	if((m.isAnnotationPresent(SecurityCheck.class) || m.getDeclaringClass().isAnnotationPresent(SecurityCheck.class))
    			&& !m.isAnnotationPresent(SkipSecurityCheck.class)) {
    		SecurityCheck sc = m.getAnnotation(SecurityCheck.class);
    		if(sc==null) {
    			sc = m.getDeclaringClass().getAnnotation(SecurityCheck.class);
    		}
    		
    		System.out.print("ATTENZIONE: Verifica autorizzazione necessaria");
    		List<String> auth = headers.get("Authorization");
    		if(auth==null) {    	                
    			System.out.println("...Header Authorization NON VALORIZZATO! RIFIUTO ACCESSO!");
    	        message.getExchange().put(Response.class, Response.status(Response.Status.UNAUTHORIZED).build());
    		}
    		else {
    			String token = (auth.get(0)!=null) ? auth.get(0) : "";
    			token = token.replace(BEARER, "").trim();
    			DecodedJWT jwt = Utils.verify(token, sc.issuer());
    			if(jwt!=null) {    			
    				System.out.println("...TOKEN OK! ["+jwt.getIssuer()+"]");
    			}
    			else {
    				System.out.println("...Autorizzazione NON VALIDA!");
    				message.getExchange().put(Response.class, Response.status(Response.Status.UNAUTHORIZED).build());
    			}
    		}
    	}
    	else {
    		System.out.println("Autorizzazione NON RICHIESTA! Proseguo con l'esecuzione.");
    	}
	}
}
