package org.mindcollapse;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/websocket-tunnel",
        subprotocols = {"guacamole"},
        configurator = EasyTunnelWebSocketEndpointConfigurator.class
)
public class EasyTunnelWebSocketEndpoint extends GuacamoleWebSocketTunnelEndpoint {
    @Override
    protected SimpleGuacamoleTunnel createTunnel(final Session webSocketSession, final EndpointConfig endpointConfig)
            throws GuacamoleException {
        return EasyTunnel.get((String) endpointConfig.getUserProperties().get("payload"));
    }
}