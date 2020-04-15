package Auto_pack;

public class Timer extends Thread{

	private static int counterSeconds=0;
	public static boolean flag=true;

	public void run()
	{
		while(!Utils.gameEnd) {
			try { 
				Thread.sleep(1000);//For some reason it only works that way!!!
			}
			catch (InterruptedException e) { e.printStackTrace(); }


			while(flag) {

				counterSeconds++;
				try { 
					Thread.sleep(1000);
				}
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}

		public static int getTimeBySeconds() {
			return counterSeconds;
		}

	}
