package tasks;

import classes.TranspoolSystem;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tasks.common.TranspoolResourcesConstants;
import tasks.components.main.TranspoolController;
import tasks.components.map.component.coordinate.CoordinateNode;
import tasks.components.map.component.coordinate.CoordinatesManager;
import tasks.components.map.component.details.StationDetailsDTO;
import tasks.components.map.component.road.ArrowedEdge;
import tasks.components.map.component.station.StationManager;
import tasks.components.map.component.station.StationNode;
import tasks.components.map.layout.MapGridLayout;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TasksMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        // load main fxml
        URL mainFXML = getClass().getResource(TranspoolResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        // wire up controller

        TranspoolController tsController = loader.getController();
        TranspoolSystem tsEngine = new TranspoolSystem();
        tsController.setPrimaryStage(primaryStage);
        tsController.setTsEngine(tsEngine);

        Graph graphMap = new Graph();
        createMap(graphMap, tsController);
        tsController.setGraph(graphMap);

        Scene scene = new Scene(root, 1280, 800);

        ScrollPane scrollPane = (ScrollPane) scene.lookup("#centerScrollPane");
        PannableCanvas canvas = graphMap.getCanvas();
        canvas.setId("canvas");
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        scrollPane.setContent(canvas);

        // set stage
        primaryStage.setTitle("TransPool");
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            graphMap.getUseViewportGestures().set(false);
            graphMap.getUseNodeGestures().set(false);
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createMap(Graph graph, TranspoolController tsController) {

        final Model model = graph.getModel();
        graph.beginUpdate();

        //StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        //createEdges(model, cm);
        tsController.setMgLayout(new MapGridLayout(cm,new StationManager(StationNode::new)));
        graph.endUpdate();

        graph.layout(tsController.getMgLayout());
    }

//    private StationManager createStations(Model model) {
//        StationManager sm = new StationManager(StationNode::new);
//
//        StationNode station = sm.getOrCreate(2, 2);
//        station.setName("This is a test for long string");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mosh");
//            trips.add("Menash");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(5, 5);
//        station.setName("B");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(7, 9);
//        station.setName("C");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mosh");
//            trips.add("Menash");
//            trips.add("Tikva");
//            trips.add("Mazal");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        station = sm.getOrCreate(4, 6);
//        station.setName("D");
//        station.setDetailsSupplier(() -> {
//            List<String> trips = new ArrayList<>();
//            trips.add("Mazal");
//            return new StationDetailsDTO(trips);
//        });
//        model.addCell(station);
//
//        return sm;
//    }

    private CoordinatesManager createCoordinates(Model model) {

        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);

        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                model.addCell(cm.getOrCreate(i+1, j+1));
            }
        }

        return cm;
    }

//    private void createEdges(Model model, CoordinatesManager cm) {
//        ArrowedEdge e13 = new ArrowedEdge(cm.getOrCreate(2,2), cm.getOrCreate(7,9));
//        e13.textProperty().set("L: 7 ; FC: 4");
//        model.addEdge(e13); // 1-3
//
//        ArrowedEdge e34 = new ArrowedEdge(cm.getOrCreate(7,9), cm.getOrCreate(4,6));
//        e34.textProperty().set("L: 12 ; FC: 14");
//        model.addEdge(e34); // 3-4
//
//        ArrowedEdge e23 = new ArrowedEdge(cm.getOrCreate(5,5), cm.getOrCreate(7,9));
//        e23.textProperty().set("L: 4 ; FC: 10");
//        model.addEdge(e23); // 2-3
//
//        Platform.runLater(() -> {
//            e13.getLine().getStyleClass().add("line1");
//            e13.getText().getStyleClass().add("edge-text");
//
//            e34.getLine().getStyleClass().add("line2");
//            e34.getText().getStyleClass().add("edge-text");
//
//            e23.getLine().getStyleClass().add("line3");
//            e23.getText().getStyleClass().add("edge-text");
//
//            //moveAllEdgesToTheFront(graph);
//        });
//
//    }

    private void moveAllEdgesToTheFront(Graph graph) {

        List<Node> onlyEdges = new ArrayList<>();

        // finds all edge nodes and remove them from the beginning of list
        ObservableList<Node> nodes = graph.getCanvas().getChildren();
        while (nodes.get(0).getClass().getSimpleName().equals("EdgeGraphic")) {
            onlyEdges.add(nodes.remove(0));
        }

        // adds them as last ones
        nodes.addAll(onlyEdges);
    }
}
