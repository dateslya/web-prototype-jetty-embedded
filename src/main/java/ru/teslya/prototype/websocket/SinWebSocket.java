package ru.teslya.prototype.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * Created by Dmitry Teslya on 22.08.2015.
 */
public class SinWebSocket extends WebSocketAdapter {

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        new Thread(new Runnable() {
            @Override
            public void run() {
                float offset = 0f;
                while (sess.isOpen()) {
                    try {
                        offset += 0.1f;
                        StringBuilder builder = new StringBuilder();
                        for (float i = 0; i < 4 * Math.PI; i += 0.1f) {
                            if (i != 0f) {
                                builder.append("|");
                            }
                            builder.append(i + offset).append(":").append(Math.sin(i + offset));
                        }
                        getRemote().sendString(builder.toString());
                        Thread.sleep(250);
                        offset += 0.1f;
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
