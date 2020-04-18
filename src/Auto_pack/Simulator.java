package Auto_pack;
import java.awt.EventQueue;
import javax.swing.*;

public class Simulator {

	public static JFrame frame;
	public static JLabel info_label_drone , info_label_config;
	public static boolean return_home = false , toogleStop = true , toogleRealMap = true , toogleAI = false;
	
	public void start() {
		
		Visualizator.initialize();
		GameVariabales.init();
		
		Painter painter = new Painter();
		painter.setBounds(0, 0, 2000, 2000);
		Simulator.frame.getContentPane().add(painter);
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(Simulator.frame::repaint);
		painterCPU.play();
				
		Utils.play();
		
		CPU unbrokenCPU = new CPU(200 , "unbroken");
		unbrokenCPU.addFunction(this::unbroken);
		unbrokenCPU.play();
		
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
					Simulator player = new Simulator();
					player.start();
					Simulator.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateInfo(int deltaTime) {
		Utils.updateInfo(deltaTime , info_label_drone , info_label_config);
	}
	
	public void unbroken(int deltaTime) {
		Utils.unbroken(deltaTime, GameVariabales.drone.lidars.get(1).getSimulationDistance(deltaTime), GameVariabales.drone.lidars.get(2).getSimulationDistance(deltaTime) , GameVariabales.drone.lidars.get(0).getSimulationDistance(deltaTime));
	}
	
}
