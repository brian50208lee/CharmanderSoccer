

public class Player {
	//固定參數
	final double GROUND_LINE = 450;
	final double VECTOR = 3;
	
	//state
	final int STATE_MOVE_LEFT = 0;
	final int STATE_MOVE_RIGHT = 1;
	final int STATE_JUMP_LEFT = 2;
	final int STATE_JUMP_RIGHT = 3;
	final int STATE_WIN = 2;
	final int STATE_LOSS = 3;
	
	//player info
	double position[] = new double[3];//x,y,state
	double vectorX;
	double vectorY;
	double accelerate;
	double size=100;
	boolean control[] = new boolean[7];
	
	//initial set
	Player(double iniX){
		accelerate=0.3;
	}
	
	//根據目前的操作改變玩家的位置
	public void moveFunction(){
		if(control[0]){
			position[0]-=VECTOR;
			if (position[2]==STATE_MOVE_LEFT||position[2]==STATE_MOVE_RIGHT) {
				position[2]=STATE_MOVE_LEFT;
			}else if (position[2]==STATE_JUMP_LEFT||position[2]==STATE_JUMP_RIGHT) {
				position[2]=STATE_JUMP_LEFT;
			}
		}		
		if(control[1]){
			position[0]+=VECTOR;
			if (position[2]==STATE_MOVE_LEFT||position[2]==STATE_MOVE_RIGHT) {
				position[2]=STATE_MOVE_RIGHT;
			}else if (position[2]==STATE_JUMP_LEFT||position[2]==STATE_JUMP_RIGHT) {
				position[2]=STATE_JUMP_RIGHT;
			}
		}
		if(control[2]&&position[1]==GROUND_LINE-size/2){
			if (position[2]==STATE_MOVE_LEFT) {
				position[2]=STATE_JUMP_LEFT;
			}else if (position[2]==STATE_MOVE_RIGHT) {
				position[2]=STATE_JUMP_RIGHT;
			}
			vectorY=-8;
		}
		if(control[3]){
			position[1]+=1;
		}
		
		position[1]+=vectorY;
		vectorY+=accelerate;
		borderCheck();
	}
	//邊界檢查，若超出邊界則重設位置
	public void borderCheck(){
		if(position[1]+size/2>GROUND_LINE){
			position[1]=GROUND_LINE-size/2;
			vectorY=0;
			if (position[2]==STATE_JUMP_LEFT) {
				position[2]=STATE_MOVE_LEFT;
			}else if (position[2]==STATE_JUMP_RIGHT) {
				position[2]=STATE_MOVE_RIGHT;
			}
		}
		
		
	}
	
	//設定目前玩家的位置和操作方式
	public void setPosition(int position[],boolean c[]){
		for (int i = 0; i < position.length; i++) {
			this.position[i]=(double)position[i];
		}
		control=c;
		moveFunction();
	}
	
	//獲得目前玩家的位置
	public int[] getPosition(){
		int tempPosition[] =new int[3];
		for (int i = 0; i < tempPosition.length; i++) {
			tempPosition[i]=(int)position[i];
		}
		return tempPosition;
	}
}
