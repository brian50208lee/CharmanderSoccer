
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.IntPredicate;

public class Client extends Thread{
	//socket state
	static public final int LINK_WAIT=0;
	static public final int LINK_TRUE=1;
	static public final int LINK_FALSE=2;
	
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Datagram datagram;
	private Datagram cliPacket;
	private GUI gui;
	private String ip;
	private GameThread gameThread;
	public int link;
	Client(GUI gui) {
		//set gui
		this.gui = gui;
	}
	public void setDatagram(Datagram datagram){
		//set game data
		this.datagram=datagram;
		this.cliPacket=datagram;
		cliPacket.initialGame();
	}
	public void setGameThread(GameThread gameThread){
		//set game main thread
		this.gameThread=gameThread;
	}
	public void setIP(String ip){
		//set connect ip
		this.ip=ip;
	}
	
		
	private void getConnection(){
		//try to get connetion
		try {
			link=LINK_WAIT;
			socket = new Socket(ip, 5555);
			link=LINK_TRUE;
			gui.lbMessage.setText("The server and client have connection!\n\n");
			output = new ObjectOutputStream(socket.getOutputStream());
			input= new ObjectInputStream(socket.getInputStream());
		} catch (Exception ex) {
			link=LINK_FALSE;
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			gui.lbMessage.setText("You don't have the connection!\n");
		}
	}
	
	@Override
	public void run(){
		//client task
		getConnection();
		recObject();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
	}
	private void sendObject(){
		//sent data to server
		try {
			output.writeObject(datagram);
			output.flush();
			output.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void recObject(){
		//receive data from server
		try {
			while (gameThread.socket!=GameThread.SOCKET_NULL) {
				Datagram obj=(Datagram)input.readObject();
				objectHandling(obj);
				sendObject();
			}
		} catch (IOException e) {
			try {socket.close();link=LINK_FALSE;} catch (IOException e1) {}
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Datagram getPacket(){
		//return game data
		return cliPacket;
	}
	
	
	private void objectHandling(Datagram obj) {
		//hangle data
		cliPacket=obj;
	}

}
