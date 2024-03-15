package com.sarvika.httpserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private ServerSocket serverSocket;
    private String baseDir;
    private boolean started;

    public Server(int port, String baseDir) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.baseDir = baseDir;
    }

    public void start() {
        this.started = true;

        LOGGER.info("starting server in base dir {} on port {}",
                new File(this.baseDir).getAbsolutePath(),
                this.serverSocket.getLocalPort());

        while (this.started) {
            try {
                RequestHandler handler = new RequestHandler(this.serverSocket.accept(), this.baseDir);
                handler.start();
            } catch (IOException e) {
                LOGGER.error("cannot complete socket connection", e);
            }
        }
    }

    public void stop() throws IOException {
        this.started = false;
        this.serverSocket.close();
    }
}
