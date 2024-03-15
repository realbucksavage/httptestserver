package com.sarvika.httpserver.server;

import com.sarvika.httpserver.api.HttpRequest;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class RequestHandler extends Thread {

    private Socket clientSocket;
    private String resourceBaseLocation;

    public RequestHandler(Socket clientSocket, String resourceBaseLocation) {
        this.clientSocket = clientSocket;
        this.resourceBaseLocation = resourceBaseLocation;

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

        File resource = new File(this.resourceBaseLocation, request.getPath());
        if (!resource.exists()) {
            writeResourceNotFound(os);
            return;
        }

        writeResource(resource, os);
    }

    private void writeResourceNotFound(OutputStream os) throws IOException {
        IOUtils.write("HTTP/1.1 404 Not Found\n\n", os, Charset.defaultCharset());
    }

    private void writeResource(File resource, OutputStream os) throws IOException {
        IOUtils.write("HTTP/1.1 200 OK\n", os, Charset.defaultCharset());

        if (resource.isFile()) {
            String contentType = Files.probeContentType(resource.toPath());
            IOUtils.write("Content-Type: " + contentType + "\n\n", os, Charset.defaultCharset());
            IOUtils.copy(new FileInputStream(resource), os);
            return;
        }


        IOUtils.write("Content-Type: text/plain\n\n", os, Charset.defaultCharset());
        String[] children = resource.list();
        if (children == null) {
            children = new String[]{};
        }

        for (String childName : children) {
            IOUtils.write(childName + "\n", os, Charset.defaultCharset());
        }
    }
}
