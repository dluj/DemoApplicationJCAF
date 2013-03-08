package dk.itu.jcafdemo.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.entity.Person;

public class BlipEntity extends Person{

	//id in person == bluetooth
//	private static String bluetooth = "000ea50050b4";
	private String location;
	private Boolean location_changed = false;
	private Boolean inUse = true;

	public BlipEntity(){
		super("000ea50050b4","blip tracker");
//		setLocation();
	}
	
	public BlipEntity(String bluetooth, String location){
		super(bluetooth,"BlipEntity");
		this.location = location;
	}

//	public String getBt(){
//		return bluetooth;
//	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Boolean locationChanged(String location){
		return this.location.equals(location);
	}
	
	public void setUse(Boolean use){
		inUse = use;
	}
	
	public Boolean inUse(){
		return inUse;
	}
	
	@Override
	public void contextChanged(ContextEvent event) {
		System.out.println("Something changed --> BLIP Entity: "+ location);
	}
	@Override
	public String getEntityInfo() {
		return "BlipEntity entity";
	}
}
