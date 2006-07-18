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

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.hyperic.hq.appdef.shared.AIQueueManagerLocal;
import org.hyperic.hq.appdef.shared.AIQueueManagerUtil;
import org.hyperic.hq.appdef.shared.AIServerLocalHome;
import org.hyperic.hq.appdef.shared.AIServerUtil;
import org.hyperic.hq.appdef.shared.AgentLocalHome;
import org.hyperic.hq.appdef.shared.AgentTypeLocalHome;
import org.hyperic.hq.appdef.shared.AgentTypeUtil;
import org.hyperic.hq.appdef.shared.AgentUtil;
import org.hyperic.hq.appdef.shared.AppdefEntityConstants;
import org.hyperic.hq.appdef.shared.AppdefEntityNotFoundException;
import org.hyperic.hq.appdef.shared.AppdefGroupManagerLocalHome;
import org.hyperic.hq.appdef.shared.AppdefGroupManagerLocal;
import org.hyperic.hq.appdef.shared.AppdefGroupManagerUtil;
import org.hyperic.hq.appdef.shared.AppdefResourceTypeValue;
import org.hyperic.hq.appdef.shared.ApplicationLocalHome;
import org.hyperic.hq.appdef.shared.ApplicationManagerLocal;
import org.hyperic.hq.appdef.shared.ApplicationManagerLocalHome;
import org.hyperic.hq.appdef.shared.ApplicationManagerUtil;
import org.hyperic.hq.appdef.shared.ApplicationNotFoundException;
import org.hyperic.hq.appdef.shared.ApplicationTypeLocalHome;
import org.hyperic.hq.appdef.shared.ApplicationTypeUtil;
import org.hyperic.hq.appdef.shared.ApplicationUtil;
import org.hyperic.hq.appdef.shared.CPropKeyValue;
import org.hyperic.hq.appdef.shared.CPropManagerLocal;
import org.hyperic.hq.appdef.shared.CPropManagerUtil;
import org.hyperic.hq.appdef.shared.ConfigManagerLocal;
import org.hyperic.hq.appdef.shared.ConfigManagerUtil;
import org.hyperic.hq.appdef.shared.ConfigResponseLocalHome;
import org.hyperic.hq.appdef.shared.ConfigResponseUtil;
import org.hyperic.hq.appdef.shared.IpLocalHome;
import org.hyperic.hq.appdef.shared.IpUtil;
import org.hyperic.hq.appdef.shared.PlatformLocalHome;
import org.hyperic.hq.appdef.shared.PlatformManagerLocal;
import org.hyperic.hq.appdef.shared.PlatformManagerLocalHome;
import org.hyperic.hq.appdef.shared.PlatformManagerUtil;
import org.hyperic.hq.appdef.shared.PlatformTypeLocalHome;
import org.hyperic.hq.appdef.shared.PlatformTypeUtil;
import org.hyperic.hq.appdef.shared.PlatformUtil;
import org.hyperic.hq.appdef.shared.ServerLocalHome;
import org.hyperic.hq.appdef.shared.ServerManagerLocal;
import org.hyperic.hq.appdef.shared.ServerManagerLocalHome;
import org.hyperic.hq.appdef.shared.ServerManagerUtil;
import org.hyperic.hq.appdef.shared.ServerNotFoundException;
import org.hyperic.hq.appdef.shared.ServerTypeLocalHome;
import org.hyperic.hq.appdef.shared.ServerTypeUtil;
import org.hyperic.hq.appdef.shared.ServerUtil;
import org.hyperic.hq.appdef.shared.ServiceLocalHome;
import org.hyperic.hq.appdef.shared.ServiceManagerLocal;
import org.hyperic.hq.appdef.shared.ServiceManagerLocalHome;
import org.hyperic.hq.appdef.shared.ServiceManagerUtil;
import org.hyperic.hq.appdef.shared.ServiceNotFoundException;
import org.hyperic.hq.appdef.shared.ServiceTypeLocalHome;
import org.hyperic.hq.appdef.shared.ServiceTypeUtil;
import org.hyperic.hq.appdef.shared.ServiceUtil;
import org.hyperic.hq.authz.shared.PermissionException;
import org.hyperic.hq.authz.shared.PermissionManager;
import org.hyperic.hq.authz.shared.ResourceManagerLocal;
import org.hyperic.hq.authz.shared.ResourceManagerUtil;
import org.hyperic.hq.authz.shared.AuthzSubjectLocalHome;
import org.hyperic.hq.authz.shared.AuthzSubjectUtil;
import org.hyperic.hq.common.SystemException;
import org.hyperic.hq.product.TypeInfo;

public abstract class AppdefSessionUtil {
    private AIQueueManagerLocal         aiqManagerLocal;
    private AIServerLocalHome           aiqServerLHome;
    private AgentLocalHome              agentLHome;
    private AgentTypeLocalHome          agentTypeLHome;
    private ApplicationLocalHome        appLHome;
    private ApplicationManagerLocalHome appMgrLHome;
    private AppdefGroupManagerLocalHome grpMgrLHome;
    private ApplicationTypeLocalHome    appTypeLHome;
    private ConfigManagerLocal          configMgrL;
    private ConfigResponseLocalHome     configRespLHome;
    private IpLocalHome                 ipLHome;
    private PlatformLocalHome           platformLHome;
    private PlatformManagerLocalHome    platformMgrLHome;
    private PlatformTypeLocalHome       platTypeLHome;
    private ResourceManagerLocal        rmLocal;
    private ServerLocalHome             serverLHome;
    private ServerManagerLocalHome      serverMgrLHome;
    private ServerTypeLocalHome         serverTypeLHome;
    private ServiceLocalHome            serviceLHome;
    private ServiceManagerLocalHome     serviceMgrLHome;
    private ServiceTypeLocalHome        serviceTypeLHome;
    private AuthzSubjectLocalHome      subjectLHome;
    private CPropManagerLocal           cpropLocal;

    protected CPropManagerLocal getCPropMgrLocal(){
        if(this.cpropLocal == null){
            try {
                this.cpropLocal = CPropManagerUtil.getLocalHome().create();
            } catch(Exception exc){
                throw new SystemException(exc);
            }
        }
        return this.cpropLocal;
    }

    protected ConfigManagerLocal getConfigMgrLocal() {
        try {
            if (configMgrL == null) {
                configMgrL = ConfigManagerUtil.getLocalHome().create();
            }
        } catch(Exception exc){
            throw new SystemException(exc);
        }
        return configMgrL;
    }

    protected IpLocalHome getIpLocalHome()
        throws NamingException 
    {
        if (ipLHome == null) {
            ipLHome = IpUtil.getLocalHome();
        }
        return ipLHome;
    }

    protected AgentLocalHome getAgentLocalHome()
        throws SystemException
    {
        if(this.agentLHome == null) {
            try {
                this.agentLHome = AgentUtil.getLocalHome();
            } catch(NamingException exc){
                throw new SystemException(exc);
            }
        }
        return agentLHome;
    }

    protected AgentTypeLocalHome getAgentTypeLocalHome()
        throws NamingException 
    {
        if (agentTypeLHome == null) {
            agentTypeLHome = AgentTypeUtil.getLocalHome();
        }
        return agentTypeLHome;
    }

    protected ConfigResponseLocalHome getConfigResponseLocalHome()  
    {
        if (configRespLHome == null) {
            try {
                configRespLHome = ConfigResponseUtil.getLocalHome();
            } catch (NamingException e) {
                throw new SystemException(e);
            }
        }
        return configRespLHome;
    }

    protected ApplicationManagerLocal getApplicationMgrLocal() {
        try {
            if (appMgrLHome == null) {
                appMgrLHome = ApplicationManagerUtil.getLocalHome();
            }
            return appMgrLHome.create();
        } catch (NamingException e) {
            throw new SystemException(e);
        } catch (CreateException e) {
            throw new SystemException(e);
        }
    }

    protected AppdefGroupManagerLocalHome getAppdefGroupManagerLocalHome() {
        try {
            if (grpMgrLHome == null) {
                grpMgrLHome = AppdefGroupManagerUtil.getLocalHome();
            }
            return grpMgrLHome;
        } catch (NamingException e) {
            throw new SystemException(e);
        }
    }

    protected AppdefGroupManagerLocal getAppdefGroupManagerLocal() {
        try {
            return getAppdefGroupManagerLocalHome().create();
        } catch (CreateException e) {
            throw new SystemException(e);
        }
    }

    protected PlatformManagerLocal getPlatformMgrLocal() {
        try {
            if (platformMgrLHome == null) {
                platformMgrLHome = PlatformManagerUtil.getLocalHome();
            }
            return platformMgrLHome.create();
        } catch (NamingException e) {
            throw new SystemException(e);
        } catch (CreateException e) {
            throw new SystemException(e);
        }
    }

    protected ServerManagerLocal getServerMgrLocal() {
        try {
            if (serverMgrLHome == null) {
                serverMgrLHome = ServerManagerUtil.getLocalHome();
            }
            return serverMgrLHome.create();
        } catch (NamingException e) {
            throw new SystemException(e);
        } catch (CreateException e) {
            throw new SystemException(e);
        }
    }

    protected ServiceManagerLocal getServiceMgrLocal() {
        try {
            if (serviceMgrLHome == null) {
                serviceMgrLHome = ServiceManagerUtil.getLocalHome();
            }
            return serviceMgrLHome.create();
        } catch (NamingException e) {
            throw new SystemException(e);
        } catch (CreateException e) {
            throw new SystemException(e);
        }
    }

    /**
     * Get the local interace of the Resource Manager
     * @return resourceManagerLocal
     */
    protected ResourceManagerLocal getResourceManager()
    {
        if(rmLocal == null) {   
        	try {
        		rmLocal = ResourceManagerUtil.getLocalHome().create();
        	} catch (Exception e) {
        		throw new SystemException(e);
        	}
        } 
        return rmLocal;
    } 

    /**
     * Get the LocalHome reference for the PlatformObject
     */
    protected PlatformLocalHome getPlatformLocalHome() 
        throws NamingException 
    {
        if(platformLHome == null){
            platformLHome = PlatformUtil.getLocalHome();
        }
        return platformLHome;
    }

    /**
     * Get the LocalHome reference for the PlatformTypeObject
     */
    protected PlatformTypeLocalHome getPlatformTypeLocalHome() 
        throws NamingException 
    {
        if(platTypeLHome == null) {
            platTypeLHome = PlatformTypeUtil.getLocalHome();
        }
        return platTypeLHome;
    }

    protected ServerLocalHome getServerLocalHome() {
        if(serverLHome == null) {
            try {
                serverLHome = ServerUtil.getLocalHome();
            } catch (NamingException e) {
                throw new SystemException(e);
            }
        }
        return serverLHome;
    }

    /**
     * Get the LocalHome reference for the ServerTypeObject
     * @return ServerTypeLocalHome
     */
    protected ServerTypeLocalHome getServerTypeLocalHome()
        throws NamingException 
    {
        if(serverTypeLHome == null) {
            serverTypeLHome = ServerTypeUtil.getLocalHome();
        }
        return serverTypeLHome;
    }

    /**
     * Get the LocalHome reference for the ServiceTypeObject
     * @return ServiceTypeLocalHome
     */
    protected ServiceTypeLocalHome getServiceTypeLocalHome()
        throws NamingException 
    {
        if(serviceTypeLHome == null) {
            serviceTypeLHome = ServiceTypeUtil.getLocalHome();
        }
        return serviceTypeLHome;
    }

    protected ServiceLocalHome getServiceLocalHome() {
        if(serviceLHome == null) {
            try {
                serviceLHome = ServiceUtil.getLocalHome();
            } catch (NamingException e) {
                throw new SystemException(e);
            }
        }
        return serviceLHome;
    }

    /**
     * Get the LocalHome reference for the ApplicationType
     * @return ApplicationTypeLocalHome
     */
    protected ApplicationTypeLocalHome getApplicationTypeLocalHome()
        throws NamingException 
    {
        if(appTypeLHome == null) {
            appTypeLHome = ApplicationTypeUtil.getLocalHome();
        }
        return appTypeLHome;
    }

    /**
     * Get the LocalHome reference for the Application
     * @return ApplicationLocalHome
     */
    protected ApplicationLocalHome getApplicationLocalHome()
        throws NamingException 
    {
        if(appLHome == null) {
            appLHome = ApplicationUtil.getLocalHome();
        }
        return appLHome;
    }

    /**
     * Get the LocalHome reference for the AIServer 
     * @return AIServerLocalHome
     */
    protected AIServerLocalHome getAIServerLocalHome()
        throws NamingException 
    {
        if(aiqServerLHome == null) {
            aiqServerLHome = AIServerUtil.getLocalHome();
        }
        return aiqServerLHome;
    }

    /**
     * Get the LocalHome reference for the AuthzSubject
     * @return AuthzSubjectLocalHome
     */
    protected AuthzSubjectLocalHome getAuthzSubjectLocalHome()
        throws NamingException 
    {
        if(subjectLHome == null) {
            subjectLHome = AuthzSubjectUtil.getLocalHome();
        }
        return subjectLHome;
    }

    /**
     * Get the LocalHome reference for the AIQueueManager 
     * @return AIServerLocalHome
     */
    protected AIQueueManagerLocal getAIQManagerLocal()
        throws CreateException, NamingException 
    {
        if(aiqManagerLocal == null) {
            aiqManagerLocal = AIQueueManagerUtil.getLocalHome().create();
        }
        return aiqManagerLocal;
    }

    protected AppdefResourceTypeValue findResourceType(int appdefType,
                                                       int appdefTypeId)
        throws AppdefEntityNotFoundException
    {
        Integer id = new Integer(appdefTypeId);

        if(appdefType == AppdefEntityConstants.APPDEF_TYPE_PLATFORM){
            PlatformManagerLocal pmLocal;

            pmLocal = this.getPlatformMgrLocal();
            return pmLocal.findPlatformTypeById(id);
        } else if(appdefType == AppdefEntityConstants.APPDEF_TYPE_SERVER){
            ServerManagerLocal smLocal;

            smLocal = this.getServerMgrLocal();
            try {
                return smLocal.findServerTypeById(id);
            } catch(FinderException exc){
                throw new ServerNotFoundException("server type id=" + 
                                                  appdefTypeId + 
                                                  " not found");
            }
        } else if(appdefType == AppdefEntityConstants.APPDEF_TYPE_SERVICE){
            ServiceManagerLocal vmLocal;

            vmLocal = this.getServiceMgrLocal();
            try {
                return vmLocal.findServiceTypeById(id);
            } catch(FinderException exc){
                throw new ServiceNotFoundException("service type id=" +
                                                   appdefTypeId +
                                                   " not found");
            }
        } else if(appdefType == 
                  AppdefEntityConstants.APPDEF_TYPE_APPLICATION)
        {
            ApplicationManagerLocal amLocal;
            
            amLocal = this.getApplicationMgrLocal();
            try {
                return amLocal.findApplicationTypeById(id);
            } catch(FinderException exc){
                throw new ApplicationNotFoundException("app type id=" +
                                                       appdefTypeId + 
                                                       "not found");
            }
        } else {
            throw new IllegalArgumentException("Unrecognized appdef type:"+
                                               " " + appdefType);
        }
    }

    protected AppdefResourceTypeValue findResourceType(TypeInfo info)
        throws AppdefEntityNotFoundException
    {
        int type = info.getType();

        if(type == AppdefEntityConstants.APPDEF_TYPE_PLATFORM){
            PlatformManagerLocal pmLocal;

            pmLocal = this.getPlatformMgrLocal();
            return pmLocal.findPlatformTypeByName(info.getName());
        } else if(type == AppdefEntityConstants.APPDEF_TYPE_SERVER){
            ServerManagerLocal smLocal;

            smLocal = this.getServerMgrLocal();
            try {
                return smLocal.findServerTypeByName(info.getName());
            } catch(FinderException exc){
                throw new ServerNotFoundException("server type name=" + 
                                                  info.getName() +
                                                  " not found");
            }
        } else if(type == AppdefEntityConstants.APPDEF_TYPE_SERVICE){
            ServiceManagerLocal vmLocal;

            vmLocal = this.getServiceMgrLocal();
            try {
                return vmLocal.findServiceTypeByName(info.getName());
            } catch(FinderException exc){
                throw new ServiceNotFoundException("service type name=" +
                                                   info.getName() +
                                                   " not found");
            }
        } else {
            throw new IllegalArgumentException("Unrecognized appdef type:"+
                                               " " + info);
        }
    }
}
