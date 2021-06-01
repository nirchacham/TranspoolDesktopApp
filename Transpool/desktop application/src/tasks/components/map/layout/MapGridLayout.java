package tasks.components.map.layout;

import com.fxgraph.graph.Graph;
import com.fxgraph.layout.Layout;
import tasks.components.map.component.coordinate.CoordinatesManager;
import tasks.components.map.component.station.StationManager;

public class MapGridLayout implements Layout {

    private final int SCALE = 110;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;

    public MapGridLayout(CoordinatesManager coordinatesManager, StationManager stationManager) {
        this.coordinatesManager = coordinatesManager;
        this.stationManager = stationManager;
    }

    @Override
    public void execute(Graph graph) {

        coordinatesManager.getAllCoordinates().forEach(coordinateNode ->
                graph
                    .getGraphic(coordinateNode)
                    .relocate(coordinateNode.getX() * SCALE, coordinateNode.getY() * SCALE)
        );
        
        final int STATION_FIX_X = -15;
        final int STATION_FIX_Y = -15;
        stationManager.getAllCoordinates().forEach(stationNode -> {
            int x = stationNode.getX();
            int y = stationNode.getY();
            graph.getGraphic(stationNode).relocate(x * SCALE + STATION_FIX_X, y * SCALE + STATION_FIX_Y);
        });
    }

    public StationManager getStationManager() {
        return stationManager;
    }

    public void setStationManager(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    public CoordinatesManager getCoordinatesManager() {
        return coordinatesManager;
    }

    public void setCoordinatesManager(CoordinatesManager coordinatesManager) {
        this.coordinatesManager = coordinatesManager;
    }

}
