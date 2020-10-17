package org.mindcollapse;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/tunnel")
public class EasyTunnelServlet extends GuacamoleHTTPTunnelServlet {
    private static final long serialVersionUID = 101L;

    protected SimpleGuacamoleTunnel doConnect(final HttpServletRequest request) throws GuacamoleException {
        return EasyTunnel.get(request.getParameter("payload"));
    }
}
