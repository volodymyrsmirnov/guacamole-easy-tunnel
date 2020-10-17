package org.mindcollapse;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.guacamole.GuacamoleClientException;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

final class EasyTunnel {
    private static final Logger logger = LoggerFactory.getLogger(EasyTunnel.class);

    static SimpleGuacamoleTunnel get(String payload) throws GuacamoleException {
        Map<String, String> parameters;

        if(payload == null || payload.isEmpty())
            throw new GuacamoleClientException("payload is a mandatory parameter");

        try {
            parameters = EasyTunnelPayloadReader.read(payload);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("Failed to decrypt the connection payload", e);
            throw new GuacamoleClientException("payload decryption error");
        }

        logger.info("Connection parameters: {}", parameters);

        if (!parameters.containsKey("protocol"))
            throw new GuacamoleClientException("protocol parameter is mandatory");

        if (!parameters.containsKey("hostname"))
            throw new GuacamoleClientException("hostname parameter is mandatory");

        if (!parameters.containsKey("port"))
            throw new GuacamoleClientException("port parameter is mandatory");

        final GuacamoleConfiguration config = new GuacamoleConfiguration();

        config.setProtocol(parameters.get("protocol"));
        config.setParameters(parameters);

        String guacdHost = System.getenv("GUACD_HOST");

        if (guacdHost == null)
            guacdHost = "guacd";

        String guacdPort = System.getenv("GUACD_PORT");

        if (guacdPort == null)
            guacdPort = "4822";

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(guacdHost, Integer.parseInt(guacdPort)),
                config, new EasyTunnelClientInformation());

        return new SimpleGuacamoleTunnel(socket);
    }
}
