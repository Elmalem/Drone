package Auto_pack;
import java.util.ArrayList;
import java.util.List;

public class GameVariabales {
	
	public static enum PixelState {blocked,explored,unexplored,visited};
	
	public static void init() {
		int map_index = 4;
		GameVariabales.realMap = new Map(Config.imageSourcePath +"\\Maps\\p1" + map_index + ".png",Config.startPoints[map_index-1]);		
		GameVariabales.degrees_left = new ArrayList<>();
		GameVariabales.degrees_left_func =  new ArrayList<>();
		GameVariabales.points = new ArrayList<Point>();	
		GameVariabales.drone = new Drone();
		GameVariabales.drone.addLidar(0); 
		GameVariabales.drone.addLidar(90);
		GameVariabales.drone.addLidar(-90);
		GameVariabales.isRotating = 0;
		GameVariabales.ai_cpu = new CPU(200,"Auto_AI");
		GameVariabales.ai_cpu.addFunction(GameVariabales::update);
		Utils.initMap();		
	}
	
	public static List<CPU> all_cpus = null;
	public static Map realMap;
	public static PixelState map[][];
	public static Drone drone; 
	public static Point droneStartingPoint;
	public static ArrayList<Point> points;
	public static int isRotating;
	public static int spin_by;
	public static ArrayList<Double> degrees_left;
	public static ArrayList<Func> degrees_left_func;
	public static boolean isSpeedUp = false;
	public static Graph mGraph = new Graph();
	public static CPU ai_cpu;
	public static boolean gameEnd =false; 
	public static boolean is_init = true;
	public static double lastFrontLidarDis = 0;
	public static boolean isRotateRight = false;
	public static double changedRight = 0;
	public static double changedLeft = 0;
	public static boolean tryToEscape = false;
	public static int leftOrRight = 1;
	public static boolean  is_finish = true;
	public static boolean isLeftRightRotationEnable = true;
	public static boolean is_risky = false;
	public static boolean try_to_escape = false;
	public static double  risky_dis = 0;
	public static boolean is_lidars_max = false;
	public static boolean start_return_home = false;
	public static Point init_point;
	public static int counter = 0;
	public static double lastGyroRotation = 0;

	public static void update(int deltaTime) {
		Utils.gameUpdates(deltaTime);
	}
}
