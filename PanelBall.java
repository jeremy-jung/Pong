import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class PanelBall extends JPanel implements Runnable {
    //Dimensions
    private int width, height; // Width and height of the ball
    private int paddleWidth = 10; int paddleLength = 50;
    private int ballX=10, ballY=110;        //initial position of the ball
    private int player1X=10, player1Y=0;  //initial position of player 1's paddle
    private int player2X=this.getWidth()-35, player2Y=0; //initial position of player 2's paddle
    
    // Scores
    private int player1Score=0, player2Score=0;
    
    // Flags or booleans
    private boolean player1UpFlag,player1DownFlag, player2UpFlag, player2DownFlag;
    private boolean movingRight;
    private boolean play, gameOver;
    private int winner = 0; // integer flag for winner
    private boolean isServer; //boolean checking if the game is run by a server
    private boolean movingUp=false;
    private boolean p1Ballspd, p2Ballspd;
    // special game events
    private int inv = 0; // integer flag for invisibility 1 if true 0 if false
    private boolean p2Inv, p1Inv; //flags for the invisibility special attack buttons
    private boolean p1Spd, p2Spd; //flags for the ball boost special attack buttons
    
    //variables
    Thread game;
    private Networks s;
    private String[] values;
    private String msg;
    private int stepSize = 10; // how many pixels to change for each movement
    private int ballstep = 5;
    private int ballsize = 8;
	private int winningScore = 30;
	/* Constructor */
    public PanelBall(Networks o, boolean isServer_) 
    {
    		s = o;
    		isServer = isServer_;
        play=true;             //game NOT over
        game=new Thread(this); //put PanelBall in a Thread
        game.start();          //start Thread
    }
    
    // Draw ball and paddles
    public void paintComponent(Graphics g)
    {
        setOpaque(false);
        super.paintComponent(g);
        
        // Draw ball
        g.setColor(Color.black);
        if (inv == 1)
        {
        		g.setColor(Color.WHITE);
        }
        g.fillOval(ballX, ballY, ballsize,ballsize);
    
        g.setColor(Color.BLUE);
        // Draw paddles
        g.fillRect(player1X, player1Y, paddleWidth, paddleLength);
        g.fillRect(player2X, player2Y, paddleWidth, paddleLength);
        
        //Draw scores
        g.setColor(Color.RED);
        g.drawString("Player1: " + player1Score, 25, 10);
        g.drawString("Player2: " + player2Score, 150, 10);

        if(gameOver)
            g.drawString("Game Over", 100, 125);
    }
    // Positions on X and Y for the ball
    public void positionBall (int nx, int ny)
    {
        ballX= nx; 
        ballY= ny; 
        this.width=this.getWidth();
        this.height=this.getHeight();
        repaint();
    }
    // Event listeners for moving paddle
    public void keyPressed(KeyEvent event)
    {
    	if (isServer)
    	{
        switch(event.getKeyCode())
        {
            // Move Player 1's Paddle
        case KeyEvent.VK_W:
        		player1UpFlag = true;
        		break;
        case KeyEvent.VK_S:
        		player1DownFlag = true;
    			break;
        case KeyEvent.VK_C:
        		p1Spd = true;
        		break;
        case KeyEvent.VK_X:
    			p1Inv = true;
    			break;
        }
    }
    else {
    switch(event.getKeyCode())
    			{
            // Move Player 2's Paddle  
        case KeyEvent.VK_UP:
    			s.send("P2UP");
    			break;
        case KeyEvent.VK_DOWN:
    			s.send("P2DOWN");
			break;
        case KeyEvent.VK_C:
    			s.send("P2SPD");
    			break;
        case KeyEvent.VK_X:
    			s.send("P2INV");
    			break;
		
    			}
        }
    }
    
    
    // Event listeners for stopping movement of paddle
    public void keyReleased(KeyEvent event)
    {
       	if(isServer)
    	{
        switch(event.getKeyCode())
        {
            // Move Player 1's Paddle
        case KeyEvent.VK_W:
        	player1UpFlag = false;
        		break;
        case KeyEvent.VK_S:
        	player1DownFlag = false;
    			break;
        case KeyEvent.VK_C:
    			p1Spd = false;
    			break;
        case KeyEvent.VK_X:
    			p1Inv = false;
    			break;
        }
    }
    else {
    switch(event.getKeyCode())
    			{
            // Move Player 2's Paddle  
        case KeyEvent.VK_UP:
    			s.send("!P2UP");
    			break;
        case KeyEvent.VK_DOWN:
    			s.send("!P2DOWN");
			break;
        case KeyEvent.VK_C:
			s.send("!P2SPD");;
			break;
        case KeyEvent.VK_X:
        		s.send("!P2INV");
        		break;

    			}
        }
    }
    // move paddle scripts
    public void movePlayers()
    {
 		 	if(player1Y+paddleLength <= this.getHeight() && player1Y-1 >= 0)
 	    	{
 	    		if (player1UpFlag)
 	    		{
 	    			player1Y -= stepSize;
 	    			repaint();
 	    		}
 	    		if (player1DownFlag)
 	    		{
 	    			player1Y += stepSize;
 	    			repaint();
 	    		}
 	    	}
 	    	else
 	    	{
 	    		if(player1Y+paddleLength > this.getHeight())
 	    	 	{
 	    	 		player1Y = this.getHeight()-paddleLength;
 	    	 		repaint();
 	    	 	}
 	    	 	else if(player1Y-1 <= 0)
 	    	 	{
 	    	 		player1Y = 1;
 	    	 		repaint();
 	    	 	}
 	    	}
 		  if(player2Y+paddleLength <= this.getHeight() && player2Y-1 >= 0)
 	    	{
 	    		if (player2UpFlag)
 			{
 				player2Y -= stepSize;
 				repaint();
 			}
 			if (player2DownFlag)
 			{
 				player2Y += stepSize;
 				repaint();
 			}
 	    	}
 	     	else 
 	    	{
 	    	 	if(player2Y+paddleLength> this.getHeight())
 	    	 	{
 	    	 		player2Y =  this.getHeight()-(paddleLength);
 	    	 		repaint();
 	    	 	}
 	    	 	else if(player2Y-1 <= 0)
 	    	 	{
 	    	 		player2Y = 1;
 	    	 		repaint();
 	    	 	}
 	    	}
 		  repaint();
 	   }
    public void run()    
    {
        while(true)  //infinite loop
        {
        	player2X = this.getWidth()-20;
        	player1X = 10;
            if(play)
            {
            	if (isServer)
            	{
                s.send(ballX + "," + ballY + "," + player1Y + "," + player2Y + "," + player1Score + "," + player2Score + "," // 0-5
                		+ winner + "," + ballstep + "," + inv); //6-  
                analyzeMsg();    
                moveBall();
                positionBall(ballX, ballY);
            // Delay for 50 milliseconds
            try 
            {
                Thread.sleep(10);
            }
            catch(InterruptedException ex)
            {
            }
            movePlayers();

            // Increase the score of the player 2 
            if ( ballX <= 0) 
            	{
            		ballRandom();
            		ballReini();
            		player2Score++;
            		
            	}
            if ( ballX+8 >= this.getWidth())
            {
            		ballRandom();
            		ballReini();
            		player1Score++;
            }
            // Game over. 
            // When the score reaches the value, the game will end
            if(player1Score==winningScore || player2Score==winningScore){
                play=false;
                gameOver=true;
                winner = 1;
            }           
            // The ball collides with the player 1
            if(ballX<=player1X+paddleWidth && ballY>=player1Y && ballY<=(player1Y+paddleLength))
            {
            		movingRight=true;
            		if (p1Spd)
            		{
            			ballstep = ballstep + 5;
            		}
            		else if (p1Inv)
            		{
            			inv = 1;
            		}
            }
            // The ball collides with the player 2
           
            if(ballX>=(player2X-ballsize) && ballY>=player2Y && ballY<=(player2Y+paddleLength))
            {
                movingRight=false;
                if (p2Spd)
        			{
        			ballstep = ballstep + 5;
        			}
                else if (p2Inv)
                {
                		inv = 1;
                }
            	}
           
            }
            	else
            	{
            		// s.send(ballX + "," + ballY + "," + player1Y + "," + player2Y + "," + player1Score + "," + player2Score);
            		msg = s.recv();
            		if (msg == "")
            		{
            			
            		}
            		else {
            		values = msg.split(",");
            		ballX = Integer.parseInt(values[0]);
            		ballY = Integer.parseInt(values[1]);
            		player1Y = Integer.parseInt(values[2]);
            		player2Y = Integer.parseInt(values[3]);
            		player1Score = Integer.parseInt(values[4]);
            		player2Score = Integer.parseInt(values[5]);
            		ballstep = Integer.parseInt(values[7]);
            		inv = Integer.parseInt(values[8]);
            		if (Integer.parseInt(values[6])==0)
            		{
            			
            		}
            		else if (Integer.parseInt(values[6])==1)
            		{
                        play=false;
                        gameOver=true;
            		}
            		positionBall(ballX, ballY);
            		movePlayers();
            		try 
					{
						Thread.sleep(10);
					}
					catch(InterruptedException ex)
					{
					}
            		}
            	}
         }
        }
    }
	 public void moveBall() {
     	if (movingRight) 
         {
             ballX += ballstep;
             if (ballX >= (width - ballsize)) movingRight = false;
         }
         else
         {
             ballX += -ballstep;
             if ( ballX <= 0) movingRight = true;
         }
     	if (movingUp) 
         {
             ballY += ballstep;
             if (ballY >= (height - ballsize)) {
             	movingUp= false;     
             }
         }
         else
         {
             ballY += -ballstep;
             if ( ballY <= 0) {
             	movingUp =  true;
             }
         }
     }
	public void mouseMoved(MouseEvent e) {
		if (isServer)
		{
			player1Y = e.getY()- (paddleLength/2);
		}
		else
		{
			player2Y = e.getY() - (paddleLength/2);
			//sends the new Y position by mouse of the player 2 paddle to the server 
			s.send("newY:" + player2Y);
			System.out.println("newY");
		}
	}
	public void ballReini()
	{
		inv = 0;
		ballstep = 5;
	}
	public void ballRandom()
	{
		ballX = (player2X-player1X)/2;
	}
	public void analyzeMsg()
	{
		 msg = s.recv();
         System.out.println(msg);
         if (msg.equals("P2UP")){
         		player2UpFlag = true;
         }
         else if (msg.equals("P2DOWN")){
         		player2DownFlag = true;
         }
         else if (msg.equals("!P2DOWN")){
         		player2DownFlag = false;
         }
         else if (msg.equals("!P2UP")){
         		player2UpFlag = false;
         }
         else if (msg.contains("newY:"))
         {
         	System.out.println("newY");
         		String value[] = msg.split(":");
         		player2Y = Integer.parseInt(value[1]);
         }
         //checks if the extra speed hit for player 2 is true
         else if(msg.equals("P2SPD"))
         {
         		p2Spd = true;
         }
       //checks if the extra speed hit for player 2 is not true
         else if (msg.equals("!P2SPD")) {
         		p2Spd = false;
         }
         else if (msg.equals("P2INV"))
         {
        	 		p2Inv = true;
         }
         else if (msg.equals("!P2INV"))
         {
        	 		p2Inv = false;
         }
	}
}
