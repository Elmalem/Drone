package Auto_pack;

import java.awt.EventQueue;
import javax.swing.*;

public class Simulator {

	public JFrame frame;
	public static JLabel info_label_drone, info_label_config, info_time;
	public static boolean return_home = false, toogleStop = true, toogleRealMap = true, toogleAI = false;
	public static Algorithm algo;

	public Simulator() {

		int map_index = 4;
		Visualizator.initialize(this);
		Map map = new Map(Config.imageSourcePath + "\\Maps\\p1" + map_index + ".png", Config.startPoints[map_index - 1]);
		algo = new Algorithm(map);

		Painter painter = new Painter(algo);
		painter.setBounds(0, 0, 2000, 2000);
		frame.getContentPane().add(painter);

		CPU painterCPU = new CPU(200 , "painter"); // 60 FPS painter
		painterCPU.functions_list.add(frame::repaint);
		painterCPU.play();
		
		Utils.play(algo);
		
		CPU updatesCPU = new CPU(60 , "updates");
		updatesCPU.functions_list.add(algo.drone::update);
		updatesCPU.play();
		
		CPU infoCPU = new CPU(6 , "update_info");
		infoCPU.functions_list.add(this::updateInfo);
		infoCPU.play();
		
		CPU timeCPU = new CPU(6 , "time_info");
		timeCPU.functions_list.add(this::updateTime);
		timeCPU.play();
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
		Utils.updateInfo(deltaTime, info_label_drone, info_label_config, algo);
	}
	
	public void updateTime(int deltaTime) {
		Utils.updateTime(deltaTime, info_time);
	}

}
