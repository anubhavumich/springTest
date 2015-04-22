package net.anubhavs.springtest.model;

public class SearchModes {
	int countBasic;
	int countAdvanced;
	int countCommandLine;
	
	
	public SearchModes(){
		countBasic = 0;
		countAdvanced = 0;
		countCommandLine = 0;
	}
	
	
	public SearchModes(int basic, int advanced, int command){
		countBasic = basic;
		countAdvanced = advanced;
		countCommandLine = command;
	}
	
	public int getCountBasic(){
		return countBasic;
	}
	
	public int getCountAdvanced(){
		return countAdvanced;
	}
	
	public int getCountCommandLine(){
		return countCommandLine;
	}
}
