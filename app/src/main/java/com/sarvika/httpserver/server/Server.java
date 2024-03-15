package com.sarvika.httpserver.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private ServerSocket serverSocket;
    private String baseDir;
    private boolean started;

    public Server(int port, String baseDir) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.baseDir = baseDir;
    }

    public void start() {
        this.started = true;

        System.out.println("accepting connections on " + this.serverSocket.getInetAddress().getHostAddress());
        while (this.started) {
            try {
                RequestHandler handler = new RequestHandler(this.serverSocket.accept(), this.baseDir);
                handler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        this.started = false;
        this.serverSocket.close();
    }
}
