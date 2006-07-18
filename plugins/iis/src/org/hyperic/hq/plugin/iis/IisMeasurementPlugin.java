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

package org.hyperic.hq.plugin.iis;

import org.hyperic.hq.product.Win32MeasurementPlugin;

import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.StringUtil;

public class IisMeasurementPlugin
    extends Win32MeasurementPlugin {

    // The special hostname that means the base server instead of a Vhost
    private static String SERVER_HOSTNAME = "_Total";
    public static String PROP_IISHOST     = "iishost";

    public String translate(String template, ConfigResponse config)
    {
        /**
         * If there is no hostname in the ConfigResponse, then we want
         * the value for the server, which is an aggregate of all of the
         * services.  IIS gives this to use with the _Total hostname.
         */
        if (config.getValue(PROP_IISHOST) == null) {
            config.setValue(PROP_IISHOST, SERVER_HOSTNAME);
        }
        template = StringUtil.replace(template,
                                      "${" + PROP_IISHOST + "}", 
                                      config.getValue(PROP_IISHOST));

        return super.translate(template, config);
    }
}
