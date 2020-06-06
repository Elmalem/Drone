package Auto_pack;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class GraphPainter  extends JComponent {

	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < GameVariabales.graph.getEdges().size(); i++) {
			Edge edge = GameVariabales.graph.getEdges().get(i);
			g.setColor(Color.black);
			g.fillOval((int)edge.getSource().getX(), (int)edge.getSource().getY(), 10, 10);
			g.fillOval((int)edge.getDestination().getX(), (int)edge.getDestination().getY(), 10, 10);
			g.setColor(Color.orange);
			g.drawLine((int)edge.getSource().getX(), (int)edge.getSource().getY(), (int)edge.getDestination().getX(), (int)edge.getDestination().getY());
		}
	}
}