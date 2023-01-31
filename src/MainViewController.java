import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;

public class MainViewController {

    @FXML
    private Label destressLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button yesButton;

    @FXML
    private ImageView imgView;

    @FXML
    private Button noButton;

    int secondsLeft = 20;
    int timerSeconds = secondsLeft + 1;

    @FXML
    void noBtn(ActionEvent event) {
        ((Stage)destressLabel.getScene().getWindow()).setIconified(true);
    }

    @FXML
    void yesBtn(ActionEvent event) {
        swapScreen("destressing");
    }

    void playNotif(String str) {
        try {
            Clip clip = AudioSystem.getClip();

            //must be done this way because if you use FileInputStream it says "mark/reset not supported" because the stream requires parsers that can read it and verify that it is supported. FileInputStream does not.
            BufferedInputStream stream2 = new BufferedInputStream(getClass().getResourceAsStream("cooldown.wav"));
            BufferedInputStream stream1 = new BufferedInputStream(getClass().getResourceAsStream("complete.wav"));  
            AudioInputStream audio2 = AudioSystem.getAudioInputStream(stream2);
            AudioInputStream audio1 = AudioSystem.getAudioInputStream(stream1);
            
            clip.open((str.equals("complete"))? audio1 : audio2);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }

    String timeFormat(int secondsLeft) {
        String text = "";
        if (secondsLeft > 60)
        {
            text = secondsLeft / 60 + ":";
            if (secondsLeft % 60 == 0)
                text = text + "00";
            else
                {
                    if (secondsLeft  % 60 > 9)
                        text = text + secondsLeft % 60;
                    else
                        text = text + "0" + secondsLeft % 60;
                }
        }
        else 
        {
            text = "0:";
            if (secondsLeft > 9)
                text = text + secondsLeft;
            else
                text = text + "0" + secondsLeft;
        }

        return text;
    }

    void swapScreen(String code) {
        boolean minimized = ((Stage)destressLabel.getScene().getWindow()).isIconified();
        switch (code)
        {
            case "confirmation":
                expandIfMinimized(minimized);
                playNotif("confirmation");
                imgView.setVisible(false);
                destressLabel.setText("Destress?");
                timerLabel.setText("");
                yesButton.setVisible(true);
                noButton.setVisible(true);
                break;
            case "destressing":
                secondsLeft = 20;
                timerLabel.setText(timeFormat(secondsLeft));
                secondsLeft--;    
                timerSwap("cooldown");
                destressLabel.setText("Destressing...");
                yesButton.setVisible(false);
                noButton.setVisible(false);
                imgView.setVisible(true);
                Image img = new Image("sleeping.gif");
                imgView.setImage(img);
                break;
            case "cooldown":
                playNotif("complete");
                secondsLeft = 20 * 60;
                timerLabel.setText(timeFormat(secondsLeft));
                secondsLeft--;
                timerSwap("confirmation");
                destressLabel.setText("Have fun!");
                Image img2 = new Image("pusheen.gif");
                imgView.setImage(img2);
                break;
        }
    }

    void timerSwap(String screenToSwapTo) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), 
        e -> {
            if (secondsLeft > 0) {
                timerLabel.setText(timeFormat(secondsLeft));
                secondsLeft--;
            }
        
            else {
                swapScreen(screenToSwapTo);
            }
        }));

        timeline.setCycleCount(secondsLeft+1);
        timeline.play();
    }

    public void expandIfMinimized(boolean check) {
        if (check) {
            ((Stage)destressLabel.getScene().getWindow()).setIconified(false);
        }
    }
}

    
