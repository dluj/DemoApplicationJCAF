package dk.itu.jcafdemo;

import java.rmi.RemoteException;
import java.util.List;

import dk.itu.jcafdemo.entity.BlipEntity;
import dk.itu.jcafdemo.entity.BlipLocation;
import dk.itu.jcafdemo.entity.Room;
import dk.itu.jcafdemo.relationship.Arrived;
import dk.itu.jcafdemo.relationship.Attending;
import dk.itu.jcafdemo.relationship.Left;
import dk.pervasive.jcaf.util.AbstractMonitor;


public class ContextTester extends AbstractMonitor {

	final BlipLocation blip_location = new BlipLocation();

	final Arrived arrived = new Arrived(this.getClass().getName());
	final Left left = new Left(this.getClass().getName());
	final Attending attending = new Attending();

	public ContextTester(String serviceUri) throws RemoteException {
		super(serviceUri);
		//		run();
	}

	@Override
	public void run() {		
		try {
			System.out.println("Server info: \n   " + getContextService().getServerInfo());
			//            getContextService().addEntity(be);
			/*
			 * get devices from location 
			 */
			blip_location.refreshDevices();
			List<BlipEntity> new_devices = blip_location.getNewDevices();
			for(int i=0;i< new_devices.size();i++){
				getContextService().addEntity(new_devices.get(i));
				getContextService().addContextItem(new_devices.get(i).getId(), arrived, new Room(blip_location.getLocation()));
			}
			List<BlipEntity> old_devices = blip_location.getOldDevices(); 
			for(int i=0;i< old_devices.size();i++){
				getContextService().addContextItem(old_devices.get(i).getId(), left, new Room(blip_location.getLocation()));
				getContextService().removeEntity(old_devices.get(i));
			}
			Thread.currentThread();
			Thread.sleep(5000);


		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e){
			System.err.println("Thread error --> " + e.toString());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try{
			ContextTester ct = new ContextTester("rmi://10.25.231.246/spct");
			Thread t = new Thread(ct);
		}catch(RemoteException e){
			System.err.println(e.toString() + "**************");
			e.printStackTrace();
		}
	}

	@Override
	public void monitor(String arg0) throws RemoteException {
		// TODO Auto-generated method stub

	}
}
