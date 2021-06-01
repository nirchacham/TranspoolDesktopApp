package tasks.components.map.component.details.visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tasks.components.map.component.details.StationDetailsDTO;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ";
//( %s ; %s)
    @FXML private Label stationNameLabel;
    @FXML private Label visitingTripsLabel;
    @FXML private Label passengersInfoLabel;

    public void setData(StationDetailsDTO data) {
        stationNameLabel.setText(data.getName());
        visitingTripsLabel.setText(data.getDrives().toString());
        passengersInfoLabel.setText(data.getPeople().toString());
    }

}
