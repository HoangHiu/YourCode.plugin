package com.plug.yourcodeplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class MyStartUpActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        MessageBusConnection projectConnection = project.getMessageBus().connect();

        projectConnection.subscribe(ProjectManager.TOPIC, new MyProjectListener());

        projectConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new MyFileEditorListener());

        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new MyLineChangeListener(), project);

        projectConnection.subscribe(VirtualFileManager.VFS_CHANGES, new MyFileEventListener());

        MessageBusConnection appConnection = ApplicationManager.getApplication().getMessageBus().connect();
        appConnection.subscribe(com.intellij.ide.AppLifecycleListener.TOPIC, new MyApplicationListener());
    }
}