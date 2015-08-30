package client;
import impl.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


//**************This is the Client program for player screen**********/  
public class FlyPlayerClient extends JPanel{

	//Dimension of window
	private  final int D_HEIGHT 	= 500;
    private  final int D_WIDTH 		= 800;
    //Reference of remote object interface for client 
    private   JavaRMIflyInterface FlyClient;
    private   String myName			= " ";
    private   JFrame frame;
    WindowListener Wadpt;
    //Depending on this Timer the coordinate of the object and and player info is updated periodically 
    private Timer timerClient;
    private Timer timerServerMsg;
    //players own mouse coordinates
    private int MyPointerX = 0;
    private int MyPointerY = 0;
    //getting message after connection is made to sever
    String ConnectionStatus="";
    //labels to show player name and score and status
    private JLabel Player1Name;
    private JLabel Player1NameLabel;
    private JLabel Player1Score;
    private JLabel Player1ScoreLabel;
    private JLabel Player1LoginStatus;
    private JLabel Player1LoginStatusLabel;
    private JLabel Player1StartStatus;
    private JLabel Player1StartStatusLabel;

    private JLabel Player2Name;
    private JLabel Player2NameLabel;
    private JLabel Player2Score;
    private JLabel Player2ScoreLabel;
    private JLabel Player2LoginStatus;
    private JLabel Player2LoginStatusLabel;
    private JLabel Player2StartStatus;
    private JLabel Player2StartStatusLabel;

    private JLabel Player3Name;
    private JLabel Player3NameLabel;
    private JLabel Player3Score;
    private JLabel Player3ScoreLabel;
    private JLabel Player3LoginStatus;
    private JLabel Player3LoginStatusLabel;
    private JLabel Player3StartStatus;
    private JLabel Player3StartStatusLabel;
    //labels to show message from server 
    private JLabel msgFromServer;
    private JLabel Msg;
    //Fly component
    private ImageShowingComponent ImgSC;
    //other two players mouse pointer component
    private playerPointer otherPlayer1Pointer;
    private playerPointer otherPlayer2Pointer;
    //panels
    JPanel panellTop;
    JPanel panelCenter;
    JPanel panelBottom;
    JPanel panelPlayer1;
    JPanel panelPlayer2;
    JPanel panelPlayer3;
    JPanel panelMsg;
    
        
    public FlyPlayerClient(String userName, String IPAddress) {
         
        //Panel for start and close button
        setTopPanel();
        //Middle Panel for playing
        setMiddlePanel();
        //Panel for players info
        setBottomPanel();
        
        setLayout(new BorderLayout());
        add(panellTop, BorderLayout.PAGE_START);
        add(panelCenter,BorderLayout.CENTER);
        add(panelBottom,BorderLayout.PAGE_END);
        
        //set the player name from login screen  
  		myName = userName;
  		
		//Setting security manager,Searching registry to get handle of remote object, and sending client name 
		try {
 			System.setSecurityManager(new SecurityManager( ));
 			
 			//FlyClient = (JavaRMIflyInterface) Naming.lookup("rmi://127.0.0.1/" + JavaRMIflyInterface.LOOKUPNAME); //needs 2 b chngd
 			
 			String serverPort = "3232";
 			//String serverAddress="10.168.111.215";
 			String serverAddress= IPAddress;
 			Registry registry=LocateRegistry.getRegistry(serverAddress,(new Integer(serverPort)).intValue());
 			FlyClient = (JavaRMIflyInterface) registry.lookup(JavaRMIflyInterface.LOOKUPNAME);
 			
 			//get connection by logging in
 			ConnectionStatus = FlyClient.logIn(myName);
		} catch (Exception e) {
			System.out.println("Exception while login: " + e.getMessage());
			e.printStackTrace();
		}
		//timer to send mouse coordinate after player presses start button to play
 	    timerClient 	= new Timer(50, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	
	            getMyPointer();														//get mouse coordinate
	            try {
					FlyClient.sendPlayerPointer(myName,MyPointerX,MyPointerY);		//send mouse coordinate
				} catch (RemoteException e1) {
					System.out.println("Exception while sending mouse coordinate: " + e1.getMessage());
					e1.printStackTrace();
				}
	        	repaint();
	        }
	    });
 	    //timer to retrieve player name, score and status 
 	    timerServerMsg 	= new Timer(50, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	repaint();
	        }
	    });
 	    timerServerMsg.start();
	}
//******This function gets the info of remote Fly, player info by calling the methods of remote object******/
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        	 try{
        			 Msg.setText(FlyClient.getMsg());										//shows messages and warning
        			 Player1Name.setText(FlyClient.getPlayer1Name());						//shows player name and score
        			 Player2Name.setText(FlyClient.getPlayer2Name());
        			 Player3Name.setText(FlyClient.getPlayer3Name());
        			 Player1Score.setText(FlyClient.getPlayer1Score());
        			 Player2Score.setText(FlyClient.getPlayer2Score());
        			 Player3Score.setText(FlyClient.getPlayer3Score());
        			 
        			 //show login and start status of all players in board
        			 showAllStatus();

        			 //Auto start self timer to send own mouse coordinate if server auto started player's game(when other players playing)
        			 if(FlyClient.getPLayer1StartStatus()==true &&
        				FlyClient.getPLayer2StartStatus()==true &&	 
        				FlyClient.getPLayer3StartStatus()==true   ){
        				 	
        				 	timerClient.start();
        			 }

        			 //shows other player pointer
        			 showOthersPointer();

        			 //shows the fly
           			 ImgSC.setBounds(FlyClient.getFlyX(), FlyClient.getFlyY(), 250, 250);	
        			 
        	 }  catch (Exception e) {
     			System.out.println("Exception in paintComponent method: " + e.getMessage());
    			e.printStackTrace();
    		}
    }	
	//*******sets size of client window*****/
	public Dimension getPreferredSize() {									
		return new Dimension(D_WIDTH, D_HEIGHT);
    }
	//*******send own pointer coordinate after adjusting according to main play screen,middle panel and mouse Jcomponent width and height
	public void getMyPointer(){
		
 		Point p1 = frame.getLocation();
 		PointerInfo a = MouseInfo.getPointerInfo();
 		Point p = a.getLocation();
 		MyPointerX = (int) (p.getX() - p1.getX() - 20 );
 		MyPointerY = (int) (p.getY() - p1.getY() - 36 - 40 );
   
	}
	//*******This method shows the main frame and check the message after login******/
//	public static void main(String[] args) {
    public void go(){

   	   if (ConnectionStatus.equals("OK")){
                frame = new JFrame(myName);
                frame.add(this);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);
           	 
                Wadpt = new WindowAdapter(){
      	 	   public void windowClosing(java.awt.event.WindowEvent e) {
      	           try {
					  FlyClient.logOut(myName);
				   } catch (RemoteException e1) {
					   System.out.println("Exception while trying to login by player: ");
					  e1.printStackTrace();
				   }
      	         }
      	       };
      	       frame.addWindowListener(Wadpt);
       }
   	   else if(ConnectionStatus.equals("Player exist with same name")){
   		JOptionPane.showMessageDialog(null, "Player exist with same name");
   	   }
   	   else if(ConnectionStatus.equals("Already 3 players playing, No space")){
   		JOptionPane.showMessageDialog(null, "Already 3 players playing, No space");
   	   }
   	   else{
      		JOptionPane.showMessageDialog(null, "Connection probem, Server not ready");
   	   }
	}
    //********This method sets top panel in frame*********/
    private void setTopPanel(){
    	panellTop = new JPanel();
    	//Start Button
    	JButton startButton = new JButton("Start");
    	startButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			try {
    				FlyClient.sendStartCount(myName);				//send own start status
    			} catch (RemoteException e1) {
    				System.out.println("Exception while sending status of player: ");
    				e1.printStackTrace();
    			} 
    			timerClient.start();
    		}
    	});
	    //Close Button 
    	JButton resetButton = new JButton("Close");
    	resetButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			try {
    				FlyClient.logOut(myName);						//on close logout
    			} catch (RemoteException e1) {
    				System.out.println("Exception while trying to logout by player: ");
    				e1.printStackTrace();
    			}
    			frame.dispose();									//close window
    		}
    	});

    	panellTop.add(startButton);
    	panellTop.add(resetButton);
    }
    //********This method sets middle panel in frame*********/
    private void setMiddlePanel(){
        panelCenter 		= new JPanel();
        ImgSC 				= new ImageShowingComponent();
        otherPlayer1Pointer = new playerPointer();
        otherPlayer2Pointer = new playerPointer();
        panelCenter.setPreferredSize(new Dimension(400,800));
        panelCenter.setBackground(Color.blue);
        panelCenter.setLayout(null);
        panelCenter.setVisible(true);
        ImgSC.setVisible(true);
        otherPlayer1Pointer.setVisible(true);
        otherPlayer2Pointer.setVisible(true);
        panelCenter.add(ImgSC);
        panelCenter.add(otherPlayer1Pointer);
        panelCenter.add(otherPlayer2Pointer);

    }
    //********This method sets bottom panel in frame*********/
    private void setBottomPanel(){

    	Player1NameLabel 		= new JLabel("Player1: ");
    	Player1Name 			= new JLabel(" ");
        Player1ScoreLabel 		= new JLabel("Score: ");
        Player1Score 			= new JLabel("0");
        Player1LoginStatusLabel = new JLabel("Login Status: ");
        Player1LoginStatus 		= new JLabel("Not Loged in");
        Player1StartStatusLabel = new JLabel("Start Status: ");
        Player1StartStatus 		= new JLabel("Not Started");

    	Player2NameLabel 		= new JLabel("Player2: ");
    	Player2Name 			= new JLabel(" ");
        Player2ScoreLabel 		= new JLabel("Score: ");
        Player2Score 			= new JLabel("0");
        Player2LoginStatusLabel = new JLabel("Login Status: ");
        Player2LoginStatus 		= new JLabel("Not Loged in");
        Player2StartStatusLabel = new JLabel("Start Status: ");
        Player2StartStatus 		= new JLabel("Not Started");

    	Player3NameLabel 		= new JLabel("Player3: ");
    	Player3Name 			= new JLabel(" ");
        Player3ScoreLabel 		= new JLabel("Score: ");
        Player3Score 			= new JLabel("0");
        Player3LoginStatusLabel = new JLabel("Login Status: ");
        Player3LoginStatus 		= new JLabel("Not Loged in");
        Player3StartStatusLabel = new JLabel("Start Status: ");
        Player3StartStatus 		= new JLabel("Not Started");
        
        panelPlayer1 = new JPanel();
        panelPlayer1.setLayout(new GridLayout(1,8));
        panelPlayer1.add(Player1NameLabel);
        panelPlayer1.add(Player1Name);
        panelPlayer1.add(Player1ScoreLabel);
        panelPlayer1.add(Player1Score);
        panelPlayer1.add(Player1LoginStatusLabel);
        panelPlayer1.add(Player1LoginStatus);
        panelPlayer1.add(Player1StartStatusLabel);
        panelPlayer1.add(Player1StartStatus);
        
        panelPlayer2 = new JPanel();
        panelPlayer2.setLayout(new GridLayout(1,8));
        panelPlayer2.add(Player2NameLabel);
        panelPlayer2.add(Player2Name);
        panelPlayer2.add(Player2ScoreLabel);
        panelPlayer2.add(Player2Score);
        panelPlayer2.add(Player2LoginStatusLabel);
        panelPlayer2.add(Player2LoginStatus);
        panelPlayer2.add(Player2StartStatusLabel);
        panelPlayer2.add(Player2StartStatus);

        panelPlayer3 = new JPanel();
        panelPlayer3.setLayout(new GridLayout(1,8));
        panelPlayer3.add(Player3NameLabel);
        panelPlayer3.add(Player3Name);
        panelPlayer3.add(Player3ScoreLabel);
        panelPlayer3.add(Player3Score);
        panelPlayer3.add(Player3LoginStatusLabel);
        panelPlayer3.add(Player3LoginStatus);
        panelPlayer3.add(Player3StartStatusLabel);
        panelPlayer3.add(Player3StartStatus);

        msgFromServer = new JLabel("Message: ");
        Msg           = new JLabel(" ");
        Msg.setForeground(Color.RED);
        panelMsg = new JPanel();
        panelMsg.add(msgFromServer);
        panelMsg.add(Msg);
        
        panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(4,1));
        panelBottom.add(panelPlayer1);
        panelBottom.add(panelPlayer2);
        panelBottom.add(panelPlayer3);
        panelBottom.add(panelMsg);
   }
    //********This method shown all players login status and status of players whether they started game*********/    
    private void showAllStatus() throws RemoteException{
    	
		 if(FlyClient.getPLayer1LoginStatus()==true){			 Player1LoginStatus.setText("Loged in");		 }
		 else										{			 Player1LoginStatus.setText("Not Loged in");	 }
		 
		 if(FlyClient.getPLayer2LoginStatus()==true){			 Player2LoginStatus.setText("Loged in");		 }
		 else										{			 Player2LoginStatus.setText("Not Loged in");	 }
		 
		 if(FlyClient.getPLayer3LoginStatus()==true){			 Player3LoginStatus.setText("Loged in");		 }
		 else										{			 Player3LoginStatus.setText("Not Loged in");	 }
		 		 
		 if(FlyClient.getPLayer1StartStatus()==true){			 Player1StartStatus.setText("Started");			 }
		 else										{			 Player1StartStatus.setText("Not Started");		 }
		 
		 if(FlyClient.getPLayer2StartStatus()==true){			 Player2StartStatus.setText("Started");			 }
		 else										{			 Player2StartStatus.setText("Not Started");		 }
		 
		 if(FlyClient.getPLayer3StartStatus()==true){			 Player3StartStatus.setText("Started");			 }
		 else										{			 Player3StartStatus.setText("Not Started");		 }
    }
    //********This method shown all players pointers*********/    
    private void showOthersPointer() throws RemoteException{
    	
    	if(FlyClient.getPlayer1Name().equals(myName)){		//if I'm 1st player show pointer and name of player 2 & 3 if they started game*/
			if(FlyClient.getPLayer2StartStatus()==true){ 
				if(FlyClient.getPLayer2PointerX()!=0 || FlyClient.getPLayer2PointerY()!=0){
					otherPlayer1Pointer.setBounds(FlyClient.getPLayer2PointerX(), FlyClient.getPLayer2PointerY(), 100, 100);
					otherPlayer1Pointer.setLabel(FlyClient.getPlayer2Name());
				}
			}
			if(FlyClient.getPLayer3StartStatus()==true){
				if(FlyClient.getPLayer3PointerX()!=0 || FlyClient.getPLayer3PointerY()!=0){
					otherPlayer2Pointer.setBounds(FlyClient.getPLayer3PointerX(), FlyClient.getPLayer3PointerY(), 100, 100);
					otherPlayer2Pointer.setLabel(FlyClient.getPlayer3Name());
				}
			}
		 }//if I'm 2nd player show pointer and name of player 1 & 3 if they started game*/
		 else if(FlyClient.getPlayer2Name().equals(myName)){
				if(FlyClient.getPLayer1StartStatus()==true){
			   if(FlyClient.getPLayer1PointerX()!=0 || FlyClient.getPLayer1PointerY()!=0){
				   otherPlayer1Pointer.setBounds(FlyClient.getPLayer1PointerX(), FlyClient.getPLayer1PointerY(), 100, 100);
				   otherPlayer1Pointer.setLabel(FlyClient.getPlayer1Name());
			   }
				}
			if(FlyClient.getPLayer3StartStatus()==true){
			   if(FlyClient.getPLayer3PointerX()!=0 || FlyClient.getPLayer3PointerY()!=0){
				   otherPlayer2Pointer.setBounds(FlyClient.getPLayer3PointerX(), FlyClient.getPLayer3PointerY(), 100, 100);
				   otherPlayer2Pointer.setLabel(FlyClient.getPlayer3Name());
			   }
			}
			 }//if I'm 3rd player show pointer and name of player 1 & 2 if they started game*/
		 else if(FlyClient.getPlayer3Name().equals(myName)){
				if(FlyClient.getPLayer1StartStatus()==true){
			   if(FlyClient.getPLayer1PointerX()!=0 || FlyClient.getPLayer1PointerY()!=0){
				   otherPlayer1Pointer.setBounds(FlyClient.getPLayer1PointerX(), FlyClient.getPLayer1PointerY(), 100, 100);
				   otherPlayer1Pointer.setLabel(FlyClient.getPlayer1Name());
			   }
				}
			if(FlyClient.getPLayer2StartStatus()==true){
			   if(FlyClient.getPLayer2PointerX()!=0 || FlyClient.getPLayer2PointerY()!=0){
				   otherPlayer2Pointer.setBounds(FlyClient.getPLayer2PointerX(), FlyClient.getPLayer2PointerY(), 100, 100);
				   otherPlayer2Pointer.setLabel(FlyClient.getPlayer2Name());
			   }
			}
		 }
    }
    //***************JComponent for the FLy************/
    class ImageShowingComponent extends JComponent {

        private BufferedImage loadImg;
	  // The MouseListener that handles the click.
	    private MouseListener listener = new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		try {
	    			if(FlyClient.playingCondition()>1){	  
	    				FlyClient.huntFly(myName);
	    			}
	    		} catch (RemoteException e1) {
	    			System.out.println("Exception while hunting fly");  
	    			e1.printStackTrace();
	    		}
	    	}
	    };
	    
  	  ImageShowingComponent() {
  		  addMouseListener(listener);
  		  setLocation(10,10);
  		  // Load the image.
  		  try {
  			  loadImg = ImageIO.read(new File ("Fly.png"));
  		  } catch (IOException e) {
  			  System.out.println("Exception while loding Fly image: ");
  			  e.printStackTrace();
  		  }
	    
	  }
  	  public void paintComponent(Graphics g) {
	    g.drawImage(loadImg, 0, 0, null);
  	  }
	  public Dimension getPreferredSize() {
	    return new Dimension(loadImg.getWidth(), loadImg.getHeight());
	  }
	}
	//**************JComponent for other player mouse************/
    class playerPointer extends JComponent {

        private BufferedImage loadImg;		//mouse pointer image
        private String label;				//mouse pointer name

        playerPointer() {
        	setLocation(10,10);
        	// Load the image.
        	try {
        		loadImg = ImageIO.read(new File ("cursor1.jpg"));
        	} catch (IOException e) {
        		System.out.println("Exception while loading corsur image");
        		e.printStackTrace();
        	}
        }
        public void setLabel(String s){ 
        	label = s;
        }
        public void paintComponent(Graphics g) {
        	g.drawImage(loadImg, 0, 0, null);
        	g.setColor(Color.WHITE);
        	g.drawString(label, 10, 10);
	    
        }
        public Dimension getPreferredSize() {
        	return new Dimension(loadImg.getWidth(), loadImg.getHeight());
        }
	}

}

