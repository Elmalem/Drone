package Auto_pack;

import java.awt.EventQueue;

/*
 * The main class to run the program
 */

public class Simulator {

	public void start() {
		Visualizator.initialize();
		GameVariabales.init();
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
