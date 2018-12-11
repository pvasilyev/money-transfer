package com.money.transfer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Application {

    public static void main(String[] args) throws Exception {
        Server jettyServer = new Server(8080);
        ServletContextHandler context =
                new ServletContextHandler(jettyServer, "/money-transfer", ServletContextHandler.SESSIONS);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "io.swagger.jaxrs.json,io.swagger.jaxrs.listing,com.money.transfer");

        // Setup Swagger servlet
        ServletHolder swaggerServlet = context.addServlet(io.swagger.jersey.config.JerseyJaxrsConfig.class, "/swagger-core");
        swaggerServlet.setInitOrder(2);
        swaggerServlet.setInitParameter("api.version", "1.0.0");
        swaggerServlet.setInitParameter("swagger.api.basepath", "/money-transfer/api");
        swaggerServlet.setInitParameter("swagger.api.host", "localhost:8080");
        swaggerServlet.setInitParameter("swagger.api.title", "Money Transfer API");

        // Setup Swagger-UI static resources
        String resourceBasePath = Application.class.getResource("/webapp").toExternalForm();
        context.setWelcomeFiles(new String[] {"index.html"});
        context.setResourceBase(resourceBasePath);
        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

}
