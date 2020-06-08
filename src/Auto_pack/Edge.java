package Auto_pack;

/*
 * This class represent the edges of the graph
 */
public class Edge {
	
	private Point a;
	private Point b;
	private double weight;
	
	public Edge(Point a , Point b ,  double weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
	
	public Point getSource() {
		return this.a;
	}
	
	public Point getDestination() {
		return this.b;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public String toString() {
		return "(" + a.toString() + " , " + b.toString() + " , " + weight + ")";
	}
}