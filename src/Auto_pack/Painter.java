package Auto_pack;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/*
 * Move to here some necessary functions
 */

public class Painter extends JComponent {
	
	private static final long serialVersionUID = 5899668374057151639L;
	
	public Painter() {}
	
	public static void paintDrone(Graphics g , Drone drone) {
		if(!drone.initPaint) {
			try {
				File f = new File(Config.drone_img_path);
				drone.mImage = ImageIO.read(f);
				drone.initPaint = true;
			} catch(Exception ex) {
				
			}
		}

		for(int i=0 ;i < drone.lidars.size() ;i++) {
			Lidar lidar = drone.lidars.get(i);
			Painter.paintLidar(g,lidar);
		}
	}
	
	public static void paintLidar(Graphics g , Lidar lidar) {
		Point actualPointToShoot= GameVariabales.drone.getPointOnMap();
		double fromRotation = GameVariabales.drone.getRotation()+lidar.degrees;
		Point to = Utils.getPointByDistance(actualPointToShoot, fromRotation, lidar.current_distance);
		g.drawLine((int)actualPointToShoot.x,(int)actualPointToShoot.y, (int)to.x, (int)to.y);
	}
	
	public static void paintMap(Graphics g , Map map) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		for(int i=0;i< map.map.length;i++) {
			for(int j=0;j< map.map[0].length;j++) {
				if(! map.map[i][j])  {
					g.drawLine(i, j, i, j);
				}
			}
		}
		g.setColor(c);
	}
	
	public static void paintBlindMap(Graphics g , Point droneStartingPoint , Drone drone ,  int map_size , GameVariabales.PixelState[][] map) {
		Color c = g.getColor();
		int i = (int)droneStartingPoint.y - (int)Config.startPoints[Config.map_index-1].x;
		int startY = i;
		for(;i<map_size;i++) {
			int j = (int)droneStartingPoint.x - (int)Config.startPoints[Config.map_index-1].y;
			int startX = j;
			for(;j<map_size;j++) {
				if(map[i][j] != GameVariabales.PixelState.unexplored)  {
					if(map[i][j] == GameVariabales.PixelState.blocked) {
						g.setColor(Color.RED);
					} 
					else if(map[i][j] == GameVariabales.PixelState.explored) {
						g.setColor(Color.YELLOW);
					}
					else if(map[i][j] == GameVariabales.PixelState.visited) {
						g.setColor(Color.BLUE);
					}
					g.drawLine(i-startY, j-startX, i-startY, j-startX);
				}
			}
		}
		g.setColor(c);
	}
	
	public static void paintForAlgo(Graphics g , Point droneStartingPoint , ArrayList<Point> points, Drone drone , int map_size , GameVariabales.PixelState[][] map) {
		if(GameVariabales.toogleRealMap) {
			Painter.paintMap(g , GameVariabales.realMap);
		}	
		Painter.paintBlindMap(g , droneStartingPoint , drone , map_size , map);
		for(int i=0;i<points.size();i++) {
			Point p = points.get(i);
			g.drawOval((int)p.x + (int)Config.startPoints[Config.map_index-1].x - 10, (int)p.y + (int)Config.startPoints[Config.map_index-1].y-10, 20, 20);
		}
		
		Painter.paintDrone(g, drone);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Painter.paintForAlgo(g , GameVariabales.droneStartingPoint , GameVariabales.points ,  GameVariabales.drone , Config.map_size , GameVariabales.map);
	
	}
}