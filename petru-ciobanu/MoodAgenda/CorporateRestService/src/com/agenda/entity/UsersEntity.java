package com.agenda.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsersEntity {
 private String id_utilisateur;
 private String utilisateur_libelle;
 private String utilisateur_password;
 private String utilisateur_nom;
 private String utilisateur_prenom;
 
/* public UsersEntity(){
	 
 }
 public UsersEntity(String userId, String userName, String userPassword) {
		this.id_utilisateur = userId;
		this.utilisateur_prenom = userName;
		this.utilisateur_password = userPassword;
	}*/
 
public String getId_utilisateur() {
	return id_utilisateur;
}
public void setId_utilisateur(String id_utilisateur) {
	this.id_utilisateur = id_utilisateur;
}
public String getUtilisateur_libelle() {
	return utilisateur_libelle;
}
public void setUtilisateur_libelle(String utilisateur_libelle) {
	this.utilisateur_libelle = utilisateur_libelle;
}
public String getUtilisateur_password() {
	return utilisateur_password;
}
public void setUtilisateur_password(String utilisateur_password) {
	this.utilisateur_password = utilisateur_password;
}
public String getUtilisateur_nom() {
	return utilisateur_nom;
}
public void setUtilisateur_nom(String utilisateur_nom) {
	this.utilisateur_nom = utilisateur_nom;
}
public String getUtilisateur_prenom() {
	return utilisateur_prenom;
}
public void setUtilisateur_prenom(String utilisateur_prenom) {
	this.utilisateur_prenom = utilisateur_prenom;
}
}
