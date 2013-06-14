package com.agenda.resource;

import java.sql.SQLException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.agenda.dao.EvenementDao;
import com.agenda.dao.LoginDao;
import com.agenda.entity.EventEntity;
import com.agenda.entity.UsersEntity;

@Path("/")
public class RestEndpoint {
	@GET
	@Produces(MediaType.TEXT_XML)
	public String respondAsReady() {
		return "Agenda service is ready!";
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public UsersEntity postLogin(@FormParam("username") String username, @FormParam("password") String password) {
		LoginDao loginDao = new LoginDao();
		UsersEntity userEntity = null;
		try {
			userEntity = loginDao.loginAction(username, password);
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
		}
		return userEntity;
	}

	@POST
	@Path("/sync_new_events")
	@Produces(MediaType.APPLICATION_JSON)
	public EventEntity sync_new_events(@FormParam("username") String username, @FormParam("sync") String sync) {
		EvenementDao evenementDao = new EvenementDao();
		EventEntity eventEntity = null;
		try {
			eventEntity = evenementDao.actionGetEvents(username, "0");
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
		}
		return eventEntity;
	}

	@POST
	@Path("/sync_update_id")
	@Produces(MediaType.APPLICATION_JSON)
	public String sync_update_id(@FormParam("username") String username, @FormParam("id_evenement") String id_evenement,
			@FormParam("id_evenement_android") String id_evenement_android, @FormParam("evenement_sync") String sync, @FormParam("where_sync") String where_sync) {
		EvenementDao syncDao = new EvenementDao();
		String eventEntity = null;
		try {
			eventEntity = syncDao.actionReceiveId(username, id_evenement, id_evenement_android, sync, where_sync);
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
		}
		return eventEntity;
	}

	@POST
	@Path("/sync_update_events")
	@Produces(MediaType.APPLICATION_JSON)
	public EventEntity sync_update_events(@FormParam("username") String username, @FormParam("sync") String sync) {
		EvenementDao syncDao = new EvenementDao();
		EventEntity eventEntity = null;
		try {
			eventEntity = syncDao.actionUpdateEvents(username, sync);
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
		}
		return eventEntity;
	}

	@POST
	@Path("/sync_insert_events")
	@Produces(MediaType.APPLICATION_JSON)
	public EventEntity sync_insert_events(@FormParam("username")String username , @FormParam("evenement_libelle")String evenement_libelle,@FormParam("evenement_id_android")String evenement_id_android,@FormParam("evenement_sync")String evenement_sync) {
		EvenementDao syncDao = new EvenementDao();
		EventEntity eventEntity = null;
		try {
			eventEntity = syncDao.actionInsertEvents(username ,evenement_libelle,evenement_id_android,evenement_sync);
		} catch (SQLException e) {
			System.out.println("Error" + e.toString());
		}
		return eventEntity;
	}
	
	
	
    

}
