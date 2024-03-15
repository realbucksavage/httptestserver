/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.sarvika.httpserver;

import com.sarvika.httpserver.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            String argsStr = Arrays.stream(args).map(s -> "'" + s + "'").collect(Collectors.joining(" "));
            LOGGER.debug("running program with arguments: {}", argsStr);
        }

        if (args.length != 2) {
            System.err.println("usage: server.jar <port> <base dir>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String baseDir = args[1];

        Server sv = new Server(port, baseDir);
        sv.start();
    }
}
