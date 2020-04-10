package Auto_pack;

public class Timer {
	
	private static double time;
	private static double difference;
	
	public static void start() {
		Timer.difference = 0;
		Timer.time = System.nanoTime();
	}
	
	public static void reset() {
		Timer.difference = 0;
		Timer.time = 0;
	}
	
	public static void pause() {
		Timer.time = System.nanoTime() - Timer.difference;
	}
	
	public static void resume() {
		Timer.difference = System.nanoTime() - Timer.time;
		Timer.time = System.nanoTime() - Timer.difference;
	}
	
	public static double getTimeBySeconds() {
		if(Timer.time > 0)
			return (double)(System.nanoTime() - Timer.time) / 1000000000;
		else
			return 0;
	}
	
}
