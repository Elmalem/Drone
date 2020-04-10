package Auto_pack;

public class Config {
	public static Point[] startPoints = {
			new Point(100,50),
			new Point(50,60),
			new Point(73,68),
			new Point(84,73),
			new Point(92,100)};
	//
	public static String imageSourcePath = "C:\\\\Users\\\\user\\\\git\\\\Matala.1";
	//
	public static int lidarLimit = 300; // in cm
	public static int lidarNoise = 1; // in cm 3
	public static int CMPerPixel = 5;
	public static double accelerate_per_second = 1; // 1 meter per second
	public static double max_speed = 0.5; // 1 meter per second
	public static double rotation_per_second = 60; // whole round per second
	public static int min_motion_accuracy = 0; // 2
	public static int max_motion_accuracy = 1; // 5
	public static int min_rotation_accuracy = 0; // 2
	public static int max_rotation_accuracy = 1; // 5
}
