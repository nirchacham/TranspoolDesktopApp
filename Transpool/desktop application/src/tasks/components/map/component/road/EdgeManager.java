package tasks.components.map.component.road;

import com.fxgraph.edges.Edge;

import java.util.HashMap;
import java.util.Map;

public class EdgeManager {
    private Map<String,ArrowedEdge> hm;

    public EdgeManager() {
        hm=new HashMap<String,ArrowedEdge>();
    }

    public Map<String,ArrowedEdge> getHm() {
        return hm;
    }

    public void addEdgeToHm(ArrowedEdge edge, String road) {
        hm.put(road,edge);
    }
}
