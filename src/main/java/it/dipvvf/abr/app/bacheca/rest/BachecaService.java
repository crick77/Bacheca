package it.dipvvf.abr.app.bacheca.rest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.dipvvf.abr.app.bacheca.model.Allegato;
import it.dipvvf.abr.app.bacheca.model.Pubblicazione;
import it.dipvvf.abr.app.bacheca.support.Database;
import it.dipvvf.abr.app.bacheca.support.Rollback;
import it.dipvvf.abr.app.bacheca.support.SetAutoCommit;
import it.dipvvf.abr.app.bacheca.support.Utils;

public class BachecaService implements Bacheca {

	@Override
	public Response getElenco(String tipo, UriInfo info) {
		return getElencoAnno(tipo, LocalDate.now().getYear(), info);
	}

	@Override
	public Response getElencoAnno(String tipo, int anno, UriInfo info) {
		tipo = tipo.trim().toUpperCase();

		// Verifica che esiste la tipologia di documento richiesta
		try (Connection con = Database.getInstance().getConnection()) {
			try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS cnt FROM pubblicazione p WHERE p.tipo = ?")) {
				ps.setString(1, tipo);
				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					int count = rs.getInt("cnt");
					if (count == 0) {
						return Response.status(Response.Status.NOT_FOUND).build();
					}
				}
			}

			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);

			// Estrae l'intero anno attuale
			try (PreparedStatement ps = con.prepareStatement(
					"SELECT p.id FROM pubblicazione p WHERE (p.tipo = ?) AND (p.data_pubblicazione BETWEEN ? AND ?) ORDER BY p.numero DESC")) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);

				try (ResultSet rs = ps.executeQuery()) {
					List<Integer> lElenco = new ArrayList<>();
					while (rs.next()) {					
						lElenco.add(rs.getInt("id"));
					}

					return Response.ok(Utils.resourcesToURI(info, lElenco)).build();
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return Response.serverError().entity(sqle.toString()).build();
		}
	}

	@Override
	public Response getCurrenYearItemDetail(String tipo, int id) {
		int anno = LocalDate.now().getYear();
		return getYearItemDetail(tipo, anno, id);
	}

	@Override
	public Response getYearItemDetail(String tipo, int anno, int id) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);

			// Estrae l'intero anno attuale
			try (PreparedStatement ps = con.prepareStatement(
					"SELECT p.* FROM pubblicazione p WHERE (p.tipo = ?) AND (p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?)")) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Pubblicazione p = new Pubblicazione();
						p.setId(rs.getInt("id"));
						p.setNumero(rs.getInt("numero"));
						p.setProprietario(rs.getString("proprietario"));
						p.setTipo(rs.getString("tipo"));
						p.setTitolo(rs.getString("titolo"));
						p.setUfficio(rs.getString("ufficio"));
						p.setDataPubblicazione(rs.getDate("data_pubblicazione"));
						p.setNomeDocumento(rs.getString("nome_documento"));
						p.setDimensione(rs.getInt("dimensione"));
						p.setContenutoDocumento(rs.getBytes("contenuto_documento"));
						
						return Response.ok(p).build();
					} else {
						return Response.status(Response.Status.NOT_FOUND).build();
					}
				}
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return Response.serverError().entity(sqle.toString()).build();
		}
	}

	@Override
	public Response getCurrenYearItemDocuments(String tipo, int id, UriInfo info) {
		int anno = LocalDate.now().getYear();
		return getYearItemDocuments(tipo, anno, id, info);
	}

	@Override
	public Response getYearItemDocuments(String tipo, int anno, int id, UriInfo info) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);
			
			String sql = "SELECT a.id FROM allegato a JOIN pubblicazone p ON a.id_elenco = e.id WHERE (p.tipo = ?) AND "+
						 "(p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?)";
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);
				
				try (ResultSet rs = ps.executeQuery()) {
					List<Integer> lIdDoc = new ArrayList<>();
					while(rs.next()) {
						lIdDoc.add(rs.getInt("id"));
					}
					
					return Response.ok(Utils.resourcesToURI(info, lIdDoc)).build();
				}
			}
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
			return Response.serverError().entity(sqle.toString()).build();
		}
	}

	@Override
	public Response getCurrenYearItemDocumentDetail(String tipo, int id, int idDoc) {
		int anno = LocalDate.now().getYear();
		return getYearItemDocumentDetail(tipo, anno, id, idDoc);
	}

	@Override
	public Response getYearItemDocumentDetail(String tipo, int anno, int id, int idAll) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);
			
			String sql = "SELECT a.* FROM allegato a JOIN pubblicazione p ON a.id_pubblicazione = p.id WHERE (p.tipo = ?) AND "+
						 "(p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?) AND (a.id = ?)";
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);
				ps.setInt(5, idAll);
				
				try (ResultSet rs = ps.executeQuery()) {					
					if(rs.next()) {
						Allegato a = new Allegato();
						a.setId(rs.getInt("id"));
						a.setNomefile(rs.getString("nomefile"));
						a.setDimensione(rs.getInt("dimensione"));
						a.setContenuto(rs.getBytes("contenuto"));
						
						return Response.ok(a).build();
					}
					else {
						return Response.status(Response.Status.NOT_FOUND).build();
					}
				}
			}
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
			return Response.serverError().entity(sqle.toString()).build();
		}
	}

	@Override
	public Response publishNew(HttpServletRequest req, UriInfo info) {
		try (Connection con = Database.getInstance().getConnection();
		     SetAutoCommit ac = new SetAutoCommit(con, false);
		     Rollback rb = new Rollback(con)) {
			
			/** START DEBUG PURPOSE - TO BE REMOVED **/
			Enumeration<String> names = req.getParameterNames();
			while(names.hasMoreElements()) {
				String n = names.nextElement();
				System.out.println("["+n+"]=["+req.getParameter(n)+"]");
			}
			Collection<Part> _parts = req.getParts();
			int count = 1;
			for(Part part : _parts) {
				String pName = part.getName();
				System.out.println("Part #"+(count++)+"=["+pName+", "+part.getSubmittedFileName()+", "+part.getSize()+"]");
			}
			/** END DEBUG PURPOSE - TO BE REMOVED **/
			
			int numeroPub = Integer.parseInt(req.getParameter("numero"));
			LocalDate dataPub = Utils.parseISODate(req.getParameter("datapubblicazione"));
			LocalDate firstDay = dataPub.with(TemporalAdjusters.firstDayOfYear());
			LocalDate lastDay = dataPub.with(TemporalAdjusters.lastDayOfYear());
			
			// check for duplicates
			String sql = "SELECT COUNT(*) AS cnt FROM pubblicazione p WHERE (p.numero = ?) AND (p.data_pubblicazione BETWEEN ? AND ?)";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, numeroPub);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				
				try (ResultSet rs = ps.executeQuery()) {					
					if(rs.next()) {
						int total = rs.getInt("cnt");
						if(total==1) return Response.status(Response.Status.CONFLICT).build();
					}
				}
			}
			
			// insert pubblicazione
			sql = "INSERT INTO pubblicazione (tipo, numero, data_pubblicazione, titolo, ufficio, proprietario, nome_documento, dimensione, contenuto_documento) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			int id = -1;
			try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				Part part = req.getPart("documento");
				String fName = part.getSubmittedFileName();
				final byte[] bytes;
	            try (InputStream in = part.getInputStream()) {
	                bytes = new byte[(int)part.getSize()];
	                in.read(bytes);
	            }
	            part.delete();
				
				ps.setString(1, req.getParameter("tipo"));
				ps.setInt(2, numeroPub);
				ps.setObject(3, dataPub);
				ps.setString(4, req.getParameter("titolo"));
				ps.setString(5, req.getParameter("ufficio"));
				ps.setString(6, req.getParameter("proprietario"));
				ps.setString(7,  fName);
				ps.setInt(8, bytes.length);
				ps.setBytes(9, bytes);
				
				ps.executeUpdate();
				
				ResultSet rsK = ps.getGeneratedKeys();
				if(rsK.next()) {
					id = rsK.getInt(1);
				}
				rsK.close();
			}
			
			// insert all attachments
			sql = "INSERT INTO allegato (id_pubblicazione, nomefile, dimensione, contenuto) values (?, ?, ?, ?)";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				Collection<Part> parts = req.getParts();
				for(Part part : parts) {
					if(!part.getName().equals("documento")) {
						String fName = part.getSubmittedFileName();
						final byte[] bytes;
			            try (InputStream in = part.getInputStream()) {
			                bytes = new byte[(int)part.getSize()];
			                in.read(bytes);
			            }
			            part.delete();
		            
			            ps.clearParameters();
						ps.setInt(1, id);
						ps.setString(2, fName);
						ps.setInt(3, bytes.length);
						ps.setBytes(4, bytes);
						
						ps.executeUpdate();
					}
				}
			}
			
			rb.commit();
			return Response.created(Utils.resourceToURI(info, id)).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.toString()).build();
		}
	}

	@Override
	public Response getCurrenYearItemStream(String tipo, int id) {
		return getYearItemStream(tipo, LocalDate.now().getYear(), id);
	}

	@Override
	public Response getYearItemStream(String tipo, int anno, int id) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);

			// Estrae l'intero anno attuale
			try (PreparedStatement ps = con.prepareStatement(
					"SELECT p.* FROM pubblicazione p WHERE (p.tipo = ?) AND (p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?)")) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						String fileName = rs.getString("nome_documento");
						byte[] fileData = rs.getBytes("contenuto_documento");
						
						return Utils.downloadFile(fileName, fileData);
					}
					else {
						return Response.status(Response.Status.NOT_FOUND).build();
					}
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.toString()).build();
		}
	}

	@Override
	public Response deleteCurrenYearItem(String tipo, int id) {
		return deleteYearItem(tipo, LocalDate.now().getYear(), id);
	}
	
	@Override
	public Response deleteYearItem(String tipo, int anno, int id) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);

			// Estrae l'intero anno attuale
			try (PreparedStatement ps = con.prepareStatement(
					"DELETE FROM pubblicazione p WHERE (p.tipo = ?) AND (p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?)")) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);
				
				if(ps.executeUpdate()==1) {
					return Response.noContent().build();
				}
				else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.toString()).build();
		}
	}
}
