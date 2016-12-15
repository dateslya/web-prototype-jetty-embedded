package ru.teslya.prototype;

import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;

import ru.teslya.prototype.security.BasicAuthenticationHandler;
import ru.teslya.prototype.websocket.SinWebSocket;

/**
 * Created by Dmitry Teslya on 22.08.2015.
 */
public class Application {

    public static final int PORT = 8080;
    public static final String BASE_PATH = System.getProperty("user.home") + "\\IdeaProjects\\web-prototype-jetty-embedded\\target\\classes";

    public static void main(String... args) throws Exception {
        Server server = new Server(PORT);

        LoginService loginService = new HashLoginService("Realm", BASE_PATH + "\\realm.properties");
        server.addBean(loginService);

        ConstraintSecurityHandler securityHandler = new BasicAuthenticationHandler();
        securityHandler.setLoginService(loginService);
        server.setHandler(securityHandler);

        WebSocketHandler webSocketHandler = new WebSocketHandler.Simple(SinWebSocket.class);
        securityHandler.setHandler(webSocketHandler);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase(BASE_PATH);
        webSocketHandler.setHandler(resourceHandler);

        resourceHandler.setHandler(new DefaultHandler());

        server.start();
        server.join();
    }
}
