/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfrfkbstopwatch;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import static java.time.Clock.system;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author rottm
 */
public class AnalogStopwatch {
    public Timeline timeline;
    public KeyFrame keyFrame;
    ImageView dialImageView;
    ImageView handImageView;
    public int x = 0;
    public double secondsElapsed = 0.0;
    public double tickTimeInSeconds = 0.01;// how to change the resolution
    public double angleDeltaPerSeconds = 6.0;
    public boolean isRunning;       
    private int lapHolder = 0;
    private int lapNum = 0;
    private double recordTime = 0;
    private double tempTime = 0;
    private double minHolder = 0;
    private double liveTempTime = 0;
    private double liveMinHolder = 0;
    private StackPane container;
    private Text liveText = new Text("--:--.--");
    public StackPane rootContainer;
    public Image dialImage;
    public Image handImage;
    
    public AnalogStopwatch(){
        setupUI();
        setupTimer();
        
    }
    
    public void setupUI(){
        rootContainer = new StackPane();
        container = new StackPane();
        dialImageView = new ImageView();
        handImageView = new ImageView();
        
        dialImage = new Image(getClass().getResourceAsStream("clockface.png"));
        handImage = new Image(getClass().getResourceAsStream("hand.png"));
        
        dialImageView.setImage(dialImage);
        handImageView.setImage(handImage);
        
        VBox view = new VBox();
        view.setAlignment(Pos.BOTTOM_CENTER);
        view.setSpacing(10);
        view.setPadding(new Insets(25, 25, 25, 25));
        
        VBox controls = new VBox();
        

        Button startStop = new Button("Start");
        Button recordReset = new Button("Record");
        
        startStop.setMaxWidth(Double.MAX_VALUE);
        recordReset.setMaxWidth(Double.MAX_VALUE);

        Text text1 = new Text("--:--.--");
        Text text2 = new Text("--:--.--");
        Text text3 = new Text("--:--.--");

        
        controls.setAlignment(Pos.BOTTOM_CENTER);
        controls.setSpacing(10);
        controls.setPadding(new Insets(25, 25, 25, 25));
        controls.getChildren().addAll(recordReset, startStop, liveText, text1, text2, text3);  
        startStop.setStyle("-fx-background-color: #008000; ");
        
        DecimalFormat df = new DecimalFormat("00.00"); 
        DecimalFormat mf = new DecimalFormat("00");
        

        startStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isRunning()){// Timeline not running
                    startStop.setText("Stop");
                    recordReset.setText("Record");
                    startStop.setStyle("-fx-background-color: #f90000; ");
                    
                    timeline.play();
                }
                else{// Timeline is running
                    startStop.setText("Start");
                    recordReset.setText("Reset");
                    startStop.setStyle("-fx-background-color: #008000; ");
                    timeline.pause();
                }   }
        });
        
        recordReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isRunning()){// Timeline not running (RESET)
                    secondsElapsed = 0;
                    lapNum = 0;
                    liveMinHolder = 0;
                    minHolder = 0;
                    tempTime = 0;
                    recordTime = 0;
                    lapHolder = 0;
                    liveText.setText("--:--.--");
                    text1.setText("--:--.--");
                    text2.setText("--:--.--");
                    text3.setText("--:--.--");
                    update();
                }
                else{// Timeline is running
                    lapNum++;
                    lapHolder++;
                    if (lapNum == 3 || lapHolder == 3){
                        lapHolder = 0;
                    }
                    tempTime = secondsElapsed;
                    tempTime = tempTime - recordTime;
                    recordTime = secondsElapsed;

                    
                    while (tempTime >= 60){
                        tempTime -= 60;
                        minHolder++;
                    }
                    
                    String minFormatted = mf.format(minHolder);
                    String formatted = df.format(tempTime); 
                    if(lapHolder == 1){
                        text1.setText("Lap " + lapNum +" "+ minFormatted +":" + formatted);
                    }
                    if(lapHolder == 2){
                        text2.setText("Lap " + lapNum +" "+ minFormatted + ":" + formatted);
                    }
                    if(lapHolder == 0 ){
                        text3.setText("Lap " + lapNum +" "+ minFormatted + ":" + formatted);
                    }
                    minHolder=0;
                }             
            }
        });
    
        container.getChildren().addAll(dialImageView, handImageView);
        view.getChildren().addAll(controls, container);
        rootContainer.getChildren().addAll(view);
        controls.toFront();
    }
    private void setLiveHour(){
        x++;
        
        if(x==6000 || x%6000==0){
            liveMinHolder++;
        }
    }
    public void setupTimer(){
        if(isRunning){
            timeline.stop();
        }
        keyFrame = new KeyFrame(Duration.millis(tickTimeInSeconds * 1000), (ActionEvent) -> {
            
            setLiveHour();
            update();
            liveTime();
        });
        timeline = new Timeline(keyFrame);
        
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    public void liveTime(){
        
    while(liveTempTime >= 60){
        liveTempTime-=60;
    }
    
    DecimalFormat df = new DecimalFormat("00.00"); 
    DecimalFormat mf = new DecimalFormat("00");
    
    String liveMinFormatted = mf.format(liveMinHolder);
    String liveFormatted = df.format(liveTempTime);
        liveText.setText(liveMinFormatted + ":" + liveFormatted);
    }
    private void update(){
        secondsElapsed += tickTimeInSeconds;
        liveTempTime = secondsElapsed;
        double rotation = secondsElapsed * angleDeltaPerSeconds;
        handImageView.setRotate(rotation);
    };
    
    public void start(){
        timeline.play();
    }
    public Double getWidth(){
        if (dialImage != null){
            return dialImage.getWidth();
        }
        else{
            return 0.0;
        }
    }
    public Double getHeight(){
        if(dialImage != null) return dialImage.getHeight();
        else return 0.0;
    }
    public Parent getRootContainer(){
        return rootContainer;
    }
    public void setTickTimeInSeconds(Double tickTimeInSeconds){
        this.tickTimeInSeconds = tickTimeInSeconds;
        setupTimer();
        if (!isRunning()){
            timeline.play();
        }
    }
    public boolean isRunning(){
        if(timeline!=null){
            if(timeline.getStatus() == Animation.Status.RUNNING){
                return true;
            }
        }
        return false;
    }
}
