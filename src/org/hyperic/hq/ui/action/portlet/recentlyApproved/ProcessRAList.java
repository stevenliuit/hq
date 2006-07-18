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

package org.hyperic.hq.ui.action.portlet.recentlyApproved;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.hq.bizapp.shared.AuthzBoss;

import org.hyperic.hq.ui.WebUser;
import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.util.SessionUtils;

import org.hyperic.hq.ui.util.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hyperic.hq.ui.action.BaseAction;

public class ProcessRAList extends BaseAction {

    private static String EXPANDED_PLATFORMS 
        = ".dashContent.recentlyApproved.expandedPlatforms";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
       throws Exception {

        Log log = LogFactory.getLog(ProcessRAList.class.getName());
        ServletContext ctx = getServlet().getServletContext();
        AuthzBoss boss = ContextUtils.getAuthzBoss(ctx);
        WebUser user = SessionUtils.getWebUser(request.getSession());
        int sessionId = user.getSessionId().intValue();

        RAListForm listForm = (RAListForm) form;

        List expandedPlatforms = user.getPreferenceAsList(EXPANDED_PLATFORMS);

        String platformId = new Integer(listForm.getPlatformId()).toString();

        if (expandedPlatforms.contains(platformId)) {
            expandedPlatforms.remove(platformId);
        } else {
            expandedPlatforms.add(platformId);
        }

        // Store in user properties as a list.
        user.setPreference(EXPANDED_PLATFORMS, expandedPlatforms);
        
        boss.setUserPrefs(user.getSessionId(),
                          user.getId(),
                          user.getPreferences());

        return mapping.findForward("success");
    }
}
