package edu.wpi.teamC.views;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamC.Capp;
import edu.wpi.teamC.Translate;
import edu.wpi.teamC.TranslateLanguageOption;
import edu.wpi.teamC.repository.LanguageSingleton;
import edu.wpi.teamC.views.serviceRequest.WeatherManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class StartUpController {

    @FXML
    private Text welcome;
    @FXML
    private AnchorPane anchor;
    @FXML
    private Rectangle r1;
    @FXML
    private Rectangle r2;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    @FXML
    private HBox hbox4;
    @FXML
    private JFXButton emergency;
    @FXML
    private JFXButton exit;
    @FXML
    private ImageView bwh;
    @FXML
    private JFXButton login;
    @FXML
    private JFXButton guest;
    @FXML
    private VBox vbox;
    @FXML
    private ImageView pic;
    @FXML
    private JFXButton signup;
    @FXML
    private JFXButton aboutus;
    @FXML
    private JFXButton credits;
    @FXML
    private Text description;
    @FXML
    private Text temp;
    @FXML private HBox hboxx;
    @FXML
    private JFXComboBox<TranslateLanguageOption> languageDropDown;
    static LanguageSingleton lang = LanguageSingleton.getInstance();
    String language = lang.s;
    String celsius = "°C";

    @FXML private Label timerLabel;
    private Timeline clockTimeline;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
    private StringProperty time = new SimpleStringProperty("");
    private void updateTime() {
        time.set(timeFormat.format(Calendar.getInstance().getTime()));
    }
    WeatherManager weatherManager;

    @FXML
    public void initialize() throws SQLException {

        //height and width of top rectangle
        r1.widthProperty().bind(anchor.widthProperty());
        r1.heightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and width of bottom rectangle
        r2.widthProperty().bind(anchor.widthProperty());
        r2.heightProperty().bind(anchor.heightProperty().multiply(522).divide(680));

        //height of top hbox with title in it
        hbox1.prefHeightProperty().bind(anchor.heightProperty().multiply(140).divide(680));

        //height and bottom anchor for bottom hbox with login/guest buttons in it
        hbox2.prefHeightProperty().bind(anchor.heightProperty().multiply(61).divide(680));
        AnchorPane.setBottomAnchor(hbox2, anchor.getPrefHeight()*309/680);

        //height and bottom anchor for middle hbox with picture in it
        hbox3.prefHeightProperty().bind(anchor.heightProperty().multiply(84).divide(680));
        AnchorPane.setBottomAnchor(hbox3, anchor.getPrefHeight()*177/680);

        hbox4.prefHeightProperty().bind(anchor.heightProperty().multiply(45).divide(680));
        AnchorPane.setBottomAnchor(hbox4, anchor.getPrefHeight()*31/680);

        //width and height for bwh image
        bwh.fitWidthProperty().bind(anchor.widthProperty().multiply(112).divide(1280));
        bwh.fitHeightProperty().bind(anchor.heightProperty().multiply(128).divide(680));

        //width and right anchor for emergency button
        emergency.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(emergency, anchor.getPrefWidth() * 94 / 1280);

        //width and right anchor for exit button
        exit.prefWidthProperty().bind(anchor.widthProperty().multiply(85).divide(1280));
        AnchorPane.setRightAnchor(exit, anchor.getPrefWidth() * 94 / 1280);

        //picture constraints (not sure if this is needed tbh)

        //AnchorPane.setRightAnchor(pic, anchor.getPrefWidth()*31/1280);
        //AnchorPane.setLeftAnchor(pic, anchor.getPrefWidth()*31/1280);
        pic.fitHeightProperty().bind(anchor.heightProperty().multiply(522).divide(680));
        pic.fitWidthProperty().bind(anchor.widthProperty());
        //AnchorPane.setBottomAnchor(pic, anchor.getPrefHeight() * 124.4375 / 680);


        AnchorPane.setBottomAnchor(hboxx, anchor.getPrefHeight()*405/680);


        //right anchor for guest and login buttons
        //AnchorPane.setRightAnchor(guest, anchor.getPrefWidth()*300/1280);
        //AnchorPane.setLeftAnchor(login, anchor.getPrefWidth()*300/1280);

        //vbox constraints
        //vbox.prefHeightProperty().bind(anchor.heightProperty().multiply(84).divide(680));
        //AnchorPane.setBottomAnchor(vbox, anchor.getPrefHeight()*18/680);
        //AnchorPane.setRightAnchor(vbox, anchor.getPrefHeight()*94/680);

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();
        timerLabel.textProperty().bind(time);
        anchor.setBottomAnchor(timerLabel, 0.0);
        anchor.setRightAnchor(timerLabel, 0.0);


        initLanguageDropDown();


        weatherManager = new WeatherManager("Boston");
        weatherManager.getWeather();
        System.out.println("test1");
        temp.setText(weatherManager.getTemperature().toString()+celsius);
        System.out.println("test2");
        description.setText("   " + weatherManager.getDescription());
        initTranslate();
    }
    void initTranslate(){

        try {
            languageDropDown.setPromptText(Translate.translate("en",lang.s,languageDropDown.getPromptText()));
            LinkedList<ButtonBase> buttonsToTranslate=new LinkedList<>();
            buttonsToTranslate.add(credits);
            buttonsToTranslate.add(aboutus);
            buttonsToTranslate.add(signup);
            buttonsToTranslate.add(emergency);
            buttonsToTranslate.add(exit);
            buttonsToTranslate.add(login);
            buttonsToTranslate.add(guest);
            Translate.translateButtonList(buttonsToTranslate);
            welcome.setText(Translate.translate("en",lang.s,welcome.getText()));
            description.setText(Translate.translate("en", lang.s, description.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initLanguageDropDown() {
        String spanish="Español";
        spanish=new String(spanish.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8);
        String french="Francés";
        french=new String(french.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8);
        celsius=new String(celsius.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8);
        languageDropDown.getItems().add(new TranslateLanguageOption("English", "en"));
        languageDropDown.getItems().add(new TranslateLanguageOption(spanish, "es"));
        languageDropDown.getItems().add(new TranslateLanguageOption(french, "fr"));
        languageDropDown.getItems().add(new TranslateLanguageOption("Deutsche", "de"));
        languageDropDown.getItems().add(new TranslateLanguageOption("Suomalainen","fi"));
        languageDropDown.getItems().add(new TranslateLanguageOption("Italiano","it"));
        String portugese="português";
        portugese=new String(portugese.getBytes(Charset.defaultCharset()),StandardCharsets.UTF_8);
        languageDropDown.getItems().add(new TranslateLanguageOption(portugese,"pt"));
        languageDropDown.getItems().add(new TranslateLanguageOption("Nederlands","nl"));



    }

    public void languageSelected() {
        lang.setS(languageDropDown.getSelectionModel().getSelectedItem().getID());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void goEmergency(ActionEvent actionEvent) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/serviceRequest/Emergency.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void goCovidInfo(ActionEvent e) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/CovidInfo.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goAbout(ActionEvent e) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/About.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goSignUp(ActionEvent e) {
        try {
            Capp.prevPage = "/edu/wpi/teamC/fxml/StartUp.fxml";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/SignUpPage.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goGuest(ActionEvent e) throws SQLException {
        try {
            LoginController.setCorrectUser("guest");
            Capp.userType = "Patient";
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/DefaultScreenGuest.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goCredits(ActionEvent e) throws SQLException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/Credits.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void LogIn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/Login.fxml"));
            Capp.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

}
