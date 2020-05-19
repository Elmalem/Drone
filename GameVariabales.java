package Auto_pack;

import java.util.ArrayList;
import java.util.List;

/*
 * Storage of static data variables
 */

public class GameVariabales {

	public static enum PixelState {
		blocked, explored, unexplored, visited
	};

	public static void init() {
		GameVariabales.realMap = new Map(Config.imageSourcePath + "\\Maps\\p1" + Config.map_index + ".png");
		GameVariabales.degrees_left = new ArrayList<>();
		GameVariabales.degrees_left_func = new ArrayList<>();
		GameVariabales.points = new ArrayList<Point>();
		GameVariabales.drone = new Drone();
		GameVariabales.drone.addLidar(0);
		GameVariabales.drone.addLidar(90);
		GameVariabales.drone.addLidar(-90);
		GameVariabales.isRotating = 0;
		Utils.initMap();
		
		Painter painter = new Painter();
		painter.setBounds(0, 0, 2000, 2000);
		Visualizator.frame.getContentPane().add(painter);
		
		GraphPainter graphPainter = new GraphPainter();
		graphPainter.setBounds(0, 0, 2000, 2000);
		Visualizator.graphFrame.getContentPane().add(graphPainter);
		
		Utils.dronePlay();

		CPU painterCPU = new CPU(200, "painter"); // 60 FPS painter
		painterCPU.addFunction(Visualizator.frame::repaint);
		painterCPU.play();
		
		CPU painterGraphCPU = new CPU(200, "painter graph"); // 60 FPS painter
		painterGraphCPU.addFunction(Visualizator.graphFrame::repaint);
		painterGraphCPU.play();

		CPU batteryCPU = new CPU(200 , "battery cpu");
		batteryCPU.addFunction(Utils::batteryUpdate);
		batteryCPU.play();
		
		CPU ai_cpu = new CPU(200, "Auto_AI");
		ai_cpu.addFunction(Utils::updateAi);
		ai_cpu.play();

		// Add this CPU to make the rotate updates program easier
		CPU rotatingCPU = new CPU(200, "rotate");
		rotatingCPU.addFunction(Utils::rotateUpdate);
		rotatingCPU.play();

		CPU updatesCPU = new CPU(60, "updates");
		updatesCPU.addFunction(GameVariabales.drone::update);
		updatesCPU.play();

		CPU infoCPU = new CPU(6, "update_info");
		infoCPU.addFunction(Utils::updateInfo);
		infoCPU.play();

	}

	public static boolean return_home = false, toogleStop = true, toogleRealMap = true, toogleAI = false;
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
	public static Graph graph;
	public static boolean gameEnd = false;
	public static boolean is_init = true;
	public static double lastFrontLidarDis = 0;
	public static boolean isRotateRight = false;
	public static double changedRight = 0;
	public static double changedLeft = 0;
	public static boolean tryToEscape = false;
	public static int leftOrRight = 1;
	public static boolean is_finish = true;
	public static boolean isLeftRightRotationEnable = true;
	public static boolean is_risky = false;
	public static boolean try_to_escape = false;
	public static double risky_dis = 0;
	public static boolean is_lidars_max = false;
	public static boolean start_return_home = false;
	public static Point init_point;
	public static int counter = 0;
	public static double lastGyroRotation = 0;
}
