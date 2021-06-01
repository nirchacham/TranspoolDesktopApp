package tasks.components.feedback;

import exceptions.NoChoiceException;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasks.components.main.TranspoolController;

public class FeedbackController {

    @FXML private RadioButton firstRadioButton;
    @FXML private ToggleGroup rateGroup;
    @FXML private RadioButton secondRadioButton;
    @FXML private RadioButton thirdRadioButton;
    @FXML private RadioButton forthRadioButton;
    @FXML private RadioButton fifthRadioButton;
    @FXML private TextArea feedbackTextArea;
    @FXML private Button submitButton;

    private int feedbackTrip;

    private TranspoolController tsController;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage=stage;
    }

    public void setFeedbackTrip(int tripNumber){
        feedbackTrip=tripNumber;
    }

    public void setMainController(TranspoolController mainController){
        tsController=mainController;
    }

    @FXML
    void submitButtonAction(ActionEvent event) {
        Stage messageStage = new Stage();
        Label message=new Label("Thank you for your feedback :)");
        try {
            int rate;
            if (!firstRadioButton.isSelected() && !secondRadioButton.isSelected() && !thirdRadioButton.isSelected()
                    && !forthRadioButton.isSelected() && !fifthRadioButton.isSelected())
                         throw new NoChoiceException("rating");
            String string = feedbackTextArea.getText();
            if(firstRadioButton.isSelected()){
                rate=1;
            }
            else if(secondRadioButton.isSelected()){
                rate=2;
            }
            else if(thirdRadioButton.isSelected()){
                rate=3;
            }
            else if(forthRadioButton.isSelected()){
                rate=4;
            }
            else{
                rate=5;
            }

            tsController.getTsEngine().createFeedback(rate,feedbackTextArea.getText(),feedbackTrip);

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
        if (!firstRadioButton.isSelected() && !secondRadioButton.isSelected() && !thirdRadioButton.isSelected()
                && !forthRadioButton.isSelected() && !fifthRadioButton.isSelected())
            ;
        else
            stage.close();
    }

}
