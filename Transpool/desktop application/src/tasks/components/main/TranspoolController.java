
package tasks.components.main;

import classes.*;
import com.fxgraph.edges.Edge;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.IEdge;
import com.fxgraph.graph.Model;
import exceptions.NoChoiceException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.common.TranspoolResourcesConstants;
import tasks.components.AddNewTrip.AddTripController;
import tasks.components.AddTripRequest.AddTripRequestController;
import tasks.components.findMatch.FindMatchController;
import tasks.components.loadXml.loadXmlController;
import tasks.components.map.component.coordinate.CoordinatesManager;
import tasks.components.map.component.details.StationDetailsDTO;
import tasks.components.map.component.road.ArrowedEdge;
import tasks.components.map.component.road.EdgeManager;
import tasks.components.map.component.station.StationManager;
import tasks.components.map.component.station.StationNode;
import tasks.components.map.layout.MapGridLayout;

import javax.xml.soap.Text;
//import javafx.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Map;

import static classes.Trip.showTripInfo;

public class TranspoolController implements Initializable {

        @FXML private Button loadXmlButton;
        @FXML private Button addNewTripButton;
        @FXML private Button addNewTripRequestButton;
        @FXML private Accordion tripAccordion;
        @FXML private Accordion tripRequestAccordion;
        @FXML private ChoiceBox<String> timeUnitChoiceBox;
        @FXML private RadioButton forwardRadioButton;
        @FXML private ToggleGroup forOrBack;
        @FXML private RadioButton backwardRadioButton;
        @FXML private Label dayLabel;
        @FXML private Label timeLabel;
        @FXML private Button matchButton;


        private TranspoolSystem tsEngine;
        private loadXmlController xmlController;
        private AddTripController tripController;
        private AddTripRequestController tripRequestController;
        private Stage primaryStage;
        private FindMatchController matchController;
        private Graph graph;
        private MapGridLayout mgLayout;
        private EdgeManager edgeManager;

        private SimpleBooleanProperty isFileSelected;
        private ObservableList<String> timeUnitList;

        public TranspoolController() {
                isFileSelected = new SimpleBooleanProperty(false);
                edgeManager=new EdgeManager();
        }

        public Accordion getTripAccordion() {
                return tripAccordion;
        }

        public Accordion getTripRequestAccordion() {
                return tripRequestAccordion;
        }

        public TranspoolSystem getTsEngine() {
                return tsEngine;
        }

        public void setTsEngine(TranspoolSystem tsEngine) {
                this.tsEngine = tsEngine;
        }

        public Stage getPrimaryStage() {
                return primaryStage;
        }

        public void setPrimaryStage(Stage primaryStage) {
                this.primaryStage = primaryStage;
        }

        public void setIsFileSelected(boolean isFileSelected) {
                this.isFileSelected.set(isFileSelected);
        }

        public Graph getGraph() {
                return graph;
        }

        public void setGraph(Graph graph) {
                this.graph = graph;
        }

        public MapGridLayout getMgLayout() {
                return mgLayout;
        }

        public void setMgLayout(MapGridLayout mgLayout) {
                this.mgLayout = mgLayout;
        }

        @FXML
        void addNewTripButtonAction(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader();
                URL loadFXML = getClass().getResource(TranspoolResourcesConstants.ADD_NEW_TRIP_FXML_RESOURCE_IDENTIFIER);
                loader.setLocation(loadFXML);
                ScrollPane root1 = loader.load();
                AddTripController trController = loader.getController();
                setAddTripController(trController);
                trController.setMainController(this);
                Stage stage=new Stage();
                stage.setTitle("Add trip");
                stage.setScene(new Scene(root1));
                stage.show();
                tripController.setStage(stage);
        }

        @FXML
        void addNewTripRequestButtonAction(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader();
                URL loadFXML = getClass().getResource(TranspoolResourcesConstants.ADD_NEW_TRIP_REQUEST_FXML_RESOURCE_IDENTIFIER);
                loader.setLocation(loadFXML);
                ScrollPane root1 = loader.load();
                AddTripRequestController trController = loader.getController();
                setAddTripRequestController(trController);
                trController.setMainController(this);
                trController.loadData();
                Stage stage=new Stage();
                stage.setTitle("Add trip request");
                stage.setScene(new Scene(root1));
                stage.show();
                tripRequestController.setStage(stage);
        }

        @FXML
        void loadXmlButtonAction(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader();
                URL loadFXML = getClass().getResource(TranspoolResourcesConstants.LOAD_XML_FXML_RESOURCE_IDENTIFIER);
                loader.setLocation(loadFXML);
                ScrollPane root1 = loader.load();
                loadXmlController XmlController = loader.getController();
                setLoadXmlController(XmlController);
                XmlController.setMainController(this);
                Stage stage = new Stage();

                stage.setTitle("Load xml file");
                stage.setScene(new Scene(root1));
                stage.show();
                xmlController.setStage(stage);//
        }

        void setLoadXmlController(loadXmlController xmlController){
                this.xmlController = xmlController;
        }

        void setAddTripController(AddTripController tripController){
                this.tripController = tripController;
        }

        void setAddTripRequestController(AddTripRequestController tripReqController){
                this.tripRequestController = tripReqController;
        }

        void setMatchController(FindMatchController matchController){
                this.matchController = matchController;
        }

        @FXML
        void UpdateButtonAction(ActionEvent event) {
                try {
                        boolean isForward;
                        if (!forwardRadioButton.isSelected() && !backwardRadioButton.isSelected())
                                throw new NoChoiceException("forward/backward");
                        isForward = forwardRadioButton.isSelected();
                        int timeUnit= receiveTime(timeUnitChoiceBox.getValue());
                        if (timeUnit == 5 || timeUnit==30)
                                tsEngine.getApplicationTime().updateTime(0,timeUnit,isForward);
                        else
                                tsEngine.getApplicationTime().updateTime(timeUnit,0,isForward);
                        String dayString = "Day: "+String.valueOf(tsEngine.getApplicationTime().getDayNumber());
                        dayLabel.setText(dayString);
                        int hour = tsEngine.getApplicationTime().getAppTime().getHour();
                        int minutes = tsEngine.getApplicationTime().getAppTime().getMinutes();
                        String timeString = "Time: "+String.format("%02d",hour)+":"+ String.format("%02d",minutes);
                        timeLabel.setText(timeString);
                        for(Trip trip : tsEngine.getTrips().getTripList()) {
                                trip.updateCurrentPassengers(tsEngine.getApplicationTime());
                                if(trip.dayMatches(tsEngine.getApplicationTime().getDayNumber())&&trip.timeMatches(tsEngine.getApplicationTime().getAppTime())) {
                                        for (TitledPane tp : tripAccordion.getPanes()) {
                                                if (Integer.valueOf(tp.getText().substring(13)).equals(trip.getTripNumber()))
                                                        tp.setContent(new TextArea(showTripInfo(trip)));
                                        }
                                }
                        }

                        //showCurrentTrips();

                }catch (Exception e){


                }

        }

        private int receiveTime(String value) { // gets string of time unit and returns the int value of it
                if(value.equals("5 Minutes"))
                        return 5;
                else if(value.equals("30 Minutes"))
                        return 30;
                else if(value.equals("1 Hour"))
                        return 1;
                else if(value.equals("2 Hours"))
                        return 2;
                else if(value.equals("1 Day"))
                        return 24;
                return 1;
        }

        public void loadTimeUnit(){
                //scheduleList.removeAll(scheduleList);
                List<String> list = new ArrayList<>(Arrays.asList("5 Minutes","30 Minutes","1 Hour","2 Hours","1 Day"));
                timeUnitList = FXCollections.observableArrayList(list);
                //scheduleList.addAll(list);
                timeUnitChoiceBox.getItems().addAll(timeUnitList);
        }

        @FXML
        void matchButtonAction(ActionEvent event) throws IOException {

                FXMLLoader loader = new FXMLLoader();
                URL loadFXML = getClass().getResource(TranspoolResourcesConstants.LOAD_MATCH_FXML_RESOURCE_IDENTIFIER);
                loader.setLocation(loadFXML);
                ScrollPane root1 = loader.load();
                FindMatchController matchController = loader.getController();
                setMatchController(matchController);
                matchController.setMainController(this);
                //matchController.setOptionsAccordion(tripRequestAccordion);
                matchController.loadData();

                Stage stage=new Stage();

                stage.setTitle("Find a match");
                stage.setScene(new Scene(root1));
                stage.show();
                matchController.setStage(stage);//

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                addNewTripButton.disableProperty().bind(isFileSelected.not());
                addNewTripRequestButton.disableProperty().bind(isFileSelected.not());
                matchButton.disableProperty().bind(isFileSelected.not());
                loadTimeUnit();
        }

        public StationManager createStations() {
                StationManager sm = new StationManager(StationNode::new);

                for(Station station:tsEngine.getMap().getStations().getStationList()) {
                        StationNode stationNode = sm.getOrCreate(station.getX(),station.getY());
                        stationNode.setName(station.getName());
                        stationNode.setDetailsSupplier(() -> {
                                List<String> passengerList = new ArrayList<>();
                                        List<String> list= tsEngine.getDriversInStation(tsEngine.getApplicationTime().getAppTime(),station.getName(),passengerList);
                                return new StationDetailsDTO(list,passengerList);
                                });

                        graph.getModel().addCell(stationNode);
                }
                return sm;
        }

        public void markTrip() {
                if(tripAccordion.getExpandedPane()==null)
                        return;
                Integer serialNumber= Integer.parseInt(tripAccordion.getExpandedPane().getText().substring(13));
                updateEdges(tsEngine.getTrips().bringTripByInt(serialNumber),"line3");
        }

//        public void showCurrentTrips(){
//                getTripAccordion().getPanes().removeAll(getTripAccordion().getPanes());
//                for (Trip trip : tsEngine.getTrips().getTripList()) {
//                        TitledPane currTrip = new TitledPane("Trip Number #" + trip.getTripNumber()
//                                ,new TextArea(showTripInfo(trip)));
//                        currTrip.setOnMouseClicked(event -> {
//                                unMarkTrip(trip.getTripNumber());
//                                if(currTrip.isExpanded())
//                                        markTrip();
//                        });
//                        if(trip.dayMatches(tsEngine.getApplicationTime().getDayNumber())&&trip.timeMatches(tsEngine.getApplicationTime().getAppTime()))
//                                getTripAccordion().getPanes().add(currTrip);
//                        //createEdges(trip);
//                }
//        }

        public void unMarkTrip(int tripNumber) {
                for(Map.Entry<String,ArrowedEdge> entry:edgeManager.getHm().entrySet()){
                        entry.getValue().getLine().getStyleClass().remove("line3");
                        entry.getValue().getText().getStyleClass().remove("edge-text");
                }
                graph.layout(mgLayout);
        }

        public void createEdges(Trip trip) {
                String currFrom = null, currTo = null;
                for(int i=0; i<trip.getCourse().getCourse().size()-1; i++) {
                        currFrom=trip.getCourse().getCourse().get(i);
                        currTo=trip.getCourse().getCourse().get(i+1);
                        Station from= tsEngine.getMap().getStations().getStationByName(currFrom);
                        Station to= tsEngine.getMap().getStations().getStationByName(currTo);
                        ArrowedEdge edge = new ArrowedEdge(mgLayout.getCoordinatesManager().getOrCreate(from.getX(),from.getY()), mgLayout.getCoordinatesManager().getOrCreate(to.getX(),to.getY()), currFrom,currTo);
                        Road road=tsEngine.getMap().getRoads().checkIfRoadExists(currFrom,currTo);
                        edgeManager.addEdgeToHm(edge,road.getOrigin()+","+road.getDestination());
                        graph.getModel().addEdge(edge);
                        Platform.runLater(() -> {
                                        edge.getLine().getStyleClass().add("line1");
                                        edge.getText().getStyleClass().add("edge-text");
                        });

                }
                graph.endUpdate();
        }

        public void updateEdges(Trip trip, String color) {
                String currFrom = null, currTo = null;
                for (int i = 0; i < trip.getCourse().getCourse().size() - 1; i++) {
                        currFrom = trip.getCourse().getCourse().get(i);
                        currTo = trip.getCourse().getCourse().get(i + 1);
                        Road road = tsEngine.getMap().getRoads().checkIfRoadExists(currFrom, currTo);
                        ArrowedEdge edge = edgeManager.getHm().get(road.getOrigin() + "," + road.getDestination());
                        edge.getLine().getStyleClass().add(color);
//                        FadeTransition ft = new FadeTransition(Duration.millis(2000),edge.getLine());
//                        ft.setFromValue(1);
//                        ft.setToValue(0.1);
//                        ft.setCycleCount(4);
//                        ft.play();

//                        if(mark) {
//                                Platform.runLater(() -> {
//                                        edge.getLine().getStyleClass().add(color);
//                                        edge.getText().getStyleClass().add("edge-text");
//                                });
//
//                        }
//                        else{
//                                Platform.runLater(() -> {
//                                        edge.getLine().getStyleClass().remove(color);
//                                        edge.getText().getStyleClass().remove("edge-text");
//                                });
//                        }

                }
                graph.endUpdate();
                graph.layout(mgLayout);
        }
}

