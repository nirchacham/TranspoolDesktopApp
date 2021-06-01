package tasks.components.findMatch;

import classes.*;
import exceptions.LettersException;
import exceptions.NoChoiceException;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.common.TranspoolResourcesConstants;
import tasks.components.AddTripRequest.AddTripRequestController;
import tasks.components.feedback.FeedbackController;
import tasks.components.main.TranspoolController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static classes.Trip.showTripInfo;
import static classes.TripRequest.showTripRequestInfo;

public class FindMatchController  {
    private TranspoolController tsController;
    private Stage stage;

    @FXML private ChoiceBox<Integer> tripRequestChoiceBox;
    @FXML private Button showTripButton;
    @FXML private ChoiceBox<Integer> tripChoiceBox;
    @FXML private Button assignButton;
    @FXML private Accordion optionsAccordion;
    @FXML private TextField maxMatchesTextField;
    @FXML private RadioButton yesRadioButton;
    @FXML private RadioButton noRadioButton;

    private ObservableList<Trip> tripObservableList;
    private ObservableList<TripRequest> tripRequestObservableList;
    private SimpleBooleanProperty isTrSelected;
    private LinkedList<LinkedList<MatchedRide>> check;


    private FeedbackController feedbackController;

    public FindMatchController() {
        isTrSelected = new SimpleBooleanProperty(false);
    }

    //private ObjectProperty<Accordion> accordionProperty;


    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    void assignButtonAction(ActionEvent event) throws IOException {
        Stage messageStage = new Stage();
        Label message=new Label("The trip request has been assigned");
        try {
            if (tripChoiceBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("a trip or exit if you don't want to");//hours/minutes check
            if(yesRadioButton.isSelected()) {
                LinkedList<MatchedRide> matchedRides=check.get(tripChoiceBox.getValue()-1);
                for(MatchedRide mr:matchedRides) {
                   // if(tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()).getTripHm().get(mr.getTrip().getTripNumber())==null)
                        tsController.getTsEngine().addPassengerToTrip(mr.getTrip(),tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()));
                    PassengerOnTrip passenger=new PassengerOnTrip(tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()),mr.getRoute().getFirst().getDayInFirstStation()
                    ,mr.getRoute().getFirst().getDayInFirstStation(),mr.getRoute().getFirst().getFirstStation(),mr.getRoute().getFirst().getSecondStation(),
                            mr.getRoute().getFirst().getTimeInFirstStation(),mr.getRoute().getFirst().getTimeInSecondStation());
                    mr.getTrip().addPassengersManager(passenger);
                }
            }
            else if(noRadioButton.isSelected()) {
                Trip trip = tsController.getTsEngine().getTrips().bringTripByInt(tripChoiceBox.getValue());
                tsController.getTsEngine().addPassengerToTrip(trip,
                        tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()));
                TripRequest request=tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue());

                PassengerOnTrip passenger2=new PassengerOnTrip( request,request.getRequestDayNumber(),request.getRequestDayNumber()
                        ,tsController.getTsEngine().getMap().getStations().getStationByName(request.getOrigin())
                        ,tsController.getTsEngine().getMap().getStations().getStationByName(request.getDestination())
                        ,trip.estimatedArrivalTimeToStation(request.getOrigin()),trip.estimatedArrivalTimeToStation(request.getDestination()));
                trip.addPassengersManager(passenger2);
            }

            tsController.getTripRequestAccordion().getPanes().removeAll(tsController.getTripRequestAccordion().getPanes());
            for(TripRequest tr:tsController.getTsEngine().getTrips().getRequestsLists()) {
                TitledPane currTripRequest = new TitledPane("Trip Request #" + tr.getTripRequestNumber()
                        ,new TextArea(showTripRequestInfo(tr)));
                tsController.getTripRequestAccordion().getPanes().add(currTripRequest);
            }
            if(noRadioButton.isSelected()) {
                FXMLLoader loader = new FXMLLoader();
                URL loadFXML = getClass().getResource(TranspoolResourcesConstants.LOAD_FEEDBACK_FXML_RESOURCE_IDENTIFIER);
                loader.setLocation(loadFXML);
                ScrollPane root1 = loader.load();
                FeedbackController feedbackController = loader.getController();
                feedbackController.setMainController(tsController);
                feedbackController.setFeedbackTrip(tripChoiceBox.getValue());

                Stage stage = new Stage();
                stage.setTitle("Add Feedback");
                stage.setScene(new Scene(root1));
                stage.show();
                feedbackController.setStage(stage);
            }
            else {
                LinkedList<MatchedRide> matchedRides=check.get(tripChoiceBox.getValue()-1);
                Set<Trip> mRides=new HashSet<>();
                for(MatchedRide mr:matchedRides)
                    mRides.add(mr.getTrip());
                for(Trip trip: mRides) {
                    FXMLLoader loader = new FXMLLoader();
                    URL loadFXML = getClass().getResource(TranspoolResourcesConstants.LOAD_FEEDBACK_FXML_RESOURCE_IDENTIFIER);
                    loader.setLocation(loadFXML);
                    ScrollPane root1 = loader.load();
                    FeedbackController feedbackController = loader.getController();
                    feedbackController.setMainController(tsController);
                    feedbackController.setFeedbackTrip(trip.getTripNumber());

                    Stage stage = new Stage();
                    stage.setTitle("Add Feedback for " + trip.getDriverName());
                    stage.setScene(new Scene(root1));
                    stage.show();
                    feedbackController.setStage(stage);
                }
            }
        }catch (Exception e){
            message = new Label(e.getMessage());
        }
        Scene scene = new Scene(message,300,200);
        message.setAlignment( Pos.CENTER );
        messageStage.setScene(scene);
        messageStage.show();
        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished((e) -> {
            messageStage.close();
            wait.playFromStart();
        });
        wait.play();
        stage.close();
    }

    @FXML
    void showTripButtonAction(ActionEvent event) throws LettersException {
        int maxNum=0, i=0;
        Stage messageStage2 = new Stage();
        Label message2=new Label();
        try {
            maxNum = Integer.valueOf(maxMatchesTextField.getText());
            if(tripRequestChoiceBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("trip request");
            if(maxMatchesTextField.getText().isEmpty())
                throw new NoChoiceException("maximum number of options to show");
            if(!yesRadioButton.isSelected() && !noRadioButton.isSelected())
                throw new NoChoiceException("yes/no");//departure/arrival check
        }catch (Exception e){
            message2 = new Label(e.getMessage());
            Scene scene = new Scene(message2,300,200);
            message2.setAlignment( Pos.CENTER );
            messageStage2.setScene(scene);
            messageStage2.show();
            stage.close();
        }
        if(yesRadioButton.isSelected()) {
            Matcher matcher = new Matcher(tsController.getTsEngine());
            check = matcher.makeAMatch(tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()), maxNum);
            if(check.size()==0) {
                Stage messageStage = new Stage();
                Label message=new Label("There was no match found for the trip request");
                Scene scene = new Scene(message,300,200);
                message.setAlignment( Pos.CENTER );
                messageStage.setScene(scene);
                messageStage.show();
            }
            else {
                List<Integer> iList=new ArrayList<>();
                optionsAccordion.getPanes().removeAll(optionsAccordion.getPanes());
                for (int j = 0; j < check.size(); j++) {
                    iList.add(i+1);
                    if (i < maxNum) {
                        TitledPane currTrip = new TitledPane("Option Number #" + (i + 1)
                                , new TextArea(MatchedRide.showMatchedRideInfo(check.get(i))));
                        optionsAccordion.getPanes().add(currTrip);
                        i++;
                    }
                }
                tripChoiceBox.getItems().removeAll(tripChoiceBox.getItems());
                for (Integer integer : iList) {
                    tripChoiceBox.getItems().add(integer);
                }
            }
            isTrSelected.setValue(true);
        }
        else if(noRadioButton.isSelected()) {
           // List<Trip> list = tsController.getTsEngine().bringOptionalTrips(tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()));
            Matcher matcher2 = new Matcher(tsController.getTsEngine());
            check = matcher2.makeAMatch(tsController.getTsEngine().getTrips().bringTripRequestByInt(tripRequestChoiceBox.getValue()), maxNum);
            List<Trip> list2 = new ArrayList<>();
            for(LinkedList<MatchedRide> mr:check){
                if(mr.size()>1)
                    ;//check.remove(mr);
                else
                    list2.add(mr.getFirst().getTrip());
            }

            //optionsAccordion.getPanes().addAll(list);
            tripObservableList = FXCollections.observableArrayList(tsController.getTsEngine().getTrips().getTripList());
            optionsAccordion.getPanes().removeAll(optionsAccordion.getPanes());

            for (Trip trip : list2) {
                if (i < maxNum) {
                    TitledPane currTrip = new TitledPane("Trip Number #" + trip.getTripNumber()
                            , new TextArea(showTripInfo(trip)));
                    optionsAccordion.getPanes().add(currTrip);
                    i++;
                }
            }
            tripChoiceBox.getItems().removeAll(tripChoiceBox.getItems());
            for (Trip trip : list2) {
                tripChoiceBox.getItems().add(trip.getTripNumber());
            }
            if (list2.isEmpty()) {
                Stage messageStage = new Stage();
                Label message = new Label("There was no match found for the trip request");
                Scene scene = new Scene(message, 300, 200);
                message.setAlignment(Pos.CENTER);
                messageStage.setScene(scene);
                messageStage.show();
            } else
                isTrSelected.setValue(true);
        }
    }

    public void setMainController(TranspoolController transpoolController) {
        tsController=transpoolController;
    }

    public Accordion getOptionsAccordion() {
        return optionsAccordion;
    }
    public void setOptionsAccordion(Accordion optionsAccordion) {

    }

//
    public void loadData() {
//optionsAccordion = tsController.getTripRequestAccordion();
        for(TripRequest tr:tsController.getTsEngine().getTrips().getRequestsLists()){
            if(!tr.isAssigned()) {
                tripRequestChoiceBox.getItems().add(tr.getTripRequestNumber());
            }
        }

        tripRequestObservableList = FXCollections.observableArrayList(tsController.getTsEngine().getTrips().getRequestsLists());
        for (TripRequest tr : tsController.getTsEngine().getTrips().getRequestsLists()) {
            if(!tr.isAssigned()) {
                TitledPane currTrip = new TitledPane("Trip Request #" + tr.getTripRequestNumber()
                        , new TextArea(showTripRequestInfo(tr)));
                optionsAccordion.getPanes().add(currTrip);
            }
        }

        assignButton.disableProperty().bind(isTrSelected.not());


//        tripObservableList = FXCollections.observableArrayList(tsController.getTsEngine().getTrips().getTripList());
//        for (Trip trip : tsController.getTsEngine().getTrips().getTripList()) {
//            TitledPane currTrip = new TitledPane("Trip Number #" + trip.getTripNumber()
//                    ,new TextArea(showTripInfo(trip)));
//            tsController.getTripAccordion().getPanes().add(currTrip);
//        }

    }
}
