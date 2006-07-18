/*
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 * 
 * Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
 * This file is part of HQ.
 * 
 * HQ is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package org.hyperic.hq.plugin.websphere;

import org.hyperic.hq.product.PluginException;

import org.hyperic.hq.plugin.websphere.ejs.EjsAdminServerControl;

import org.hyperic.util.config.ConfigResponse;

public class AdminControlAE
    extends WebsphereControlPlugin {

    private static final String DEFAULT_SCRIPT =
        "bin/startupServer.sh";

    private EjsAdminServerControl wscp;

    public void configure(ConfigResponse config)
        throws PluginException
    {
        super.configure(config);
        this.wscp = new EjsAdminServerControl();
        this.wscp.configure(config);
        this.wscp.init(manager);
    }

    protected String getDefaultScript() {
        return DEFAULT_SCRIPT;
    }

    protected boolean isBackgroundCommand() {
        return true;
    }

    protected int doCommand(String action) {
        int res = this.wscp.checkIsRunning(action);

        if (res != 0) {
            //if action eq "start" and port is bound
            //startupServer.sh will keep trying
            //over and over to start the server.
            return res;
        }

        if (action.equals("start")) {
            String script = getControlProgram();

            getLog().info("command script=" + script);

            return super.doCommand(script, new String[0]);
        }
        else if (action.equals("stop")) {
            try {
                this.wscp.doAction(action);
            } catch (PluginException e) {
                getLog().error(e.getMessage(), e);
            }
            setMessage(this.wscp.getMessage());
            return this.wscp.getResult();
        }

        return super.doCommand(action); //restart
    }
}
