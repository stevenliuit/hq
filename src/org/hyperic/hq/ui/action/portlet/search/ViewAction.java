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

package org.hyperic.hq.ui.action.portlet.search;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;

import org.hyperic.hq.appdef.shared.AppdefResourceTypeValue;
import org.hyperic.hq.bizapp.shared.AppdefBoss;
import org.hyperic.hq.common.ApplicationException;
import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.Portal;

import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.util.pager.PageControl;
import org.hyperic.util.pager.PageList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.hyperic.hq.ui.action.resource.hub.ResourceHubForm;
import org.hyperic.util.timer.StopWatch;

/**
 * An <code>TilesAction</code> that sets up for searching the Resource Hub portal.
 */
public class ViewAction extends TilesAction {

    // ---------------------------------------------------- Public Methods

    /**
     * Set up the Resource Hub portal.
     */
    public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        StopWatch timer = new StopWatch();
        Log timingLog = LogFactory.getLog("DASHBOARD-TIMING");

        Log log = LogFactory.getLog(ViewAction.class.getName());
        ResourceHubForm hubForm = (ResourceHubForm) form;
        ServletContext ctx = getServlet().getServletContext();
        AppdefBoss boss = ContextUtils.getAppdefBoss(ctx);
        String[][] entityTypes = boss.getAppdefTypeStrArrMap();

        if (entityTypes != null){
            for (int i=0; i<entityTypes.length; i++) {
                if(!entityTypes[i][0].equals("5") ){
                    hubForm.addFunction(new LabelValueBean(entityTypes[i][1],
                                                           entityTypes[i][0]));
                    log.debug( entityTypes[i][1]+ " = " + entityTypes[i][0]);
                }
            }
            hubForm.addFunction( new LabelValueBean("mixedGroups", "5" ) );
            hubForm.addFunction( new LabelValueBean("compatibleGroups", "5" ) );
            
        }
        timingLog.trace("SearchHubPrepare - timing ["+timer.toString()+"]");
        return null;
    }
}
