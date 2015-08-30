package server;
import impl.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import javax.swing.Timer;
// This is the Remote object which gives the coordinates of the fly and stores and gives player names and scores and status
// To be available for remote connection should implement the remote object reference (JavaRMIflyInterface) and
// extend UnicastRemoteObject

public class FlyRemoteObj extends UnicastRemoteObject implements JavaRMIflyInterface {

	private static final int D_HEIGHT 		= 400;
    private static final int D_WIDTH 		= 800;
    private static final int INCREMENT 		= 8;
    //player names
    private static String PlayerName1		= "No Player1";
    private static String PlayerName2		= "No Player2";
    private static String PlayerName3		= "No Player3";
    //messages to players
    private static String PlayerMsg			= "";
    //player scores
    private static int Player1Score 		= 0;
    private static int Player2Score 		= 0;
    private static int Player3Score 		= 0;
    //player pointer coordinates
    private static int Player1PointerX 		= 0;
    private static int Player1PointerY 		= 0;
    private static int Player2PointerX 		= 0;
    private static int Player2PointerY 		= 0;
    private static int Player3PointerX 		= 0;
    private static int Player3PointerY 		= 0;
    //player count
    private static int playerCounter 		= 0;
    //player login and start status
    private static boolean player1StartFlag = false;
    private static boolean player2StartFlag = false;
    private static boolean player3StartFlag = false;
    private static boolean player1LoginFlag = false;
    private static boolean player2LoginFlag = false;
    private static boolean player3LoginFlag = false;
    //X and Y coordinate of the FLy object
    private int randXLoc					= 0;						
    private int randYLoc					= 0;
    //variables to control Fly movement
    private boolean godown 					= false;
    private boolean goLeft					= false;
    private int randUpDown					= 0;
    private int randLeftRight				= 0;
    private Random random1;
    //timers to change coordinate and direction
    private Timer timerCoordinate 			= null;
    private Timer timerDirection 			= null;
    //latch for updating score
    //private boolean Mutex 					= false;
    
    //*********constructor*****************// 
	public FlyRemoteObj() throws RemoteException {
        super();
        random1 = new Random();											//initialize random object
        
		timerCoordinate = new Timer(100, new ActionListener() {			//Timer to change the coordinate of the remote object
	        public void actionPerformed(ActionEvent e) {
	        	if((player1StartFlag==true && player2StartFlag==true) ||	//start changing coordinate if any two player started game 
	        	   (player2StartFlag==true && player3StartFlag==true) ||
	        	   (player1StartFlag==true && player3StartFlag==true) ){
	        			moveFly();
	        	}
	        }
	    });
	    timerDirection = new Timer(700, new ActionListener() {			//Timer to change the direction of the remote object
	        public void actionPerformed(ActionEvent e) {
	        	if((player1StartFlag==true && player2StartFlag==true) ||	//start changing coordinate if any two player started game
	 	           (player2StartFlag==true && player3StartFlag==true) ||
	 	           (player1StartFlag==true && player3StartFlag==true) ){
	        			setFlyDirection();
	        	}
	        }
	    });
    	timerDirection.start();											//start's both timers
        timerCoordinate.start();
        
        setFlyAxis();													//initialize Fly Coordinate
	}
   
	//*********This method changes the coordinates of the Fly*********/
    public void moveFly() {
    	if(randYLoc > D_HEIGHT || randYLoc < 0 || randXLoc > D_WIDTH || randXLoc < 0 ) {
    		setFlyAxis();setFlyDirection();								//if Fly flees out of window reset coordinate and direction
    	}
        if (godown) {
            if(goLeft){            	randYLoc += INCREMENT;                    randXLoc -= INCREMENT;            }
            else      {        		randYLoc += INCREMENT;                    randXLoc += INCREMENT;            }
         } 
         else {
            if(goLeft){            	randYLoc -= INCREMENT;                    randXLoc -= INCREMENT;            }
            else      {            	randYLoc -= INCREMENT;                    randXLoc += INCREMENT;            }
         }
    }
    
    //*********This method changes the moving direction of the Fly*********/
    public void setFlyDirection(){
    	randUpDown    = random1.nextInt(D_HEIGHT);
        randLeftRight = random1.nextInt(D_WIDTH);
    	if (randUpDown    >=250) { godown=true; }   else { godown=false; }
    	if (randLeftRight >=400) { goLeft=true; }	else { goLeft=false; }
    }
    
    //*********This method sets the coordinates of the object first time and every time its relocated*********/
    public void setFlyAxis(){
        randXLoc = random1.nextInt(D_WIDTH);
        randYLoc = random1.nextInt(D_HEIGHT);
    }
    
    //**********************************Methods to control player********************************************/
    
    //*********This method***********************************************************************************/ 
    //*    					sets the name of the player at first when player connects************************/
    //*						increases player counter, sets flag if any player is logged in*******************/ 
    //*						sends status and other message to players****************************************/
    //*						auto start 3rd player if 2 players playing***************************************/ 	
    public String logIn(String s){
    	
    	String reply="OK";							  //and checks if player with same name exist or if all player slots full	
    	
    	if(PlayerName1.equals("No Player1")) { 		  //if slot for 1st player is empty and does not match with same name as other player
    		if(!s.equals(PlayerName2) && !s.equals(PlayerName3)){
    			PlayerName1 		= s;
    			player1LoginFlag 	= true;
        		playerCounter		= playerCounter + 1;
    		}
    		else{
    			reply 				= "Player exist with same name";
    		}
    	}
    	else if(PlayerName2.equals("No Player2")) {   //if slot for 2nd player is empty and does not match with same name as other player
    		if(!s.equals(PlayerName1)&& !s.equals(PlayerName3)){
    			PlayerName2 		= s; 
    			player2LoginFlag 	= true;
        		playerCounter		= playerCounter + 1;
    		}
    		else{
    			reply 				=  "Player exist with same name";
    		}
    	}
    	else if(PlayerName3.equals("No Player3")) {   //if slot for 3rd player is empty and does not match with same name as other player
    		if(!s.equals(PlayerName1)&& !s.equals(PlayerName2)){
    			PlayerName3 		= s; 
    			player3LoginFlag 	= true;
        		playerCounter		= playerCounter + 1;
    		}
    		else{
    			reply 				=  "Player exist with same name";
    		}
    	}
    	else { reply 				=  "Already 3 players playing, No space"; };   //if all slots full
    	
    	//*****************************************************/
    	//**if only one player is logged in send wait message**/
    	if (playerCounter==1){
    		PlayerMsg 				= "wait for atleast another player to start game";
    	}
    	//**if two player is logged in send message that game can be started**/
    	if (playerCounter==2){
    		PlayerMsg 				= "atlest two player found you can start game";
    	}
    	
    	//*****************************************************/
    	//**Auto start logic -when two players playing and 3rd logged in auto start 3rd player's game even if he does not start himself **/
    	if(player1StartFlag == true && player2StartFlag == true){
    		if(player3LoginFlag==true){
    			player3StartFlag 	= true;
    			PlayerMsg 			= PlayerName3 + " Auto started, as two players already playing";
    		}
    	}
    	else if(player1StartFlag == true && player3StartFlag == true){
    		if(player2LoginFlag==true){
    			player2StartFlag 	= true;
    			PlayerMsg 			= PlayerName2 + " Auto started, as two players already playing";
    		}
    	}
    	else if(player2StartFlag == true && player3StartFlag == true){
    		if(player1LoginFlag==true){
    			player1StartFlag 	= true;
    			PlayerMsg 			= PlayerName1 + " Auto started, as two players already playing";
    		}
    	}

    	return reply;
    }
    //*********This method gives score to player**************/
    public synchronized void huntFly(String playerName){						
    	//if(Mutex==false){ Mutex = true;}
    	//while(Mutex){
    		if(PlayerName1.equals(playerName))		{ Player1Score = Player1Score + 1; }
    		else if(PlayerName2.equals(playerName))	{ Player2Score = Player2Score + 1; }
    		else if(PlayerName3.equals(playerName))	{ Player3Score = Player3Score + 1; }
    		
    		PlayerMsg 				= playerName +  " Hunted Fly";
    		
    		setFlyAxis();setFlyDirection();						//reset Fly
    		//Mutex=false;
    	//}
    }
    //*********This method reset player info in case player logout********/
    public void logOut(String playerName){						

    	if(PlayerName1.equals(playerName)){ 
    		Player1Score 		= 0; 
    		Player1PointerX		= 0;
    		Player1PointerY		= 0;
    		playerCounter		= playerCounter - 1;
			player1LoginFlag 	= false;
    		player1StartFlag 	= false;
        	PlayerMsg 			= PlayerName1 + " Loged out";
    		PlayerName1 		= "No Player1";
    	}
		else if(PlayerName2.equals(playerName)){ 
			Player2Score 		= 0;
			Player2PointerX		= 0;
			Player2PointerY		= 0;
    		playerCounter		= playerCounter - 1;
    		player2LoginFlag 	= false;
    		player2StartFlag 	= false;
        	PlayerMsg 			= PlayerName2 + " Loged out";
			PlayerName2 		= "No Player2";
    	}
		else if(PlayerName3.equals(playerName)){ 
			Player3Score 		= 0;
			Player3PointerX		= 0;
			Player3PointerY		= 0;
    		playerCounter		= playerCounter - 1;
    		player3LoginFlag 	= false;
    		player3StartFlag 	= false;
        	PlayerMsg 			= PlayerName3 + " Loged out";
			PlayerName3 		= "No Player3";
    	}
    	//***if only one player logged in reset game by reseting all players score and ask player to wait 
    	if (playerCounter==1){
    		PlayerMsg 			= "wait for atleast another player to start game again";
    		Player1Score 		= 0;
    		Player2Score 		= 0;
    		Player3Score 		= 0;
    	}
    }
    //*******This method sends a players pointer coordinate to server*******/ 
    public void sendPlayerPointer(String playerName, int xLoc, int yLoc){	
    	if(PlayerName1.equals(playerName)){ 		Player1PointerX = xLoc; Player1PointerY = yLoc;}
		else if(PlayerName2.equals(playerName)){ 	Player2PointerX = xLoc; Player2PointerY = yLoc;}
		else if(PlayerName3.equals(playerName)){ 	Player3PointerX = xLoc; Player3PointerY = yLoc;}
    }
    //*******This method handles game start status of each player and Auto start 3rd player if other two already started***/ 
    public void sendStartCount(String s){
    	
    	//**set start flag of the player if he has not started game already and send message ***/
    	if(s.equals(PlayerName1) && player1StartFlag == false){
    		player1StartFlag 	= true;
        	PlayerMsg 			= PlayerName1 + " started game";
    	}
    	else if(s.equals(PlayerName2) && player2StartFlag == false){
    		player2StartFlag 	= true;
        	PlayerMsg 			= PlayerName2 + " started game";
    	}
    	else if(s.equals(PlayerName3) && player3StartFlag == false){
    		player3StartFlag 	= true;
        	PlayerMsg 			= PlayerName3 + " started game";
    	}
    	
    	//Auto start logic - when two players started game and 3rd logged in but not started game auto start 3rd player's game**/
    	if(player1StartFlag == true && player2StartFlag == true){
    		if(player3LoginFlag == true){
    			player3StartFlag 		= true;
    			PlayerMsg 				= PlayerName3 + " Auto started, as two players already playing";
    		}
    	}
    	else if(player1StartFlag == true && player3StartFlag == true){
    		if(player2LoginFlag == true){
    			player2StartFlag 		= true;
    			PlayerMsg 				= PlayerName2 + " Auto started, as two players already playing";
    		}
    	}
    	else if(player2StartFlag == true && player3StartFlag == true){
    		if(player1LoginFlag == true){
    			player1StartFlag 		= true;
    			PlayerMsg 				= PlayerName1 + " Auto started, as two players already playing";
    		}
    	}
    }
    //*********This method sends warning Message to player*********/
    public String getMsg(){ 				return PlayerMsg;    }
    //*********This methods gives X,Y coordinate of Fly to player*********/
    public int getFlyX(){ 	    			return randXLoc;     }
    public int getFlyY(){     				return randYLoc;     }
    //*********These methods gives the name of the players to all players*********/    
    public String getPlayer1Name(){        	return PlayerName1;    }
    public String getPlayer2Name(){        	return PlayerName2;    }
    public String getPlayer3Name(){        	return PlayerName3;    }
    //*********These methods gives the score of players to all players*********/    
    public String getPlayer1Score(){        return Integer.toString(Player1Score);    }
    public String getPlayer2Score(){        return Integer.toString(Player2Score);    }
    public String getPlayer3Score(){        return Integer.toString(Player3Score);    }
    //*********These methods gives the pointer position of players to all players*********/
    public int getPLayer1PointerX(){ 		return Player1PointerX;}
    public int getPLayer1PointerY(){ 		return Player1PointerY;}
    public int getPLayer2PointerX(){ 		return Player2PointerX;}
    public int getPLayer2PointerY(){ 		return Player2PointerY;}
    public int getPLayer3PointerX(){ 		return Player3PointerX;}
    public int getPLayer3PointerY(){ 		return Player3PointerY;}
    //*********These methods gives login and game start status of players to all players*********/
    public boolean getPLayer1StartStatus(){ return player1StartFlag;   }
    public boolean getPLayer2StartStatus(){ return player2StartFlag;   }
    public boolean getPLayer3StartStatus(){ return player3StartFlag;   }
    public boolean getPLayer1LoginStatus(){ return player1LoginFlag;   }
    public boolean getPLayer2LoginStatus(){ return player2LoginFlag;   }
    public boolean getPLayer3LoginStatus(){ return player3LoginFlag;   }
    //*********This method gives player count to all players*********/
    public int playingCondition(){ return playerCounter;}
}

