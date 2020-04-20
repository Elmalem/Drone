package Auto_pack;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Drone {
	
	private double gyroRotation;
	private Point sensorOpticalFlow;
	private Point location;
	public List<Lidar> lidars;
	private double rotation;
	private double speed;
	private CPU cpu;
	
	public Drone() {
		location = new Point();
		sensorOpticalFlow = new Point();
		lidars = new ArrayList<>();
		speed = 0.5;	
		rotation = 0;
		gyroRotation = rotation;	
		cpu = new CPU(100,"Drone");
	}
	
	public void play() {
		cpu.play();
	}
	
	public void stop() {
		cpu.stop();
	}
	
	
	public void addLidar(int degrees) {
		Lidar lidar = new Lidar(degrees);
		lidars.add(lidar);
		cpu.addFunction(lidar::getSimulationDistance);
	}
	
	public Point getPointOnMap() {
		double x = Config.startPoints[Config.map_index-1].x + location.x;
		double y = Config.startPoints[Config.map_index-1].y + location.y;
		return new Point(x,y);
	}
	
	public void update(int deltaTime) {	
		double distancedMoved;
		if(lidars.get(0).current_distance < Config.minimumCenterDistanceToWall && !GameVariabales.is_init) {
			speed = 0;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int rotateTo = lidars.get(1).current_distance < lidars.get(2).current_distance ? 2 : 1;
			switch(rotateTo) {
			case 1 : 
				this.rotateLeft(deltaTime);
				break;
			case 2 : 
				this.rotateRight(deltaTime);
				break;
			}
			speed = 1;
		}
		distancedMoved = (speed*100)*((double)deltaTime/1000);
		location =  Utils.getPointByDistance(location, rotation, distancedMoved);
		double noiseToDistance = Utils.noiseBetween(Config.min_motion_accuracy,Config.max_motion_accuracy,false);
		sensorOpticalFlow = Utils.getPointByDistance(sensorOpticalFlow, rotation, distancedMoved*noiseToDistance);		
		double noiseToRotation = Utils.noiseBetween(Config.min_rotation_accuracy,Config.max_rotation_accuracy,false);
		double milli_per_minute = 60000;
		gyroRotation += (1 - noiseToRotation) * deltaTime/milli_per_minute;
		gyroRotation = formatRotation(gyroRotation);
	}
	
	public static double formatRotation(double rotationValue) {
		rotationValue %= 360;
		if(rotationValue < 0) {
			rotationValue = 360 - rotationValue;
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
		double rotationChanged = Config.rotation_per_second * deltaTime / 1000;	
		rotation += rotationChanged;
		rotation = formatRotation(rotation);	
		gyroRotation += rotationChanged;
		gyroRotation = formatRotation(gyroRotation);
	}
	
	public void rotateRight(int deltaTime) {
		double rotationChanged = -Config.rotation_per_second * deltaTime / 1000;	
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
		info += "Location: " + location +"<br>";
		info += "gyroRotation: " + df.format(gyroRotation) +"<br>";
		info += "sensorOpticalFlow: " + sensorOpticalFlow +"<br>";
		info += "</html>";
		return info;
	}
}
