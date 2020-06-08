package Auto_pack;
import java.util.ArrayList;

/*
 * This class represent the graph of the interested points the drone left behind 
 */
class Graph {
	
	private ArrayList<Edge> edges;
	private ArrayList<Point> vertex;
	
	Graph (Point init) {
		this.edges = new ArrayList<Edge>();
		this.vertex = new ArrayList<Point>();
		this.vertex.add(init);
	}
	
	public void addVertex(Point point) {
		this.vertex.add(point);
		Point temp = vertex.get(vertex.size() - 2);
		this.addEdge(temp , point , Utils.getDistanceBetweenPoints(point, temp));
	}
	
	private void addEdge(Point a , Point b , double weight) {
		this.edges.add(new Edge(a , b , weight));
	}
	
	public ArrayList<Edge> getEdges(){
		return this.edges;
	}
	
	public String toHtmlString() {
		String str = "<html> Graph";
		for (int i = 0; i < edges.size(); i++) {
			str += "<BR>" + edges.get(i).toString();
		} 
		return str += "</html>";
	}
	
}