package tasks.components.AddTripRequest;

import classes.Station;
import classes.Time;
import exceptions.NameException;
import exceptions.NoChoiceException;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.common.TranspoolResourcesConstants;
import tasks.components.main.TranspoolController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static classes.Trip.showTripInfo;
import static classes.TripRequest.showTripRequestInfo;

public class AddTripRequestController  {

    @FXML private TextField nameTextField;
    @FXML private RadioButton departureRadioButton;
    @FXML private RadioButton arrivalRadioButton;
    @FXML private ChoiceBox<String> originChoiseBox;
    @FXML private ChoiceBox<String> destinationChoiseBox;
    @FXML private ChoiceBox<String> hoursChoiseBox;
    @FXML private ChoiceBox<String> minutesChoiseBox;
    @FXML private TextField dayTextField;
    @FXML private ToggleGroup pref;
    @FXML private Button submitButton;

    ObservableList<String> stationList;
    ObservableList<String> hourList;
    ObservableList<String> minuteList;


    private TranspoolController tsController;
    private Stage stage;

    public AddTripRequestController() {
    }

    @FXML
    public void departureRadioButtonAction(ActionEvent event) {


    }

    @FXML
    public void arrivalRadioButtonAction(ActionEvent event) {

    }

    @FXML
    public void nameTextFieldAction(ActionEvent event) {
        nameTextField.getText();
    }


    public void loadData() {
        loadStations();
        loadTime();
    }
    public void loadStations(){
       // stationList.removeAll(stationList);
        List<String> list = tsController.getTsEngine()
                .getMap().getStations().getStationList().stream().map(Station::getName).collect(Collectors.toList());
        stationList = FXCollections.observableArrayList(list);
        //stationList.addAll(list);
        originChoiseBox.getItems().addAll(stationList);
        destinationChoiseBox.getItems().addAll(stationList);
    }

    public void loadTime(){
        //hourList.removeAll();
        //minuteList.removeAll();
        List<String> hlist= new ArrayList<>();
        List<String> mlist= new ArrayList<>();
        for(int i=0;i<24;i++){
            hlist.add(String.format("%02d",i));
        }
        for(int i=0;i<60;i+=5){
            mlist.add(String.format("%02d",i));
        }
        hourList = FXCollections.observableArrayList(hlist);
        //hourList.addAll(hlist);
        hoursChoiseBox.getItems().addAll(hourList);

        minuteList = FXCollections.observableArrayList(mlist);
        //minuteList.addAll(mlist);
        minutesChoiseBox.getItems().addAll(minuteList);
    }

    @FXML
    public void dayTextFieldAction(ActionEvent event) {
        dayTextField.getText();
    }

    public AddTripRequestController(TranspoolController tsController) {
        this.tsController = tsController;
    }

    @FXML
    public void submitButtonAction(ActionEvent event) throws IOException {
        boolean isDep=false;
        boolean isMyChoiceBoxEmpty;
        Stage messageStage = new Stage();
        Label message=new Label("The trip request was added successfully!");
        try {
            Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
            Matcher matcher = pattern.matcher(nameTextField.getText());
            boolean ok = true;
            if(nameTextField.getText().isEmpty())//name check
                throw new NoChoiceException("your name");
             ok = nameTextField.getText().matches(".*\\d+.*");
            if (!matcher.find()) {
                throw new NameException();
            }
            if(originChoiseBox.getSelectionModel().isEmpty() || destinationChoiseBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("origin/destination");// origin/destination check

            if(hoursChoiseBox.getSelectionModel().isEmpty() || minutesChoiseBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("hours/minutes");//hours/minutes check
            if(dayTextField.getText().isEmpty())
                throw new NoChoiceException("a day");//day check
            int hours= Integer.parseInt(hoursChoiseBox.getValue());
            int min=Integer.parseInt(minutesChoiseBox.getValue());
            int day=Integer.parseInt(dayTextField.getText());
            if(!departureRadioButton.isSelected() && !arrivalRadioButton.isSelected())
                throw new NoChoiceException("departure/arrival");//departure/arrival check
            isDep=departureRadioButton.isSelected();

            tsController.getTsEngine().createTripRequest(nameTextField.getText(),originChoiseBox.getValue(),
                    destinationChoiseBox.getValue(),new Time(hours,min),isDep,day);//create trip and add it to the list

            TitledPane currTripRequest = new TitledPane("Trip Request #" + tsController.getTsEngine().getTrips()
                    .getRequestsLists().get((tsController.getTsEngine().getTrips()
                            .getRequestsLists().size())-1).getTripRequestNumber()
                    ,new TextArea(showTripRequestInfo(tsController.getTsEngine().getTrips()
                    .getLastTripRequest())));
            tsController.getTripRequestAccordion().getPanes().add(currTripRequest);
    }catch(Exception e) {
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

    public void setMainController(TranspoolController transpoolController) {
        tsController=transpoolController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
