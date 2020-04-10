package Auto_pack;
import java.awt.EventQueue;
import javax.swing.*;

public class Simulator {

	public JFrame frame;
	public static JLabel info_label , info_label2;
	public static boolean return_home = false , toogleStop = true , toogleRealMap = true , toogleAI = false;
	public static Algorithm algo;
	
	public Simulator() {
		Timer.start();
		Visualizator.initialize(this);
		start();
	}
	
	public void start() {
		int map_index = 2;
		Map map = new Map(Config.imageSourcePath+"\\Maps\\p1" + map_index + ".png",Config.startPoints[map_index-1]);
		algo = new Algorithm(map);
		
		Painter painter = new Painter(algo);
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);
		
		CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
		painterCPU.addFunction(frame::repaint);
		painterCPU.play();
		
		Utils.play(algo);
		
		CPU updatesCPU = new CPU(60,"updates");
		updatesCPU.addFunction(algo.drone::update);
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
		Utils.updateInfo(deltaTime , info_label , info_label2 , algo);
	}
	
}
