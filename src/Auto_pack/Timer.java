package Auto_pack;

/*
 * Add simple clock for count the seconds with thread functionality
 */
public class Timer extends Thread{

	private static int counterSeconds=0;
	public static boolean flag=true;

	public void run()
	{
		while(!GameVariabales.gameEnd) {
			try { 
				Thread.sleep(1000);
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
