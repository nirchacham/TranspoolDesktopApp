package tasks.components.loadXml;

import checks.FileCheck;
import classes.TranspoolSystem;
import classes.Trip;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.PannableCanvas;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.LoadXmlTask;
import tasks.components.main.TranspoolController;
import tasks.components.map.component.station.StationManager;
import tasks.components.map.layout.MapGridLayout;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;

import static classes.Trip.showTripInfo;

public class loadXmlController  {

    @FXML private ProgressBar loadProgressBar;
    @FXML private Label showStatusLabel;
    @FXML private Label progressPercentLabel;
    private Stage stage;
    private TranspoolController tsController;

    private ListProperty<Trip> tripListProp;
    //ObservableList<TripRequest> trObservableList;
    private ObservableList<Trip> tripObservableList;

    @FXML
    void browseButtonAction(ActionEvent event) throws JAXBException {
        stage.setAlwaysOnTop(false);
        FileChooser file = new FileChooser();
        file.setTitle("Select XML file");
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files","*.xml"));
        File f = file.showOpenDialog(tsController.getPrimaryStage());
//        if(f!=null) {
//            showStatusLabel.setText("");
//        }
        if(f==null)
            return;
        LoadXmlTask xmlTask=tsController.getTsEngine().loadXmlFile(f);

        bindTaskToUIComponents(xmlTask, () -> tsController.setIsFileSelected(true));
        new Thread(xmlTask).start();
        stage.setAlwaysOnTop(true);
    }

    public void setMainController(TranspoolController mainController){
        tsController=mainController;
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        showStatusLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        loadProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });

    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.showStatusLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.loadProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);

        tripObservableList = FXCollections.observableArrayList(tsController.getTsEngine().getTrips().getTripList());
        tsController.getTripAccordion().getPanes().clear();
        tsController.getTripRequestAccordion().getPanes().clear();
        for (Trip trip : tsController.getTsEngine().getTrips().getTripList()) {
            TitledPane currTrip = new TitledPane("Trip Number #" + trip.getTripNumber()
                    ,new TextArea(showTripInfo(trip)));
            currTrip.setOnMouseClicked(event -> {
                tsController.unMarkTrip(trip.getTripNumber());
                if(currTrip.isExpanded() && trip.dayMatches(tsController.getTsEngine().getApplicationTime().getDayNumber()) && trip.timeMatches(tsController.getTsEngine().getApplicationTime().getAppTime()))
                    tsController.markTrip();
            });
            tsController.getTripAccordion().getPanes().add(currTrip);
            tsController.createEdges(trip);
        }
        //tsController.showCurrentTrips();
        StationManager sm = tsController.createStations();

        tsController.getMgLayout().setStationManager(sm);
        tsController.getGraph().endUpdate();
        tsController.getGraph().layout(tsController.getMgLayout());
        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished((e) -> {
            stage.close();
            wait.playFromStart();
        });
        wait.play();
    }


    public void setStage(Stage stage) {
        this.stage=stage;
    }
}
