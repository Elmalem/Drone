package Auto_pack;

public class Config {
	public static Point[] startPoints = {
			new Point(100,50),
			new Point(50,60),
			new Point(73,68),
			new Point(84,73),
			new Point(92,100)};
	public static final int map_index = 4;
	public static final String drone_img_path = Config.imageSourcePath + "\\Maps\\drone_3_pixels.png";
	public static final double max_distance_between_points = 100;
	public static final double save_point_after_seconds = 3;
	public static final int max_angle_risky = 10;
	public static final int max_risky_distance = 150;
	public static final double max_rotation_to_direction = 20;
	public static final int map_size = 3000;
	public static final String imageSourcePath = "C:\\\\Users\\\\user\\\\git\\\\Drone";	
	public static final int lidarLimit = 300; // in cm
	public static final int lidarNoise = 1; // in cm 3
	public static final int CMPerPixel = 5;
	public static final double accelerate_per_second = 1; // 1 is one meter per second
	public static final double max_speed = 0.5; // 0.5 is one meter per second
	public static final double rotation_per_second = 60; // whole round per second
	public static final int min_motion_accuracy = 0; // 2
	public static final int max_motion_accuracy = 1; // 5
	public static final int min_rotation_accuracy = 0; // 2
	public static final int max_rotation_accuracy = 1; // 5
}
