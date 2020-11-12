import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Networks {
	private String ip;
	private int port = 5001;
	private PrintWriter out;
	private BufferedReader in;
	private Socket client;
	private ServerSocket serv;
	private boolean isServer;
	// Networks was constructed as a server 
	public Networks()
	{
		System.out.println("Constructing server Networks object");
		isServer = true;
	}
	
	// Networks was constructed as a client 
	public Networks(String ipAddress)
	{
		ip = ipAddress;
		isServer = false;
	}
	
	public String getInfo()
	{
		String s = client.toString() + "\n" + serv.toString();
		return s;
	}
	
	public void start()
	{
		if (isServer)
		{
			System.out.println("Starting Server");
			try {
				serv = new ServerSocket(port);
				client = serv.accept();
				
				out =
						new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(
						new InputStreamReader(client.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				client = new Socket(ip, port);

				out =
						new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(
						new InputStreamReader(client.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String recv()
	{
		try {
			if (in.ready())
			{
				String s = in.readLine();
				//System.out.println(s);
				return s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void send(String msg)
	{
		out.println(msg);
	}

	public boolean getIsServer()
	{
		return isServer;
	}

}
