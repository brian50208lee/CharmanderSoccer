
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
	//socket state
	static public final int LINK_WAIT=0;
	static public final int LINK_TRUE=1;
	static public final int LINK_FALSE=2;
	
	private ServerSocket server;
	private Socket socket;
	private ObjectInputStream input ;
	private ObjectOutputStream output;
	private Datagram datagram;
	private Datagram serPacket;
	private GUI gui;
	private GameThread gameThread;
	public int link;
	Server(GUI gui) {
		//set gui
		this.gui = gui;
	}
	public void setGameThread(GameThread gameThread){
		//set game main thread
		this.gameThread=gameThread;
	}
	public void setDatagram(Datagram datagram){
		//set game data
		this.datagram=datagram;
		this.serPacket=datagram;
		serPacket.initialGame();
	}
		
	private  void waitConnection(){
		//wait to cennect
		try {
			//----------waitting for connection----------
			server = new ServerSocket(5555);
			server.setSoTimeout(10000);
			gui.lbMessage.setText("Waiting for connection 10 second ...\n");
			link=LINK_WAIT;
			socket = server.accept();
			link=LINK_TRUE;
			input= new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			//----------when getConnect start server thread----------
			Datagram obj=new Datagram();
			gui.lbMessage.setText("The server and client have connection!\n\n");
			output.writeObject(obj);
		} catch (Exception ex) {
			link=LINK_FALSE;
			try {
				server.close();
				//socket.close();
			} 
			catch (IOException e) {}
			gui.lbMessage.setText("You don't have the connection!\n");
		}
	}
	
	@Override
	public void run(){
		//server task
		waitConnection();
		recObject();
		try {
			server.close();
			socket.close();
		} 
		catch (IOException e) {}
	}
	private  void sendObject(){
		//send data to client
		try {
				output.writeObject(datagram);
				output.flush();
				output.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private  void recObject(){
		//receive object from client
		try {
			while (gameThread.socket!=GameThread.SOCKET_NULL) {
				Datagram obj=(Datagram)input.readObject();
				objectHandling(obj);
				sleep(1000/120);
				sendObject();
			}
		} catch (IOException e) {
			try {server.close();socket.close();link=LINK_FALSE;} catch (IOException e1) {}
			gui.lbMessage.setText("You don't have the connection!\n");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Datagram getPacket(){
		//return game data
		return serPacket;
	}	


	private void objectHandling(Datagram obj) {
		//handle data
		serPacket=obj;
	}
}
