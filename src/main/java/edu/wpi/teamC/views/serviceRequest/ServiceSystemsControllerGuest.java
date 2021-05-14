package edu.wpi.teamC.views.serviceRequest;

import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.repository.LanguageSingleton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ServiceSystemsControllerGuest {
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    Translate tr = new Translate();

    @FXML
    private Text serviceSystemsTitle;
    @FXML
    private Button Emergency;
    @FXML
    private Button logOutButton;
    @FXML
    private Button interpreterButton;
    @FXML
    private Button externalButton;
    @FXML
    private Button medicineButton;
    @FXML
    private Button counselingButton;
    @FXML
    private Button maintenanceButton;
    @FXML
    private Button floralButton;
    @FXML
    private Button sanitationButton;
    @FXML
    private Button foodButton;
    @FXML
    private Button internalButton;
    @FXML
    private Button ticketsButton;
    @FXML private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private ImageView bwh;
    @FXML private VBox vbox;
    @FXML private ImageView pic;


    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }


    public void translate() {
        try {
            ArrayList<ButtonBase> buttons = new ArrayList<ButtonBase>();
            buttons.add(Emergency);
            buttons.add(logOutButton);
            buttons.add(interpreterButton);
            buttons.add(externalButton);
            buttons.add(medicineButton);
            buttons.add(counselingButton);
            buttons.add(maintenanceButton);
            buttons.add(floralButton);
            buttons.add(sanitationButton);
            buttons.add(foodButton);
            buttons.add(internalButton);
            buttons.add(ticketsButton);
            System.out.println(serviceSystemsTitle.getText());
            System.out.println(language);
            serviceSystemsTitle.setText(tr.translate("en", language, serviceSystemsTitle.getText()));
            tr.translateButtonList(buttons);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        translate();
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);

        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);


        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        //height of top hbox with title in it
        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        //width and right anchor for emergency button
        Emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(Emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and right anchor for exit button
        logOutButton.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(logOutButton, anchor.getPrefWidth() * 94 / 1280);

        vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        pic.fitHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        pic.fitWidthProperty().bind(anchor.widthProperty());



    }

    public void goEmergency(ActionEvent actionEvent) {
        if (Capp.userType == "Admin") {
            Capp.prevPage = "/edu/wpi/teamC/fxml/serviceRequest/ServiceSystemsAdmin.fxml";
        } else if (Capp.userType == "Employee") {
        } else {
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goSanitation(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Sanitation.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goFloral(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/FloralDelivery.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goFood(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Food.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void goInterpreter(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Interpreter.fxml")); // change to interpreter fxml once scene builder is made
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goMedicine(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/MedDelivery.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goInternalPat(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/IntPatTrans.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goExternalPat(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/ExtPatTrans.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goCounseling(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Counseling.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void goEmployeeTickets(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/EmployeeTickets.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goMaintenance(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Maintenance.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goHome(ActionEvent actionEvent) {
        if (Capp.userType.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreen.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (Capp.userType.equals("Employee")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenEmployee.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
                Capp.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void goHelp(ActionEvent actionEvent) {
        //currently directs to previous page
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Capp.prevPage));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
