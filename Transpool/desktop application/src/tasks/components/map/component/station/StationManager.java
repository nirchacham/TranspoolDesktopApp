package tasks.components.map.component.station;

import tasks.components.map.component.util.NodesManager;

import java.util.function.BiFunction;

/*
Makes sure that each Station node will e created exactly once
 */
public class StationManager extends NodesManager<StationNode> {

    public StationManager(BiFunction<Integer, Integer, StationNode> factory) {
        super(factory);
    }
}
