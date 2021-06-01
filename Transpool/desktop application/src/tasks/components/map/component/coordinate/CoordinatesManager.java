package tasks.components.map.component.coordinate;

import tasks.components.map.component.util.NodesManager;
import tasks.components.map.component.coordinate.CoordinateNode;

import java.util.function.BiFunction;

/*
Makes sure that each coordinate node will e created exactly once
 */
public class CoordinatesManager extends NodesManager<CoordinateNode> {

    public CoordinatesManager(BiFunction<Integer, Integer, CoordinateNode> factory) {
        super(factory);
    }
}
