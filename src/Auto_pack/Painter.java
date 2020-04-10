package Auto_pack;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import Auto_pack.Algorithm.PixelState;

public class Painter extends JComponent {
	
	private static final long serialVersionUID = 5899668374057151639L;
	Algorithm algo;
	
	public Painter(Algorithm algo) {
		this.algo = algo;
	}
	
	public static void paintDrone(Graphics g , Drone drone) {
		if(!drone.initPaint) {
			try {
				File f = new File(drone.drone_img_path);
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
		Point actualPointToShoot= lidar.drone.getPointOnMap();
		double fromRotation = lidar.drone.getRotation()+lidar.degrees;
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
	
	public static void paintBlindMap(Graphics g , Point droneStartingPoint , Drone drone ,  int map_size , PixelState[][] map) {
		Color c = g.getColor();
		int i = (int)droneStartingPoint.y - (int)drone.startPoint.x;
		int startY = i;
		for(;i<map_size;i++) {
			int j = (int)droneStartingPoint.x - (int)drone.startPoint.y;
			int startX = j;
			for(;j<map_size;j++) {
				if(map[i][j] != PixelState.unexplored)  {
					if(map[i][j] == PixelState.blocked) {
						g.setColor(Color.RED);
					} 
					else if(map[i][j] == PixelState.explored) {
						g.setColor(Color.YELLOW);
					}
					else if(map[i][j] == PixelState.visited) {
						g.setColor(Color.BLUE);
					}
					g.drawLine(i-startY, j-startX, i-startY, j-startX);
				}
			}
		}
		g.setColor(c);
	}
	
	public static void paintForAlgo(Graphics g , Point droneStartingPoint , ArrayList<Point> points, Drone drone , int map_size , PixelState[][] map) {
		if(Simulator.toogleRealMap) {
			Painter.paintMap(g , drone.realMap);
		}	
		Painter.paintBlindMap(g , droneStartingPoint , drone , map_size , map);
		for(int i=0;i<points.size();i++) {
			Point p = points.get(i);
			g.drawOval((int)p.x + (int)drone.startPoint.x - 10, (int)p.y + (int)drone.startPoint.y-10, 20, 20);
		}
		
		Painter.paintDrone(g, drone);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Painter.paintForAlgo(g , algo.droneStartingPoint , algo.points ,  algo.drone , algo.map_size , algo.map);
	
	}
}
