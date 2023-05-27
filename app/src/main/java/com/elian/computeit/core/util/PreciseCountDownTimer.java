package com.elian.computeit.core.util;

import java.util.Timer;
import java.util.TimerTask;

// https://stackoverflow.com/questions/23323823/android-countdowntimer-tick-is-not-accurate
public abstract class PreciseCountDownTimer extends Timer {

    private final long totalTime, interval, delay;
    private TimerTask task;
    private long startTime = -1;
    private long timeLeft;
    private boolean restart = false, wasCancelled = false, wasStarted = false;

    public PreciseCountDownTimer(long totalTime, long interval) {
        this(totalTime, interval, 0);
    }

    public PreciseCountDownTimer(long totalTime, long interval, long delay) {
        super("PreciseCountDownTimer", true);
        this.delay = delay;
        this.interval = interval;
        this.totalTime = totalTime;
        this.task = getTask(totalTime);
    }

    public void start() {
        wasStarted = true;
        this.scheduleAtFixedRate(task, delay, interval);

        if (!restart) onStart();
    }

    public void restart() {
        if (!wasStarted) {
            start();
        }
        else if (wasCancelled) {
            wasCancelled = false;
            this.task = getTask(totalTime);
            this.startTime = -1;
            start();
        }
        else {
            this.restart = true;
            onRestart();
        }
    }

    public void stop() {
        this.wasCancelled = true;
        this.task.cancel();

        onStop();
    }

    public void resume() {
        wasCancelled = false;
        this.task = getTask(timeLeft);
        this.startTime = -1;
        start();

        onResume();
    }

    // Call this when there's no further use for this timer
    public void dispose() {
        cancel();
        purge();
    }

    private TimerTask getTask(final long totalTime) {
        return new TimerTask() {
            @Override public void run() {
                if (startTime < 0 || restart) {
                    startTime = scheduledExecutionTime();
                    timeLeft = totalTime;
                    restart = false;
                }
                else {
                    timeLeft = totalTime - ( scheduledExecutionTime() - startTime );

                    if (timeLeft < 0) {
                        this.cancel();
                        startTime = -1;
                        onFinish();
                        return;
                    }
                }

                onTick(timeLeft, totalTime - timeLeft);
            }
        };
    }

    public abstract void onStart();
    public abstract void onTick(long timeLeft, long timeSinceStart);
    public abstract void onRestart();
    public abstract void onStop();
    public abstract void onResume();
    public abstract void onFinish();
}