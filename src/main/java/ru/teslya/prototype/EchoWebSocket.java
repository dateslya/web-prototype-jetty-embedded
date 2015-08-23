package ru.teslya.prototype;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Dmitry Teslya on 22.08.2015.
 */
public class EchoWebSocket extends WebSocketAdapter {

    private static final Logger log = LoggerFactory.getLogger(EchoWebSocket.class);

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sess.isOpen()) {
                    try {
                        getRemote().sendString("Hello, client!");
                        Thread.sleep(3000);
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        log.error("Error occurred", cause);
    }

    @Override
    public void onWebSocketText(String message) {
        log.info("Received message: {}", message);
        try {
            getRemote().sendString(message);
        } catch (IOException e) {
            log.error("Error occurred", e);
        }
    }
}
