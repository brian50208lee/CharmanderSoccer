import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;

public class Painter_game extends JComponent implements Runnable,KeyListener{
	final int FRAME_PER_SECOND = 300;
	//Image
	private static Image im_ball;
	private static Image im_bg;
	private static Image im_p1_left;
	private static Image im_p1_right;
	private static Image im_p1_jumpleft;
	private static Image im_p1_jumpright;
	private static Image im_p2_left;
	private static Image im_p2_right;
	private static Image im_p2_jumpleft;
	private static Image im_p2_jumpright;
	private static Image im_time_digit;
	private static Image im_time_mark;
	private static Image im_score_digit;
	private static Image im_changeRound;;
	//class
	Thread stThread;
	GameThread gameThread;
	Clock clock;
	//pic state
	final int pic_player_tick=30;
	private int pic_player=0;
	final int pic_changeRountd_tick=5;
	private int pic_changeRountd=0;
	//data
	private int position_player[][]=new int[2][3];//x,y,direction
	private int position_ball[]=new int[3];//x,y,state
	private int score[]=new int[4];//digit in ten and one
	
	
	

	
	public Painter_game() {
		//construct
		readFile();
		setFocusable(true);
		addKeyListener(this);
	}
	public void setGameThread(GameThread gameThread){
		//set game main thread
		this.gameThread=gameThread;
	}
	public void setClock(Clock clock){
		//set clock
		this.clock=clock;
	}
	
	public void startPainter(){
		//start to paint
		stThread=new Thread(this);
		stThread.start();
	}

	public void run() {
		//painter task
		while (gameThread.state!=GameThread.STATE_MENU) {
			try {
				updatePainter();
				repaint();
				stThread.sleep(1000/FRAME_PER_SECOND);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		gameThread.gui.remove(gameThread.gui.painter_game);
	}
	//----------更新Data----------
    public void updatePainter() {
    	//遊戲動畫更新
    	pic_player=(pic_player+1)%(8*pic_player_tick);
    	if (pic_changeRountd!=0) {
    		pic_changeRountd=(pic_changeRountd+1)%(8*pic_changeRountd_tick);
		}
	}

                           
	public void paintComponent(Graphics g) {
		//paint
		super.paintComponent(g);
		//draw background
		g.drawImage(im_bg,0, 0, null);
		//draw score
		g.drawImage(im_time_digit, 366,145,366+25,145+25*2,0, 150 * score[0],100,150 * score[0] + 150,null);
		g.drawImage(im_time_digit, 398,145,398+25,145+25*2,0, 150 * score[1],100,150 * score[1] + 150,null);
		g.drawImage(im_time_digit, 220,145,220+25,145+25*2,0, 150 * score[2],100,150 * score[2] + 150,null);
		g.drawImage(im_time_digit, 252,145,252+25,145+25*2,0, 150 * score[3],100,150 * score[3] + 150,null);
		//draw time
		g.drawImage(im_time_digit, 250,59,255+25,59+25*2,0, 150 * clock.time[0],100,150 * clock.time[0] + 150,null);
		g.drawImage(im_time_digit, 282,59,287+25,59+25*2,0, 150 * clock.time[1],100,150 * clock.time[1] + 150,null);
		g.drawImage(im_time_digit, 332,59,334+25,59+25*2,0, 150 * clock.time[2],100,150 * clock.time[2] + 150,null);
		g.drawImage(im_time_digit, 364,59,366+25,59+25*2,0, 150 * clock.time[3],100,150 * clock.time[3] + 150,null);
		g.drawImage(im_time_mark, 308,59,308+25,59+25*2,0, 0+clock.mark*150,100,150+clock.mark*150,null);
		//draw player1
		if (position_player[0][2]==0) {g.drawImage(im_p1_left,position_player[0][0]-50, position_player[0][1]-50,position_player[0][0]+50,position_player[0][1]+50,0, 100*(pic_player/pic_player_tick), 100,100 * (pic_player/pic_player_tick) + 100, null);
		}else if (position_player[0][2]==1) {g.drawImage(im_p1_right,position_player[0][0]-50, position_player[0][1]-50,position_player[0][0]+50,position_player[0][1]+50,0, 100*(pic_player/pic_player_tick), 100,100 * (pic_player/pic_player_tick) + 100, null);
		}else if (position_player[0][2]==2) {g.drawImage(im_p1_jumpleft,position_player[0][0]-50, position_player[0][1]-50,position_player[0][0]+50,position_player[0][1]+50,0, 100*(pic_player/pic_player_tick), 100,100 * (pic_player/pic_player_tick) + 100, null);
		}else if (position_player[0][2]==3) {g.drawImage(im_p1_jumpright,position_player[0][0]-50, position_player[0][1]-50,position_player[0][0]+50,position_player[0][1]+50,0, 100*(pic_player/pic_player_tick), 100,100 * (pic_player/pic_player_tick) + 100, null);
		}
		//draw player2
		if (position_player[1][2]==0) {g.drawImage(im_p2_left,position_player[1][0]-50, position_player[1][1]-50,position_player[1][0]+50,position_player[1][1]+50, 0, 100 * (pic_player/pic_player_tick), 100,100 *(pic_player/pic_player_tick) + 100, null);
		}else if (position_player[1][2]==1) {g.drawImage(im_p2_right,position_player[1][0]-50, position_player[1][1]-50,position_player[1][0]+50,position_player[1][1]+50, 0, 100 * (pic_player/pic_player_tick), 100,100 *(pic_player/pic_player_tick) + 100, null);
		}else if (position_player[1][2]==2) {g.drawImage(im_p2_jumpleft,position_player[1][0]-50, position_player[1][1]-50,position_player[1][0]+50,position_player[1][1]+50, 0, 100 * (pic_player/pic_player_tick), 100,100 *(pic_player/pic_player_tick) + 100, null);
		}else if (position_player[1][2]==3) {g.drawImage(im_p2_jumpright,position_player[1][0]-50, position_player[1][1]-50,position_player[1][0]+50,position_player[1][1]+50, 0, 100 * (pic_player/pic_player_tick), 100,100 *(pic_player/pic_player_tick) + 100, null);
		}
		//draw ball
		g.drawImage(im_ball,position_ball[0]-20,position_ball[1]-20, null);
		//draw changeRound
		if (pic_changeRountd!=0) {
			g.drawImage(im_changeRound,0, 0,650,510,0,0+pic_changeRountd*100/pic_changeRountd_tick,100,100+pic_changeRountd*100/pic_changeRountd_tick, null);
		}
		
	}

	public static void readFile(){
		//main menu pic
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		im_ball=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/ball/ball.png"));
		im_bg=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/back.png"));
		im_p1_left=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p1/left.png"));
		im_p1_right=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p1/right.png"));
		im_p1_jumpleft=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p1/jumpLeft.png"));
		im_p1_jumpright=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p1/jumpRight.png"));
		im_p2_left=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p2/left.png"));
		im_p2_right=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p2/right.png"));
		im_p2_jumpleft=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p2/jumpLeft.png"));
		im_p2_jumpright=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/p2/jumpRight.png"));
		im_time_digit=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/scoreboard/clock/digit.png"));
		im_time_mark=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/scoreboard/clock/mark.png"));
		im_score_digit=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/scoreboard/score/digit.png"));
		im_changeRound=toolkit.createImage(ClassLoader.getSystemResource("img/painter_game/change.png"));
	}
	
	
	
	//key control
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 37) {
			gameThread.setCtrlData(1, 0, true);
		}
		if (e.getKeyCode() == 39) {
			gameThread.setCtrlData(1, 1, true);
		}
		if (e.getKeyCode() == 38) {
			gameThread.setCtrlData(1, 2, true);
		}
		if (e.getKeyCode() == 40) {
			gameThread.setCtrlData(1, 3, true);
		}
		if (e.getKeyCode() == 44) {
			gameThread.setCtrlData(1, 4, true);
		}
		if (e.getKeyCode() == 46) {
			gameThread.setCtrlData(1, 5, true);
		}
		if (e.getKeyCode() == 47) {
			gameThread.setCtrlData(1, 6, true);
		}

		if (e.getKeyCode() == 68) {
			gameThread.setCtrlData(0, 0, true);
		}
		if (e.getKeyCode() == 71) {
			gameThread.setCtrlData(0, 1, true);
		}
		if (e.getKeyCode() == 82) {
			gameThread.setCtrlData(0, 2, true);
		}
		if (e.getKeyCode() == 70) {
			gameThread.setCtrlData(0, 3, true);
		}
		if (e.getKeyCode() == 90) {
			gameThread.setCtrlData(0, 4, true);
		}
		if (e.getKeyCode() == 88) {
			gameThread.setCtrlData(0, 5, true);
		}
		if (e.getKeyCode() == 67) {
			gameThread.setCtrlData(0, 6, true);
		}
		if (e.getKeyCode()==27) {
			gameThread.nextState(GameThread.STATE_MENU);
		}
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 37) {
			gameThread.setCtrlData(1, 0, false);
		}
		if (e.getKeyCode() == 39) {
			gameThread.setCtrlData(1, 1, false);
		}
		if (e.getKeyCode() == 38) {
			gameThread.setCtrlData(1, 2, false);
		}
		if (e.getKeyCode() == 40) {
			gameThread.setCtrlData(1, 3, false);
		}
		if (e.getKeyCode() == 44) {
			gameThread.setCtrlData(1, 4, false);
		}
		if (e.getKeyCode() == 46) {
			gameThread.setCtrlData(1, 5, false);
		}
		if (e.getKeyCode() == 47) {
			gameThread.setCtrlData(1, 6, false);
		}
		if (e.getKeyCode() == 68) {
			gameThread.setCtrlData(0, 0, false);
		}
		if (e.getKeyCode() == 71) {
			gameThread.setCtrlData(0, 1, false);
		}
		if (e.getKeyCode() == 82) {
			gameThread.setCtrlData(0, 2, false);
		}
		if (e.getKeyCode() == 70) {
			gameThread.setCtrlData(0, 3, false);
		}
		if (e.getKeyCode() == 90) {
			gameThread.setCtrlData(0, 4, false);
		}
		if (e.getKeyCode() == 88) {
			gameThread.setCtrlData(0, 5, false);
		}
		if (e.getKeyCode() == 67) {
			gameThread.setCtrlData(0, 6, false);
		}
	}
	
	
	
	
	
	
	
	public void update_position_player(int position_1P[],int position_2P[]){
		//call by gameThread to updata data
		this.position_player[0]=position_1P;
		this.position_player[1]=position_2P;
	}
	public void update_position_ball(int position_ball[]){
		//call by gameThread to updata data
		this.position_ball=position_ball;
	}
	public void update_score(int score[]){
		//call by gameThread to updata data
		for (int i = 0; i < this.score.length; i++) {
			if (this.score[i]!=score[i]) {
				pic_changeRountd=1;
			}
		}
		this.score[0]=score[0];
		this.score[1]=score[1];
		this.score[2]=score[2];
		this.score[3]=score[3];
	}
	public void update_done(){
		//call by gameThread to repaint
		repaint();
	}

}