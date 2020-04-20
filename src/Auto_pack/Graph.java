package Auto_pack;
import java.util.Set;
import org.jgrapht.graph.*;
import org.jgrapht.alg.spanning.*;
import org.jgrapht.graph.DefaultEdge;

/*
 * Unchanged
 */

public class Graph {

	DefaultDirectedGraph<Point, DefaultEdge> g;
	
    public Graph() {
    	g = new DefaultDirectedGraph<Point, DefaultEdge>(DefaultEdge.class);
    }
    public void addVertex(Point name) {
        Point last_vertex = null;
        Set<Point> all = g.vertexSet();
        if(all.size() > 0) {
        	last_vertex = getLastElement(all);
        }
        g.addVertex(name);
        if(last_vertex != null) 
        	g.addEdge(last_vertex, name);
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
        g.addEdge(v1, v2);
    }

    public String getOutput() {
    	return g.toString();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSpanningTree() {
        KruskalMinimumSpanningTree k = new KruskalMinimumSpanningTree(g);
        System.out.println(k.getSpanningTree().toString());
    }

}