package com.money.transfer;

import io.swagger.jersey.config.JerseyJaxrsConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import static org.glassfish.jersey.servlet.ServletProperties.JAXRS_APPLICATION_CLASS;

/**
 * Simple launcher which aims to bootstrap embedded Jetty Server at 8080 port and expose Money Transfer API.
 * <p>
 * Current behavior is to:
 * <ul>
 * <li>Start local Jetty.</li>
 * <li>Register Jersey Servlet to scan all JAX-RS annotated resources.</li>
 * <li>Setup Swagger Servlet, which will scan all resources and compile swagger.json.</li>
 * <li>Setup Swagger UI that will be used whenever user will open localhost:8080 in browser.</li>
 * </ul>
 *
 * @author pvasilyev
 */
public class Launcher {

    private static final String CONTEXT = "/money-transfer";
    private static final String JERSEY_API_PATH = "/api";

    public static void main(String[] args) throws Exception {
        Server jettyServer = new Server(8080);
        ServletContextHandler context =
                new ServletContextHandler(jettyServer, CONTEXT, ServletContextHandler.SESSIONS);

        // Setup Jersey Servlet Container
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, JERSEY_API_PATH + "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(JAXRS_APPLICATION_CLASS, ApplicationConfig.class.getName());

        // Setup Swagger servlet
        ServletHolder swaggerServlet = context.addServlet(JerseyJaxrsConfig.class, "/swagger-core");
        swaggerServlet.setInitOrder(2);
        swaggerServlet.setInitParameter("api.version", "1.0.0");
        swaggerServlet.setInitParameter("swagger.api.basepath", CONTEXT + JERSEY_API_PATH);
        swaggerServlet.setInitParameter("swagger.api.host", "localhost:8080");
        swaggerServlet.setInitParameter("swagger.api.title", "Money Transfer API");

        // Setup Swagger-UI static resources
        String resourceBasePath = Launcher.class.getResource("/webapp").toExternalForm();
        context.setWelcomeFiles(new String[]{"index.html"});
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
