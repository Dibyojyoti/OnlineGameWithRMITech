package impl;

import java.rmi.Remote;
import java.rmi.RemoteException;

//This is the remote interface need to be implemented by remote object
public interface JavaRMIflyInterface extends Remote {

	/**methods to get message and FLy coordinate*/
	public String getMsg() throws RemoteException;
	public int getFlyX() throws RemoteException;
	public int getFlyY() throws RemoteException;
    /**methods to get player name,score and pointer*/
    public String getPlayer1Name()  throws RemoteException;
    public String getPlayer2Name()  throws RemoteException;
    public String getPlayer3Name()  throws RemoteException;
    public String getPlayer1Score()  throws RemoteException;
    public String getPlayer2Score()  throws RemoteException;
    public String getPlayer3Score()  throws RemoteException;
    public int getPLayer1PointerX()  throws RemoteException;
    public int getPLayer1PointerY()  throws RemoteException;
    public int getPLayer2PointerX()  throws RemoteException;
    public int getPLayer2PointerY()  throws RemoteException;
    public int getPLayer3PointerX()  throws RemoteException;
    public int getPLayer3PointerY()  throws RemoteException;
    /**methods to get player login and start status*/
    public boolean getPLayer1LoginStatus()  throws RemoteException;
    public boolean getPLayer1StartStatus()  throws RemoteException;
    public boolean getPLayer2LoginStatus()  throws RemoteException;
    public boolean getPLayer2StartStatus()  throws RemoteException;
    public boolean getPLayer3LoginStatus()  throws RemoteException;
    public boolean getPLayer3StartStatus()  throws RemoteException;
    /**methods to login logout and send own pointer coordinate*/ 
    public String logIn(String Name) throws RemoteException;
    public void huntFly(String s)  throws RemoteException;
    public void logOut(String Name) throws RemoteException;
    public void sendPlayerPointer(String Name,int x, int y)  throws RemoteException;
    /**check if at least 2 player exist at any point of time to start,continue game*/
    public int playingCondition()  throws RemoteException;
    public void sendStartCount(String s)  throws RemoteException;
    /** The name that will be used in the RMI registry service to register Remote Object */
	public final static String LOOKUPNAME = "FlyObject";
}

