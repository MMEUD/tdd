package com.mwt.adcom.common.system.tasks.dbsync;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tools.ant.BuildException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mwt.adcom.utils.Util;
import com.mwt.common.framework.dao.io.ImportHelper;
import com.mwt.common.framework.dao.metadata.TableMetaData;
import com.mwt.common.system.servers.config.PropertiesLoader;
import com.mwt.common.system.tasks.GenericTask;
import com.mwt.common.utils.FileUtils;
import com.mwt.common.utils.TaskUtils;

public class DbSync extends GenericTask {
    
	private String build = null;	
	
	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	@SuppressWarnings("unchecked")
	public void execute() throws BuildException {

    	setupProperties();
    	PropertiesLoader pl = PropertiesLoader.getInstance();
    	this.setDriver(pl.get("connection.driver"));
		this.setUrl(pl.get("connection.url"));
		this.setUser(pl.get("connection.username"));
		this.setPassword(pl.get("connection.password"));
    	
    	String error = "";
		if (openConnection()) {
			SAXReader reader = null;
			Document document = null;
			String syncPath = pl.getServerPath() + pl.get("project") + File.separator + "system" + File.separator + "temp" + File.separator + "sync";
			File syncXmlFile = new File(syncPath + File.separator + "sync.xml"); 
			File writerFile = new File(syncPath + File.separator + "writer.txt"); 
			if (syncXmlFile.exists()) {
				try {
		        	reader = new SAXReader();  
		        	document = reader.read(syncXmlFile);
		
		        
		        	List<Element> theList = document.selectNodes("//sync/*");
		        	for(int i = 0; i < theList.size(); i++) {
		        		Element elem = (Element)theList.get(i);
		        		if (elem.matches("//table")){
		        			String tableName = elem.attributeValue("name");					
							String[] operations = elem.attributeValue("operation").split("\\,");
							
							String ddlPath = syncPath + File.separator + tableName + ".ddl";
							String existentDDL = null;
							String expectedDDL = null;
							try {
					    		TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), tableName);
								existentDDL = ImportHelper.getTempTableDDL(tableName, tmd.getColumns(), tmd.getPkFieldsNames(), true).toString();
							} catch (Exception e) {						
							}
							try {
								expectedDDL = FileUtils.getContents(new File(ddlPath));
							} catch (Exception e) {						
							}
							
							if (existentDDL == null) {
								TaskUtils.executeUpdate(conn, expectedDDL);
								try {
						    		TableMetaData tmd = new TableMetaData(conn, pl.get("connection.schema"), tableName);
									existentDDL = ImportHelper.getTempTableDDL(tableName, tmd.getColumns(), tmd.getPkFieldsNames(), true).toString();
								} catch (Exception e) {						
								}
							}
		
		
							import_table(conn, syncPath, tableName, expectedDDL, operations);
							log("table " + tableName + " " + elem.attributeValue("operation"));
		        		}
		        		
		        	
		        	}
		        	TaskUtils.executeUpdate(conn, "alter table lien_responsable_site change code_responsable code_responsable varchar(12)");	
		        	TaskUtils.executeUpdate(conn, "alter table responsable change code_responsable code_responsable varchar(12)");
		        	TaskUtils.executeUpdate(conn, "update lien_responsable_site set code_responsable = concat('1',lpad(code_enseigne ,5, '00000'),lpad(code_responsable,5, '00000'))");
		        	TaskUtils.executeUpdate(conn, "update responsable set code_responsable = concat('1',lpad(code_enseigne ,5, '00000'),lpad(code_responsable,5, '00000'))");
		        	
		        	ArrayList<HashMap<String, Object>> newusers = TaskUtils.select(conn, "select distinct z.*, case when (y.code_country is null or code_country in ("+ 
		        			pl.get("pay_francophone.ids")+")) then 'fr' else 'en' end as language "+
					" from lien_responsable_site lrs "+
					" join site y on y.code_site = lrs.code_site and lrs.code_enseigne = y.code_enseigne"+
					" join ("+
					" select x.* from (select r.code_responsable as id , r.code_enseigne,substr((md5(replace(concat(coalesce(lower(r.prenom_responsable),''),coalesce(lower(r.nom_responsable),'')),' ',''))),1,8) as password,"+
								  		 " prenom_responsable, nom_responsable,poste, telephone, fonction, 'true' as activ,date_modification, concat(replace(substr(concat(lower(case when strcmp(coalesce(prenom_responsable,''),'')=0 then 'm' else"+ 
								  		 " substr(prenom_responsable,1,1) end), '.', coalesce(nom_responsable,'')),1,10),' ',''),substr(md5(code_responsable),1,5)) as login"+
								  		 " from responsable r)x"+
					" left join user u on u.id_user = x.id"+
					" where u.id_user is null"+
					" )as z on lrs.code_responsable =  z.id");
			  		
					
					
		        	TaskUtils.executeUpdate(conn, "insert low_priority into user  (id_user, code_enseigne, password, firstname, lastname, mail, phone, function, activ, date_modif, login)"+
			  		" select x.* from (select r.code_responsable as id , r.code_enseigne,substr((md5(replace(concat(coalesce(lower(r.prenom_responsable),''),coalesce(lower(r.nom_responsable),'')),' ',''))),1,8) as password," +
			  		" prenom_responsable, nom_responsable,poste, telephone, fonction, 'true',date_modification, concat(replace(substr(concat(lower(case when strcmp(coalesce(prenom_responsable,''),'')=0 then 'm' else "+
			  		" substr(prenom_responsable,1,1) end), '.', coalesce(nom_responsable,'')),1,10),' ',''),substr(md5(code_responsable),1,5)) as login"+
			  		" from responsable r)x"+
			  		" left join user u on u.id_user = x.id"+
			  		" where u.id_user is null");
		        	TaskUtils.executeUpdate(conn, "delete user u from user u "+
									" left outer join responsable r on u.id_user = r.code_responsable "+
									" where r.code_responsable is null and u.id_user<>-1");
		        	TaskUtils.executeUpdate(conn, "update user u inner join (select r.* from responsable r)x on x.code_responsable = u.id_user set u.lastname = x.nom_responsable,"+
		        			" u.firstname = x.prenom_responsable, u.phone = x.telephone, u.function = x.fonction, u.mail = x.poste, u.date_modif=x.date_modification"+
  		                    " where u.date_modif<x.date_modification");
		        	
		        	
	  		          
	  		         if(pl.get("mail.newusers").equals("true")){ 
  		             sendMailsForNewUsers(conn, newusers);        
	  		         }
  		           TaskUtils.executeUpdate(conn, "insert into user_profilebo "+
					" select u.id_user, case when u.code_enseigne in ("+pl.get("idShopi")+") then 4 else 2 end as id_profilebo from user u"+
					" left join user_profilebo ubo on u.id_user = ubo.id_user  where  ubo.id_user is null and u.id_user<>-1");
					
  		         TaskUtils.executeUpdate(conn, "insert into user_profilefo "+
							" select u.id_user, 1 from user u"+
							" left join user_profilefo ubo on u.id_user = ubo.id_user  where  ubo.id_user is null and u.id_user<>-1");
							
				} catch (DocumentException e) {
					e.printStackTrace();
					error = e.getMessage();
				} finally {
					
					closeConnection();
				}
				log("DbSync OK");  

			}
			if (writerFile.exists()) {
				String text = FileUtils.getContents(writerFile).trim();
				log(text);
				
			}
		}
		
		
    }
    
    private void sendMailsForNewUsers(Connection conn, 
			ArrayList<HashMap<String, Object>> newusers) {
    	 //send mail with username/password to the new user created in AdCom
    	ConcurrentHashMap rb = null;
    	Iterator<HashMap<String, Object>> it = newusers.iterator();
    	
    	while (it.hasNext()){
    		HashMap<String, Object> newuser = it.next();
    		PropertiesLoader pm = PropertiesLoader.getInstance();
    		rb = (ConcurrentHashMap)pm.getLanguage((String)newuser.get("language"));
    		
    		String userEmail = (String)newuser.get("poste");
    		String username = (String)newuser.get("login");
    		String password = (String)newuser.get("password");
    		String idenseigne = ""+newuser.get("code_enseigne");
    		
    		TaskUtils.executeUpdate(conn, "update user set language='"+newuser.get("language")+"' where id_user="+newuser.get("id"));
    		
    		if(userEmail!= null && !userEmail.equals("")) {
    	          String mailSubject = (String)rb.get("mailnewuser.subject");
    	          String mailSubjectAdmin = (String)rb.get("mailnewuser.subjectAdmin");
    	          String imgPath = pm.get("mail.img");
    	          String mailMessage;
    	          String mailMessageAdmin;

    	          if(!Util.strTokenizer(pm.get("idShopi"), ",").contains(idenseigne)){ //for Shopi the email is differnet
    	              mailMessage = Util.genMailNewUser(username, password, (userEmail==null)?"":userEmail, imgPath, false, rb);
    	              mailMessageAdmin = Util.genMailNewUser(username, password, (userEmail==null)?"":userEmail, imgPath, true, rb);
    	          } else {
    	          	
    	              mailMessage = Util.genMailNewUserShopi(username, password, (userEmail==null)?"":userEmail, imgPath, false, rb);
    	              mailMessageAdmin = Util.genMailNewUserShopi(username, password, (userEmail==null)?"":userEmail, imgPath, true, rb);
    	          }

    	          Util.sendMail(userEmail, mailSubject, mailMessage, Util.getMailFrom(), "html", Util.getMailFromName());
    	          Util.sendMail(Util.getMailTo(idenseigne), mailSubjectAdmin, mailMessageAdmin, Util.getMailFrom(), "html", Util.getMailFromName());
    	        } else {
    	          // send mail to administrator - "for this user can not be create username and password bacause there is no email address"
    	          String mailSubject = (String)rb.get("mailnewuser.subjectNoAccount");
    	          String mailMessage = (String)rb.get("mailnewuser.text1") + " " + newuser.get("prenom_responsable") + " " + newuser.get("nom_responsable") + " " + rb.get("mailnewuser.text2");
    	          Util.sendMail(Util.getMailTo(idenseigne), mailSubject, mailMessage, Util.getMailFrom(), "html", Util.getMailFromName());
    	        }
    	}
        

		
	}

	private void import_table(Connection conn, String syncPath, String tableName, String ddl, String[] operations) {
    	PropertiesLoader pl = PropertiesLoader.getInstance();
    	HashMap<String, String> sql = new HashMap<String, String>();
    	
    	try {
    		sql.put(tableName + "_drop", "drop table x_" + tableName);
    		sql.put(tableName + "_clean", "delete from x_" + tableName);
    		sql.put(tableName + "_test", "select count(*) as cnt from x_" + tableName);
    		
    		TableMetaData metadata = new TableMetaData(conn, pl.get("connection.schema"), tableName);
    		sql.put(tableName + "_create", ddl.replaceAll("CREATE TABLE ", "CREATE TABLE x_").replaceAll("CONSTRAINT PK_", "CONSTRAINT PK_x_"));
    		sql.put(tableName + "_insert", ImportHelper.getInsertQueryDB2(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());
    		sql.put(tableName + "_update", ImportHelper.getUpdateQueryMysql(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());
    		sql.put(tableName + "_delete", ImportHelper.getDeleteQueryDB2(tableName, "x_" + tableName, metadata.getPkFieldsNames(), metadata.getColumnNames()).toString());

    		//System.out.println(sql.get(tableName + "_insert"));
    		//System.out.println(sql.get(tableName + "_update"));
    		
    		// create table temp or clean it
    		boolean failed = false;
    		Statement stm = null;
    		try {
    			stm = conn.createStatement();
    			stm.execute(sql.get(tableName + "_test"));
    			stm.close();
    		} catch (SQLException e) { 
    			failed = true;
    		} finally {
    			try {
    				if (stm != null) {
    					stm.close();
    				}
    			} catch (SQLException e2) {
    			}			
    		}
    		if (failed) {
    			TaskUtils.executeUpdate(conn, sql.get(tableName + "_create"));
        	} else {
        		TaskUtils.executeUpdate(conn, sql.get(tableName + "_clean"));
        	}
    	
        	// import csv
        	CsvImport csvImport = new CsvImport(this, metadata, syncPath + File.separator + tableName + ".csv", tableName + ".csv", tableName);
        	csvImport.process();
        	
        	// operations
        	for (int i=0; i < operations.length; i++) {
        		String operation = operations[i];
        		if ("clean".equals(operation)) {
        			TaskUtils.executeUpdate(conn, "delete from " + tableName);
        		}
        		if ("insert".equals(operation)) {
        			TaskUtils.executeUpdate(conn, sql.get(tableName + "_insert"));
        		}
        		if ("update".equals(operation)) {
        			TaskUtils.executeUpdate(conn, sql.get(tableName + "_insert"));
        			if (metadata.getNonPKColumns().size() > 0) {
        				TaskUtils.executeUpdate(conn, sql.get(tableName + "_update"));
        			}
        		}
        	}
        	
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
}

