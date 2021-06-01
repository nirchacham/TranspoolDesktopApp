package tasks.components.AddNewTrip;

import classes.Schedule;
import classes.Station;
import classes.Time;
import classes.Trip;
import exceptions.*;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.components.main.TranspoolController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static classes.Trip.showTripInfo;

public class AddTripController implements Initializable {

    @FXML private Button SubmitButton;
    @FXML private TextField DriversNameTextField;
    @FXML private ChoiceBox<Integer> CapacityChoiceBox;
    @FXML private TextField PPKTextField;
    @FXML private ChoiceBox<String> MinuteChoiceBox;
    @FXML private ChoiceBox<String> HourChoiceBox;
    @FXML private TextField CourseTextField;
    @FXML private TextField dayTextField;
    @FXML private ChoiceBox<String> schedulingChoiceBox;

    ObservableList<Integer> capacityList;
    ObservableList<String> hourList;
    ObservableList<String> minuteList;
    ObservableList<String> scheduleList;

    private TranspoolController tsController;
    private Stage stage;

    @FXML
    public void CourseTextFieldAction(ActionEvent event) {
        CourseTextField.getText();
    }

    @FXML
    public void DriversNameTextFieldAction(ActionEvent event) {
       // DriversNameTextField.getText();
    }

    @FXML
    public void PPKTextFieldAction(ActionEvent event) {
       // PPKTextField.getText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTime();
        loadScheduling();
        loadCapacity();
    }

    public void loadScheduling(){
        List<String> list = new ArrayList<>(Arrays.asList("One time","Daily","Alternatly","Weekly","Monthly"));
        scheduleList = FXCollections.observableArrayList(list);
        schedulingChoiceBox.getItems().addAll(scheduleList);
    }

    public void loadCapacity(){
        List<Integer> list = new ArrayList<>(Arrays.asList(0,1,2,3,4));
        capacityList = FXCollections.observableArrayList(list);
        CapacityChoiceBox.getItems().addAll(capacityList);
    }

    public void loadTime(){
        List<String> hlist= new ArrayList<>();
        List<String> mlist= new ArrayList<>();
        for(int i=0;i<24;i++){
            hlist.add(String.format("%02d",i));
        }
        for(int i=0;i<60;i+=5){
            mlist.add(String.format("%02d",i));
        }
        hourList = FXCollections.observableArrayList(hlist);
        HourChoiceBox.getItems().addAll(hourList);
        minuteList = FXCollections.observableArrayList(mlist);
        MinuteChoiceBox.getItems().addAll(minuteList);
    }

    @FXML
    public void SubmitButtonAction(ActionEvent event) throws IOException {
        Stage messageStage = new Stage();
        Label message=new Label("The trip was added successfully!");
        try {
            Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
            Matcher matcher = pattern.matcher(DriversNameTextField.getText());
            DriversNameTextField.getText().matches(".*\\d+.*");
            if(DriversNameTextField.getText().isEmpty())//name check
                throw new NoChoiceException("your name");
            if (!matcher.find()) {
                throw new NameException();
            }
            if(HourChoiceBox.getSelectionModel().isEmpty() || MinuteChoiceBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("hours/minutes");//hours/minutes check
            if(schedulingChoiceBox.getSelectionModel().isEmpty())
                throw new NoChoiceException("scheduling");
            if(PPKTextField.getText().isEmpty())//name check
                throw new NoChoiceException("PPK");
            if(dayTextField.getText().isEmpty())//name check
                throw new NoChoiceException("day");
            int hours= Integer.parseInt(HourChoiceBox.getValue());
            int min=Integer.parseInt(MinuteChoiceBox.getValue());
            int PPK=Integer.parseInt(PPKTextField.getText());
            int day=Integer.parseInt(dayTextField.getText());
            Pattern pattern2 = Pattern.compile("^[0-9]+$");
            matcher = pattern2.matcher(PPKTextField.getText());
            PPKTextField.getText().matches(".*\\d+.*");
            if (!matcher.find()) {
                throw new LettersException();
            }
            tsController.getTsEngine().createTrip(DriversNameTextField.getText(),CapacityChoiceBox.getValue(),
                    PPK,tsController.getTsEngine().createRoute(CourseTextField.getText()),new Time(hours,min),
                    new Schedule(schedulingChoiceBox.getValue()),day);

            Trip trip=tsController.getTsEngine().getTrips().getLastTrip();
            TitledPane currTrip = new TitledPane("Trip Number #" + trip.getTripNumber()
                    ,new TextArea(showTripInfo(trip)));
            currTrip.setOnMouseClicked(event1 -> {
                tsController.unMarkTrip(trip.getTripNumber());
                if(currTrip.isExpanded() && trip.dayMatches(tsController.getTsEngine().getApplicationTime().getDayNumber()) && trip.timeMatches(tsController.getTsEngine().getApplicationTime().getAppTime()))
                    tsController.markTrip();
            });
            tsController.getTripAccordion().getPanes().add(currTrip);

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
