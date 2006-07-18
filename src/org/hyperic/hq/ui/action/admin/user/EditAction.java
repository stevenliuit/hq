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

package org.hyperic.hq.ui.action.admin.user;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.util.RequestUtils;
import org.hyperic.hq.ui.util.ActionUtils;
import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.action.BaseAction;

import org.hyperic.hq.authz.shared.AuthzSubjectValue;
import org.hyperic.hq.bizapp.shared.AuthzBoss;
import org.hyperic.hq.authz.shared.PermissionException;

/**
 * Edits a <code>AuthzSubjectValue</code>.
 */
public class EditAction extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {

        Log log = LogFactory.getLog(EditAction.class.getName());            
        log.trace("modifying user properties action");                    
        EditForm userForm = (EditForm)form;
            
        Integer sessionId = RequestUtils.getSessionId(request);


        //get the spiderSubjectValue of the user to be edited.
        ServletContext ctx = getServlet().getServletContext();            
        AuthzBoss authzBoss = ContextUtils.getAuthzBoss(ctx);
        AuthzSubjectValue user = ContextUtils.getAuthzBoss(ctx)
            .findSubject(RequestUtils.getSessionId(request), userForm.getId() );

        ActionForward forward = checkSubmit(request, mapping, form,
                                            Constants.USER_PARAM, 
                                            userForm.getId() );

        if (forward != null) {
            request.setAttribute(Constants.USER_ATTR, user);
            return forward;
        }                                                                     

        user.setFirstName   ( userForm.getFirstName() );
        user.setLastName    ( userForm.getLastName() );
        user.setDepartment  ( userForm.getDepartment() );
        user.setName        ( userForm.getName() );
        user.setEmailAddress( userForm.getEmailAddress() );
        user.setPhoneNumber ( userForm.getPhoneNumber() );
        user.setSMSAddress  ( userForm.getSmsAddress() );
        if ( userForm.getEnableLogin().equals("yes") ) {
            user.setActive(true);
        } else {
            user.setActive(false);
        }

        log.trace("Saving user." + user);            
        authzBoss.saveSubject(sessionId, user);

        return returnSuccess(request, mapping, Constants.USER_PARAM,
                             userForm.getId());

      
    }               
    
}
