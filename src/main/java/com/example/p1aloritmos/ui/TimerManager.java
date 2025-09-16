package com.example.p1aloritmos.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerManager {
    private final Label timerLabel;
    private Timeline timer;
    private int secondsElapsed = 0;

    public TimerManager(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    public void startTimer() {
        if (timer != null) timer.stop();
        secondsElapsed = 0;
        if (timerLabel != null) timerLabel.setText("⏱ 00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            if (timerLabel != null) timerLabel.setText("⏱ " + formatTime(secondsElapsed));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) timer.stop();
    }

    private String formatTime(int totalSecs) {
        int mins = totalSecs / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
