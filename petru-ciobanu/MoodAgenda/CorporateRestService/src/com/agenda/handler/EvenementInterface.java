package com.agenda.handler;

import com.agenda.entity.EventEntity;

public interface EvenementInterface {
   EventEntity  sync_new_events(String username , String sync);
   EventEntity  sync_update_id(String username,String id_evenement,String evenement_id_android,String sync,String where_sync);
   EventEntity  sync_update_events(String username,String sync);
   EventEntity  sync_insert_events(String username , String evenement_libelle,String evenement_id_android,String evenement_sync);
}
