package com.plug.yourcodeplugin;

import com.intellij.ide.AppLifecycleListener;


public class MyApplicationListener implements AppLifecycleListener {
    private Long appDuration;
    private Long appStartTime;
    private Long appEndTime;

    @Override
    public void appStarted() {
        appStartTime = System.currentTimeMillis();
        System.out.println("IDE opened at: " + appStartTime);
    }

    @Override
    public void appClosing(){
        appEndTime = System.currentTimeMillis() -  appStartTime;
        System.out.println("IDE closed at: " + appEndTime);
        appDuration = appEndTime - appStartTime;
        System.out.println("IDE ran for: " + appDuration);
    }
}
