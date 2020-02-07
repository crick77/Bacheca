package it.dipvvf.abr.app.bacheca.soap.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import it.dipvvf.abr.app.bacheca.rest.BoardService;

public class ClientPasswordHandler implements CallbackHandler {
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    	WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
    	pc.setPassword(BoardService.MAILSERVICE_PASSWORD);
    }
}
