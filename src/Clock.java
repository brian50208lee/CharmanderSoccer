import java.sql.Time;

public class Clock extends Thread {
	//clock info
	final int START_TIME_MIN = 1;
	final int START_TIME_SEC = 0;
	final int MARK_LIGHT = 0;
	final int MARK_DARK = 1;
	
	
	//clock data
	public int time[] = new int[4];
	public int mark;

	GameThread gameThread;
	public Clock() {
		initialClock();
	}
	public void setGameThread(GameThread gameThread) {
		//set game main thread
		this.gameThread = gameThread;
	}
	public void startClock(){
		//start to count down
		this.start();
	}
	@Override
	public void run() {
		//count down
		
		
		int i=0;//mark light or dark
		while (gameThread.state!=GameThread.STATE_MENU) {
			i=(i+1)%2;
			if (i==0) {
				mark=MARK_LIGHT;
			}else if (i==1) {
				mark=MARK_DARK;
				countDown();
				//System.out.println(time[0]+""+time[1]+":"+time[2]+""+time[3]);
			}
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	
	public void initialClock(){
		//initial clock
		time[0]=(START_TIME_MIN/10);
		time[1]=(START_TIME_MIN)%10;
		time[2]=(START_TIME_SEC/10);
		time[3]=(START_TIME_SEC)%10;
		mark=MARK_LIGHT;
	}
	
	
	
	
	private void countDown() {
		//count down time
		time[3]--;
		if (time[3]<0) {
			time[3]=9;
			time[2]--;
		}
		if (time[2]<0) {
			time[2]=5;
			time[1]--;
		}
		if (time[1]<0) {
			time[1]=9;
			time[0]--;
		}
		if (time[0]<0) {
			time[0]=0;
			time[1]=0;
			time[2]=0;
			time[3]=0;
		}
	}

}
