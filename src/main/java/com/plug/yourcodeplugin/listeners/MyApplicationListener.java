package com.plug.yourcodeplugin.listeners;

import com.intellij.ide.AppLifecycleListener;
import com.plug.yourcodeplugin.helpers.TimeHelper;

public class MyApplicationListener implements AppLifecycleListener {
    private final long appStartTime;

    public MyApplicationListener() {
        this.appStartTime = TimeHelper.getCurrentTimeMillis();
        System.out.println("IDE opened at: " + TimeHelper.longToTimestamp(appStartTime));
    }

    @Override
    public void appClosing() {
        long appEndTime = TimeHelper.getCurrentTimeMillis();
        System.out.println("IDE closed at: " + TimeHelper.longToTimestamp(appEndTime));

        long appDuration = appEndTime - appStartTime;
        System.out.println("IDE ran for: " + TimeHelper.convertMillis(appDuration, TimeHelper.TimeUnit.SECONDS) + " seconds");
    }
}