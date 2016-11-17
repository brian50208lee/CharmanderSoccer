import java.awt.Label;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class GUI extends JFrame{
	//control dif
	static final int CONTROL_LEFT=0;
	static final int CONTROL_RIGHT=1;
	static final int CONTROL_1P=0;
	static final int CONTROL_2P=1;
	static final int CONTROL_CLIENT=0;
	static final int CONTROL_SERVER=1;
	
	
	Painter_menu painter_menu;
	Painter_game painter_game;
	GameThread gameThread;
	Label lbMessage;//for test

	
	
	public GUI() {
		//construct
		drawGUI();
	}
	public void setGameThread(GameThread gameThread){
		//set game main thread
		this.gameThread=gameThread;
	}
	public void drawGUI(){
		//draw GUI
		
		//frame
		setSize(650,510);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("src/img/game_icon/ic_launch.png").getImage());
		setTitle("ヒトカゲキックサッカー");
		setLocationRelativeTo(null);
		setResizable(false);

		//test label
		lbMessage=new Label();
		lbMessage.setVisible(false);
		lbMessage.setBounds(20,20,400,20);
		add(lbMessage);
		
		
		//done
		setVisible(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	public void chagePainter(int state){
		//change GUI painter
		switch (state) {
		case GameThread.STATE_MENU:
			startPainterMenu();
			System.out.println("start Menu");
			break;
		case GameThread.STATE_PK:
			startPainterGame();
			System.out.println("start PK");
			break;
		case GameThread.STATE_SOCKET:
			startPainterGame();
			System.out.println("start Socket");
			break;
		}
		setVisible(true);
		transferFocus();
	}
	
	
	
	
	public  void startPainterGame() {
		//game painter
		painter_game =new Painter_game();
		painter_game.setGameThread(gameThread);
		painter_game.setClock(gameThread.clock);
		painter_game.startPainter();
		add(painter_game);
	}
	public  void startPainterMenu() {
		//menu painter
		painter_menu =new Painter_menu();
		painter_menu.setGameThread(gameThread);
		painter_menu.startPainter();
		add(painter_menu);
	}
}
