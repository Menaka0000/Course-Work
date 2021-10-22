package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;


public class SetDateAndTime {
    public static String name;
    public static String loginTime;

    public static void setDateAndTime(Label lblDate, Label lblTime,Label name1,Label login){
        name1.setText(name);

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("a");
        String timeNow=sdf.format(new Date());
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + " : " + currentTime.getMinute() +
                            " : " + currentTime.getSecond()+"  "+timeNow
            );
        }),
                new KeyFrame(Duration.seconds(1))

        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
        login.setText(loginTime);
    }
}
