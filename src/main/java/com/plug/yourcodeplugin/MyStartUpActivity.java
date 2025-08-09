package com.plug.yourcodeplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.messages.MessageBusConnection;
import com.plug.yourcodeplugin.listeners.*;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyStartUpActivity implements ProjectActivity {
    @Override
    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        System.out.println("Welcome to YourCodePlugin");
        MessageBusConnection projectConnection = project.getMessageBus().connect();

        projectConnection.subscribe(ProjectManager.TOPIC, new MyProjectListener());

        projectConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new MyFileEditorListener());

        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new MyLineChangeListener(), project);

        projectConnection.subscribe(VirtualFileManager.VFS_CHANGES, new MyFileEventListener());

        MessageBusConnection appConnection = ApplicationManager.getApplication().getMessageBus().connect();
        appConnection.subscribe(com.intellij.ide.AppLifecycleListener.TOPIC, new MyApplicationListener());
        return null;
    }
}