package com.plug.yourcodeplugin;

import com.intellij.ide.AppLifecycleListener;

public class MyApplicationListener implements AppLifecycleListener {
    private final long appStartTime;

    public MyApplicationListener() {
        this.appStartTime = System.currentTimeMillis();
        System.out.println("IDE opened at: " + appStartTime);
    }

    @Override
    public void appClosing() {
        long appEndTime = System.currentTimeMillis();
        System.out.println("IDE closed at: " + appEndTime);

        long appDuration = appEndTime - appStartTime;
        System.out.println("IDE ran for: " + appDuration + " milliseconds");
    }
}