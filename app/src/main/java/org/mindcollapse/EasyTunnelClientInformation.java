package org.mindcollapse;

import java.util.Arrays;
import java.util.List;
import org.apache.guacamole.protocol.GuacamoleClientInformation;

final class EasyTunnelClientInformation extends GuacamoleClientInformation
{
    public List<String> getAudioMimetypes() {
        return Arrays.asList("audio/L8", "audio/L16");
    }
}
