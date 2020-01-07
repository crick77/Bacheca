package it.dipvvf.abr.app.bacheca.support;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	private static Database _instance;
	private DataSource ds;

	private Database() {
		try {
			InitialContext cxt = new InitialContext();
			ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/bacheca");

			if (ds == null) {
				throw new RuntimeException("Data source not found!");
			}
		} catch (NamingException ne) {
			throw new RuntimeException("Impossibile recuperare il context!", ne);
		}
	}
	
	public synchronized static Database getInstance() {
		if(_instance == null) {
			_instance = new Database();
		}
		return _instance;
	}
	
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
