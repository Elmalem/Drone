package Auto_pack;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Utils {
	
	public static void stopAllCPUS() {
		for(int i=0;i<GameVariabales.all_cpus.size();i++) {
			GameVariabales.all_cpus.get(i).isPlay = false;
		}
	}
	
	public static void resumeAllCPUS() {
		for(int i=0; i<GameVariabales.all_cpus.size(); i++) {
			GameVariabales.all_cpus.get(i).resume();
		}
	}
	
	public static void updateInfo(int deltaTime , JLabel info_label , JLabel info_label2) {
		info_label.setText(GameVariabales.drone.getInfoHTML());
		info_label2.setText("<html>" + String.valueOf(GameVariabales.counter) + " <BR>isRisky:" + String.valueOf(GameVariabales.is_risky) + 
				"<BR>" + String.valueOf(GameVariabales.risky_dis) +  "<BR> Time : " + String.valueOf(Timer.getTimeBySeconds()) + "</html>");
	}

	public static void stopCPUS() {
		Utils.stopAllCPUS();
	}
	
	public static void resumseCPUS() {
		Utils.resumeAllCPUS();
	}
	
	public static void play() {
		GameVariabales.drone.play();
	}

	// CM sign
	public static Point getPointByDistance(Point fromPoint, double rotation, double distance) {
		double radians = Math.PI*(rotation/180);
		
		double i= distance/Config.CMPerPixel;
		double xi = fromPoint.x + Math.cos(radians)*i;
		double yi = fromPoint.y + Math.sin(radians)*i;
		
		return new Point(xi,yi);
	}
	
	public static double noiseBetween(double min,double max,boolean isNegative) {
		Random rand = new Random();
		double noiseToDistance = 1;
		double noise = (min + rand.nextFloat()*(max-min))/100;
		if(!isNegative) {
			return noiseToDistance + noise;
		}
		
		if(rand.nextBoolean()) {
			return noiseToDistance + noise;
		} else {
			return noiseToDistance - noise;
		}
		
	}
	
	public static void setPixel(double x, double y,GameVariabales.PixelState state , GameVariabales.PixelState[][] map) {
		int xi = (int)x;
		int yi = (int)y;
		
		if(state == GameVariabales.PixelState.visited) {
			map[xi][yi] = state; 
			return;
		}
		
		if(map[xi][yi] == GameVariabales.PixelState.unexplored) {
			map[xi][yi] = state; 
		}
	}
	
	public static double getRotationBetweenPoints (Point from, Point to) {
		double y1 = from.y - to.y;
		double x1 = from.x - to.x;
		double radians = Math.atan(y1/ x1);	
		double rotation = radians * 180 / Math.PI; 
		return rotation;
	}
	
	public static void ai(int deltaTime) {		
		if(!Simulator.toogleAI) {
			return;
		}
			
		if(GameVariabales.is_init) {
			Utils.speedUp();
			Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
			GameVariabales.init_point = new Point(dronePoint);
			GameVariabales.points.add(dronePoint);
			GameVariabales.mGraph.addVertex(dronePoint);
			GameVariabales.is_init = false;
		}
		
		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();

		if(Simulator.return_home) {		
			if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) <  Config.max_distance_between_points) {
				if(GameVariabales.points.size() <= 1 && Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) <  Config.max_distance_between_points/5) {
					Utils.speedDown();
				} else {
					Utils.removeLastPoint();
				}
			}
		} else {
			if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) >=  Config.max_distance_between_points) {
				GameVariabales.points.add(dronePoint);
				GameVariabales.mGraph.addVertex(dronePoint);
			}
		}
		
		GameVariabales.spin_by = Config.max_angle_risky;
		
		if(!GameVariabales.is_risky) {
		
			Lidar lidar0 = GameVariabales.drone.lidars.get(0);
			if(lidar0.current_distance <= Config.max_risky_distance ) {
				GameVariabales.is_risky = true;
				GameVariabales.risky_dis = lidar0.current_distance;				
			}
			
			Lidar lidar1 = GameVariabales.drone.lidars.get(1);
			if(lidar1.current_distance <= Config.max_risky_distance/3 ) {
				GameVariabales.is_risky = true;
			}
			
			Lidar lidar2 = GameVariabales.drone.lidars.get(2);
			if(lidar2.current_distance <= Config.max_risky_distance/3 ) {
				GameVariabales.is_risky = true;
			}

			
		} else {
			if(!GameVariabales.try_to_escape) {
				
				GameVariabales.try_to_escape = true;
				
				Lidar lidar1 = GameVariabales.drone.lidars.get(1);
				double a = lidar1.current_distance;
				
				Lidar lidar2 = GameVariabales.drone.lidars.get(2);
				double b = lidar2.current_distance;
				
				Lidar lidar0 = GameVariabales.drone.lidars.get(0);
				double c = lidar0.current_distance;
				
				if(a > 270 && b > 270) {	
					
				GameVariabales.is_lidars_max = true;
//				Point l1 = Utils.getPointByDistance(dronePoint, lidar1.degrees + GameVariabales.drone.getGyroRotation(), lidar1.current_distance);
//				Point l2 = Utils.getPointByDistance(dronePoint, lidar2.degrees + GameVariabales.drone.getGyroRotation(), lidar2.current_distance);
//				Point last_point = Utils.getAvgLastPoint();
//				double dis_to_lidar1 = Utils.getDistanceBetweenPoints(last_point,l1);
//				double dis_to_lidar2 = Utils.getDistanceBetweenPoints(last_point,l2);
				
				if(Simulator.return_home) {
					if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) <  Config.max_distance_between_points) {
						Utils.removeLastPoint();
					}
				} else {
					if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) >=  Config.max_distance_between_points) {
						GameVariabales.points.add(dronePoint);
						GameVariabales.mGraph.addVertex(dronePoint);
					}
				}
				
				Point l1 = Utils.getPointByDistance(dronePoint, GameVariabales.drone.lidars.get(1).degrees + GameVariabales.drone.getGyroRotation(), GameVariabales.drone.lidars.get(1).current_distance);
				Point l2 = Utils.getPointByDistance(dronePoint, GameVariabales.drone.lidars.get(2).degrees + GameVariabales.drone.getGyroRotation(), GameVariabales.drone.lidars.get(2).current_distance);
				Point last_point = Utils.getAvgLastPoint();
				double dis_to_lidar1 = Utils.getDistanceBetweenPoints(last_point,l1);
				double dis_to_lidar2 = Utils.getDistanceBetweenPoints(last_point,l2);
				
				GameVariabales.spin_by = 90;
							 	
				if(Simulator.return_home || dis_to_lidar1 < dis_to_lidar2) {
					GameVariabales.spin_by *= -1;
				}
				
			} else {			
				if(a < b || GameVariabales.risky_dis >= 100) {
					GameVariabales.spin_by *= (-1 ); 
				}
			}
				
			if(((a <= 1 && b <= 1 && c <= 1) && (Math.abs(GameVariabales.drone.getPointOnMap().x - Config.startPoints[Config.map_index - 1].x) > 1 && Math.abs(GameVariabales.drone.getPointOnMap().y - Config.startPoints[Config.map_index - 1].y) > 1))) {
				stopCPUS();
				GameVariabales.gameEnd=true;
				gameOverMessage();
				System.exit(0);
			}
			
			Utils.spinBy(GameVariabales.spin_by,true, new Func() { 
					@Override
					public void method() {
						GameVariabales.try_to_escape = false;
						GameVariabales.is_risky = false;
					}
			});
		}
	}
	}
	
	public static void unbroken(int deltaTime) {
		if(!GameVariabales.is_init) {
		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
		Point l1 = Utils.getPointByDistance(dronePoint, GameVariabales.drone.lidars.get(1).degrees + GameVariabales.drone.getGyroRotation(), GameVariabales.drone.lidars.get(1).current_distance);
		Point l2 = Utils.getPointByDistance(dronePoint, GameVariabales.drone.lidars.get(2).degrees + GameVariabales.drone.getGyroRotation(), GameVariabales.drone.lidars.get(2).current_distance);
		Point last_point = Utils.getAvgLastPoint();
		double dis_to_lidar1 = Utils.getDistanceBetweenPoints(last_point,l1);
		double dis_to_lidar2 = Utils.getDistanceBetweenPoints(last_point,l2);
		System.out.println("Distance to lidar 1 : " + dis_to_lidar1);
		System.out.println("Distance to lidar 2 : " + dis_to_lidar2);

		if (((GameVariabales.drone.lidars.get(0).current_distance <= 100 && GameVariabales.drone.lidars.get(1).current_distance <= 100 && GameVariabales.drone.lidars.get(2).current_distance <= 100)) && (!GameVariabales.is_init)) {
			GameVariabales.spin_by *= -1;
		}	
		if(dis_to_lidar1 <= 50 && dis_to_lidar2 <= 50) {
			GameVariabales.spin_by *= -1;
		}
	  }
	} 
	
	public static void rotateUpdate(int deltaTime) {
		if(GameVariabales.isRotating != 0) {
			Utils.updateRotating(deltaTime);
		}
	}
	
	public static void gameUpdates(int deltaTime) {
		
		Utils.updateVisited();
		Utils.updateMapByLidars();
		
		Utils.ai(deltaTime);
		
		if (GameVariabales.isSpeedUp) {
			GameVariabales.drone.speedUp(deltaTime);
		} else {
			GameVariabales.drone.slowDown(deltaTime);
		}
	}
	
	
	
	public static void initMap() {
		
		GameVariabales.map = new GameVariabales.PixelState[Config.map_size][Config.map_size];
		
		for(int i=0;i<Config.map_size;i++) {
			for(int j=0;j<Config.map_size;j++) {
				GameVariabales.map[i][j] = GameVariabales.PixelState.unexplored;
			}
		}
		
		GameVariabales.droneStartingPoint = new Point(Config.map_size/2,Config.map_size/2);
	}
	
	// Unused
	public static void doLeftRight() {
		
		if(GameVariabales.is_finish) {
			GameVariabales.leftOrRight *= -1;
			GameVariabales.counter++;
			GameVariabales.is_finish = false;
			
			Utils.spinBy(Config.max_rotation_to_direction*GameVariabales.leftOrRight,false,new Func() {
				@Override
				public void method() {
					GameVariabales.is_finish = true;
				}
			});
		}
	}
	
	public static void speedUp() {
		GameVariabales.isSpeedUp = true;
	}
	
	public static void speedDown() {
		GameVariabales.isSpeedUp = false;
	}
	
	
	public static void updateVisited() {
		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + GameVariabales.droneStartingPoint.x,dronePoint.y + GameVariabales.droneStartingPoint.y);
		Utils.setPixel(fromPoint.x,fromPoint.y,GameVariabales.PixelState.visited,GameVariabales.map);		
	}

	public static void updateMapByLidars() {
		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + GameVariabales.droneStartingPoint.x,dronePoint.y + GameVariabales.droneStartingPoint.y);
		
		for(int i=0;i<GameVariabales.drone.lidars.size();i++) {
			Lidar lidar = GameVariabales.drone.lidars.get(i);
			double rotation = GameVariabales.drone.getGyroRotation() + lidar.degrees;
			for(int distanceInCM=0;distanceInCM < lidar.current_distance;distanceInCM++) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, distanceInCM);
				Utils.setPixel(p.x,p.y,GameVariabales.PixelState.explored,GameVariabales.map);
			}
			
			if(lidar.current_distance > 0 && lidar.current_distance < Config.lidarLimit - Config.lidarNoise) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, lidar.current_distance);
				Utils.setPixel(p.x,p.y,GameVariabales.PixelState.blocked,GameVariabales.map);
			}
		}
	}
	
	public static void updateRotating(int deltaTime) {
		
		if(GameVariabales.degrees_left.size() == 0) {
			return;
		}
		
		double degrees_left_to_rotate = GameVariabales.degrees_left.get(0);
		
		boolean isLeft = true;
		if(degrees_left_to_rotate > 0) {
			isLeft = false;
		}
		
		double curr =  GameVariabales.drone.getGyroRotation();
		double just_rotated = 0;
		
		if(isLeft) {
			
			just_rotated = curr - GameVariabales.lastGyroRotation;
			if(just_rotated > 0) {
				just_rotated = -(360 - just_rotated);
			}
		} else {
			just_rotated = curr - GameVariabales.lastGyroRotation;
			if(just_rotated < 0) {
				just_rotated = 360 + just_rotated;
			}
		}
		
		GameVariabales.lastGyroRotation = curr;
		degrees_left_to_rotate-=just_rotated;
		GameVariabales.degrees_left.remove(0);
		GameVariabales.degrees_left.add(0,degrees_left_to_rotate);
		
		if((isLeft && degrees_left_to_rotate >= 0) || (!isLeft && degrees_left_to_rotate <= 0)) {
			GameVariabales.degrees_left.remove(0);
			
			Func func = GameVariabales.degrees_left_func.get(0);
			if(func != null) {
				func.method();
			}
			GameVariabales.degrees_left_func.remove(0);
			
			
			if(GameVariabales.degrees_left.size() == 0) {
				GameVariabales.isRotating = 0;
			}
			return; 
		}
		
		int direction = (int)(degrees_left_to_rotate / Math.abs(degrees_left_to_rotate));
		GameVariabales.drone.rotateLeft(deltaTime * direction);
		
	}
	
	public static void spinBy(double degrees,boolean isFirst, Func func) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();
		if(isFirst) {
			GameVariabales.degrees_left.add(0,degrees);
			GameVariabales.degrees_left_func.add(0,func);		
		} else {
			GameVariabales.degrees_left.add(degrees);
			GameVariabales.degrees_left_func.add(func);
		}
		GameVariabales.isRotating =1;
	}
	
	public static void spinBy(double degrees,boolean isFirst) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();
		if(isFirst) {
			GameVariabales.degrees_left.add(0,degrees);
			GameVariabales.degrees_left_func.add(0,null);
		} else {
			GameVariabales.degrees_left.add(degrees);
			GameVariabales.degrees_left_func.add(null);
		}
		
		GameVariabales.isRotating =1;
	}
	
	public static void spinBy(double degrees) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();
		
		GameVariabales.degrees_left.add(degrees);
		GameVariabales.degrees_left_func.add(null);
		GameVariabales.isRotating = 1;
	}
	
	public static Point getLastPoint() {
		if(GameVariabales.points.size() == 0) {
			return GameVariabales.init_point;
		}
		
		Point p1 = GameVariabales.points.get(GameVariabales.points.size()-1);
		return p1;
	}
	
	public static Point removeLastPoint() {
		if(GameVariabales.points.isEmpty()) {
			return GameVariabales.init_point;
		}
		
		return GameVariabales.points.remove(GameVariabales.points.size()-1);
	}
	
	
	public static Point getAvgLastPoint() {
		if(GameVariabales.points.size() < 2) {
			return GameVariabales.init_point;
		}	
		Point p1 = GameVariabales.points.get(GameVariabales.points.size()-1);
		Point p2 = GameVariabales.points.get(GameVariabales.points.size()-2);
		return new Point((p1.x + p2.x) /2, (p1.y + p2.y) /2);
	}

	public static double getDistanceBetweenPoints(Point from, Point to) {
		double x1 = (from.x - to.x)*(from.x - to.x);
		double y1 = (from.y-to.y)*(from.y-to.y);
		return Math.sqrt(x1+y1);	
	}
	
	/////////////////////////////////////////////////////////////
	   public static void gameOverMessage()
	    {
	        JOptionPane.showMessageDialog(null, "Game Over!!!");
	    }	
	
}