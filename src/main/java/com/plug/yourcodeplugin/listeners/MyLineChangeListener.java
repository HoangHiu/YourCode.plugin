package com.plug.yourcodeplugin.listeners;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.plug.yourcodeplugin.services.StatService;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyLineChangeListener implements DocumentListener {
    private final Map<Document, Integer> docLineCounts = new ConcurrentHashMap<>();

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document doc = event.getDocument();

        int currentLines = doc.getLineCount();

        int prevLines = docLineCounts.getOrDefault(doc, Math.max(0, currentLines - estimateLineDelta(event)));

        int delta = currentLines - prevLines;
        if (delta > 0) {
            StatService.getInstance().addLines(delta);
        } else if (delta < 0) {
            StatService.getInstance().removeLines(Math.abs(delta));
        }

        docLineCounts.put(doc, currentLines);

        StatService.getInstance().recordEditActivity();
    }

    private int estimateLineDelta(DocumentEvent event) {
        int newLines = (int) event.getNewFragment().chars().filter(ch -> ch == '\n').count();
        int oldLines = (int) event.getOldFragment().chars().filter(ch -> ch == '\n').count();
        return newLines - oldLines;
    }
}
