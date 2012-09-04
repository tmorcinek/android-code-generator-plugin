package com.morcinek.android.codegenerator.logic;

public interface ActivityGeneratorInterface {

	public String generate();
	
	public String getImports();
	public String getPackage();
	public String getHeader();
	public String getFields();
	public String getConstants();
	public String getDeclaration();
	
	public String getCreateMethod();
	public String getMethods();
}
