package org.nemesis.grpc;

import java.io.IOException;

import com.karlz.grpc.game.NemesisGrpc.NemesisImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class NemesisServer {
    private final Server server;

    public NemesisServer(int port) {
        server = ServerBuilder.forPort(port).addService(new NemesisService()).build();
    }

    public void start() {
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class NemesisService extends NemesisImplBase {

    }
}
