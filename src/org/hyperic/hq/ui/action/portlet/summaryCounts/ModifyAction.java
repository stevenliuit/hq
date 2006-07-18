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

package org.hyperic.hq.ui.action.portlet.summaryCounts;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.hyperic.util.StringUtil;

import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.util.DashboardUtils;
import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.WebUser;
import org.hyperic.hq.ui.action.BaseAction;
import org.hyperic.hq.bizapp.shared.AuthzBoss;

/**
 * An <code>Action</code> that loads the <code>Portal</code>
 * identified by the <code>PORTAL_PARAM</code> request parameter (or
 * the default portal, if the parameter is not specified) into the
 * <code>PORTAL_KEY</code> request attribute.
 */
public class ModifyAction extends BaseAction {

    // --------------------------------------------------------- Public Methods
    
    /**
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if the application business logic throws
     *  an exception
     */
    public ActionForward execute(ActionMapping mapping,
                            ActionForm form,
                            HttpServletRequest request,
                            HttpServletResponse response)
    throws Exception {
        
        Log log = LogFactory.getLog(ModifyAction.class.getName());
        ServletContext ctx = getServlet().getServletContext();
        AuthzBoss boss = ContextUtils.getAuthzBoss(ctx);

        PropertiesForm pForm = (PropertiesForm) form;
        HttpSession session = request.getSession();

        ActionForward forward = checkSubmit(request, mapping, form);

        if (forward != null) {
            return forward;
        }

        WebUser user = (WebUser) session.getAttribute( Constants.WEBUSER_SES_ATTR );

        if(!pForm.isDisplayOnDash())
            DashboardUtils.removePortlet( user, pForm.getPortletName());

        String application = Boolean.toString( pForm.isApplication() );
        String platform = Boolean.toString( pForm.isPlatform() );
        String cluster = Boolean.toString( pForm.isCluster() );
        String server = Boolean.toString( pForm.isServer() );
        String service = Boolean.toString( pForm.isService() );

        user.setPreference(".dashContent.summaryCounts.application", application );
        user.setPreference(".dashContent.summaryCounts.platform", platform );
        user.setPreference(".dashContent.summaryCounts.group.cluster", cluster );           
        user.setPreference(".dashContent.summaryCounts.server", server );
        user.setPreference(".dashContent.summaryCounts.service", service );

        String groupMixed= Boolean.toString( pForm.isGroupMixed() );            
        String groupGroups = Boolean.toString( pForm.isGroupGroups() );  
        String groupPlatServerService = Boolean.toString( pForm.isGroupPlatServerService() );            
        String groupApplication = Boolean.toString( pForm.isGroupApplication() );           

        user.setPreference(".dashContent.summaryCounts.group.mixed", groupMixed);
        user.setPreference(".dashContent.summaryCounts.group.groups", groupGroups);
        user.setPreference(".dashContent.summaryCounts.group.plat.server.service", groupPlatServerService);
        user.setPreference(".dashContent.summaryCounts.group.application", groupApplication);

        String applicationTypes = StringUtil.arrayToString( pForm.getApplicationTypes() );
        String platformTypes    = StringUtil.arrayToString( pForm.getPlatformTypes() );
        String clusterTypes     = StringUtil.arrayToString( pForm.getClusterTypes() );
        String serverTypes      = StringUtil.arrayToString( pForm.getServerTypes() );
        String serviceTypes     = StringUtil.arrayToString( pForm.getServiceTypes() );

        user.setPreference(".dashContent.summaryCounts.serviceTypes", serviceTypes );
        user.setPreference(".dashContent.summaryCounts.serverTypes", serverTypes );
        user.setPreference(".dashContent.summaryCounts.group.clusterTypes", clusterTypes );
        user.setPreference(".dashContent.summaryCounts.platformTypes", platformTypes);
        user.setPreference(".dashContent.summaryCounts.applicationTypes", applicationTypes);

        LogFactory.getLog("user.preferences").trace("Invoking setUserPrefs"+
            " in summaryCounts/ModifyAction " +
            " for " + user.getId() + " at "+System.currentTimeMillis() +
            " user.prefs = " + user.getPreferences());
        boss.setUserPrefs(user.getSessionId(), user.getId(), user.getPreferences() );            
        session.removeAttribute(Constants.USERS_SES_PORTAL);
        return mapping.findForward("success");

    }
}
