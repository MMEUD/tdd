package com.laura.testemood.command;

public class Context {
	

private final static String GENERAL_NAMESPACE = "general";

private String namespace ;
private String[] args ;



public String[] getArgs() {
	return args;
}
public void setArgs(String[] args) {
	this.args = args;
}
public Context() {
	this.namespace = GENERAL_NAMESPACE;
}
public String getNamespace() {
	return namespace;
}
public void setNamespace(String namespace) {
	this.namespace = namespace;
}


}
