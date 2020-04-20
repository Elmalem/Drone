package Auto_pack;
import java.awt.EventQueue;

/*
 * The main class to run the program
 */

public class Simulator {

	public void start() {
		
		Visualizator.initialize();
		GameVariabales.init();
		
		Painter painter = new Painter();
		painter.setBounds(0, 0, 2000, 2000);
		Visualizator.frame.getContentPane().add(painter);
		
		Utils.dronePlay();
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(Visualizator.frame::repaint);
		painterCPU.play();
		
		CPU ai_cpu = new CPU(200,"Auto_AI");
		ai_cpu.addFunction(Utils::updateAi);
		ai_cpu.play();
			
		// Add this CPU to make the rotate updates program easier
		CPU rotatingCPU = new CPU(200,"rotate");
		rotatingCPU.addFunction(Utils::rotateUpdate);
		rotatingCPU.play();
		
		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(GameVariabales.drone::update);
		updatesCPU.play();
		
		CPU infoCPU = new CPU(6,"update_info");
		infoCPU.addFunction(Utils::updateInfo);
		infoCPU.play();
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulator player = new Simulator();
					player.start();
					Visualizator.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
