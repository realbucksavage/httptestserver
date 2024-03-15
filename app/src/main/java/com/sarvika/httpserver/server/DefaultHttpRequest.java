package com.sarvika.httpserver.server;

import com.sarvika.httpserver.api.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultHttpRequest implements HttpRequest {

    private String method;
    private String path;

    private String protocol;

    private Map<String, String> headers = new HashMap<>();

    DefaultHttpRequest(Socket clientSocket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String line = br.readLine();
        this.parseProtocolLine(line);

        while (true) {
            line = br.readLine();
            if (StringUtils.isBlank(line)) {
                break;
            }

            String[] headerSplits = line.split(": ");
            String key = headerSplits[0].toLowerCase();
            String value = headerSplits[1];
            this.headers.put(key, value);
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    private void parseProtocolLine(String line) {
        String[] splits = line.split(" ");
        this.method = splits[0];
        this.path = splits[1];
        this.protocol = splits[2];
    }
}
