package it.dipvvf.app.bacheca.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.junit.jupiter.api.Test;

import it.dipvvf.abr.app.mail.soap.MailSOAP;
import it.dipvvf.abr.app.mail.soap.MailSOAPServiceService;
import it.dipvvf.abr.app.mail.soap.SendMail;

public class MailSOAPTest {
	public final static String MAILSERVICE_USERNAME = "mailuser1";
	public final static String MAILSERVICE_PASSWORD = "passmail1";
	public static int resultId = 10;
	
	@Test
    public void testMail() {
		// Chiama servizio di spedizione mail in async (SOAP)
		MailSOAP mailService = new MailSOAPServiceService().getMailSOAPServicePort();
		
		Client wsClient = ClientProxy.getClient(mailService);
		Endpoint mailEndpoint = wsClient.getEndpoint();
		
		Map<String, Object> outProps = new HashMap<>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, MAILSERVICE_USERNAME);
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientTestPasswordHandler.class.getName());
		mailEndpoint.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
		
		SendMail sm = new SendMail();
		sm.setSender("ciccio@send.com");
		sm.setSubject("Prova invio");
		sm.setBody("Testo della mail");
		sm.getRecipients().add("all@mondo.com");
		MailTestAsyncHandler mtah = new MailTestAsyncHandler(resultId);
		Future<?> response = mailService.sendMailAsync(sm, mtah);
		
		System.out.print("Attesa completamento");
		while (!response.isDone()) {
			try {
				Thread.sleep(500);
				System.out.print(".");
			}
			catch(InterruptedException ie) {}
		}
		System.out.println("Completato! Risposta: "+mtah.isResult());
		
		assertTrue(mtah.isResult());
    }

}
