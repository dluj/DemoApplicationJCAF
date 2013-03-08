package dk.itu.jcafdemo.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.entity.GenericEntity;

public class BlipLocation extends GenericEntity {

	private List<BlipEntity> terminals = new ArrayList<BlipEntity>();
	private List<BlipEntity> new_terminals = new ArrayList<BlipEntity>();
	private List<BlipEntity> deleted_terminals = new ArrayList<BlipEntity>();
	
	private String location;
	private String url_string = "http://pit.itu.dk:7331/terminals-in/";
	
	public BlipLocation(){
		super("daniel@itu.dk");
		location = "itu.zone0.zonedorsyd";
	}
	
	public BlipLocation(String location){
		super();
		this.location = location;
	}
	
	public String getLocation(){
		return location;
	}
	//get all devices in area and add to the list the ones that are new, and delete the ones that are not anymore
	public void refreshDevices(){
		URL url;
		HttpURLConnection conn;
		InputStreamReader isr;
		BufferedReader rd;
		unUseTerminals();
		try {
			url = new URL(url_string+location);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			isr = new InputStreamReader(conn.getInputStream());
			rd = new BufferedReader(isr);
			JSONArray jterminals = (JSONArray)JSONValue.parse(isr);
			JSONObject aux;
			String terminal_id;
			for(int i=0; i< jterminals.size();i++){
				aux = (JSONObject)jterminals.get(i);
				terminal_id = aux.get("terminal-id").toString();
				if(newTerminal(terminal_id)){
					this.terminals.add(new BlipEntity(terminal_id, location));
					new_terminals.add(new BlipEntity(terminal_id, location));
				}
				aux = null;
				terminal_id = null;
			}
			rd.close();
			deleteUnused();
		} catch (Exception e) {
			System.out.println("BLIP "+ e.toString() + "\n ************** \n");
			e.printStackTrace();
		}
	}
	
	//Delete unused terminals, meaning they left the area
	public void deleteUnused(){
		for(int i=0;i<terminals.size();i++){
			if(!terminals.get(i).inUse()){
				deleted_terminals.add(terminals.get(i));
				terminals.remove(i);
				i = i-1;
			}
		}
	}
	
	//set false to property inUse for each terminal to know which terminals are still in area
	private void unUseTerminals(){
		for(int i=0;i<terminals.size();i++){
			terminals.get(i).setUse(false);
		}
		new_terminals = new ArrayList<BlipEntity>();
		deleted_terminals = new ArrayList<BlipEntity>();
	}
	
	//true if terminal is not in actual list of terminals in area
	private Boolean newTerminal(String terminal_id){
		for(int i=0;i<terminals.size();i++){
			if(terminals.get(i).getId().equals(terminal_id)){
				terminals.get(i).setUse(true);
				return false;
			}
		}
		return true;
	}
	
	//get new devices in location
	public List<BlipEntity> getNewDevices(){
		return new_terminals;
	}
	
	//get old devices in location
	public List<BlipEntity> getOldDevices(){
		return deleted_terminals;
	}
	
	@Override
	public void contextChanged(ContextEvent event) {
		System.out.println("Something changed --> BLIP Location: "+ location);
	}
	@Override
	public String getEntityInfo() {
		return "BlipLocation location -> "+location;
	}

}
