package ru.teslya.prototype;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.websocket.server.WebSocketHandler;

import java.util.Collections;

/**
 * Created by Dmitry Teslya on 22.08.2015.
 */
public class Application {

    public static final int PORT = 8080;
    public static final String BASE_PATH = System.getProperty("user.home") + "\\workspace\\web-prototype-jetty-embedded\\target\\classes";

    public static void main(String... args) throws Exception {
        Server server = new Server(PORT);

        LoginService loginService = new HashLoginService("Realm", BASE_PATH + "\\realm.properties");
        server.addBean(loginService);

        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setConstraintMappings(Collections.singletonList(constraintMapping));
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setLoginService(loginService);
        server.setHandler(securityHandler);

        WebSocketHandler webSocketHandler = new WebSocketHandler.Simple(EchoWebSocket.class);
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
