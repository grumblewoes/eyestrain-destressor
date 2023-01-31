import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {

    try {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("MainViewController.fxml")));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("pusheen.png"));
        primaryStage.setTitle("Destresso");
        primaryStage.setScene(scene);
        primaryStage.show();
 
        //in order to minimize to system tray
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {minimize(primaryStage);});

    }
    catch (IOException e) {
        
        e.printStackTrace();
    }
    
}

//minimizes to system tray on window exit
public void minimize(Stage primaryStage) {
         //Check the SystemTray is supported
         if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        try {
            //the image class is abstract, so it needs to indirectly be instantiated
            java.awt.Image image = ImageIO.read(getClass().getResource("pusheen.png"));
            final TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true); //so image size = icon size
        
            final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem openItem = new MenuItem("Open");
        MenuItem exitItem = new MenuItem("Quit");

        //Add components to pop-up menu
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);

        //logic for items
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(() -> {
                    primaryStage.show();
                });
        }});

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
        }});

        trayIcon.setPopupMenu(popup);

        try 
        {
            tray.add(trayIcon);
        } 
        catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
}

 public static void main(String[] args) {
        launch(args);
    }
}