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
						
		CPU unbrokenCPU = new CPU(200 , "unbroken");
		unbrokenCPU.addFunction(this::unbroken);
		unbrokenCPU.play();
		
		Utils.play();
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(Simulator.frame::repaint);
		painterCPU.play();
		
		CPU ai_cpu = new CPU(200,"Auto_AI");
		ai_cpu.addFunction(this::updateAi);
		ai_cpu.play();
			
		CPU rotatingCPU = new CPU(200,"rotate");
		rotatingCPU.addFunction(this::rotateUpdate);
		rotatingCPU.play();
		
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
		Utils.unbroken(deltaTime, GameVariabales.drone.lidars.get(1).getDistance(deltaTime), GameVariabales.drone.lidars.get(2).getDistance(deltaTime) , GameVariabales.drone.lidars.get(0).getDistance(deltaTime));
	}
	
	public void rotateUpdate(int deltaTime) {
		Utils.rotateUpdate(deltaTime);
	}
	
	public void updateAi(int deltaTime) {
		Utils.gameUpdates(deltaTime);
	}

	
}
