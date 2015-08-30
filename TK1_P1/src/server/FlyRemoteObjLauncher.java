package server;
import impl.*;

import java.net.InetAddress;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

//This is the loader of the remote object which also changes the coordinate and the direction of the remote object using timer
public class FlyRemoteObjLauncher {
	
    private static FlyRemoteObj Fly;
    
    private static String thisAddress;
    private static int thisPort;
    private static Registry registry;

	public FlyRemoteObjLauncher() {
		getFly();										//Creates the instance of remote object 
	}

	private void getFly() {								//Method to set color and create the remote object instance
		try {
			Fly = new FlyRemoteObj();					//Remote object instance
		} catch (RemoteException e) {
			System.out.println("RemoteException while trying to build remote object");
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {							//Main method of launcher
		new FlyRemoteObjLauncher();
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager( ));			//Sets security manager
		}
		try {
			System.out.println("JavaRMIServer starting...");
			//Naming.rebind(JavaRMIflyInterface.LOOKUPNAME, Fly);			//Binds remote object to registry //needs to b changed
			thisAddress= (InetAddress.getLocalHost()).toString();
			thisPort=3232;
			System.out.println(thisAddress+" "+thisPort);
			registry  = LocateRegistry.createRegistry( thisPort );
			registry.rebind(JavaRMIflyInterface.LOOKUPNAME, Fly);
			
			System.out.println("JavaRMIServer ready.");
		}catch (Exception e) {
			 System.out.println("Exception while trying to connect the server");
				e.printStackTrace() ;	
		}
	}
}

