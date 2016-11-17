
public class Ball {
	//固定參數
	final double GROUND_LINE = 450;
	final double GROUND_SIDE = 640;
	final double DOOR_HEIGHT = GROUND_LINE-160;
	
	final double VECTOR = 7;
	final double reboundPara = -0.8;
	final double strictionPara = -0.05;
	final double SIZE_ball=40;
	final double SIZE_player=100;
	
	//ball info
	double position[] = new double[3];// x,y,size
	double vectorX;
	double vectorY;
	double accelerate;
	double size;
	int timer = 0;
	int win = 0; //no:0 l:1 r:2
	int state = 0;

	//initial set
	Ball(double iniX) {
		position[0] = iniX;// initial x
		position[1] = GROUND_LINE - SIZE_ball / 2;// initial y

		vectorX = 0;
		vectorY = 0;
		accelerate = 0.2;
	}

	//根據目前的狀態改變球的位置
	public void moveFunction() {
		position[0] += vectorX;
		position[1] += vectorY;
		if(state==0){
			vectorY += accelerate;
		}
		
		if (position[1] >= GROUND_LINE - SIZE_ball / 2 && vectorX != 0) {
			double temp = vectorX;
			vectorX += (strictionPara * vectorX);
			
				if(Math.abs(vectorX) < 1){
					vectorX = 0;
				}else{
					vectorX +=  (4*strictionPara * vectorX);
				}
				
			}
		
		borderCheck();

	}

	//邊界檢查
	public void borderCheck() {

		if (position[0] - SIZE_ball / 2 < 0) {
			if(position[1] >DOOR_HEIGHT){
				win = 1;
			}else{
				position[0] = 0 + SIZE_ball / 2;
				vectorX =  (-1 * vectorX);
			}
			
		}

		if (position[0] + SIZE_ball / 2 > GROUND_SIDE) {
			if(position[1] >DOOR_HEIGHT){
				win = 2;
			}else{
				position[0] = GROUND_SIDE - SIZE_ball / 2;
				vectorX =  (-1 * vectorX);
			}
		}

		if (position[1] + SIZE_ball / 2 >= GROUND_LINE) {
			position[1] = GROUND_LINE - SIZE_ball / 2;
			state=0;
			if (vectorY > 1) {
				vectorY = (reboundPara * vectorY*0.5);
			} else {

				vectorY = 0;
			}

		}

		if (position[1] - SIZE_ball / 2 < 0) {
			position[1] = 1 + SIZE_ball / 2;
			vectorY = (-1 * vectorY);

		}

	}

	//根據玩家跟球的相對位置，和玩家的操作按鍵來踢球
	public void kickBall(int p[],boolean c[],double range) {
			state=0;
			vectorX = 0.8*((position[0] - p[0]) / range * VECTOR);
			vectorY =  1.6*((position[1] - p[1]) / range * VECTOR);
			position[0] += 0.2*vectorX;
			position[1] += 0.2*vectorY;
			if(c[4]){
				
				if(c[0]||c[1]){
					
					if(c[2]){
						state=1;
						vectorX=((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*2);
						vectorY= -0.8*VECTOR;
					}
					else if(c[3]){
						state=1;
						vectorX=((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*3);
						vectorY= 1.6*VECTOR;
					}else{
						state=1;
						vectorX= ((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*2.2);
						vectorY= (GROUND_LINE-position[1])/GROUND_SIDE*VECTOR*3;
					}
					
					if(c[0]){
						vectorX = -1*Math.abs(vectorX);					
					}else{
						vectorX = Math.abs(vectorX);					
					}

				}else if(c[2]){
					state=0;
					vectorX=((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*0.25);
					vectorY= -1.9*VECTOR;
				}
				else if(c[3]){
					state=1;
					vectorX=((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*1.2);
					vectorY= 1.7*VECTOR;
				}else{
					state=0;
					vectorX=((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*0.3);
					vectorY=0;
				}
			}
			if(c[5]){
			
					vectorX = -((position[0] - p[0]) / Math.abs(position[0] - p[0]) *VECTOR*0.15);
					vectorY = -0.9*VECTOR;
			}
	}
	
	//判斷是否有碰到玩家
	public boolean hitPlayer(int p[],boolean c[]){
		double range = 0;
		
		range = Math.sqrt((p[0] - position[0]) * (p[0] - position[0])
				+ (p[1] - position[1]) * (p[1] - position[1]));
		if (range <= (SIZE_player + SIZE_ball) / 2) {
			//System.out.println(" range : "+range+" size : "+(p[2] + position[2]) / 2+"　vxy : "+(Math.sqrt(vectorX*vectorX+vectorY*vectorY)));
			kickBall(p, c, range);
			return true;
		}else if(range <= Math.sqrt(vectorX*vectorX+vectorY*vectorY)){
			
			double tempX = position[0] + vectorX;
			double tempY = position[1] + vectorY;
			double tempO = 0;
			double tempA = tempY-position[1];
			double tempB = tempX-position[0];
			tempO = tempA*tempX-tempY*tempB;
			
			double tempR = 0;
			
			tempR = Math.abs((tempA*p[0]-tempB*p[1]-tempO)/(Math.sqrt(tempA*tempA+tempB*tempB)));
			//System.out.println("tempR : "+tempR+" s : "+ position[2]);
			if(tempR<=SIZE_ball){
				System.out.println("yes");
				kickBall(p, c, range);
				return true;
			}
		}
		
		
		
		
		return false;
	}
	
	//設定當下兩名玩家的狀態
	public void setData(int p1[],int p2[],boolean p1C[],boolean p2C[]){
		if(timer%2==0){
			hitPlayer(p1,p1C);
			hitPlayer(p2,p2C);
		}else{
			hitPlayer(p2,p2C);
			hitPlayer(p1,p1C);
		}
		
		moveFunction();
		timer++;
		
	}

	//獲得球的位置
	public int[] getBallPosition() {
		int ballPosition[]=new int[3];
		ballPosition[0]=(int)position[0];
		ballPosition[1]=(int)position[1];
		ballPosition[2]=(int)position[2];
		return ballPosition;
	}
	
	//判定得分
	public int getWin(){
		return win;
		
	}

}
