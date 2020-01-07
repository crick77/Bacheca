package it.dipvvf.abr.app.bacheca.support;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public final class Utils {
	private Utils() {
	}
	
	public static String resourceToURI(UriInfo uriInfo, Object resource) {                       
        ArrayList<Object> resources = new ArrayList<>();
        resources.add(resource);
        return resourcesToURI(uriInfo, resources).get(0);
    }
	
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
}
