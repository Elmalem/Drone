package Auto_pack;
import java.util.ArrayList;

public class Algorithm {

	enum PixelState {blocked,explored,unexplored,visited};
	
	public Algorithm(Map realMap) {
		mGraph = new Graph();
		degrees_left = new ArrayList<>();
		degrees_left_func =  new ArrayList<>();
		points = new ArrayList<Point>();	
		drone = new Drone(realMap);
		drone.addLidar(0);
		drone.addLidar(90);
		drone.addLidar(-90);
		Utils.initMap(this);		
		isRotating = 0;
		ai_cpu = new CPU("Algorithm cpu - AI");
		ai_cpu.functions_list.add(this::update);
	}
	
	public void update(int deltaTime) {
		Utils.updateForAlgo(deltaTime , this);
	}
	
	// Class configuration  by types
		
	int map_size = 3000;
	int isRotating;
	int leftOrRight = 1;
	int counter = 0;

	double lastFrontLidarDis = 0;
	double changedRight = 0;
	double changedLeft = 0;
	double  risky_dis = 0;
	double save_point_after_seconds = 3;
	double lastGyroRotation = 0;
	
	boolean is_init = true;
	boolean isRotateRight = false;
	boolean tryToEscape = false;
	boolean  is_finish = true;
	boolean isLeftRightRotationEnable = true;
	boolean is_risky = false;
	boolean try_to_escape = false;
	boolean is_lidars_max = false;
	boolean start_return_home = false;
	boolean isSpeedUp = false;

	Point init_point;
	Point droneStartingPoint;

	ArrayList<Point> points;
	ArrayList<Double> degrees_left;
	ArrayList<Func> degrees_left_func;
	
	PixelState map[][];
	
	Drone drone;
	
	Graph mGraph;
	
	CPU ai_cpu;	
}
