import java.io.Serializable;
import java.util.ResourceBundle.Control;
import java.util.function.IntPredicate;


public class Datagram implements Serializable{
	
	public int position_player[][]=new int[2][3];//x,y,direction
	public boolean contorl[][]=new boolean[2][7];//L,U,R,D,KEY1,KEY2,KEY3
	public int position_ball[]=new int[3];//x,y,state
	public int score[]=new int[4];//digit in ten and one
	
	
	public void initialGame(){
		//inital all data 
		position_player[0][0]=130 ;
		position_player[0][1]=400 ;
		position_player[1][0]=500 ;
		position_player[1][1]=400 ;
		position_player[0][2]=1;
		position_player[1][2]=0;
		position_ball[0]=320;
		position_ball[1]=450;
		score[0]=0;
		score[1]=0;
		score[2]=0;
		score[3]=0;
	}
	public void initialRound(){
		//initial round data
		position_player[0][0]=130 ;
		position_player[0][1]=400 ;
		position_player[1][0]=500 ;
		position_player[1][1]=400 ;
		position_player[0][2]=1;
		position_player[1][2]=0;
		position_ball[0]=320;
		position_ball[1]=430;
	}
	

	
	//set game data
	public void setBallPosition(int position_ball[]){
		this.position_ball=position_ball;
	}
	public void setPlayerPosition(int player,int position_player[]){
		this.position_player[player]=position_player;
	}
	public void setControl(int player,boolean contorl[]){
		this.contorl[player]=contorl;
	}
	public void setScore(int score[]){
		this.score=score;
	}
	
	
	//get game data
	public int[] getBallPosition(){
		return this.position_ball;
	}
	public int[] getPlayerPosition(int player){
		return this.position_player[player];
	}
	public int[][] getPlayerPosition(){
		return this.position_player;
	}
	public boolean[] getControl(int player){
		return this.contorl[player];
	}
	public int[] getScore(){
		return this.score;
	}
	
	
	

}
