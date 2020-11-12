import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Basic extends JFrame{
	private JPanel jContentPane;
	private PanelBall panel;  // Panel of the game class 
	private String result;
	Networks s; // Networks object ( can be server or client )
	private boolean isServer; // flag for server
	/* Constructor */
	public Basic()  
	{
		super();
		result = JOptionPane.showInputDialog("Server or client?");
		
		if (result.equalsIgnoreCase("server"))
		{
			s = new Networks();
			isServer = true;
		}
		else if (result.equalsIgnoreCase("client"))
		{
			String ip;
			ip = JOptionPane.showInputDialog("ip?");
			s = new Networks(ip);
			isServer = false;
		}
		else
		{
			throw new IllegalArgumentException("Expected client or server, got " + result);
		}
		
        s.start(); // start Networks object to begin communicating
		initializeScreen(); // render the game interface
		
        // Listeners for the keyboard
        this.addKeyListener(new KeyAdapter() { 
            // Key release event listener
            public void keyReleased(KeyEvent evt) {
                myKeyReleased(evt);
            }
            // Key press event listener
            public void keyPressed(KeyEvent evt) {
                myKeyPressed(evt);
            }
        });
        //Listeners for the MouseMotion
        this.addMouseMotionListener(new MouseAdapter() {
        			public void mouseMoved(MouseEvent e) {
        				panel.mouseMoved(e);
        			}
        		});
	}
	private PanelBall getPanel() {
		if (panel == null) {
			panel = new PanelBall(s, isServer);  // Create a new game
		}
		return panel;
	}
    //	Send the key pressed to the game class
	private void myKeyPressed(KeyEvent e)
    {
        panel.keyPressed(e);
    }
    //	Send the key released to the game class
    private void myKeyReleased(KeyEvent e)
    {
        panel.keyReleased(e);
    }
    
    // Render the game interface
	private void initializeScreen() {
		this.setResizable(false);
		this.setBounds(new Rectangle(100, 184, 1000, 500)); // Position on the desktop
		this.setContentPane(getJContentPane());
		this.setTitle("Pong" + result);
	}

	private JPanel getJContentPane() 
	{
		if (jContentPane == null) 
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Basic thisClass = new Basic();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
}
