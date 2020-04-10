package Auto_pack;

public class Timer {
	
	private static double time;
	
	public static void start() {
		Timer.time = System.nanoTime();
	}
	
	public static void reset() {
		Timer.time = 0;
	}
	
	public static double getTimeBySeconds() {
		if(Timer.time != 0)
			return (double)(System.nanoTime() - Timer.time) / 1000000000;
		else
			return 0;
	}
	
}
