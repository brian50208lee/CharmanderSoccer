import javax.swing.JOptionPane;


public class GameThread extends Thread{

	final int win_point=10;//1~99
	
	//state
	static final int STATE_MENU=0;
	static final int STATE_PK=1;
	static final int STATE_SOCKET=2;
	public int state=-1;
	public int nextState;
	
	//socket
	static final int SOCKET_NULL=0;
	static final int SOCKET_SERVER=1;
	static final int SOCKET_CLIENT=2;
	public int socket;
	
	
	
	public boolean run;
	public Datagram datagram;
	public Server server;
	public Client client;
	public Clock clock;
	public Player player1,player2;
	public Ball ball;
	public GUI gui;
	
	//control
	boolean control[][]=new boolean[2][7];
	
	public GameThread() {
		//construt initial
		datagram=new Datagram();
		datagram.initialGame();
		nextState=STATE_MENU;
		socket=SOCKET_NULL;
	}
	public void setGUI(GUI gui){
		//set GUI
		this.gui=gui;
	}
	public void stardThread(){
		//start thread
		run=true;
		start();
	}
	public void setCtrlData(int player, int ctrl, boolean pressed) {
		//get control
		control[player][ctrl] = pressed;
}
	
	
	public void run() {
		//game task
		while (run) {
			if (state!=nextState) {
				chageState(nextState);
			}
			if (state==STATE_MENU) {
				if (socket==SOCKET_SERVER) {
					while (server.link==Server.LINK_WAIT) {
						gui.lbMessage.setText("waitting ...");
					}
					if (server.link==Server.LINK_TRUE) {
						nextState(STATE_SOCKET);
					}else if (server.link==Server.LINK_FALSE) {
						JOptionPane.showMessageDialog(gui, "Time out !");
						nextState(STATE_MENU);
					}
				}else if (socket==SOCKET_CLIENT) {
					while (client.link==Client.LINK_WAIT) {
						gui.lbMessage.setText("waitting ...");
					}
					if (client.link==Client.LINK_TRUE) {
						nextState(STATE_SOCKET);
					}else if (client.link==Client.LINK_FALSE) {
						JOptionPane.showMessageDialog(gui, "Can't connect to sever !");
						nextState(STATE_MENU);
					}
				}
			}else if (state==STATE_PK) {
				taskPK();
			}else if (state==STATE_SOCKET) {
				if (socket==SOCKET_SERVER) {
					taskServer();
					if (server.link==Server.LINK_FALSE) {
						nextState(STATE_MENU);
					}
				}else if (socket==SOCKET_CLIENT) {
					taskClient();
					if (client.link==Client.LINK_FALSE) {
						nextState(STATE_MENU);
					}
				}
			}
			try {
				sleep(1000/100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	public void chageState(int state){
		//change state
		this.state=state;
		if (state==STATE_PK||state==STATE_SOCKET) {
			clock=new Clock();
			clock.setGameThread(this);
			clock.startClock();
			ball=new Ball(320);
			player1 = new Player(135);
			player2 = new Player(500);
			gui.chagePainter(state);
			datagram.initialGame();
			initialRound();
		}else if (state==STATE_MENU) {
			gui.chagePainter(state);
		}
		
	}
	
	
	
	public void nextState(int state){
		//set next state
		switch (state) {
		case STATE_MENU:
			nextState=STATE_MENU;
			socket=SOCKET_NULL;
			break;
		case STATE_PK:
			nextState=STATE_PK;
			break;
		case STATE_SOCKET:
			nextState=STATE_SOCKET;
			break;
		}
	}
	public void startServer(){
		//start socket
		server=new Server(gui);
		server.setDatagram(datagram);
		server.setGameThread(this);
		server.start();
		this.socket=SOCKET_SERVER;
	}
	public void startClient(String ip){
		//start socket
		client=new Client(gui);
		client.setDatagram(datagram);
		client.setGameThread(this);
		client.setIP(ip);
		client.start();
		this.socket=SOCKET_CLIENT;
	}
	
	
	
	
	
	
	
	public void taskPK(){
		//單人模式
		
		
		//get data
		datagram.setControl(GUI.CONTROL_1P, control[GUI.CONTROL_1P]);
		datagram.setControl(GUI.CONTROL_2P, control[GUI.CONTROL_2P]);
		//calculate 
		player1.setPosition(datagram.getPlayerPosition(0),datagram.getControl(0));
		datagram.setPlayerPosition(0, player1.getPosition());
		player2.setPosition(datagram.getPlayerPosition(1),datagram.getControl(1));
		datagram.setPlayerPosition(1, player2.getPosition());
		ball.setData(datagram.position_player[0], datagram.position_player[1],datagram.contorl[0], datagram.contorl[1]);
		datagram.setBallPosition(ball.getBallPosition());
		deadBall();
		//repaint
		update_painter();
		//check game over
		check_gameOver();
	}
	public void taskServer(){
		//server模式
		
		//get data
		Datagram Packet=server.getPacket();
		datagram.setControl(GUI.CONTROL_CLIENT, Packet.getControl(GUI.CONTROL_CLIENT));
		datagram.setControl(GUI.CONTROL_SERVER, control[GUI.CONTROL_SERVER]);
		datagram.setPlayerPosition(0, Packet.getPlayerPosition(0));
		//calculate
		player2.setPosition(datagram.getPlayerPosition(1),datagram.getControl(1));
		datagram.setPlayerPosition(1, player2.getPosition());
		ball.setData(datagram.position_player[0], datagram.position_player[1],datagram.contorl[0], datagram.contorl[1]);
		datagram.setBallPosition(ball.getBallPosition());
		deadBall();
		//repaint
		update_painter();
		//check game over
		check_gameOver();
	}
	public void taskClient(){
		//client 模式
		
		//get data
		Datagram Packet=client.getPacket();
		datagram.setControl(GUI.CONTROL_CLIENT, control[GUI.CONTROL_RIGHT]);
		datagram.setPlayerPosition(1, Packet.getPlayerPosition(1));
		datagram.setBallPosition(Packet.getBallPosition());
		for (int i = 0; i < datagram.getScore().length; i++) {
			if (datagram.getScore()[i]!=Packet.getScore()[i]) {
				initialRound();
			}
		}
		datagram.setScore(client.getPacket().getScore());
		//calclate
		player1.setPosition(datagram.getPlayerPosition(0),datagram.getControl(0));
		datagram.setPlayerPosition(0, player1.getPosition());
		//repaint
		update_painter();
		//check game over
		check_gameOver();
	}	
	

	public void deadBall(){
		//進球
				if (ball.getWin() == 1) {
					datagram.score[1]++;
					if (datagram.score[1] == 10) {
						datagram.score[0]++;
						datagram.score[1] = 0;
						ball.win=0;
					}
					initialRound();
				}
				if (ball.getWin() == 2) {
					datagram.score[3]++;
					if (datagram.score[3] == 10) {
						datagram.score[2] ++;
						datagram.score[3] = 0;
						ball.win=0;
					}	
					initialRound();
				}
	}
	public void update_painter(){
		//update data and repaint
		gui.painter_game.update_position_player(datagram.getPlayerPosition(0), datagram.getPlayerPosition(1));
		gui.painter_game.update_position_ball(datagram.getBallPosition());
		gui.painter_game.update_score(datagram.getScore());
		gui.painter_game.update_done();
	}
	
	public void initialRound(){
		//initial data to new round
		player1 = new Player(500);
		player1 = new Player(130);
		ball = new Ball(320);
		datagram.initialRound();
		for (int i = 0; i < control.length; i++) {
			for (int j = 0; j < control[0].length; j++) {
				control[i][j]=false;
			}
		}
	}
	
	
	
	public void check_gameOver(){
		//check gameover and show dialog
		int score[]=datagram.getScore();
		int time[]=clock.time;
		int score_P1=score[2]*10+score[3];
		int score_P2=score[0]*10+score[1];
		
		
		if (time[0]==0&&time[1]==0&&time[2]==0&&time[3]==0) {
			if (score_P1>score_P2) {
				if (state==STATE_PK) {
					JOptionPane.showMessageDialog(gui, "Time's up , P1 win");
				}else if (state==STATE_SOCKET&&socket==SOCKET_SERVER) {
					JOptionPane.showMessageDialog(gui, "Time's up , You lose");
				}else if (state==STATE_SOCKET&&socket==SOCKET_CLIENT) {
					JOptionPane.showMessageDialog(gui, "Time's up , You win");
				}
			}else if (score_P1<score_P2) {
				if (state==STATE_PK) {
					JOptionPane.showMessageDialog(gui, "Time's up , P2 win");
				}else if (state==STATE_SOCKET&&socket==SOCKET_SERVER) {
					JOptionPane.showMessageDialog(gui, "Time's up , You win");
				}else if (state==STATE_SOCKET&&socket==SOCKET_CLIENT) {
					JOptionPane.showMessageDialog(gui, "Time's up , You lose");
				}
			}else if (score_P1==score_P2) {
				if (state==STATE_PK) {
					JOptionPane.showMessageDialog(gui, "Time's up , deuce");
				}else if (state==STATE_SOCKET&&socket==SOCKET_SERVER) {
					JOptionPane.showMessageDialog(gui, "Time's up , deuce");
				}else if (state==STATE_SOCKET&&socket==SOCKET_CLIENT) {
					JOptionPane.showMessageDialog(gui, "Time's up , deuce");
				}
			}
			nextState=STATE_MENU;
			socket=SOCKET_NULL;
		}else if (score_P1==win_point) {
			if (state==STATE_PK) {
				JOptionPane.showMessageDialog(gui, "P1 win");
			}else if (state==STATE_SOCKET&&socket==SOCKET_SERVER) {
				JOptionPane.showMessageDialog(gui, "You lose");
			}else if (state==STATE_SOCKET&&socket==SOCKET_CLIENT) {
				JOptionPane.showMessageDialog(gui, "You win");
			}
			nextState=STATE_MENU;
			socket=SOCKET_NULL;
		}else if(score_P2==win_point){
			if (state==STATE_PK) {
				JOptionPane.showMessageDialog(gui, "P2 win");
			}else if (state==STATE_SOCKET&&socket==SOCKET_SERVER) {
				JOptionPane.showMessageDialog(gui, "You win");
			}else if (state==STATE_SOCKET&&socket==SOCKET_CLIENT) {
				JOptionPane.showMessageDialog(gui, "You lose");
			}
			nextState=STATE_MENU;
			socket=SOCKET_NULL;
		}
	}
}
