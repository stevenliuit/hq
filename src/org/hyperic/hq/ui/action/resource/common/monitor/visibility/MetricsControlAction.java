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

package org.hyperic.hq.ui.action.resource.common.monitor.visibility;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hyperic.hq.bizapp.shared.AuthzBoss;
import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.WebUser;
import org.hyperic.hq.ui.action.BaseAction;
import org.hyperic.hq.ui.action.BaseActionMapping;
import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.util.SessionUtils;

/**
 * A <code>BaseAction</code> that handles metrics control form
 * submissions.
 */
public class MetricsControlAction extends BaseAction {

    private static Log log =
        LogFactory.getLog(MetricsControlAction.class.getName());

    // ---------------------------------------------------- Public Methods

    /**
     * Modify the metrics summary display as specified in the given
     * <code>MetricsControlForm</code>.
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        MetricsControlForm controlForm = (MetricsControlForm) form;

        HttpSession session = request.getSession();
        WebUser user = SessionUtils.getWebUser(session);

        // See if this is part of a workflow
        if (mapping instanceof BaseActionMapping) {
            BaseActionMapping smap = (BaseActionMapping) mapping;
            String workflow = smap.getWorkflow();
            if (workflow != null) {
                SessionUtils.pushWorkflow(session, mapping, workflow);
            }
        }

        Integer sessionId = user.getSessionId();
        ServletContext ctx = getServlet().getServletContext();
        AuthzBoss boss = ContextUtils.getAuthzBoss(ctx);

        Map forwardParams = controlForm.getForwardParams();
        if (controlForm.isEditRangeClicked()) {
            return returnEditRange(request, mapping, forwardParams);
        }
        else if (controlForm.isAdvancedClicked()) {
            return returnEditRange(request, mapping, forwardParams);
        }
        else {
            Integer lastN = controlForm.getRn();
            Integer unit = controlForm.getRu();

            user.setPreference(WebUser.PREF_METRIC_RANGE_LASTN, lastN);
            user.setPreference(WebUser.PREF_METRIC_RANGE_UNIT, unit);
            user.setPreference(WebUser.PREF_METRIC_RANGE, null);

            if (controlForm.isSimpleClicked()) {
                user.setPreference(WebUser.PREF_METRIC_RANGE_RO, Boolean.FALSE);
                
                if (log.isDebugEnabled()) {
                    LogFactory.getLog("user.preferences").debug(
                        "Invoking setUserPrefs in MetricsControlAction " +
                        " for " + user.getId() + " at " +
                        System.currentTimeMillis() + " user.prefs = " +
                        user.getPreferences());
                }
            }
            else if (controlForm.isRangeClicked() && log.isDebugEnabled()) {
                log.debug("updating metric display .. lastN [" + lastN +
                          "] .. unit [" + unit + "]");
                LogFactory.getLog("user.preferences").debug(
                  "Invoking setUserPrefs in MetricsControlAction " +
                  " for " + user.getId() + " at "+System.currentTimeMillis() +
                  " user.prefs = " + user.getPreferences());
            }

            boss.setUserPrefs(sessionId, user.getId(), user.getPreferences());
        }

        // assume the return path has been set- don't use forwardParams
        return returnSuccess(request, mapping);
    }

    // ---------------------------------------------------- Private Methods

    private ActionForward returnEditRange(HttpServletRequest request,
                                          ActionMapping mapping,
                                          Map params)
        throws Exception {
        return constructForward(request, mapping, Constants.EDIT_RANGE_URL,
                                params, NO_RETURN_PATH);
    }
}
