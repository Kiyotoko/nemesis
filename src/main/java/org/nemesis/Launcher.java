package org.nemesis;

import org.nemesis.grpc.RemoteServer;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        new RemoteServer();
        Application.launch(App.class, args);
    }
}
