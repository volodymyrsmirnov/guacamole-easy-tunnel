package org.mindcollapse;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class EasyTunnelWebSocketEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        config.getUserProperties().put(
                "payload", request.getParameterMap().get("payload").get(0));

        super.modifyHandshake(config, request, response);
    }
}
