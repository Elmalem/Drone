package Auto_pack;
import java.awt.EventQueue;
import javax.swing.*;

public class Simulator {

	public JFrame frame;
	public static JLabel info_label_drone , info_label_config;
	public static boolean return_home = false , toogleStop = true , toogleRealMap = true , toogleAI = false;
	public static Algorithm algo;
	
	public Simulator() {
		Visualizator.initialize(this); 
		start();
	}
	
	public void start() {
		
		Map map = new Map(Config.imageSourcePath+"\\Maps\\p1" + 4 + ".png",Config.startPoints[3]);
		algo = new Algorithm(map);
		
		Painter painter = new Painter(algo);
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);
		
		CPU cpu = new CPU("Main"); // 60 FPS painter
		cpu.functions_list.add(frame::repaint);
		cpu.functions_list.add(algo.drone::update);
		cpu.functions_list.add(this::updateInfo);
		cpu.play();
		Utils.play(algo);

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
		Utils.updateInfo(deltaTime , info_label_drone , info_label_config , algo);
	}
	
}
