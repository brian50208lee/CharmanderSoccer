import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class Painter_menu extends JComponent implements Runnable,KeyListener{
	final int FRAME_PER_SECOND = 40;
	//main menu
	private static Image im_main_bg;
	private static Image im_main_bg2;
	private static Image im_main_option;
	//link menu
	private static Image im_link_bg;
	private static Image im_link_bg2;
	private static Image im_link_option;
	private static Image im_link_dialog_bg;
	private static Image im_link_dialog_inputIP;
	private static Image im_link_dialog_watting;
	//select
	static final int SELECT_1PLAER=0;
	static final int SELECT_2PLAER=1;
	static final int SELECT_SERVER=0;
	static final int SELECT_CLIENT=1;
	static final int SELECT_BACK=2;
	private int select;
	//menu
	static final int MENU_MAIN=0;
	static final int MENU_LINK=1;
	static final int MENU_INPUT=2;
	private int menu;
	//background
	private int bg_d=1;
	private int bg_move=0;
	//option
	static final int opt_width_normal=200;
	static final int opt_height_normal=50;
	private int opt_dx_focus=2;
	private int opt_dy_focus=1;
	private int opt_width_focus=200;
	private int opt_height_focus=50;
	//dialog pic
	static final int dialog_tick=15;
	private int dialog_pic=0;


	
	
	

	Thread stThread;
	GameThread gameThread;
	JLabel txtInput;
	public Painter_menu() {
		//construct initial data
		select=SELECT_1PLAER;
		txtInput=new JLabel();
		txtInput.setBounds(270,335,110,30);
		txtInput.setVisible(false);
		add(txtInput);
		readFile();
		setFocusable(true);
		addKeyListener(this);
	}
	public void setGameThread(GameThread gameThread){
		//set game main thread
		this.gameThread=gameThread;
	}
	
	public void startPainter(){
		//start to paint
		stThread=new Thread(this);
		stThread.start();
	}

	public void run() {
		//painter task
		while (gameThread.state==GameThread.STATE_MENU) {
			try {
				updatePainter();
				repaint();
				stThread.sleep(1000/FRAME_PER_SECOND);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		gameThread.gui.remove(gameThread.gui.painter_menu);
		System.out.println(1);
	}
	//----------更新Data----------
    public void updatePainter() {
    	//更新背景 畫面
		if (bg_move+bg_d>=100||bg_move+bg_d<=0) {
    		bg_d=(-1)*bg_d;
		}
    	bg_move+=bg_d;
    	if (menu==MENU_MAIN) {
        	if (opt_width_focus+opt_dx_focus>=(opt_width_normal+30)||opt_width_focus+opt_dx_focus<=opt_width_normal) {
        		opt_dx_focus=(-1)*opt_dx_focus;
        		opt_dy_focus=(-1)*opt_dy_focus;
    		}
        	opt_width_focus+=opt_dx_focus;
        	opt_height_focus+=opt_dy_focus;
		}else if (menu==MENU_LINK) {
			if (gameThread.socket==GameThread.SOCKET_NULL) {
				
			}else {
				dialog_pic=(dialog_pic+dialog_tick)%1500;
			}
		}else if (menu==MENU_INPUT) {
			
		}
    	
	}

                           
	public void paintComponent(Graphics g) {
		//paint backgroud
		super.paintComponent(g);
		g.drawImage(im_main_bg,0, 0,650,510,0+bg_move,0+bg_move, 650+bg_move, 510+bg_move, null);
		g.drawImage(im_main_bg2, 0, 0, null);
		
		//switch menu and paint
		if (menu==MENU_MAIN) {
			if (select==SELECT_1PLAER) {
				g.drawImage(im_main_option, (650-opt_width_focus)/2, 280-(opt_height_focus)/2,(650+opt_width_focus)/2,280+(opt_height_focus)/2,0,0,250,50,null);
				g.drawImage(im_main_option, (650-opt_width_normal)/2, 350-(opt_height_normal)/2,(650+opt_width_normal)/2,350+(opt_height_normal)/2,0,50,250,100,null);
			}else if (select==SELECT_2PLAER) {
				g.drawImage(im_main_option, (650-opt_width_normal)/2, 280-(opt_height_normal)/2,(650+opt_width_normal)/2,280+(opt_height_normal)/2,0,0,250,50,null);
				g.drawImage(im_main_option, (650-opt_width_focus)/2, 350-(opt_height_focus)/2,(650+opt_width_focus)/2,350+(opt_height_focus)/2,0,50,250,100,null);
			}
		}else if (menu==MENU_LINK) {
			if (gameThread.socket==GameThread.SOCKET_NULL) {
				g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 240,(650 + opt_width_normal)/2, 240+opt_height_normal,0,0,200,50, null);
				g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 300,(650 + opt_width_normal)/2, 300+opt_height_normal,200,0,400,50, null);
				g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 360,(650 + opt_width_normal)/2, 360+opt_height_normal,400,0,600,50, null);
				if (select==SELECT_SERVER) {
					g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 240,(650 + opt_width_normal)/2, 240+opt_height_normal,0,50,200,100, null);
				}else if (select==SELECT_CLIENT) {
					g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 300,(650 + opt_width_normal)/2, 300+opt_height_normal,200,50,400,100, null);
				}else if (select==SELECT_BACK) {
					g.drawImage(im_link_option, (650 - opt_width_normal) / 2, 360,(650 + opt_width_normal)/2, 360+opt_height_normal,400,50,600,100, null);
				}
			}else{
				g.drawImage(im_link_dialog_bg, (650 - opt_width_normal) / 2, 230,(650 + opt_width_normal)/2, 370+opt_height_normal,0,0,300,200, null);
				g.drawImage(im_link_dialog_watting, (650 - opt_width_normal) / 2, 230,(650 + opt_width_normal)/2, 370+opt_height_normal,300*(dialog_pic/300),0,300+300*(dialog_pic/300),200, null);
			}

		}else if (menu==MENU_INPUT) {
			g.drawImage(im_link_dialog_inputIP, (650 - opt_width_normal) / 2, 230,(650 + opt_width_normal)/2, 370+opt_height_normal,0,0,300,200, null);
		}

	}

	public static void readFile(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//main menu pic
		im_main_bg=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_main/im_background.png"));
		im_main_bg2=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_main/im_background2.png"));
		im_main_option=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_main/im_option.png"));
		//link menu pic
		im_link_bg=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/im_background.png"));
		im_link_bg2=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/im_background2.png"));
		im_link_option=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/im_option.png"));
		im_link_dialog_bg=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/dialog/im_bg.png"));
		im_link_dialog_inputIP=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/dialog/im_inputIP.png"));
		im_link_dialog_watting=toolkit.createImage(ClassLoader.getSystemResource("img/painter_menu/menu_link/dialog/im_waitting.png"));
	}

	
	
	//key control
	@Override	
	public void keyPressed(KeyEvent e) {
		if (menu==MENU_MAIN) {
			if (e.getKeyCode() == 38) {
				select=(select==0)?1:(select-1);
			}else if (e.getKeyCode() == 40) {
				select=(select==1)?0:(select+1);
			}else if (e.getKeyCode() == 10) {
				if (select==SELECT_1PLAER) {
					gameThread.nextState(GameThread.STATE_PK);
					System.out.println("SELECT_1PLAER");
				}else if (select==SELECT_2PLAER) {
					System.out.println("SELECT_2PLAER");
					menu=MENU_LINK;
					select=SELECT_SERVER;
				}
			}
		}else if (menu==MENU_LINK){
			if (e.getKeyCode() == 38) {
				select=(select==0)?2:(select-1);
			}else if (e.getKeyCode() == 40) {
				select=(select==2)?0:(select+1);
			}else if (e.getKeyCode() == 10) {
				System.out.println("enter");
				if (select==SELECT_SERVER) {
					gameThread.startServer();
				}else if(select==SELECT_CLIENT){
					menu=MENU_INPUT;
					txtInput.setVisible(true);
				}else if(select==SELECT_BACK){
					menu=MENU_MAIN;
					select=SELECT_1PLAER;
				}
			}
		}else  if (menu==MENU_INPUT){
			if (e.getKeyCode()==8&&txtInput.getText().length()>0) {
				txtInput.setText(txtInput.getText().substring(0, txtInput.getText().length()-1));
			}else if(e.getKeyCode()==10){
					gameThread.startClient(txtInput.getText());
					txtInput.setText("");
					txtInput.setVisible(false);
					menu=MENU_LINK;
			}else if(e.getKeyCode()==27){
				menu=MENU_LINK;
			}else {
				txtInput.setText(txtInput.getText()+e.getKeyChar());
			}
		}

		repaint();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	
	
	
	public void newServer(){
		//start server
		gameThread.startServer();
	}
	public void newClient(){
		//start client
		gameThread.startClient(txtInput.getText());
	}

}
