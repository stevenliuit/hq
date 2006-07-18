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

package org.hyperic.hq.auth.server.session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.hyperic.hq.auth.shared.PrincipalsLocal;
import org.hyperic.hq.auth.shared.PrincipalsLocalHome;
import org.hyperic.hq.auth.shared.PrincipalsUtil;
import org.hyperic.hq.auth.shared.SessionManager;
import org.hyperic.hq.auth.shared.SessionNotFoundException;
import org.hyperic.hq.authz.shared.AuthzSubjectManagerLocal;
import org.hyperic.hq.authz.shared.AuthzSubjectManagerUtil;
import org.hyperic.hq.authz.shared.AuthzSubjectValue;
import org.hyperic.hq.authz.shared.PermissionException;
import org.hyperic.hq.common.ApplicationException;
import org.hyperic.hq.common.SystemException;
import org.hyperic.hq.common.shared.HQConstants;
import org.hyperic.hq.common.shared.ServerConfigManagerUtil;
import org.hyperic.util.ConfigPropertyException;

import org.jboss.security.Util;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

/** The AuthManger
 *
 * @ejb:bean name="AuthManager"
 *      jndi-name="ejb/auth/AuthManager"
 *      local-jndi-name="LocalAuthManager"
 *      view-type="local"
 *      type="Stateless"
 */

public class AuthManagerEJBImpl implements SessionBean {

    // Always authenticate against the HQ application realm
    private static final String appName = HQConstants.ApplicationName;

    // By default all default login module options are used.
    private HashMap configOptions = new HashMap();

    public AuthManagerEJBImpl() {}

    public void ejbCreate() {}

    public void ejbRemove() {}

    public void ejbActivate() {}

    public void ejbPassivate() {}

    public void setSessionContext(SessionContext ctx) {}

    /**
     * Set the JAAS configuration options
     * @ejb:interface-method
     */
    public void setConfigurationOptions(HashMap options)
    {
        configOptions = options;
    }

    private AuthzSubjectValue createSubjectValue(String name, String dsn)
        throws FinderException
    {
        AuthzSubjectValue  value = new AuthzSubjectValue();
        value.setName(name);
        value.setAuthDsn(dsn);
        // set it to active, else they wont be able to log in
        value.setActive(true);
        return value;
    }

    /**
     * Authenticates the user using the given password
     * @param user The user to authenticate
     * @param password The password for the user
     * @return session id that is associated with the user
     * @throws SQLException 
     * @ejb:interface-method
     * @ejb:transaction type="SUPPORTS"
     */
    public int getSessionId(String user, String password)
        throws SecurityException, LoginException, ConfigPropertyException,
               ApplicationException {

        if(password == null)
            throw new LoginException("No password was given");
            
        try {
            // get the configuration properties
            Properties config = ServerConfigManagerUtil.getLocalHome().create()
                .getConfig();

            UsernamePasswordHandler handler =
                new UsernamePasswordHandler(user, password.toCharArray());

            LoginContext loginContext = new LoginContext(appName, handler);
            loginContext.login();
            loginContext.logout();

            // User is authenticated, get the id from the authz system
            // and return an id from the Session Manager
            AuthzSubjectManagerLocal subjMgrLocal = 
                    AuthzSubjectManagerUtil.getLocalHome().create();
        
            AuthzSubjectValue subject = null;
            try {
                subject = subjMgrLocal.findSubjectByAuth(user, appName);
                if (!subject.getActive()) {
                    throw new LoginException("User account has been disabled.");
                }

            } catch (NamingException ne) {
                throw new SystemException("Unable to contact authorization " +
                                             "system", ne);
            } catch (FinderException fe) {
                // User not found, check to make sure the standard JDBC JAAS
                // provider is being used before creating the authz resources.
                if (!config.getProperty(HQConstants.JAASProvider).
                    equals(HQConstants.JDBCJAASProvider)) {
                    subject = new AuthzSubjectValue();
                }
                else {
                    // User not found in the authz system.  Create it.
                    try {
                        AuthzSubjectValue overlord =
                            subjMgrLocal.findOverlord();
                        AuthzSubjectValue newUser 
                            = createSubjectValue(user, appName);

                        subjMgrLocal.createSubject(overlord, newUser);
                        subjMgrLocal.findSubjectByAuth(newUser.getName(), 
                                                       newUser.getAuthDsn());                                                   
                    } catch (CreateException e) {
                        throw new
                            ApplicationException("Unable to add user to " +
                                                    "authorization system", e);
                    }
                }
            }

            SessionManager mgr = SessionManager.getInstance();
            return mgr.put(subject);
        } catch (NamingException e) {
            throw new SystemException("Naming Error in getSessionId", e);
        } catch (CreateException e) {
            throw new SystemException("CreateException in getSessionId", e);
        } catch (FinderException e) {
            throw new ApplicationException(
                "Unable to find Overlord User to create authz user entry", e);
        } 
    }

    /**
     * Get a session ID based on username only
     * @param user The user to authenticate
     * @return session id that is associated with the user
     * @ejb:interface-method
     * @ejb:transaction type="SUPPORTS"
     */
    public int getUnauthSessionId(String user)
        throws LoginException, ApplicationException, ConfigPropertyException {
        try {
            SessionManager mgr = SessionManager.getInstance();
            try {
                int sessionId = mgr.getIdFromUsername(user);
                if (sessionId > 0)
                    return sessionId;
            } catch (SessionNotFoundException e) {
                // Continue   
            }

            // Get the id from the authz system and return an id from the
            // Session Manager
            AuthzSubjectManagerLocal subjMgrLocal = 
                    AuthzSubjectManagerUtil.getLocalHome().create();
        
            AuthzSubjectValue subject = null;
            try {
                subject = subjMgrLocal.findSubjectByAuth(user, appName);
                if (!subject.getActive()) {
                    throw new LoginException("User account has been disabled.");
                }    
            } catch (NamingException ne) {
                throw new SystemException("Unable to contact authorization " +
                                          "system", ne);
            }
    
            return mgr.put(subject, 30000);     // 30 seconds only
        } catch (NamingException e) {
            throw new SystemException("Naming Error in getSessionId", e);
        } catch (CreateException e) {
            throw new SystemException("CreateException in getSessionId", e);
        } catch (FinderException e) {
            throw new ApplicationException(
                "Unable to find Overlord User to create authz user entry", e);
        } 
    }

    /**
     * Add a user to the internal database
     *
     * @param subject The subject of the currently logged in user
     * @param username The username to add
     * @param password The password for this user
     * @exception NamingException If we can't find the PrinipalsLocalHome
     * @exception CreateExceptoin If we try to add a duplicate user
     *
     * @ejb:interface-method
     * @ejb:transaction type="REQUIRED"
     */
    public void addUser(AuthzSubjectValue subject, 
                        String username, String password)
        throws NamingException, CreateException
    {
        PrincipalsLocalHome lhome = PrincipalsUtil.getLocalHome();
        PrincipalsLocal local = lhome.create(username, password);
    }
    
    /**
     * Change the password for a user.
     *
     * @param subject The subject of the currently logged in user
     * @param username The username whose password will be changed.
     * @param password The new password for this user
     * @exception NamingException If we can't find the PrinipalsLocalHome
     *
     * @ejb:interface-method
     * @ejb:transaction type="REQUIRED"
     */
    public void changePassword(AuthzSubjectValue subject, 
                               String username, String password)
        throws SystemException, FinderException, PermissionException
    {
        try {
            // AUTHZ check
            if(!subject.getName().equals(username)) {
                // users can change their own passwords... only
                // peeps with modifyUsers can modify other 
                AuthzSubjectManagerUtil.getLocalHome().create()
                    .checkModifyUsers(subject);
            }
            PrincipalsLocalHome lhome = PrincipalsUtil.getLocalHome();
            PrincipalsLocal local = lhome.findByUsername(username);
            // hash the password as is done in ejbCreate. Fixes 4661
            String hash = Util.createPasswordHash("MD5", "base64",
                                              null, null, password);
            local.setPassword(hash);
        } catch (CreateException e) {
            throw new SystemException(e);
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Delete a user from the internal database
     *
     * @param subject The subject of the currently logged in user
     * @param username The user to delete
     * @exception NamingException If we can't find PrincipalsLocalHome
     * @exception FinderException If we can't find the user to delete
     * @exception RemoveException If the user could not be removed 
     *
     * @ejb:interface-method
     * @ejb:transaction-type="REQUIRED"
     */
    public void deleteUser(AuthzSubjectValue subject, String username)
        throws NamingException, FinderException, RemoveException
    {
        PrincipalsLocalHome lhome = PrincipalsUtil.getLocalHome();
        PrincipalsLocal local = lhome.findByUsername(username);
        local.remove();
    }

    /**
     * Check existence of a user
     *
     * @param subject The subject of the currently logged in user
     * @param username The username of the user to get
     * @exception NamingException If we can't find PrincipalsLocalHome
     * @exception FinderException
     *
     * @ejb:interface-method
     * @ejb:transaction-type="SUPPORTS"
     */
    public boolean isUser(AuthzSubjectValue subject, String username)
        throws NamingException
    {
        try {
            PrincipalsLocalHome lhome = PrincipalsUtil.getLocalHome();
            return lhome.findByUsername(username) != null;
        }
        catch (FinderException e) {
            return false;
        }
    }

    /**
     * Get a collection of all users
     *
     * @param subject The subject of the currently logged in user
     * @exception NamingException If we can't find PrincipalsLocalHome
     * @exception FinderException
     *
     * @ejb:interface-method
     * @ejb:transaction-type="SUPPORTS"
     */
    public Collection getAllUsers(AuthzSubjectValue subject)
        throws NamingException, FinderException
    {
        PrincipalsLocalHome lhome = PrincipalsUtil.getLocalHome();
        
        Collection principals = lhome.findAllUsers();
        Collection users = new ArrayList();
        
        for (Iterator i = principals.iterator(); i.hasNext();) {
            PrincipalsLocal p = (PrincipalsLocal)i.next();
            users.add(p.getPrincipal());
        }
        
        return users;
    }
}
