package com.plug.yourcodeplugin.listeners;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.plug.yourcodeplugin.services.StatService;
import org.jetbrains.annotations.NotNull;

public class MyFileEditorListener implements FileEditorManagerListener {

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        if (event.getNewFile() != null) {
            StatService.getInstance().fileOpened();
        }
        if (event.getOldFile() != null) {
            StatService.getInstance().fileClosed();
        }
    }

}
