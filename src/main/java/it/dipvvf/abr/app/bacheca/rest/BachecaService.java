package it.dipvvf.abr.app.bacheca.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.dipvvf.abr.app.bacheca.model.Allegato;
import it.dipvvf.abr.app.bacheca.model.Elenco;
import it.dipvvf.abr.app.bacheca.support.Database;
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
			try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS cnt FROM elenco e WHERE e.tipo = ?")) {
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
					"SELECT e.id FROM elenco AS e WHERE (e.tipo = ?) AND (e.data_pubblicazione BETWEEN ? AND ?) ORDER BY e.numero DESC")) {
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
					"SELECT e.* FROM elenco AS e WHERE (e.tipo = ?) AND (e.data_pubblicazione BETWEEN ? AND ?) AND e.id = ?")) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Elenco e = new Elenco();
						e.setId(rs.getInt("id"));
						e.setNumero(rs.getInt("numero"));
						e.setProprietario(rs.getString("proprietario"));
						e.setTipo(rs.getString("tipo"));
						e.setTitolo(rs.getString("titolo"));
						e.setUfficio(rs.getString("ufficio"));
						e.setDataPubblicazione(rs.getDate("data_pubblicazione"));

						return Response.ok(e).build();
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
			
			String sql = "SELECT a.id FROM allegato AS a JOIN elenco e ON a.id_elenco = e.id WHERE (e.tipo = ?) AND "+
						 "(e.data_pubblicazione BETWEEN ? AND ?) AND (e.id = ?) ORDER BY a.ordine";
			
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
	public Response getYearItemDocumentDetail(String tipo, int anno, int id, int idDoc) {
		try (Connection con = Database.getInstance().getConnection()) {
			LocalDate firstDay = LocalDate.of(anno, 1, 1);
			LocalDate lastDay = LocalDate.of(anno, 12, 31);
			
			String sql = "SELECT a.* FROM allegato AS a JOIN elenco e ON a.id_elenco = e.id WHERE (e.tipo = ?) AND "+
						 "(e.data_pubblicazione BETWEEN ? AND ?) AND (e.id = ?) AND (a.id = ?)";
			
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, tipo);
				ps.setObject(2, firstDay);
				ps.setObject(3, lastDay);
				ps.setInt(4, id);
				ps.setInt(5, idDoc);
				
				try (ResultSet rs = ps.executeQuery()) {					
					if(rs.next()) {
						Allegato a = new Allegato();
						a.setId(rs.getInt("id"));
						a.setNomefile(rs.getString("nomefile"));
						a.setDimensione(rs.getInt("dimensione"));
						a.setDescrizione(rs.getString("descrizione"));
						a.setOrdine(rs.getInt("ordine"));
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
	
	
}
