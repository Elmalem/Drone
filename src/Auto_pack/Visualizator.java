package Auto_pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Visualizator {
	
	// This class only generate the visualization settings 
	
	public static void initialize(Simulator main) {
		main.frame = new JFrame();
		main.frame.setSize(1800,700);
		main.frame.setTitle("Drone Simulator");
		main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.frame.getContentPane().setLayout(null);
		
		JButton timeButton = new JButton("Timer");
		timeButton.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				System.out.println(Timer.displaySeconds());
			  }
		});
		timeButton.setBounds(1470, 0, 170, 50);
		main.frame.getContentPane().add(timeButton);
		
		
		JButton stopBtn = new JButton("Start/Pause");
		stopBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  if(main.toogleStop) {
					  CPU.stopAllCPUS();
				  } else {
					  CPU.resumeAllCPUS();
				  }
				  main.toogleStop = !main.toogleStop;
			  }
		});
		stopBtn.setBounds(1300, 0, 170, 50);
		main.frame.getContentPane().add(stopBtn);

		JButton speedBtn1 = new JButton("speedUp");
		speedBtn1.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.speedUp(Simulator.algo);
			  }
		});
		speedBtn1.setBounds(1300, 100, 100, 50);
		main.frame.getContentPane().add(speedBtn1);
		
		JButton speedBtn2 = new JButton("speedDown");
		speedBtn2.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.speedDown(Simulator.algo);
			  }
		});
		speedBtn2.setBounds(1400, 100, 100, 50);
		main.frame.getContentPane().add(speedBtn2);
		
		JButton spinBtn1 = new JButton("spin180");
		spinBtn1.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(180 , Simulator.algo);
			  }
		});
		spinBtn1.setBounds(1300, 200, 100, 50);
		main.frame.getContentPane().add(spinBtn1);
		
		JButton spinBtn2 = new JButton("spin90");
		spinBtn2.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(90 , Simulator.algo);
			  }
		});
		spinBtn2.setBounds(1400, 200, 100, 50);
		main.frame.getContentPane().add(spinBtn2);
		
		JButton spinBtn3 = new JButton("spin60");
		spinBtn3.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(60 , Simulator.algo);
			  }
		});
		spinBtn3.setBounds(1500, 200, 100, 50);
		main.frame.getContentPane().add(spinBtn3);
		
		JButton spinBtn4 = new JButton("spin45");
		spinBtn4.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(60 , Simulator.algo);
			  }
		});
		spinBtn4.setBounds(1300, 300, 100, 50);
		main.frame.getContentPane().add(spinBtn4);
		
		JButton spinBtn5 = new JButton("spin30");
		spinBtn5.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(30 , Simulator.algo);
			  }
		});
		spinBtn5.setBounds(1400, 300, 100, 50);
		main.frame.getContentPane().add(spinBtn5);
		
		JButton spinBtn6 = new JButton("spin-30");
		spinBtn6.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(-30 , Simulator.algo);
			  }
		});
		spinBtn6.setBounds(1500, 300, 100, 50);
		main.frame.getContentPane().add(spinBtn6);
		
		JButton spinBtn7 = new JButton("spin-45");
		spinBtn7.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(-45 , Simulator.algo);
			  }
		});
		spinBtn7.setBounds(1600, 300, 100, 50);
		main.frame.getContentPane().add(spinBtn7);
		
		JButton spinBtn8 = new JButton("spin-60");
		spinBtn8.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Utils.spinBy(-60 , Simulator.algo);
			  }
		});
		spinBtn8.setBounds(1700, 300, 100, 50);
		main.frame.getContentPane().add(spinBtn8);

		JButton toogleMapBtn = new JButton("toogle Map");
		toogleMapBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Simulator.toogleRealMap = !Simulator.toogleRealMap;
			  }
		});
		toogleMapBtn.setBounds(1300, 400, 120, 50);
		main.frame.getContentPane().add(toogleMapBtn);
		
		JButton toogleAIBtn = new JButton("toogle AI");
		toogleAIBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Simulator.toogleAI = !Simulator.toogleAI;
			  }
		});
		toogleAIBtn.setBounds(1400, 400, 120, 50);
		main.frame.getContentPane().add(toogleAIBtn);

		JButton returnBtn = new JButton("Return Home");
		returnBtn.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  Simulator.return_home = !Simulator.return_home;
				  Utils.speedDown(Simulator.algo);
				  Utils.spinBy(180, true,  Simulator.algo,new Func() {
						@Override
						public void method() {
							Utils.speedUp(Simulator.algo);
						}
					});
			  }
		});
		returnBtn.setBounds(1500, 400, 120, 50);
		main.frame.getContentPane().add(returnBtn);
		
		Simulator.info_label = new JLabel();
		Simulator.info_label.setBounds(1300, 500, 300, 200);
		main.frame.getContentPane().add(Simulator.info_label);
	
		main.info_label2 = new JLabel();
		main.info_label2.setBounds(1400, 450, 300, 200);
		main.frame.getContentPane().add(main.info_label2);
	}
}
