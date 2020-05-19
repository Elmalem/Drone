package Auto_pack;

import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Storage the static functionality of the program with the main algorithm function
 */

public class Utils {

	public static void stopAllCPUS() {
		for (int i = 0; i < GameVariabales.all_cpus.size(); i++) {
			GameVariabales.all_cpus.get(i).setPlay();
		}
	}

	public static void resumeAllCPUS() {
		for (int i = 0; i < GameVariabales.all_cpus.size(); i++) {
			GameVariabales.all_cpus.get(i).resume();
		}
	}

	public static void updateInfo(int deltaTime, JLabel info_label, JLabel info_label2, JLabel graphInfo) {
		DecimalFormat dfff = new DecimalFormat("#.##");
		info_label.setText(GameVariabales.drone.getInfoHTML());
		info_label2.setText("<html>" + String.valueOf(GameVariabales.counter) + " <BR>isRisky:"
				+ String.valueOf(GameVariabales.is_risky) + "<BR>" + String.valueOf(GameVariabales.risky_dis)
				+ "<BR> Time : " + String.valueOf(Timer.getTimeBySeconds()) + "<BR> Battery : "
				+ String.valueOf(dfff.format(GameVariabales.drone.getBattery().getStamina())) + "</html>");
		if (GameVariabales.graph != null)
			graphInfo.setText(GameVariabales.graph.toHtmlString());
	}

	public static void stopCPUS() {
		Utils.stopAllCPUS();
	}

	public static void resumseCPUS() {
		Utils.resumeAllCPUS();
	}

	public static void dronePlay() {
		GameVariabales.drone.play();
	}

	// CM sign
	public static Point getPointByDistance(Point fromPoint, double rotation, double distance) {
		double radians = Math.PI * (rotation / 180);

		double i = distance / Config.CMPerPixel;
		double xi = fromPoint.getX() + Math.cos(radians) * i;
		double yi = fromPoint.getY() + Math.sin(radians) * i;

		return new Point(xi, yi);
	}

	public static double noiseBetween(double min, double max, boolean isNegative) {
		Random rand = new Random();
		double noiseToDistance = 1;
		double noise = (min + rand.nextFloat() * (max - min)) / 100;
		if (!isNegative) {
			return noiseToDistance + noise;
		}

		if (rand.nextBoolean()) {
			return noiseToDistance + noise;
		} else {
			return noiseToDistance - noise;
		}

	}

	public static void setPixel(double x, double y, GameVariabales.PixelState state,
			GameVariabales.PixelState[][] map) {
		int xi = (int) x;
		int yi = (int) y;

		if (state == GameVariabales.PixelState.visited) {
			map[xi][yi] = state;
			return;
		}

		if (map[xi][yi] == GameVariabales.PixelState.unexplored) {
			map[xi][yi] = state;
		}
	}

	public static double getRotationBetweenPoints(Point from, Point to) {
		double y1 = from.getY() - to.getY();
		double x1 = from.getX() - to.getX();
		double radians = Math.atan(y1 / x1);
		double rotation = radians * 180 / Math.PI;
		return rotation;
	}

	public static void ai(int deltaTime) {
		if (!GameVariabales.toogleAI) {
			return;
		}

		if (GameVariabales.is_init) {
			Utils.speedUp();
			Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
			GameVariabales.init_point = new Point(dronePoint);
			GameVariabales.graph = new Graph(GameVariabales.drone.getPointOnMap());
			GameVariabales.points.add(dronePoint);
			GameVariabales.is_init = false;
		}

		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();

		Utils.isReturnHome(dronePoint);

		GameVariabales.spin_by = Config.max_angle_risky;

		if (!GameVariabales.is_risky) {

			Lidar lidar0 = GameVariabales.drone.getLidars().get(0);
			if (lidar0.getCurrentDistance() <= Config.max_risky_distance) {
				GameVariabales.is_risky = true;
				GameVariabales.risky_dis = lidar0.getCurrentDistance();
			}

			Lidar lidar1 = GameVariabales.drone.getLidars().get(1);
			if (lidar1.getCurrentDistance() <= Config.max_risky_distance / 3) {
				GameVariabales.is_risky = true;
			}

			Lidar lidar2 = GameVariabales.drone.getLidars().get(2);
			if (lidar2.getCurrentDistance() <= Config.max_risky_distance / 3) {
				GameVariabales.is_risky = true;
			}

		} else {
			if (!GameVariabales.try_to_escape) {

				GameVariabales.try_to_escape = true;

				Lidar lidar1 = GameVariabales.drone.getLidars().get(1);
				double a = lidar1.getCurrentDistance();

				Lidar lidar2 = GameVariabales.drone.getLidars().get(2);
				double b = lidar2.getCurrentDistance();

				if (a > 270 && b > 270) {

					GameVariabales.is_lidars_max = true;
					Utils.isReturnHome(dronePoint);
					GameVariabales.spin_by = 90;
					Utils.isReturnHome(dronePoint);
					Point l1 = Utils.getPointByDistance(dronePoint,
							GameVariabales.drone.getLidars().get(1).getDegrees()
									+ GameVariabales.drone.getGyroRotation(),
							GameVariabales.drone.getLidars().get(1).getCurrentDistance());
					Point l2 = Utils.getPointByDistance(dronePoint,
							GameVariabales.drone.getLidars().get(2).getDegrees()
									+ GameVariabales.drone.getGyroRotation(),
							GameVariabales.drone.getLidars().get(2).getCurrentDistance());
					Point last_point = Utils.getAvgLastPoint();
					double dis_to_lidar1 = Utils.getDistanceBetweenPoints(last_point, l1);
					double dis_to_lidar2 = Utils.getDistanceBetweenPoints(last_point, l2);

					if (dis_to_lidar1 < dis_to_lidar2) {
						GameVariabales.spin_by *= -1;
					}

				} else {
					if (a < b || GameVariabales.risky_dis >= 100) {
						GameVariabales.spin_by *= (-1);
					}
				}

				Utils.isBlock();

				Utils.spinBy(GameVariabales.spin_by, true, new Func() {
					@Override
					public void method() {
						GameVariabales.try_to_escape = false;
						GameVariabales.is_risky = false;
					}
				});
			}
		}
	}

	public static void isBlock() {
		// Alarmed the drone entered the wall (should not happened)
		if (GameVariabales.realMap.isCollide((int) (GameVariabales.drone.getPointOnMap().getX()),
				(int) (GameVariabales.drone.getPointOnMap().getY()))
				|| GameVariabales.drone.getBattery().getStamina() < 0) {
			stopCPUS();
			GameVariabales.gameEnd = true;
			gameOverMessage();
			System.exit(0);
		}
	}

	public static void interestedPoints(Point dronePoint) {
		int importantDistToWall = 10;
		int minDistanceBetweenImportantPoints = 20;
		if (((GameVariabales.drone.getLidars().get(0).getCurrentDistance() < 20
				&& GameVariabales.drone.getLidars().get(1).getCurrentDistance() < importantDistToWall)
				|| (GameVariabales.drone.getLidars().get(0).getCurrentDistance() < 20
						&& GameVariabales.drone.getLidars().get(2).getCurrentDistance() < importantDistToWall)
				|| (GameVariabales.drone.getLidars().get(1).getCurrentDistance() < importantDistToWall
						&& GameVariabales.drone.getLidars().get(2).getCurrentDistance() < importantDistToWall))
				&& Utils.getDistanceBetweenPoints(dronePoint, GameVariabales.points
						.get(GameVariabales.points.size() - 1)) > minDistanceBetweenImportantPoints) {
			GameVariabales.points.add(dronePoint);
			GameVariabales.graph.addVertex(GameVariabales.drone.getPointOnMap());
		}
	}

	public static float getAngle(Point target, Point source) {
		float angle = (float) Math.toDegrees(Math.atan2(target.getY() - source.getY(), target.getX() - source.getX()));
		if (angle < 0)
			angle += 360;
		return angle;
	}

	public static void returnHome(Point dronePoint) {
		float angleToInitPoint = Utils.getAngle(GameVariabales.init_point, dronePoint);
		double rotation = GameVariabales.drone.getGyroRotation();
		System.out.println("angle to init : " + angleToInitPoint + " , rotation : " + rotation);
		if (Math.abs(rotation - angleToInitPoint) > 10) {
			GameVariabales.spin_by *= -1;
		}
	}

	public static void isReturnHome(Point dronePoint) {
		if (GameVariabales.return_home || GameVariabales.drone.getBattery().getStamina() <= 50) {
			// Implementing the technique
			if (Utils.getDistanceBetweenPoints(Utils.getLastPoint(), dronePoint) < Config.max_distance_between_points) {
				if (GameVariabales.points.size() <= 1 && Utils.getDistanceBetweenPoints(Utils.getLastPoint(),
						dronePoint) < Config.max_distance_between_points / 5) {
					Utils.speedDown();
				} else {
					Utils.removeLastPoint();
				}
			}
			//
		} else {
			Utils.interestedPoints(dronePoint);
		}

		if (GameVariabales.is_risky && !GameVariabales.try_to_escape) {
			//
			if (GameVariabales.return_home || GameVariabales.drone.getBattery().getStamina() <= 50) {
				if (Utils.getDistanceBetweenPoints(Utils.getLastPoint(),
						dronePoint) < Config.max_distance_between_points) {
					Utils.removeLastPoint();
				}
			} else {
				Utils.interestedPoints(dronePoint);
			}
			Lidar lidar1 = GameVariabales.drone.getLidars().get(1);
			double a = lidar1.getCurrentDistance();
			Lidar lidar2 = GameVariabales.drone.getLidars().get(2);
			double b = lidar2.getCurrentDistance();

			if (a > 270 && b > 270) {

				if (GameVariabales.return_home || GameVariabales.drone.getBattery().getStamina() <= 50) {
					returnHome(dronePoint);
				}

			}
			//
		}
	}

	public static void batteryUpdate(int deltaTime) {
		GameVariabales.drone.getBattery().setStamina();
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	public static void rotateUpdate(int deltaTime) {
		if (GameVariabales.isRotating != 0) {
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

		for (int i = 0; i < Config.map_size; i++) {
			for (int j = 0; j < Config.map_size; j++) {
				GameVariabales.map[i][j] = GameVariabales.PixelState.unexplored;
			}
		}

		GameVariabales.droneStartingPoint = new Point(Config.map_size / 2, Config.map_size / 2);
	}

	// Unused !!!
	public static void doLeftRight() {

		if (GameVariabales.is_finish) {
			GameVariabales.leftOrRight *= -1;
			GameVariabales.counter++;
			GameVariabales.is_finish = false;

			Utils.spinBy(Config.max_rotation_to_direction * GameVariabales.leftOrRight, false, new Func() {
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
		Point fromPoint = new Point(dronePoint.getX() + GameVariabales.droneStartingPoint.getX(),
				dronePoint.getY() + GameVariabales.droneStartingPoint.getY());
		Utils.setPixel(fromPoint.getX(), fromPoint.getY(), GameVariabales.PixelState.visited, GameVariabales.map);
	}

	public static void updateInfo(int deltaTime) {
		Utils.updateInfo(deltaTime, Visualizator.info_label_drone, Visualizator.info_label_config,
				Visualizator.graphInfo);
	}

	public static void updateAi(int deltaTime) {
		Utils.gameUpdates(deltaTime);
	}

	public static void updateMapByLidars() {
		Point dronePoint = GameVariabales.drone.getOpticalSensorLocation();
		Point fromPoint = new Point(dronePoint.getX() + GameVariabales.droneStartingPoint.getX(),
				dronePoint.getY() + GameVariabales.droneStartingPoint.getY());

		for (int i = 0; i < GameVariabales.drone.getLidars().size(); i++) {
			Lidar lidar = GameVariabales.drone.getLidars().get(i);
			double rotation = GameVariabales.drone.getGyroRotation() + lidar.getDegrees();
			for (int distanceInCM = 0; distanceInCM < lidar.getCurrentDistance(); distanceInCM++) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, distanceInCM);
				Utils.setPixel(p.getX(), p.getY(), GameVariabales.PixelState.explored, GameVariabales.map);
			}

			if (lidar.getCurrentDistance() > 0 && lidar.getCurrentDistance() < Config.lidarLimit - Config.lidarNoise) {
				Point p = Utils.getPointByDistance(fromPoint, rotation, lidar.getCurrentDistance());
				Utils.setPixel(p.getX(), p.getY(), GameVariabales.PixelState.blocked, GameVariabales.map);
			}
		}
	}

	public static void updateRotating(int deltaTime) {

		if (GameVariabales.degrees_left.size() != 0) {

			double degrees_left_to_rotate = GameVariabales.degrees_left.get(0);
			boolean isLeft = (degrees_left_to_rotate > 0) ? false : true;
			double curr = GameVariabales.drone.getGyroRotation();
			double just_rotated = 0;
			just_rotated = curr - GameVariabales.lastGyroRotation;
			if (isLeft) {
				if (just_rotated > 0) {
					just_rotated = -(360 - just_rotated);
				}
			} else {
				if (just_rotated < 0) {
					just_rotated = 360 + just_rotated;
				}
			}

			GameVariabales.lastGyroRotation = curr;
			degrees_left_to_rotate -= just_rotated;
			GameVariabales.degrees_left.remove(0);
			GameVariabales.degrees_left.add(0, degrees_left_to_rotate);

			if ((isLeft && degrees_left_to_rotate >= 0) || (!isLeft && degrees_left_to_rotate <= 0)) {
				GameVariabales.degrees_left.remove(0);

				Func func = GameVariabales.degrees_left_func.get(0);
				if (func != null) {
					func.method();
				}
				GameVariabales.degrees_left_func.remove(0);

				if (GameVariabales.degrees_left.size() == 0) {
					GameVariabales.isRotating = 0;
				}
				return;
			}

			int direction = (int) (degrees_left_to_rotate / Math.abs(degrees_left_to_rotate));
			GameVariabales.drone.rotateLeft(deltaTime * direction);
		} else
			return;
	}

	public static void spinBy(double degrees, boolean isFirst, Func func) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();
		if (isFirst) {
			GameVariabales.degrees_left.add(0, degrees);
			GameVariabales.degrees_left_func.add(0, func);
		} else {
			GameVariabales.degrees_left.add(degrees);
			GameVariabales.degrees_left_func.add(func);
		}
		GameVariabales.isRotating = 1;
	}

	public static void spinBy(double degrees, boolean isFirst) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();
		if (isFirst) {
			GameVariabales.degrees_left.add(0, degrees);
			GameVariabales.degrees_left_func.add(0, null);
		} else {
			GameVariabales.degrees_left.add(degrees);
			GameVariabales.degrees_left_func.add(null);
		}

		GameVariabales.isRotating = 1;
	}

	public static void spinBy(double degrees) {
		GameVariabales.lastGyroRotation = GameVariabales.drone.getGyroRotation();

		GameVariabales.degrees_left.add(degrees);
		GameVariabales.degrees_left_func.add(null);
		GameVariabales.isRotating = 1;
	}

	public static Point getLastPoint() {
		if (GameVariabales.points.size() == 0) {
			return GameVariabales.init_point;
		}

		Point p1 = GameVariabales.points.get(GameVariabales.points.size() - 1);
		return p1;
	}

	public static Point removeLastPoint() {
		if (GameVariabales.points.isEmpty()) {
			return GameVariabales.init_point;
		}

		return GameVariabales.points.remove(GameVariabales.points.size() - 1);
	}

	public static Point getAvgLastPoint() {
		if (GameVariabales.points.size() < 2) {
			return GameVariabales.init_point;
		}
		Point p1 = GameVariabales.points.get(GameVariabales.points.size() - 1);
		Point p2 = GameVariabales.points.get(GameVariabales.points.size() - 2);
		return new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	}

	public static double getDistanceBetweenPoints(Point from, Point to) {
		double x1 = (from.getX() - to.getX()) * (from.getX() - to.getX());
		double y1 = (from.getY() - to.getY()) * (from.getY() - to.getY());
		return Math.sqrt(x1 + y1);
	}

	public static void gameOverMessage() {
		JOptionPane.showMessageDialog(null, "Game Over!!!");
	}

}