package Auto_pack;
import java.util.ArrayList;

public class Algorithm {

	enum PixelState {blocked,explored,unexplored,visited};
	
	
	int map_size = 3000;
	PixelState map[][];
	Drone drone;
	Point droneStartingPoint;
	ArrayList<Point> points;
	int isRotating;
	ArrayList<Double> degrees_left;
	ArrayList<Func> degrees_left_func;
	boolean isSpeedUp = false;
	Graph mGraph = new Graph();
	CPU ai_cpu;
	
	public Algorithm(Map realMap) {
		degrees_left = new ArrayList<>();
		degrees_left_func =  new ArrayList<>();
		points = new ArrayList<Point>();	
		drone = new Drone(realMap);
		drone.addLidar(0);
		drone.addLidar(90);
		drone.addLidar(-90);
		Utils.initMap(this);		
		isRotating = 0;
		ai_cpu = new CPU(200,"Auto_AI");
		ai_cpu.addFunction(this::update);
	}
	
	public void update(int deltaTime) {
		Utils.updateForAlgo(deltaTime , this);
	}
	
	boolean is_init = true;
	double lastFrontLidarDis = 0;
	boolean isRotateRight = false;
	double changedRight = 0;
	double changedLeft = 0;
	boolean tryToEscape = false;
	int leftOrRight = 1;
	double max_rotation_to_direction = 20;
	boolean  is_finish = true;
	boolean isLeftRightRotationEnable = true;
	boolean is_risky = false;
	int max_risky_distance = 150;
	boolean try_to_escape = false;
	double  risky_dis = 0;
	int max_angle_risky = 10;
	boolean is_lidars_max = false;
	double save_point_after_seconds = 3;
	double max_distance_between_points = 100;
	boolean start_return_home = false;
	Point init_point;
	int counter = 0;
	double lastGyroRotation = 0;
}
