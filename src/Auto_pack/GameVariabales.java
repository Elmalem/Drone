package Auto_pack;
import java.util.ArrayList;

public class GameVariabales {
	
	public static enum PixelState {blocked,explored,unexplored,visited};
	
	public GameVariabales(Map map) {
		GameVariabales.degrees_left = new ArrayList<>();
		GameVariabales.degrees_left_func =  new ArrayList<>();
		GameVariabales.points = new ArrayList<Point>();	
		GameVariabales.drone = new Drone(map);
		GameVariabales.drone.addLidar(0);
		GameVariabales.drone.addLidar(90);
		GameVariabales.drone.addLidar(-90);
		GameVariabales.isRotating = 0;
		
		Utils.initMap();		
		GameVariabales.ai_cpu = new CPU(200,"Auto_AI");
		GameVariabales.ai_cpu.addFunction(this::update);
	}
	
	static int map_size = 3000;
	static PixelState map[][];
	static Drone drone; 
	static Point droneStartingPoint;
	static ArrayList<Point> points;
	static int isRotating;
	static ArrayList<Double> degrees_left;
	static ArrayList<Func> degrees_left_func;
	static boolean isSpeedUp = false;
	static Graph mGraph = new Graph();
	static CPU ai_cpu;
	public static boolean gameEnd =false; 
	static boolean is_init = true;
	static double lastFrontLidarDis = 0;
	static boolean isRotateRight = false;
	static double changedRight = 0;
	static double changedLeft = 0;
	static boolean tryToEscape = false;
	static int leftOrRight = 1;
	static double max_rotation_to_direction = 20;
	static boolean  is_finish = true;
	static boolean isLeftRightRotationEnable = true;
	static boolean is_risky = false;
	static int max_risky_distance = 150;
	static boolean try_to_escape = false;
	public static double  risky_dis = 0;
	static int max_angle_risky = 10;
	static boolean is_lidars_max = false;
	static double save_point_after_seconds = 3;
	static double max_distance_between_points = 100;
	static boolean start_return_home = false;
	static Point init_point;
	static int counter = 0;
	static double lastGyroRotation = 0;
	
	
	public void update(int deltaTime) {
		Utils.updateForAlgo(deltaTime , this);
	}
}
