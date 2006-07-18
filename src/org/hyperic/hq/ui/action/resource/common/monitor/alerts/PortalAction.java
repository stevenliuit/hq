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

package org.hyperic.hq.ui.action.resource.common.monitor.alerts;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.hq.appdef.shared.AppdefEntityID;
import org.hyperic.hq.bizapp.shared.EventsBoss;
import org.hyperic.hq.events.shared.AlertDefinitionValue;
import org.hyperic.hq.events.shared.AlertValue;
import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.Portal;
import org.hyperic.hq.ui.Portlet;
import org.hyperic.hq.ui.action.resource.ResourceController;
import org.hyperic.hq.ui.exception.ParameterNotFoundException;
import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.util.RequestUtils;
import org.hyperic.hq.ui.util.SessionUtils;
import org.hyperic.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * A dispatcher for the alerts portal.
 *
 */
public class PortalAction extends ResourceController {
    protected static Log log =
        LogFactory.getLog(PortalAction.class.getName());

    protected Properties getKeyMethodMap() {
        log.trace("Building method map ...");
        Properties map = new Properties();
        map.put(Constants.MODE_VIEW,  "listAlerts");
        map.put(Constants.MODE_LIST,  "listAlerts");
        map.put("viewAlert", "viewAlert");
        return map;
    }

    private void setTitle(AppdefEntityID aeid, Portal portal, String titleName) 
        throws Exception  {

        switch (aeid.getType()) {
            
            case 1: 
                break;

            case 2: {
                titleName = StringUtil.replace(titleName,"platform","server"); 
                break;
            }
            case 3: {
                titleName = StringUtil.replace(titleName,"platform","service"); 
                break;
            }
            case 4: {
                titleName = StringUtil.replace(titleName,"platform","application"); 
                break;
            }
            case 5: {
                titleName = StringUtil.replace(titleName,"platform","group"); 
                break;
            }
            default : {
                titleName = StringUtil.replace(titleName,"platform.",""); 
            }
        }
        
        portal.setName(titleName);

    }

    public ActionForward viewAlert(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
        // Get alert definition name
        ServletContext ctx = getServlet().getServletContext();
        int sessionID = RequestUtils.getSessionId(request).intValue();
        EventsBoss eb = ContextUtils.getEventsBoss(ctx);

        Integer alertId = new Integer( request.getParameter("a") );
        AlertValue av = eb.getAlert(sessionID, alertId);
        AlertDefinitionValue adv =
            eb.getAlertDefinition( sessionID, av.getAlertDefId() );
        request.setAttribute(Constants.TITLE_PARAM2_ATTR, adv.getName());
        
        AppdefEntityID aeid =
            new AppdefEntityID(adv.getAppdefType(), adv.getAppdefId());
        setResource(request, aeid, false);

        Portal portal = Portal.createPortal();
        setTitle(aeid, portal, "alert.current.platform.detail.Title");
        portal.addPortlet(new Portlet(".events.alert.view"), 1);

        portal.setDialog(true);
        request.setAttribute(Constants.PORTAL_KEY, portal);

        return null;
    }

    public ActionForward listAlerts(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
        throws Exception
    {
        setResource(request);
        
        super.setNavMapLocation(request,mapping,
                                 Constants.ALERT_LOC); 
        // clean out the return path        
        SessionUtils.resetReturnPath(request.getSession());
        // set the return path
        try {
            setReturnPath(request, mapping);
        }
        catch (ParameterNotFoundException pne) {
            if (log.isDebugEnabled())
                 log.debug("", pne);
        }
        
        Portal portal = Portal.createPortal();
        AppdefEntityID aeid = RequestUtils.getEntityId(request);
        setTitle(aeid, portal, "alerts.alert.platform.AlertList.Title");
        portal.setDialog(false);
        portal.addPortlet(new Portlet(".events.alert.list"),1);
        request.setAttribute(Constants.PORTAL_KEY, portal);

        return null;
    }
}

// EOF
