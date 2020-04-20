package Auto_pack;
import java.util.Random;

public class Lidar {
	double degrees;
	double current_distance;
	
	public Lidar(double degrees) {
		this.degrees = degrees;
		this.current_distance = 0;
	}
	
	public double getDistance(int deltaTime) {
		Point actualPointToShoot= GameVariabales.drone.getPointOnMap();
		double rotation = GameVariabales.drone.getRotation() + degrees;
		
		double distanceInCM = 1;
		while(distanceInCM <= Config.lidarLimit) { 
			Point p = Utils.getPointByDistance(actualPointToShoot, rotation, distanceInCM);
			if(GameVariabales.realMap.isCollide((int)p.x,(int)p.y)) {
				break;
		}
			distanceInCM++;
		}
		
		
		return distanceInCM;
	}
	
	public double getSimulationDistance(int deltaTime) {
		Random ran= new Random();
		double distanceInCM;
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
