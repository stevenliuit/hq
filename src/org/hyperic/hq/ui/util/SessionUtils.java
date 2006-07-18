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

package org.hyperic.hq.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.WebUser;
import org.hyperic.hq.ui.beans.ReturnPath;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Utilities class that provides convenience methods for operating on
 * bizapp objects.
 */
public class SessionUtils {

    // Limit the size of the return stack so that it
    // doesn't grow to infinite proportions.
    private static final int RETURN_STACK_MAX_SIZE = 128;
    
    /**
     * Retrieve the cached <code>WebUser</code> representing the user
     * interacting with server.
     *
     * @param session the http session
     */
    public static WebUser getWebUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object attr = session.getAttribute(Constants.WEBUSER_SES_ATTR);
        if (attr == null) {
            return null;
        }
        return (WebUser) attr;
    }

    /**
     * Retrieve the array of pending ids stored under the named
     * session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     */
    public static Integer[] getList(HttpSession session, String attr) {
        Object list = session.getAttribute(attr);
        Integer[] pendingIds = null;
        try {
            pendingIds = (Integer[]) list;
        }
        catch (ClassCastException e) {
            List pendingIdsList = (List) list;
            pendingIds = (Integer[]) pendingIdsList.toArray(new Integer[0]);
        }

        if (pendingIds == null) {
            pendingIds = new Integer[0];
        }

        return pendingIds;
    }

    /**
     * Retrieve the <code>List</code> of pending ids stored under the named
     * session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     */
    public static List getListAsList(HttpSession session, String attr) {
        Object list = session.getAttribute(attr);
        List pendingIds = null;
        try {
            pendingIds = (List) list;
        }
        catch (ClassCastException e) {
            Integer[] pendingIdsArray = (Integer[]) list;
            pendingIds = Arrays.asList(pendingIdsArray);
        }

        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        return pendingIds;
    }

    /**
     * Retrieve the <code>List</code> of pending ids stored under the named
     * session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     */
    public static List getListAsListStr(HttpSession session, String attr) {
        Object list = session.getAttribute(attr);
        List pendingIds = null;
        try {
            pendingIds = (List) list;
        }
        catch (ClassCastException e) {
            String[] pendingIdsArray = (String[]) list;
            pendingIds = Arrays.asList(pendingIdsArray);
        }

        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        return pendingIds;
    }

    /**
     * Merge the given ids into the array of pending ids stored under
     * the named session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     * @param ids the ids to be added
     */
    public static void addToList(HttpSession session, String attr,
                                 List ids) {
        /* copy the selected pending ids over to the available
           ids. those that were copied will not be included in
           the final list of available users generated by the
           form prepare action. */
        List pendingIds = (List) session.getAttribute(attr);
        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        session.setAttribute(attr, mergeIds(pendingIds, ids));
    }

    public static void addToList(HttpSession session, String attr,
                                 Integer[] ids) {
        addToList(session, attr, Arrays.asList(ids));
    }

    /**
     * Merge the given ids into the array of pending ids stored under
     * the named session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     * @param ids the ids to be added
     */
    public static void addToListStr(HttpSession session, String attr,
                                 List ids) {
        /* copy the selected pending ids over to the available
           ids. those that were copied will not be included in
           the final list of available users generated by the
           form prepare action. */
        List pendingIds = (List) session.getAttribute(attr);
        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        session.setAttribute(attr, mergeStrIds(pendingIds, ids));
    }

    public static void addToList(HttpSession session, String attr,
                                 String[] ids) {
         addToListStr(session, attr, Arrays.asList(ids));
    }

    /**
     * Remove the given ids from the array of pending ids stored under
     * the named session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     * @param ids the ids to be removed
     */
    public static void removeFromList(HttpSession session, String attr,
                                      List ids) {
        /* remove the selected pending ids from the current list
           of pending ids. */
        List pendingIds = (List) session.getAttribute(attr);
        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        session.setAttribute(attr, grepIds(pendingIds, ids));
    }

    public static void removeFromList(HttpSession session, String attr,
                                      Integer[] ids) {
        removeFromList(session, attr, Arrays.asList(ids));
    }

    /**
     * Remove the given ids from the array of pending ids stored under
     * the named session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     * @param ids the ids to be removed
     */
    public static void removeFromListStr(HttpSession session, String attr,
                                      List ids) {
        /* remove the selected pending ids from the current list
           of pending ids. */
        List pendingIds = (List) session.getAttribute(attr);
        if (pendingIds == null) {
            pendingIds = new ArrayList();
        }

        session.setAttribute(attr, grepStrIds(pendingIds, ids));
    }

    public static void removeFromList(HttpSession session, String attr,
                                      String[] ids) {
        removeFromListStr(session, attr, Arrays.asList(ids));
    }

    /**
     * Remove the array of pending ids stored under the named
     * session attribute.
     *
     * @param session the http session
     * @param attr the name of the session attribute
     */
    public static void removeList(HttpSession session, String attr) {
        session.removeAttribute(attr);
    }

    /**
     * Retrieve the "return path" that locates the point at which a
     * subflow was included into a primary workflow.
     *
     * @param session the http session
     */
    public static String getReturnPath(HttpSession session) {
        LinkedList stack 
            = (LinkedList) session.getAttribute(Constants.RETURN_LOC_SES_ATTR);
                
        if(stack == null || stack.isEmpty()) {
            return null;
        }
        
        ReturnPath returnPath = (ReturnPath) stack.getFirst();
                
        return returnPath.getPath();
    }

    /**
     * Set the "returnPath", the possible point of origin for a workflow.
     *
     * @param session the http session
     * @param path the return path
     */
    public static void setReturnPath(HttpSession session, String path) {
        setReturnPath(session, path, Boolean.FALSE);
    }
    
    /**
     * Set the "returnPath," the possible point of origin for a worklow 
     *
     * @param session The http session.
     * @param path The return path url represented as a String.
     * @param ignore 
     */
    public static void setReturnPath(HttpSession session, 
                                     String path, 
                                     Boolean ignore) {
        LinkedList stack 
            = (LinkedList) session.getAttribute(Constants.RETURN_LOC_SES_ATTR);
        
        ReturnPath returnPath = new ReturnPath();
        
        if(stack == null) {
            stack = new LinkedList();        
        }
        
        returnPath.setPath(path);
        returnPath.setIgnore(ignore);

        stack.addFirst(returnPath);
        
        // don't let it grow too large.
        if (stack.size() > RETURN_STACK_MAX_SIZE) {
            stack.removeLast();
        }
    
        session.setAttribute(Constants.RETURN_LOC_SES_ATTR, stack);
    }

    /**
     * clear out the return path stack.
     *
     */
    public static void resetReturnPath(HttpSession session) {
        LinkedList stack = new LinkedList();
        
        session.setAttribute(Constants.RETURN_LOC_SES_ATTR, stack);
    }


     /**
     * Unset the "return path" that locates the point at which a
     * subflow was included into a primary workflow.
     *
     * @param session the http session
     */
    public static void unsetReturnPath(HttpSession session) {
                
        LinkedList retstack 
            = (LinkedList) session.getAttribute(Constants.RETURN_LOC_SES_ATTR);
        
        if(retstack != null && retstack.size() >= 1 ) {
            retstack.removeFirst();   
        } 
    }

     /**
     * Retrieve wether the "return path" should be paid attention to for new.
     * should default to false, which means that it is not ignored.
     *
     * @param session the http session
     * @return whether or not to ignore the return path
     */
    public static Boolean getReturnPathIgnoredForOk(HttpSession session) {
        LinkedList stack = (LinkedList) session.getAttribute(Constants.RETURN_LOC_SES_ATTR);

        if(stack == null || stack.isEmpty())
            return Boolean.FALSE;
        
        ReturnPath returnPath = (ReturnPath) stack.getFirst();
        
        if (returnPath == null)
            return Boolean.FALSE;
        
        return returnPath.getIgnore();
       
    }

    /**
     * Unset the "return path" that locates the point at which a
     * subflow was included into a primary workflow.
     *
     * @param session the http session
     */
    public static void unsetReturnPathIgnoredForOk(HttpSession session) {
        LinkedList stack = (LinkedList) session.getAttribute(Constants.RETURN_LOC_SES_ATTR);
        ReturnPath returnPath = (ReturnPath) stack.getFirst();        
        returnPath.setIgnore(Boolean.TRUE);        
    }
    
    /**
     * Remove any old workflows
     * @param session
     * @param mapping
     * @param workflowName
     */
    public static void clearWorkflow(HttpSession session, String workflowName) {
        HashMap workflows = (HashMap) session
                .getAttribute(Constants.WORKFLOW_SES_ATTR);

        if (workflows != null)
            workflows.remove(workflowName);
    }
    
    /**
     * Takes the current returnPath and pushes it onto the
     * workflow's stack.
     * @param session The HttpSesion to get and save the
     *                workflow to/from
     * @param mapping Use the input attribute from this mapping if a
     *                returnPath is not defined in the current session.
     * @param workflowName The name of the workflow scope to save the 
     *                input under.
     */
    public static void pushWorkflow(HttpSession session, 
                                    ActionMapping mapping,
                                    String workflowName) {
        HashMap workflows 
            = (HashMap)session.getAttribute(Constants.WORKFLOW_SES_ATTR);
        if (workflows == null) {
            workflows = new HashMap();
        }
        
        String returnPath = getReturnPath(session);
        if (returnPath == null) {
            returnPath = mapping.getInput();
        }
        
        LinkedList urlStack = (LinkedList)workflows.get(workflowName);
        // whacking getIsFirst.. for some reason, even if i set the
        // property to false in struts-config.xml, it's still getting
        // set back to true. i can't have this, so i'm whacking the
        // check altogether. doesn't seem to harm anything.
        // if (urlStack == null || mapping.getIsFirst().booleanValue()) {
        if (urlStack == null) {
            urlStack = new LinkedList();
        }
        
        urlStack.addLast(returnPath);
        workflows.put(workflowName, urlStack);

        session.setAttribute(Constants.WORKFLOW_SES_ATTR, workflows);
    }

    /**
     * Takes the current returnPath and pops it off of the
     * workflow's stack.
     * @param session The HttpSesion to get and save the
     *                workflow to/from
     * @param mapping Use the input attribute from this mapping if a
     *                returnPath is not defined in the current session.
     * @param workflowName The name of the workflow scope to save the 
     *                input under.
     */
    public static String popWorkflow(HttpSession session,
                                     String workflowName) {
        HashMap workflows 
            = (HashMap)session.getAttribute(Constants.WORKFLOW_SES_ATTR);
        if (workflows == null) {
            return null;
        }      
        
        LinkedList urlStack = (LinkedList)workflows.get(workflowName);
        if (urlStack == null || urlStack.isEmpty() ) {
            return null;
        }
        
        String returnUrl = (String)urlStack.removeLast();
        workflows.put(workflowName, urlStack);        
        session.setAttribute(Constants.WORKFLOW_SES_ATTR, workflows);

        return returnUrl;
    }
    
    /**
     * Returns the size of a workflow's stack.
     * @param session The HttpSesion to get and save the
     *                workflow to/from
     * @param workflowName The name of the workflow scope to save the 
     *                input under.
     */
    public static int countWorkflow(HttpSession session,
                                    String workflowName) {
        HashMap workflows 
            = (HashMap)session.getAttribute(Constants.WORKFLOW_SES_ATTR);
        if (workflows == null) {
            return 0;
        }
        
        LinkedList urlStack = (LinkedList)workflows.get(workflowName);
        if (urlStack == null || urlStack.isEmpty() ) {
            return 0;
        }

        return urlStack.size();
    }
    
    private static List grepIds(List all, List matches) {
        if (all == null || matches == null) {
            return new ArrayList();
        }

        // build an index of matches
        HashSet index = new HashSet();
        Iterator mi = matches.iterator();
        while (mi.hasNext()) {
            index.add(mi.next());
        }

        // find everything that's not a match
        ArrayList ids = new ArrayList();
        Iterator ai = all.iterator();
        while (ai.hasNext()) {
            Integer id = (Integer) ai.next();
            if (! index.contains(id)) {
                ids.add(id);
            }
        }

        return ids;
    }

    /**
     * searches for strings which does not match
     */
    private static List grepStrIds(List pending, List newItems) {
        if (pending == null || newItems == null) {
            return new ArrayList();
        }

        // build an index of matches
        HashSet index = new HashSet();
        Iterator mi = newItems.iterator();
        while (mi.hasNext()) {
            index.add(mi.next());
        }

        // find everything that's not a match
        ArrayList ids = new ArrayList();
        Iterator ai = pending.iterator();
        while (ai.hasNext()) {
            String id = (String) ai.next();
            if (! index.contains(id)) {
                ids.add(id);
            }
        }

        return ids;
    }

    private static List mergeIds(List orig, List added) {
        if (orig == null && added == null) {
            return new ArrayList();
        }
        if (orig == null) {
            return added;
        }
        if (added == null) {
            return orig;
        }

        // build an index of orig and add originals
        ArrayList ids = new ArrayList();
        HashSet index = new HashSet();
        Iterator oi = orig.iterator();
        while (oi.hasNext()) {
            Object next = oi.next();
            index.add(next);
            ids.add(next);
        }

        // add anything that's not in the index
        Iterator ai = added.iterator();
        while (ai.hasNext()) {
            Integer id = (Integer) ai.next();
            if (! index.contains(id)) {
                ids.add(id);
            }
        }

        return ids;
    }
    
    /**
     * same as the mergeIds except using strings IDs of form:
     * 
     * .
     */
    private static List mergeStrIds(List orig, List added) {
        if (orig == null && added == null) {
            return new ArrayList();
        }
        if (orig == null) {
            return added;
        }
        if (added == null) {
            return orig;
        }

        // build an index of orig and add originals
        ArrayList ids = new ArrayList();
        HashSet index = new HashSet();
        Iterator oi = orig.iterator();
        while (oi.hasNext()) {
            Object next = oi.next();
            index.add(next);
            ids.add(next);
        }

        // add anything that's not in the index
        Iterator ai = added.iterator();
        while (ai.hasNext()) {
            String id = (String) ai.next();
            if (! index.contains(id)) {
                ids.add(id);
            }
        }

        return ids;
    }
    
    /**
     * Set a confirmation message upon completion of a user action.
     *
     * @param session The servlet session object
     * @param key the message resource key
     */
    public static void setConfirmation(HttpSession session,
                                       String key) {
        ActionMessage msg = new ActionMessage(key);
        ActionMessages msgs = new ActionMessages();
        msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
        if (session != null) { 
            session.setAttribute(Globals.MESSAGE_KEY, msgs);
        }
    }

    /**
     * Set a confirmation message with a replacement value upon
     * completion of a user action.
     *
     * @param session The servlet session object
     * @param key the message resource key
     * @param value0 the replacement value
     */
    public static void setConfirmation(HttpSession session,
                                       String key, Object value0) {
        ActionMessage msg = new ActionMessage(key, value0);
        ActionMessages msgs = new ActionMessages();
        msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
        if (session != null) {
            session.setAttribute(Globals.MESSAGE_KEY, msgs);
        }
    }

    public static void unsetConfirmation(HttpSession session, String key) {
        session.removeAttribute(Globals.MESSAGE_KEY);
    }

    /**
     * Set an error message when a user action fails with a user-level
     * error.
     *
     * @param session The servlet session object
     * @param key the message resource key
     */
    public static void setError(HttpSession session, String key) {
        setError(session, key, ActionMessages.GLOBAL_MESSAGE);
    }

    /**
     * Set an error message when a user action fails with a user-level
     * error.
     *
     * @param session The servlet session object
     * @param key the message resource key
     * @param property the form property for which the error occurred
     */
    public static void setError(HttpSession session, String key,
                                String property) {
        ActionMessage err = new ActionMessage(key);
        ActionErrors errs = new ActionErrors();
        errs.add(property, err);
        if (session != null) {
            session.setAttribute(Globals.ERROR_KEY, errs);
        }
    }

    /**
     * Set an error message with a replacement value when a user
     * action fails with a user-level error.
     *
     * @param session The servlet session object
     * @param key the message resource key
     * @param value0 the replacement value
     */
    public static void setError(HttpSession session, String key,
                                Object value0) {
        setError(session, key, value0, ActionMessages.GLOBAL_MESSAGE);
    }
    
    /**
     * Set an error message with a replacement value when a user
     * action fails with a user-level error.
     *
     * @param session The servlet session object
     * @param key the message resource key
     * @param value0 the replacement value
     * @param property the form property for which the error occurred
     */
    public static void setError(HttpSession session, String key,
                                Object value0, String property) {
        ActionMessage err = new ActionMessage(key, value0);
        ActionMessages errs = new ActionErrors();
        errs.add(property, err);
        if (session != null) {
            session.setAttribute(Globals.ERROR_KEY, errs);
        }
    }

    public static void unsetError(HttpSession session) {
        if (session != null) {
            session.removeAttribute(Globals.ERROR_KEY);
        }
    }
    
    /**
     * Move the attribute from the session to request scope.
     * @param request The request to move the attribute to.
     * @param key Key indicating the attribute to move scope.
     */
    public static void moveAttribute(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false /* dont' create new one */);
        if (session != null) {
            request.setAttribute(key,  
                session.getAttribute(key));
            session.removeAttribute(key);
        }
    }
}
