package Auto_pack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Drone {

	// Location and movement variables
	private double gyroRotation;
	private double rotation;
	private double speed;
	// Location and movement functionality
	private Point sensorOpticalFlow;
	private Point location;
	private CPU cpu;

	private List<Lidar> lidars;

	private Battery battery;

	public Drone() {
		this.location = new Point();
		this.sensorOpticalFlow = new Point();
		this.lidars = new ArrayList<>();
		this.speed = 0.5;
		this.rotation = 0;
		this.gyroRotation = rotation;
		this.cpu = new CPU(100, "Drone");
		this.setBattery(new Battery());
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
		double x = Config.startPoints[Config.map_index - 1].getX() + location.getX();
		double y = Config.startPoints[Config.map_index - 1].getY() + location.getY();
		return new Point(x, y);
	}

	/*
	 * Rearrange rotation before collision OR when we want to return home and going
	 * far away
	 */
	public void update(int deltaTime) {
		Long dt = (long) 1000;

		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();

		if ((GameVariabales.toogleAI && lidars.get(0).getCurrentDistance() < Config.minimumCenterDistanceToWall
				&& !GameVariabales.is_init)) {
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int rotateTo = lidars.get(1).getCurrentDistance() < lidars.get(2).getCurrentDistance() ? 2 : 1;
			switch (rotateTo) {
			case 1:
				rotateLeft(deltaTime);
				break;
			case 2:
				rotateRight(deltaTime);
				break;
			}
		}

		// Return home situation
		if (Timer.getTimeBySeconds() - Utils.lastTime > 2
				&& !GameVariabales.is_init
				&& Utils.lastDistance < Utils.getDistanceBetweenPoints(this.sensorOpticalFlow,
						GameVariabales.init_point)
				&& Utils.isReturnHome(dronePoint, deltaTime) 
				&& !Utils.isHomeDirection(dronePoint)) {
			uturn(deltaTime);
		}

		goForward(deltaTime);
	}

	public void goForward(int deltaTime) {
		double distancedMoved = (speed * 100) * ((double) deltaTime / 1000);
		location = Utils.getPointByDistance(location, rotation, distancedMoved);
		double noiseToDistance = Utils.noiseBetween(Config.min_motion_accuracy, Config.max_motion_accuracy, false);
		sensorOpticalFlow = Utils.getPointByDistance(sensorOpticalFlow, rotation, distancedMoved * noiseToDistance);
		double noiseToRotation = Utils.noiseBetween(Config.min_rotation_accuracy, Config.max_rotation_accuracy, false);
		gyroRotation += (1 - noiseToRotation) * deltaTime / 60000;
		gyroRotation = formatRotation(gyroRotation);
	}

	public static double formatRotation(double rotationValue) {
		rotationValue %= 360;
		if (rotationValue < 0) {
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

	public void uturn(int deltaTime) {
		rotation += 180;
		rotation = formatRotation(rotation);
		gyroRotation += 180;
		gyroRotation = formatRotation(gyroRotation);
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
		speed += (Config.accelerate_per_second * deltaTime / 1000);
		if (speed > Config.max_speed) {
			speed = Config.max_speed;
		}
	}

	public void setSpeed(double s) {
		this.speed = s;
	}

	public double getSpeed() {
		return speed;
	}

	public List<Lidar> getLidars() {
		return this.lidars;
	}

	public void slowDown(int deltaTime) {
		speed -= (Config.accelerate_per_second * deltaTime / 1000);
		if (speed < 0) {
			speed = 0;
		}
	}

	public String getInfoHTML() {
		DecimalFormat df = new DecimalFormat("#.####");
		String info = "<html>";
		info += "Rotation: " + df.format(rotation) + "<br>";
		info += "Location: " + location + "<br>";
		info += "gyroRotation: " + df.format(gyroRotation) + "<br>";
		info += "sensorOpticalFlow: " + sensorOpticalFlow + "<br>";
		info += "</html>";
		return info;
	}

	public Battery getBattery() {
		return battery;
	}

	public void setBattery(Battery battery) {
		this.battery = battery;
	}
}