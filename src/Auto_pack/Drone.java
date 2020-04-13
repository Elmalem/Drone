package Auto_pack;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Drone {
	
	public String drone_img_path = Config.imageSourcePath + "\\Maps\\drone_3_pixels.png";
	private double gyroRotation;
	private double rotation;
	private double speed;
	private Point sensorOpticalFlow;
	private Point pointFromStart;
	public Point startPoint;
	public List<Lidar> lidars;
	public Map realMap;
	private CPU cpu;
	
	public Drone(Map realMap) {
		this.realMap = realMap;
		this.startPoint = realMap.drone_start_point;
		pointFromStart = new Point();
		sensorOpticalFlow = new Point();
		lidars = new ArrayList<>();
		speed = 0.2;	
		rotation = 0;
		gyroRotation = rotation;	
		cpu = new CPU("Drone");
	}
	
	public void play() {
		cpu.play();
	}
	
	public void stop() {
		cpu.stop();
	}
	
	
	public void addLidar(int degrees) {
		Lidar lidar = new Lidar(this,degrees);
		lidars.add(lidar);
		cpu.functions_list.add(lidar::getSimulationDistance);
	}
	
	public Point getPointOnMap() {
		double x = startPoint.x + pointFromStart.x;
		double y = startPoint.y + pointFromStart.y;
		return new Point(x,y);
	}
	
	public void update(int deltaTime) {
		
		double distancedMoved = speed * (double)deltaTime/10;
		
//		System.out.println("Distance from start: " + distancedMoved + " , deltaTime : " + deltaTime);
		
		pointFromStart =  Utils.getPointByDistance(pointFromStart, rotation, distancedMoved);
		
		double noiseToDistance = Utils.noiseBetween(Config.min_motion_accuracy,Config.max_motion_accuracy,false);
		
		sensorOpticalFlow = Utils.getPointByDistance(sensorOpticalFlow, rotation, distancedMoved*noiseToDistance);
		
		double noiseToRotation = Utils.noiseBetween(Config.min_rotation_accuracy,Config.max_rotation_accuracy,false);
		
		// delta / milli_per_minute
		
		gyroRotation += (1-noiseToRotation)*deltaTime/60000;
		gyroRotation = formatRotation(gyroRotation);
	}
	
	public static double formatRotation(double rotationValue) {
		rotationValue %= 360;
		if(rotationValue < 0) {
			rotationValue = 360 -rotationValue;
		}
		return rotationValue;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public double getGyroRotation() {
		return gyroRotation;
	}
	
	public Point getOpticalSensorLocation() {
		return new Point(sensorOpticalFlow);
	}

	
	public void rotateLeft(int deltaTime) {
		double rotationChanged = Config.rotation_per_second*deltaTime/1000;
		
		rotation += rotationChanged;
		rotation = formatRotation(rotation);
		
		gyroRotation += rotationChanged;
		gyroRotation = formatRotation(gyroRotation);
	}
	
	public void rotateRight(int deltaTime) {
		double rotationChanged = -Config.rotation_per_second*deltaTime/1000;
		
		rotation += rotationChanged;
		rotation = formatRotation(rotation);
		
		gyroRotation += rotationChanged;
		gyroRotation = formatRotation(gyroRotation);
	}
	
	public void speedUp(int deltaTime) {
		speed += (Config.accelerate_per_second*deltaTime/1000);
		if(speed > Config.max_speed) {
			speed =Config.max_speed;
		}
	}
	
	public void slowDown(int deltaTime) {
		speed -= (Config.accelerate_per_second*deltaTime/1000);
		if(speed < 0) {
			speed = 0;
		}
	}
	
	
	boolean initPaint = false;
	BufferedImage mImage;
	int j=0;
	
	public String getInfoHTML() {
		DecimalFormat df = new DecimalFormat("#.####");
		
		String info = "<html>";
		info += "Rotation: " + df.format(rotation) +"<br>";
		info += "Location: " + pointFromStart +"<br>";
		info += "gyroRotation: " + df.format(gyroRotation) +"<br>";
		info += "sensorOpticalFlow: " + sensorOpticalFlow +"<br>";
		info += "</html>";
		return info;
	}
}
