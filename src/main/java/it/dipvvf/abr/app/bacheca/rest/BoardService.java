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
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.map.ObjectMapper;

import it.dipvvf.abr.app.bacheca.model.Allegato;
import it.dipvvf.abr.app.bacheca.model.Pubblicazione;
import it.dipvvf.abr.app.bacheca.rest.security.SecurityCheck;
import it.dipvvf.abr.app.bacheca.support.Utils;
import it.dipvvf.abr.app.bacheca.support.soap.IndexInvocationCallback;
import it.dipvvf.abr.app.bacheca.support.soap.MailAsyncHandler;
import it.dipvvf.abr.app.bacheca.support.sql.Database;
import it.dipvvf.abr.app.bacheca.support.sql.Rollback;
import it.dipvvf.abr.app.bacheca.support.sql.SetAutoCommit;
import it.dipvvf.abr.app.index.soap.Index;
import it.dipvvf.abr.app.index.soap.IndexSOAPImpService;
import it.dipvvf.abr.app.mail.soap.Attachment;
import it.dipvvf.abr.app.mail.soap.MailSOAP;
import it.dipvvf.abr.app.mail.soap.MailSOAPServiceService;
import it.dipvvf.abr.app.mail.soap.SendMail;

public class BoardService implements Board {
	public final static String INDEX_API = "http://localhost:8080/Bacheca/api/index";

	@Override
	public Response getElenco(String tipo, String query, UriInfo info) {
		return getElencoAnno(tipo, LocalDate.now().getYear(), query, info);
	}

	@Override
	public Response getElencoAnno(String tipo, int anno, String query, UriInfo info) {
		tipo = tipo.trim().toUpperCase();
		query = (query!=null) ? query.trim() : "";
		
		// minimo tre caratteri per iniziare una ricerca
		boolean doIndexSearch = (query.length()>=3);
		Integer[] matchId = new Integer[0];
		
		// cercare nell'indice?
		if(doIndexSearch) {
			// Effettua la query sull'indice
			matchId = performSearch(query);
			// nulla? restituisci ok con entity vuoto.
			if(matchId.length==0) {
				return Response.ok().build();
			}
		}
		
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
			String sql = "SELECT p.id, p.tipo, p.numero, p.data_pubblicazione, p.titolo, p.ufficio, p.proprietario, ";
			sql+="p.nome_documento, p.dimensione FROM pubblicazione AS p WHERE (p.tipo = ?) AND (p.data_pubblicazione BETWEEN ? AND ?) ";
			if(doIndexSearch && matchId.length>0) {
				sql+="AND p.id IN ("+String.join(",", Collections.nCopies(matchId.length, "?"))+") ";
			}
			sql+="ORDER BY p.numero DESC";
		
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				if(doIndexSearch && matchId.length>0) {
					for(int i=0;i<matchId.length;i++) {
						ps.setObject(i+4, matchId[i]);
					}
				}
				
				try (ResultSet rs = ps.executeQuery()) {
					List<Pubblicazione> lElenco = new ArrayList<>();
					while (rs.next()) {	
						Pubblicazione p = new Pubblicazione();
						p.setId(rs.getInt("id"));
						p.setTipo(rs.getString("tipo"));
						p.setNumero(rs.getInt("numero"));
						p.setDataPubblicazione(rs.getDate("data_pubblicazione"));
						p.setTitolo(rs.getString("titolo"));
						p.setUfficio(rs.getString("ufficio"));
						p.setProprietario(rs.getString("proprietario"));
						p.setNomeDocumento(rs.getString("nome_documento"));
						p.setDimensione(rs.getInt("dimensione"));
						
						lElenco.add(p);
					}

					//return Response.ok(Utils.resourcesToURI(info, lElenco)).build();
					return Response.ok(lElenco).build();
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
			
			String sql = "SELECT a.id, a.nomefile, a.dimensione FROM allegato a JOIN pubblicazione p ON a.id_pubblicazione = p.id WHERE (p.tipo = ?) AND "+
						 "(p.data_pubblicazione BETWEEN ? AND ?) AND (p.id = ?)";
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);
				
				try (ResultSet rs = ps.executeQuery()) {
					List<Allegato> lDocs = new ArrayList<>();
					while(rs.next()) {
						Allegato a = new Allegato();
						a.setId(rs.getInt("id"));
						a.setNomefile(rs.getString("nomefile"));
						a.setDimensione(rs.getInt("dimensione"));
						
						lDocs.add(a);
					}
					
					//return Response.ok(Utils.resourcesToURI(info, lIdDoc)).build();
					return Response.ok(lDocs).build();
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
	@SecurityCheck(issuer = AuthService.ISSUER)
	public Response publishNew(HttpServletRequest req, UriInfo info) {
		try (Connection con = Database.getInstance().getConnection();
		     SetAutoCommit ac = new SetAutoCommit(con, false);
		     Rollback rb = new Rollback(con)) {
			
			// Email attachment list
			List<Attachment> lAtt = new ArrayList<>();
			
			/** START DEBUG PURPOSE - TO BE REMOVED **/
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
			sql = "INSERT INTO pubblicazione (tipo, numero, data_pubblicazione, titolo, ufficio, proprietario, nome_documento, dimensione, contenuto_documento, mail_status, indexing_status) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
				ps.setString(10, "PENDING");
				ps.setString(11, "PENDING");
				
				ps.executeUpdate();
				
				try(ResultSet rsK = ps.getGeneratedKeys()) {
					if(rsK.next()) {
						id = rsK.getInt(1);
					}
				}
				
				// Add attachment for email
				Attachment att = new Attachment();
				att.setName(fName);
				att.setData(bytes);
				lAtt.add(att);
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
						
						// Add attachment for email
						Attachment att = new Attachment();
						att.setName(fName);
						att.setData(bytes);
						lAtt.add(att);
					}
				}
			}
			
			rb.commit();
			
			// Chiama servizio di indicizzazione in async (REST)
			WebClient client = WebClient.create(INDEX_API+"/"+id);
			client.getHeaders().add("Accept", "text/plain,text/html,application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			client.getHeaders().add("Content-Type", "text/plain");
			client.async()
				  .method("POST", Entity.text(req.getParameter("titolo")), new IndexInvocationCallback(id));
					
			// Chiama servizio di spedizione mail in async (SOAP)
			MailSOAP mailService = new MailSOAPServiceService().getMailSOAPServicePort();
			
			SendMail sm = new SendMail();
			sm.setSender("ciccio@send.com");
			sm.setSubject("Prova invio");
			sm.setBody("Testo della mail");
			sm.getRecipients().add("all@mondo.com");
			sm.getAttachments().addAll(lAtt);
			mailService.sendMailAsync(sm, new MailAsyncHandler(id));
	        
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
	@SecurityCheck(issuer = AuthService.ISSUER)
	public Response deleteCurrenYearItem(String tipo, int id) {
		return deleteYearItem(tipo, LocalDate.now().getYear(), id);
	}
	
	@Override
	@SecurityCheck(issuer = AuthService.ISSUER)
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
					WebClient client = WebClient.create(INDEX_API+"/"+id);
					client.getHeaders().add("Accept", "text/plain,text/html,application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					client.getHeaders().add("Content-Type", "text/plain");
					client.delete();
					
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

	@Override
	public Response getAnnualita(String tipo, UriInfo info) {
		try (Connection con = Database.getInstance().getConnection()) {
			// Estrae l'intero anno attuale
			String sql = "SELECT DISTINCT(EXTRACT(year FROM p.data_pubblicazione)) AS anno FROM pubblicazione p WHERE (p.tipo = ?) ORDER BY anno DESC";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				
				List<Integer> lAnni = new ArrayList<>();
				try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						lAnni.add(rs.getInt("anno"));
					}
					
					return Response.ok(lAnni).build();
				}
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.serverError().entity(ex.toString()).build();
		}
	}
	
	private Integer[] performSearch(String query) {
		Integer[] matchId = new Integer[0];
		// non effettuare ricerche per testi < 3 caratteri
		if(query.length()>3) {
			// interroga l'indice
			WebClient client = WebClient.create(INDEX_API+"/search");
			client.query("q", query);
			client.getHeaders().add("Accept", "text/plain,text/html,application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			client.getHeaders().add("Content-Type", "text/plain");
			Response response = client.get();
			// qualche risultato?
			if(response.getStatus()==200) {
				try(InputStream is = (InputStream)response.getEntity()) {
					StringBuilder sb = new StringBuilder();
					int c;
					while((c = is.read()) != -1) {
						sb.append((char)c);
					}
					// recupera gli id che fanno match
					ObjectMapper om = new ObjectMapper();
					matchId = om.readValue(sb.toString(), Integer[].class);
				}
				catch(Exception e) {
					// in caso di errore non fare nulla ma restituisci risultato vuoto
				}
			}
		}
		
		return matchId;	
	}

	@Override
	public Response getCurrenYearItemDocumentStream(String tipo, int id, int idAll) {
		int anno = LocalDate.now().getYear();
		return getYearItemDocumentStream(tipo, anno, id, idAll);
	}

	@Override
	public Response getYearItemDocumentStream(String tipo, int anno, int id, int idAll) {
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
						String fileName = rs.getString("nomefile");
						byte[] fileData = rs.getBytes("contenuto");
						
						return Utils.downloadFile(fileName, fileData);
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
	@SecurityCheck(issuer = AuthService.ISSUER)
	public Response performBoardReindex() {
		// Invoca via SOAP il servizio di reindicizzazione...
		IndexSOAPImpService service = new IndexSOAPImpService();
		Index indexSOAP = service.getIndexSOAPImpPort();
		indexSOAP.reindex();
		
		return Response.ok().build();
	}
}
