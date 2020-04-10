package Auto_pack;

public class Timer {
	
	public static long time = 0;
	
	public static void start() {
		Timer.time = System.currentTimeMillis();
	}
	
	public static String displaySeconds() {
		return "Time (per seconds) : " + Timer.getSeconds();
	}
	
	public static double getSeconds() {
		Timer.time = System.currentTimeMillis() - Timer.time;
		return (double)Timer.time /  1000000000L;
	}
	
	public static long getMillis() {
		Timer.time = System.currentTimeMillis() - Timer.time;
		return Timer.time;
	}
}
