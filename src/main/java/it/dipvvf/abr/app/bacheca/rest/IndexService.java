package it.dipvvf.abr.app.bacheca.rest;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.dipvvf.abr.app.bacheca.support.InverseIndex;

public class IndexService implements Index {

	@Override
	public Response index(int id, String body) {
		InverseIndex.access().add(id, body);
		return Response.ok().build();
	}

	@Override
	public Response search(String query) {
		List<Integer> docIds = InverseIndex.access().search(query);
		if(docIds.size()==0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		else {
			return Response.ok(docIds).build();
		}
	}

	@Override
	public Response delete(int id) {
		InverseIndex.access().delete(id);
		return Response.ok().build();
	}

}
