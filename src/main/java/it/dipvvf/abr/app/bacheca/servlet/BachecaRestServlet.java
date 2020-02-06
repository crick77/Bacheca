package it.dipvvf.abr.app.bacheca.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

/**
 * Servlet di inizializzazione REST con salvataggio dell'indice alla chiusura.
 * 
 * @author ospite
 *
 */
public class BachecaRestServlet extends CXFNonSpringJaxrsServlet {
	private static final long serialVersionUID = -2242818805107181424L;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
