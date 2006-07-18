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

package org.hyperic.hq.authz.server.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hyperic.hq.authz.shared.AuthzConstants;
import org.hyperic.hq.authz.shared.AuthzSubjectLocal;
import org.hyperic.hq.authz.shared.AuthzSubjectLocalHome;
import org.hyperic.hq.authz.shared.AuthzSubjectManagerLocalHome;
import org.hyperic.hq.authz.shared.AuthzSubjectManagerUtil;
import org.hyperic.hq.authz.shared.AuthzSubjectUtil;
import org.hyperic.hq.authz.shared.AuthzSubjectValue;
import org.hyperic.hq.authz.shared.OperationLocal;
import org.hyperic.hq.authz.shared.OperationLocalHome;
import org.hyperic.hq.authz.shared.OperationUtil;
import org.hyperic.hq.authz.shared.OperationValue;
import org.hyperic.hq.authz.shared.PermissionException;
import org.hyperic.hq.authz.shared.PermissionManager;
import org.hyperic.hq.authz.shared.PermissionManagerFactory;
import org.hyperic.hq.authz.shared.ResourceGroupLocal;
import org.hyperic.hq.authz.shared.ResourceGroupLocalHome;
import org.hyperic.hq.authz.shared.ResourceGroupManagerLocalHome;
import org.hyperic.hq.authz.shared.ResourceGroupManagerUtil;
import org.hyperic.hq.authz.shared.ResourceGroupPK;
import org.hyperic.hq.authz.shared.ResourceGroupUtil;
import org.hyperic.hq.authz.shared.ResourceGroupValue;
import org.hyperic.hq.authz.shared.ResourceLocal;
import org.hyperic.hq.authz.shared.ResourceLocalHome;
import org.hyperic.hq.authz.shared.ResourceManagerLocalHome;
import org.hyperic.hq.authz.shared.ResourceManagerUtil;
import org.hyperic.hq.authz.shared.ResourceTypeLocal;
import org.hyperic.hq.authz.shared.ResourceTypeLocalHome;
import org.hyperic.hq.authz.shared.ResourceTypeUtil;
import org.hyperic.hq.authz.shared.ResourceTypeValue;
import org.hyperic.hq.authz.shared.ResourceUtil;
import org.hyperic.hq.authz.shared.ResourceValue;
import org.hyperic.hq.authz.shared.RoleLocal;
import org.hyperic.hq.authz.shared.RoleLocalHome;
import org.hyperic.hq.authz.shared.RolePK;
import org.hyperic.hq.authz.shared.RoleUtil;
import org.hyperic.hq.authz.shared.RoleValue;
import org.hyperic.hq.authz.values.OwnedResourceGroupValue;
import org.hyperic.hq.common.SystemException;
import org.hyperic.hq.common.shared.HQConstants;
import org.hyperic.util.jdbc.DBUtil;
import org.hyperic.util.pager.PageControl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the parent class for all Authz Session Beans
 */
public abstract class AuthzSession {

    public static final Log log
        = LogFactory.getLog(AuthzSession.class.getName());

    protected static final String DATASOURCE = HQConstants.DATASOURCE;
    protected static InitialContext ic = null;

    // Homes for the entities
    protected ResourceLocalHome resourceHome;
    protected ResourceTypeLocalHome resTypeHome;
    protected ResourceGroupLocalHome groupHome;
    protected AuthzSubjectLocalHome subjectHome;
    protected OperationLocalHome operationHome;
    protected RoleLocalHome roleHome;
    
    // Homes for the sessions
    protected ResourceGroupManagerLocalHome groupMgrHome;
    protected AuthzSubjectManagerLocalHome subjectMgrHome;
    protected ResourceManagerLocalHome resourceMgrHome;

    protected SessionContext ctx;

    /** now the home cache methods **/
    protected ResourceLocalHome getResourceHome() {
        try {
            if (resourceHome == null) {
                resourceHome = ResourceUtil.getLocalHome();
            }
            return resourceHome;
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    protected ResourceTypeLocalHome getResourceTypeHome() throws NamingException {
        if (resTypeHome == null) {
            resTypeHome = ResourceTypeUtil.getLocalHome();
        }
        return resTypeHome;
    }

    protected ResourceGroupLocalHome getGroupHome() throws NamingException {
        if (groupHome == null) {
            groupHome = ResourceGroupUtil.getLocalHome();
        }
        return groupHome;
    }

    protected AuthzSubjectLocalHome getSubjectHome() throws NamingException {
        if (subjectHome == null) {
            subjectHome = AuthzSubjectUtil.getLocalHome();
        }
        return subjectHome;
    }

    protected OperationLocalHome getOperationHome() throws NamingException {    
        if (operationHome == null) {
            operationHome = OperationUtil.getLocalHome();
        }
        return operationHome;
    }

    protected RoleLocalHome getRoleHome() throws NamingException {
        if (roleHome == null) {
            roleHome = RoleUtil.getLocalHome();
        }
        return roleHome;
    }

    protected ResourceGroupManagerLocalHome getGroupMgrHome() throws 
        NamingException {
        if (groupMgrHome == null) {
            groupMgrHome = ResourceGroupManagerUtil.getLocalHome();
        }
        return groupMgrHome;
    }

    protected ResourceManagerLocalHome getResourceMgrHome() throws
        NamingException {
        if (resourceMgrHome == null) {
            resourceMgrHome = ResourceManagerUtil.getLocalHome();
        }
        return resourceMgrHome;
    }

    protected AuthzSubjectManagerLocalHome getSubjectMgrHome() throws
        NamingException {
        if (subjectMgrHome == null) {
            subjectMgrHome = AuthzSubjectManagerUtil.getLocalHome();
        }
        return subjectMgrHome;
    }

    /** 
     * @return The value-object of the overlord
     * @ejb:interface-method
     * @ejb:transaction type="SUPPORTS"
     */
    public AuthzSubjectValue findOverlord()
        throws NamingException, FinderException {
        return findSubjectByAuth(AuthzConstants.overlordName,
                                 AuthzConstants.overlordDsn);
    }

    protected AuthzSubjectLocal findOverlordEJB()
        throws NamingException, FinderException {
        return getSubjectHome().findByAuth(AuthzConstants.overlordName,
                                      AuthzConstants.overlordDsn);
    }

    protected ResourceTypeLocal getRootResourceType()
        throws NamingException, FinderException {
           return getResourceTypeHome()
                .findByName(AuthzConstants.typeResourceTypeName); 
    }

    /**
     * Find the subject that has the given name and authentication source.
     * @param name Name of the subject.
     * @param authDsn DSN of the authentication source. Authentication sources
     * are defined externally.
     * @return The value-object of the subject of the given name and authenticating source.
     * @ejb:interface-method
     * @ejb:transaction type="SUPPORTS"
     */
    public AuthzSubjectValue findSubjectByAuth(String name, String authDsn)
        throws NamingException, FinderException {
        return getSubjectHome().findByAuth(name, authDsn)
            .getAuthzSubjectValue();
    }

    protected ResourceGroupLocal findRootResourceGroup()
        throws NamingException, FinderException {
        return getGroupHome().findByName(AuthzConstants.authzResourceGroupName);
    }

    protected Set toLocals(Object[] values)
        throws NamingException, FinderException {
        Set locals = new HashSet();
        boolean isOperation = false;
        boolean isResource = false;
        boolean isResourceGroup = false;
        boolean isRole = false;
        boolean isAuthzSubject = false;

        OperationLocalHome opLome = null;
        ResourceLocalHome rLome = null;
        ResourceGroupLocalHome groupLome = null;
        AuthzSubjectLocalHome subjectLome = null;
        RoleLocalHome roleLome = null;
        if (values != null && (values.length > 0)) {
            int counter = 0;
            Object value = values[0];
            if (value instanceof
                org.hyperic.hq.authz.shared.OperationValue) {
                opLome = OperationUtil.getLocalHome();
                isOperation = true;
            } else if (value instanceof
                       org.hyperic.hq.authz.shared.ResourceValue) {
                rLome = ResourceUtil.getLocalHome();
                isResource = true;
            } else if (value instanceof
                       org.hyperic.hq.authz.shared.ResourceGroupValue) {
                groupLome = ResourceGroupUtil.getLocalHome();
                isResourceGroup = true;
            } else if (value instanceof
                       org.hyperic.hq.authz.shared.RoleValue) {
                roleLome = RoleUtil.getLocalHome();
                isRole = true;
            } else if (value instanceof
                       org.hyperic.hq.authz.shared.AuthzSubjectValue) {
                subjectLome = AuthzSubjectUtil.getLocalHome();
                isAuthzSubject = true;
            } else {
                log.error("Invalid type.");
            }
            for (counter = 0; counter < values.length; counter++) {
                if (isOperation) {
                    OperationValue op = (OperationValue)values[counter];
                    locals.add(opLome.findByPrimaryKey(op.getPrimaryKey()));
                } else if (isResource) {
                    ResourceValue r = (ResourceValue)values[counter];
                    locals.add(rLome.findByPrimaryKey(r.getPrimaryKey()));
                } else if (isResourceGroup) {
                    ResourceGroupValue group = (ResourceGroupValue)values[counter];
                    locals.add(groupLome.findByPrimaryKey(group.getPrimaryKey()));
                } else if (isRole) {
                    RoleValue role = (RoleValue)values[counter];
                    locals.add(roleLome.findByPrimaryKey(role.getPrimaryKey()));
                } else if (isAuthzSubject) {
                    AuthzSubjectValue subject = (AuthzSubjectValue)values[counter];
                    locals.add(subjectLome.findByPrimaryKey(subject.getPrimaryKey()));
                } else {
                    log.error("Invalid type.");
                }
            }
        }
        return locals;
    }

    protected Object[] fromLocals(Collection locals, Class c) {
        Object[] values = null;
        Iterator it = locals.iterator();
        int counter = 0;
        boolean isOperation = false;
        boolean isResource = false;
        boolean isResourceGroup = false;
        boolean isRole = false;
        boolean isAuthzSubject = false;

        if (c == org.hyperic.hq.authz.shared.OperationValue.class) {
            values = new OperationValue[locals.size()];
            isOperation = true;
        } else if (c == org.hyperic.hq.authz.shared.ResourceValue.class) {
            values = new ResourceValue[locals.size()];
            isResource = true;
        } else if (c == org.hyperic.hq.authz.shared.ResourceGroupValue.class) {
            values = new ResourceGroupValue[locals.size()];
            isResourceGroup = true;
        } else if (c == org.hyperic.hq.authz.shared.RoleValue.class) {
            values = new RoleValue[locals.size()];
            isRole = true;
        } else if (c == org.hyperic.hq.authz.shared.AuthzSubjectValue.class) {
            values = new AuthzSubjectValue[locals.size()];
            isAuthzSubject = true;
        } else {
            log.error("Invalid type.");
        }

        while (it.hasNext()) {
            if (isOperation) {
                values[counter] =
                    ((OperationLocal)it.next()).getOperationValue();
            } else if (isResource) {
                values[counter] =
                    ((ResourceLocal)it.next()).getResourceValue();
            } else if (isResourceGroup) {
                values[counter] =
                    ((ResourceGroupLocal)it.next()).getResourceGroupValue();
            } else if (isRole) {
                values[counter] =
                    ((RoleLocal)it.next()).getRoleValue();
            } else if (isAuthzSubject) {
                values[counter] =
                    ((AuthzSubjectLocal)it.next()).getAuthzSubjectValue();
            } else {
                log.error("Invalid type.");
            }
            counter++;
        }
        return values;
    }

    protected AuthzSubjectLocal lookupSubject(AuthzSubjectValue subject)
        throws FinderException {
        try {
            return getSubjectHome().findByPrimaryKey(subject.getPrimaryKey());
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    protected AuthzSubjectLocal lookupSubject(Integer id)
        throws FinderException {
        try {
            return getSubjectHome().findById(id);
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    protected ResourceTypeLocal lookupType(ResourceTypeValue type)
        throws NamingException, FinderException {
        return getResourceTypeHome().findByPrimaryKey(type.getPrimaryKey());
    }

    protected ResourceGroupLocal lookupGroup(ResourceGroupValue group)
        throws NamingException, FinderException {
        return getGroupHome().findByPrimaryKey(group.getPrimaryKey());
    }

    protected ResourceGroupLocal lookupGroup(Integer id)
        throws NamingException, FinderException {
        ResourceGroupPK groupPk = new ResourceGroupPK(id);
        return getGroupHome().findByPrimaryKey(groupPk);
    }

    protected ResourceLocal lookupResource(ResourceValue resource)
        throws FinderException {
        if (resource.getId() == null) {
            return this.lookupResourceByInstance(resource.getResourceTypeValue()
                .getName(),resource.getInstanceId());
        } 
        return getResourceHome().findByPrimaryKey(resource.getPrimaryKey());
    }

    protected ResourceLocal lookupResourceByInstance(String resTypeName, 
        Integer instId) throws FinderException {
        try {
            ResourceTypeLocal typeLocal =
                getResourceTypeHome().findByName(resTypeName);
            return getResourceHome().findByInstanceId(typeLocal,instId);
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Get a list of RolePK's a user has viewRole permission
     * for (or owns)
     */
    protected List getViewableRolePKs(AuthzSubjectValue who) 
        throws NamingException, PermissionException,
               FinderException {
        PermissionManager pm = PermissionManagerFactory.getInstance();
        List roleIds = 
            pm.findOperationScopeBySubject(who,
                                           AuthzConstants.roleOpViewRole,
                                           AuthzConstants.roleResourceTypeName,
                                           PageControl.PAGE_ALL);
        List pks = new ArrayList();
        // now make ResourceGroupPKs out of em
        for(int i = 0; i < roleIds.size(); i++) {
            Integer id = (Integer)roleIds.get(i);
            pks.add(new RolePK(id));
        }
        return pks;
    }

    protected OperationLocal lookupOperation (ResourceTypeLocal type, 
        String opName) throws FinderException {
        try {
            return getOperationHome().findByTypeAndName(type, opName);
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Get a list of ResourceGroupPK's a user has viewResourceGroup permission
     * for (or owns)
     */
    protected List getViewableGroupPKs(AuthzSubjectValue who) 
        throws NamingException, PermissionException,
               FinderException {
        
        PermissionManager pm = PermissionManagerFactory.getInstance();         
        List groupIds = 
            pm.findOperationScopeBySubject(who,
                                           AuthzConstants.groupOpViewResourceGroup,
                                           AuthzConstants.groupResourceTypeName,
                                           PageControl.PAGE_ALL);
        List pks = new ArrayList();
        // now make ResourceGroupPKs out of em
        for(int i = 0; i < groupIds.size(); i++) {
            Integer id = (Integer)groupIds.get(i);
            pks.add(new ResourceGroupPK(id));
        }
        return pks;
    }

    /** 
     * Filter a collection of groupLocal objects to only include those viewable
     * by the specified user
     */
    protected Collection filterViewableGroups(AuthzSubjectValue who,
        Collection groups) throws NamingException, FinderException, 
                                  PermissionException {
        // finally scope down to only the ones the user can see
        List viewable = getViewableGroupPKs(who);
        for(Iterator i = groups.iterator(); i.hasNext();) {
            ResourceGroupLocal aEJB = (ResourceGroupLocal)i.next();
            if(!viewable.contains((ResourceGroupPK)aEJB.getPrimaryKey())) {
                i.remove();
            }
        }
        return groups;
    }

    protected InitialContext getInitialContext() throws NamingException {
        if (ic == null) ic = new InitialContext();
        return ic;
    }

   protected Connection getDBConn() throws SQLException {
        Connection conn;
        try {
            conn = DBUtil.getConnByContext(this.getInitialContext(), DATASOURCE);
        } catch(NamingException exc){
            throw new SystemException("Unable to get database context: " +
                                         exc.getMessage(), exc);
        }
        return conn;
    }
    
    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }
    
    protected SessionContext getSessionContext() {
        return this.ctx;
    }
    
    protected void rollback() {
        if(!getSessionContext().getRollbackOnly()) {
            getSessionContext().setRollbackOnly();
        }
    }
}
