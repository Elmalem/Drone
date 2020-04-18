package Auto_pack;
import java.awt.EventQueue;
import javax.swing.*;

public class Simulator {

	public JFrame frame;
	public static JLabel info_label_drone , info_label_config;
	public static boolean return_home = false , toogleStop = true , toogleRealMap = true , toogleAI = false;
	 
	public Simulator() {
		Visualizator.initialize(this);
		start();
	}
	
	public void start() {
		int map_index = 4;
		Map map = new Map(Config.imageSourcePath +"\\Maps\\p1" + map_index + ".png",Config.startPoints[map_index-1]);
		
		Painter painter = new Painter();
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(frame::repaint);
		
		painterCPU.play();
		Utils.play();
		
		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(GameVariabales.drone::update);
		
		updatesCPU.play();
		
		CPU infoCPU = new CPU(6,"update_info");
		infoCPU.addFunction(this::updateInfo);
		
		infoCPU.play();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulator simulator_ = new Simulator();
					simulator_.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateInfo(int deltaTime) {
		Utils.updateInfo(deltaTime , info_label_drone , info_label_config);
	}
	
}
