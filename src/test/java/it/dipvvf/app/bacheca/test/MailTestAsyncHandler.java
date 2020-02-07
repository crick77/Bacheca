package it.dipvvf.app.bacheca.test;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import it.dipvvf.abr.app.mail.soap.MailReturnResponse;

public class MailTestAsyncHandler implements AsyncHandler<MailReturnResponse> {
	private boolean result;

	public MailTestAsyncHandler(int publishId) {
		super();
		this.result = false;
	}

	@Override
	public void handleResponse(Response<MailReturnResponse> res) {
		try {
			result = res.get().isStatus();
		}
		catch(Exception e) {}
	}

	public boolean isResult() {
		return result;
	}
}
