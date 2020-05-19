package Auto_pack;
import java.util.Random;

/*
 * This class present drone optical sensors
 */

public class Lidar {
	/*
	 * We already removed the drone variable because some OOP reasons
	 */
	private double degrees;
	private double current_distance;
	
	public Lidar(double degrees) {
		this.degrees = degrees;
		this.current_distance = 0;
	}
	
	public double getDegrees() {
		return this.degrees;
	}
	
	public double getCurrentDistance() {
		return this.current_distance;
	}
	
	public double getDistance(int deltaTime) {
		Point actualPointToShoot= GameVariabales.drone.getPointOnMap();
		double rotation = GameVariabales.drone.getRotation() + degrees;
		
		double distanceInCM = 1;
		while(distanceInCM <= Config.lidarLimit) { 
			Point p = Utils.getPointByDistance(actualPointToShoot, rotation, distanceInCM);
			if(GameVariabales.realMap.isCollide((int)p.getX(),(int)p.getY())) {
				break;
		}
			distanceInCM++;
		}
		
		
		return distanceInCM;
	}
	
	public double getSimulationDistance(int deltaTime) {
		Random ran= new Random();
		double distanceInCM;
		// The minimum center distance to all added by us
		if(ran.nextFloat() <= 0.05f) { // 5% of the time, not getting an answer
			distanceInCM = Config.minimumCenterDistanceToWall;
		} else {
			distanceInCM = getDistance(deltaTime);
			distanceInCM += (int)ran.nextInt(Config.lidarNoise * 2) - Config.lidarNoise; // +- 5 CM to the final calc
		}
		this.current_distance = distanceInCM; // store it for instance get
		return distanceInCM;
	}
	
}
