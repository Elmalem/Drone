package Auto_pack;
import java.util.Set;
import org.jgrapht.graph.*;
import org.jgrapht.alg.spanning.*;
import org.jgrapht.graph.DefaultEdge;

/*
 * Unchanged
 */

public class Graph {

	private DefaultDirectedGraph<Point, DefaultEdge> graph;
	
    public Graph() {
    	graph = new DefaultDirectedGraph<Point, DefaultEdge>(DefaultEdge.class);
    }
    public void addVertex(Point name) {
        Point last_vertex = null;
        Set<Point> all = graph.vertexSet();
        if(all.size() > 0) {
        	last_vertex = getLastElement(all);
        }
        graph.addVertex(name);
        if(last_vertex != null) 
        	graph.addEdge(last_vertex, name);
    }
    
    public Point getLastElement(Set<Point> c) {
    	Point last = null;
    	if(c.size() > 0) {
    		for(Point x : c) {
    			last = x;
    		}
    	}
        return last;
    }
    public void addEdge(Point v1,Point v2) {
    	graph.addEdge(v1, v2);
    }

    public String getOutput() {
    	return graph.toString();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSpanningTree() {
        KruskalMinimumSpanningTree k = new KruskalMinimumSpanningTree(graph);
        System.out.println(k.getSpanningTree().toString());
    }

}