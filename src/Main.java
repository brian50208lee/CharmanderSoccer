
public class Main {

	public static void main(String[] args) {
		//start GUI and gameThread
		GUI gui=new GUI();
		GameThread gameThread=new GameThread();
		
		gui.setGameThread(gameThread);
		gameThread.setGUI(gui);
		gameThread.stardThread();
	}

}
