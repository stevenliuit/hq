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

package org.hyperic.hq.appdef.server.session;

import org.hyperic.util.pager.PagerProcessor;

import org.hyperic.hq.appdef.shared.AppServiceLocal;
import org.hyperic.hq.appdef.shared.ServiceLocal;
import org.hyperic.hq.appdef.shared.ServiceClusterLocal;
import org.hyperic.hq.appdef.shared.ServiceTypeLocal;
import org.hyperic.hq.appdef.shared.ServiceVOHelperUtil;

public class PagerProcessor_service implements PagerProcessor {

    public PagerProcessor_service () {}

    public Object processElement ( Object o ) {
        if ( o == null ) return null;
        try {
            if ( o instanceof ServiceLocal ) {
                return ServiceVOHelperUtil.getLocalHome().create()
                    .getServiceValue((ServiceLocal)o);
            }
            if ( o instanceof AppServiceLocal ) {
                return ((AppServiceLocal) o).getAppServiceValue();
            }
            if ( o instanceof ServiceClusterLocal ) {
                return ((ServiceClusterLocal) o).getServiceClusterValue();
            }
            if ( o instanceof ServiceTypeLocal ) {
                return ServiceVOHelperUtil.getLocalHome().create()
                    .getServiceTypeValue((ServiceTypeLocal)o);
            }
        } catch ( Exception e ) {
            throw new IllegalStateException("Error converting to ServiceValue: " + e);
        }
        return o;
    }
}
