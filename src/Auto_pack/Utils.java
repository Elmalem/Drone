package Auto_pack;
import java.util.Random;
import javax.swing.JLabel;
import Auto_pack.Algorithm.PixelState;

// This class present most of the functionalities in the program

public class Utils {
	
	public static void updateInfo(int deltaTime , JLabel info_label , JLabel info_label2 , Algorithm algo) {
		info_label.setText(algo.drone.getInfoHTML());
		info_label2.setText("<html>" + String.valueOf(algo.counter) + " <BR>isRisky:" + String.valueOf(algo.is_risky) + 
				"<BR>" + String.valueOf(algo.risky_dis) +  "<BR> Time : " + String.valueOf(Timer.getTimeBySeconds()) + "</html>");
	}

	public static void stopCPUS() {
		CPU.stopAllCPUS();
	}
	
	public static void resumseCPUS() {
		CPU.resumeAllCPUS();
	}
	
	public static void play(Algorithm algo) {
		algo.drone.play();
		algo.ai_cpu.play();
	}

	// CM size sign
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
	
	public static void setPixel(double x, double y,PixelState state , PixelState[][] map) {
		int xi = (int)x;
		int yi = (int)y;
		
		if(state == PixelState.visited) {
			map[xi][yi] = state; 
			return;
		}
		
		if(map[xi][yi] == PixelState.unexplored) {
			map[xi][yi] = state; 
		}
	}
	
	public static double getRotationBetweenPoints (Point from, Point to) {
		double y_diff = from.y - to.y;
		double x_diff = from.x - to.x;
		double radians = Math.atan(y_diff/ x_diff);	
		double rotation = radians * 180 / Math.PI; 
		return rotation;
	}
	
	public static void ai(int deltaTime , Algorithm algo) {		
		if(!Simulator.toogleAI) {
			return;
		}
	
		Point dronePoint = algo.drone.getOpticalSensorLocation();

		if(algo.is_init) {
			Utils.speedUp(algo);
			// Start location
			algo.init_point = new Point(dronePoint);
			algo.points.add(dronePoint);
			algo.mGraph.addVertex(dronePoint);
			algo.is_init = false;
		}
		
		if(Simulator.return_home) {
			// 100 is max_distance_between_points
			if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(algo), dronePoint) <  100) {
				// 20 is max_distance_between_points / 5
				if(algo.points.size() <= 1 && Utils.getDistanceBetweenPoints(Utils.getLastPoint(algo), dronePoint) <  20) {
					Utils.speedDown(algo);
				} else {
					Utils.removeLastPoint(algo);
				}
			}
		} else {
			// 100 is max_distance_between_points
			if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(algo), dronePoint) >=  100) {
				/* $$$$$$$$$$$$$$$$$$$ */
				System.out.println("SIGN...");
				/* $$$$$$$$$$$$$$$$$$$ */
				algo.points.add(dronePoint);
				algo.mGraph.addVertex(dronePoint);
			}
		}
		
		if(!algo.is_risky) {
			for (int i = 0; i < algo.drone.lidars.size(); i++) {
				Lidar lidar = algo.drone.lidars.get(i);
				switch(i) {
				// 150 present algo max risky distance
				// 50 is 150 / 3
					case 0:
						if(lidar.current_distance <= 150 ) {
							algo.is_risky = true;
							algo.risky_dis = lidar.current_distance;				
						}
						break;
					case 1:
					case 2:
						if(lidar.current_distance <= 50 ) {
							algo.is_risky = true;
						}
						break;
				}
			}
		} else {
			if(!algo.try_to_escape) {
				algo.try_to_escape = true;
				
				Lidar lidar0 = algo.drone.lidars.get(0);
				double forward_lidar_current_dis = lidar0.current_distance;
				
				Lidar lidar1 = algo.drone.lidars.get(1);
				double right_lidar_current_dis = lidar1.current_distance;
				
				Lidar lidar2 = algo.drone.lidars.get(2);
				double left_lidar_current_dis = lidar2.current_distance;		
				
				// 10 present algorithm max_angle_risky
				int spin_by = 10;
								
				// 30 meter from max lidar length
				if(right_lidar_current_dis > 270 && left_lidar_current_dis > 270) {
				algo.is_lidars_max = true;
				Point l1 = Utils.getPointByDistance(dronePoint, lidar1.degrees + algo.drone.getGyroRotation(), lidar1.current_distance);
				Point l2 = Utils.getPointByDistance(dronePoint, lidar2.degrees + algo.drone.getGyroRotation(), lidar2.current_distance);
				Point last_point = Utils.getAvgLastPoint(algo);
				double dis_to_lidar1 = Utils.getDistanceBetweenPoints(last_point,l1);
				double dis_to_lidar2 = Utils.getDistanceBetweenPoints(last_point,l2);
				
				if(Simulator.return_home) {
					// 100 is max_distance_between_points
					if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(algo), dronePoint) <  100) {
						Utils.removeLastPoint(algo);
					}
				} else {
					// 100 is max_distance_between_points
					if( Utils.getDistanceBetweenPoints(Utils.getLastPoint(algo), dronePoint) >=  100) {
						algo.points.add(dronePoint);
						algo.mGraph.addVertex(dronePoint);
					}
				}
				
				// 30 meter distance from the edges case
				spin_by = 90;
				if(Simulator.return_home) {
					spin_by *= -1;
				}
				
				if(dis_to_lidar1 < dis_to_lidar2) {
					spin_by *= (-1 ); 
				}
								
				
			} else {
				// If my left distance grater than the right one spin by oposite
				if(right_lidar_current_dis < left_lidar_current_dis) {
					spin_by *= (-1 ); 
				}
				else if(algo.risky_dis >= 100) {
					spin_by *= (-1 ); 
//					int risky_dis_from_algo = (int)algo.risky_dis;
//					switch(risky_dis_from_algo) {
//						
//					}
				}
			}
				
			if((right_lidar_current_dis<=1 && left_lidar_current_dis<=1 && forward_lidar_current_dis<=1) && (dronePoint.x > 1 && dronePoint.y > 1)) {
				System.out.println("Game over !");
				stopCPUS();
			}
			Utils.spinBy(spin_by,true, algo, new Func() { 
					@Override
					public void method() {
						algo.try_to_escape = false;
						algo.is_risky = false;
					}
			});
		}
	}
	}
	
	public static void updateForAlgo(int deltaTime , Algorithm algo) {
		Utils.updateVisited(algo);
		Utils.updateMapByLidars(algo);
		
		Utils.ai(deltaTime , algo);
		
		
		if(algo.isRotating != 0) {
			Utils.updateRotating(deltaTime , algo);
		}
		if(algo.isSpeedUp) {
			algo.drone.speedUp(deltaTime);
		} else {
			algo.drone.slowDown(deltaTime);
		}
		
	}
	
	
	
	public static void initMap(Algorithm algo) {
		algo.map = new PixelState[algo.map_size][algo.map_size];
		for(int i=0;i<algo.map_size;i++) {
			for(int j=0;j<algo.map_size;j++) {
				algo.map[i][j] = PixelState.unexplored;
			}
		}
		
		algo.droneStartingPoint = new Point(algo.map_size/2,algo.map_size/2);
	}
	
	
	public static void doLeftRight(Algorithm algo) {
		if(algo.is_finish) {
			algo.leftOrRight *= -1;
			algo.counter++;
			algo.is_finish = false;
			// 20 is algorithm max_rotation_to_direction
			Utils.spinBy(20 * algo.leftOrRight,false , algo,new Func() {
				@Override
				public void method() {
					algo.is_finish = true;
				}
			});
		}
	}
	
	public static void speedUp(Algorithm algo) {
		algo.isSpeedUp = true;
	}
	
	public static void speedDown(Algorithm algo) {
		algo.isSpeedUp = false;
	}
	
	
	public static void updateVisited(Algorithm algo) {
		Point dronePoint = algo.drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + algo.droneStartingPoint.x,dronePoint.y + algo.droneStartingPoint.y);
		Utils.setPixel(fromPoint.x,fromPoint.y,PixelState.visited,algo.map);		
	}

	public static void updateMapByLidars(Algorithm algo) {
		Point dronePoint = algo.drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.x + algo.droneStartingPoint.x,dronePoint.y + algo.droneStartingPoint.y);
		
		for(int i=0;i<algo.drone.lidars.size();i++) {
			Lidar lidar = algo.drone.lidars.get(i);
			double rotation = algo.drone.getGyroRotation() + lidar.degrees;
			for(int distanceInCM=0;distanceInCM < lidar.current_distance;distanceInCM++) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, distanceInCM);
				Utils.setPixel(p.x,p.y,PixelState.explored,algo.map);
			}
			
			if(lidar.current_distance > 0 && lidar.current_distance < Config.lidarLimit - Config.lidarNoise) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, lidar.current_distance);
				Utils.setPixel(p.x,p.y,PixelState.blocked,algo.map);
			}
		}
	}
	
	public static void updateRotating(int deltaTime , Algorithm algo) {
		
		if(algo.degrees_left.size() == 0) {
			return;
		}
		
		double degrees_left_to_rotate = algo.degrees_left.get(0);
		boolean isLeft = true;
		if(degrees_left_to_rotate > 0) {
			isLeft = false;
		}
		
		double curr =  algo.drone.getGyroRotation();
		double just_rotated = 0;
		
		if(isLeft) {
			
			just_rotated = curr - algo.lastGyroRotation;
			if(just_rotated > 0) {
				just_rotated = -(360 - just_rotated);
			}
		} else {
			just_rotated = curr - algo.lastGyroRotation;
			if(just_rotated < 0) {
				just_rotated = 360 + just_rotated;
			}
		}
		
	
		 
		algo.lastGyroRotation = curr;
		degrees_left_to_rotate-=just_rotated;
		algo.degrees_left.remove(0);
		algo.degrees_left.add(0,degrees_left_to_rotate);
		
		if((isLeft && degrees_left_to_rotate >= 0) || (!isLeft && degrees_left_to_rotate <= 0)) {
			algo.degrees_left.remove(0);
			
			Func func = algo.degrees_left_func.get(0);
			if(func != null) {
				func.method();
			}
			algo.degrees_left_func.remove(0);
			
			
			if(algo.degrees_left.size() == 0) {
				algo.isRotating = 0;
			}
			return; 
		}
		
		int direction = (int)(degrees_left_to_rotate / Math.abs(degrees_left_to_rotate));
		algo.drone.rotateLeft(deltaTime * direction);
		
	}
	
	public static void spinBy(double degrees,boolean isFirst, Algorithm algo, Func func) {
		algo.lastGyroRotation = algo.drone.getGyroRotation();
		if(isFirst) {
			algo.degrees_left.add(0,degrees);
			algo.degrees_left_func.add(0,func);
		
			
		} else {
			algo.degrees_left.add(degrees);
			algo.degrees_left_func.add(func);
		}
		
		algo.isRotating =1;
	}
	
	public static void spinBy(double degrees,boolean isFirst , Algorithm algo) {
		algo.lastGyroRotation = algo.drone.getGyroRotation();
		if(isFirst) {
			algo.degrees_left.add(0,degrees);
			algo.degrees_left_func.add(0,null);
		} else {
			algo.degrees_left.add(degrees);
			algo.degrees_left_func.add(null);
		}
		
		algo.isRotating =1;
	}
	
	public static void spinBy(double degrees , Algorithm algo) {
		algo.lastGyroRotation = algo.drone.getGyroRotation();
		
		algo.degrees_left.add(degrees);
		algo.degrees_left_func.add(null);
		algo.isRotating = 1;
	}
	
	public static Point getLastPoint(Algorithm algo) {
		if(algo.points.size() == 0) {
			return algo.init_point;
		}
		
		Point p1 = algo.points.get(algo.points.size()-1);
		return p1;
	}
	
	public static Point removeLastPoint(Algorithm algo) {
		if(algo.points.isEmpty()) {
			return algo.init_point;
		}
		
		return algo.points.remove(algo.points.size()-1);
	}
	
	
	public static Point getAvgLastPoint(Algorithm algo) {
		if(algo.points.size() < 2) {
			return algo.init_point;
		}
		
		Point p1 = algo.points.get(algo.points.size()-1);
		Point p2 = algo.points.get(algo.points.size()-2);
		return new Point((p1.x + p2.x) /2, (p1.y + p2.y) /2);
	}

	public static double getDistanceBetweenPoints(Point from, Point to) {
		double x1 = (from.x - to.x)*(from.x - to.x);
		double y1 = (from.y-to.y)*(from.y-to.y);
		return Math.sqrt(x1+y1);	
	}
	
}
