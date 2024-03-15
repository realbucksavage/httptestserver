package com.sarvika.httpserver.api;

import java.util.Map;

public interface HttpRequest {

    String getMethod();

    String getPath();

    String getProtocol();

    String getQuery();

    Map<String, String> getHeaders();
}
