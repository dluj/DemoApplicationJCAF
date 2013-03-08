package dk.itu.jcafdemo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import dk.itu.jcafdemo.entity.BlipEntity;
import dk.itu.jcafdemo.entity.BlipLocation;
import dk.itu.jcafdemo.entity.Room;
import dk.itu.jcafdemo.entity.Visitor;
import dk.itu.jcafdemo.item.Presentation;
import dk.itu.jcafdemo.relationship.*;
import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.EntityListener;
import dk.pervasive.jcaf.impl.RemoteEntityListenerImpl;
import dk.pervasive.jcaf.util.AbstractContextClient;


public class ContextTester extends AbstractContextClient {

	private RemoteEntityListenerImpl listener1;
//	private RemoteEntityListenerImpl listener2;

    final Visitor visitor1 = new Visitor("visitor1@itu.dk", "Visitor 1");
//    final Visitor visitor2 = new Visitor("visitor2@itu.dk", "Visitor 2");
    final BlipEntity blip = new BlipEntity();
    final BlipLocation blip_location = new BlipLocation();
    
//    final Room room1 = new Room("room1@itu.dk", 2, 'C', 10);
//    final Room itu_room_5c = new Room("itu.zone5.zone5c", 5, 'C',1);
    
	final Arrived arrived = new Arrived(this.getClass().getName());
	final Left left = new Left(this.getClass().getName());

	final Attending attending = new Attending();
	
	public ContextTester(String serviceUri) {
		super(serviceUri);

		try {
			listener1 = new RemoteEntityListenerImpl(); //comp 1 || display 1
			listener1.addEntityListener(new EntityListener() {
				
				@Override
				public void contextChanged(ContextEvent event) {
					System.out.println("Listener1: " + event.toString());
				}
			});			
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		run();
	}

	@Override
	public void run() {		
		try {
            System.out.println("Server info: \n   " + getContextService().getServerInfo());
//        	getContextService().addEntityListener(listener1,"visitor1@itu.dk");
//            getContextService().addEntity(blip_location);
//        	getContextService().addEntityListener(listener1, blip_location.getId());
        	while(true){
        		/*
        		 * get devices from location 
        		 */
        		blip_location.refreshDevices();
        		List<BlipEntity> new_devices = blip_location.getNewDevices();
        		for(int i=0;i< new_devices.size();i++){
        			getContextService().addEntity(new_devices.get(i));
        			getContextService().addEntityListener(listener1,new_devices.get(i).getId());
                    getContextService().addContextItem(new_devices.get(i).getId(), arrived, new Room(blip_location.getLocation()));
        		}
        		List<BlipEntity> old_devices = blip_location.getOldDevices(); 
        		for(int i=0;i< old_devices.size();i++){
//        			getContextService().addEntity(old_devices.get(i));
                    getContextService().addContextItem(old_devices.get(i).getId(), left, new Room(blip_location.getLocation()));
                    getContextService().removeEntityListener(listener1,old_devices.get(i).getId());
//        			getContextService().removeEntity(old_devices.get(i));
        		}
        		Thread.currentThread();
				Thread.sleep(1000);
        	}
        	
		} catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e){
        	System.err.println("Thread error --> " + e.toString());
        	e.printStackTrace();
        }

	}

	public static void main(String[] args) {
		new ContextTester("rmi://10.25.231.246/spct");
	}
}
