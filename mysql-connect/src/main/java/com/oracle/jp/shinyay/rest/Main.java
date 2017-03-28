package com.oracle.jp.shinyay.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class Main {
    public static final String BASE_URI;
    public static final String PROTOCOL;
    public static final Optional<String> HOST;
    public static final Optional<String> PORT;
    public static final String PATH;

    static{
        PROTOCOL = "http://";
        HOST = Optional.ofNullable(System.getenv("HOSTNAME"));
        PORT = Optional.ofNullable(System.getenv("PORT"));
        PATH = "myapp";
        BASE_URI = PROTOCOL
                + HOST.orElseGet(() -> "localhost")
                + ":"
                + PORT.orElseGet(() -> "8080")
                + "/"
                + PATH
                + "/";
    }

    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig().packages("com.oracle.jp.shinyay.rest");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}

