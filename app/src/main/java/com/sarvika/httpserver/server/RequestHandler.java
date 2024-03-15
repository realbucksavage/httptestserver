package com.sarvika.httpserver.server;

import com.sarvika.httpserver.api.HttpRequest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class RequestHandler extends Thread {

    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

        System.out.println("client connected from " + clientSocket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        System.out.println("serving request...");
        try {
            HttpRequest request = new DefaultHttpRequest(this.clientSocket);
            this.serveRequest(request);
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serveRequest(HttpRequest request) throws IOException {
        System.out.println("serving content at: " + request.getPath());
        OutputStream os = this.clientSocket.getOutputStream();
        IOUtils.write(request.getProtocol() + " 200 OK\n", os, Charset.defaultCharset());
    }
}
