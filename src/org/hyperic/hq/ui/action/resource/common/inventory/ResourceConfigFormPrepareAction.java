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

package org.hyperic.hq.ui.action.resource.common.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.hyperic.hq.appdef.shared.AppdefEntityConstants;
import org.hyperic.hq.appdef.shared.AppdefEntityID;
import org.hyperic.hq.appdef.shared.AppdefResourceValue;
import org.hyperic.hq.appdef.shared.ConfigFetchException;
import org.hyperic.hq.appdef.shared.ServerValue;
import org.hyperic.hq.bizapp.shared.AppdefBoss;
import org.hyperic.hq.bizapp.shared.ProductBoss;
import org.hyperic.hq.product.PluginNotFoundException;
import org.hyperic.hq.product.ProductPlugin;
import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.util.BizappUtils;
import org.hyperic.hq.ui.util.ContextUtils;
import org.hyperic.hq.ui.util.RequestUtils;
import org.hyperic.util.StringUtil;
import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.config.ConfigSchema;

/**
 * A subclass of <code> TilesAction </code> that populates the
 * <code>ResourceConfigForm</code> form with configOptions from the
 * Product plugin.
 */
public class ResourceConfigFormPrepareAction extends TilesAction {
    Log log =
        LogFactory.getLog(ResourceConfigFormPrepareAction.class.getName());

    //if this resource has help text, build up a map of all
    //configuration values which can then be applied to variables
    //in the help text.
    protected void addHelpProperties(Map helpProps,
                                   ConfigSchema schema,
                                   ConfigResponse config) {

        helpProps.putAll(schema.getDefaultProperties());
        helpProps.putAll(config.toProperties());
    }

    protected void addExtraHelpProperties(ProductBoss pboss, Map helpProps) {
        String installpath =
            (String)helpProps.get(ProductPlugin.PROP_INSTALLPATH);

        if (installpath != null) {
            //escaped version of installpath suitable for insertion
            //into agent.properties
            helpProps.put(ProductPlugin.PROP_INSTALLPATH + ".escaped",
                          StringUtil.replace(installpath, "\\", "\\\\"));
        }

        try {
            // Version information
            helpProps.put(Constants.APP_VERSION, pboss.getVersion());
        } catch (Exception e) { }
    }

        
    /**
     * Retrieve this data and store it in the
     * <code>ResourceConfigForm</code>:
     *
     */
    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {
        // HACK for the ProblemResources portlet to tell us
        // to redirect back to dash when done
        if (request.getParameter("todash") != null) {
            request.setAttribute("todash", Boolean.TRUE);
        }

        ResourceConfigForm resourceForm = (ResourceConfigForm) form;

        int sessionId = RequestUtils.getSessionId(request).intValue();
        ServletContext ctx = getServlet().getServletContext();

        AppdefEntityID rid = RequestUtils.getEntityId(request);

        AppdefBoss boss = ContextUtils.getAppdefBoss(ctx);
        
        AppdefResourceValue resource
            = (AppdefResourceValue) boss.findById(sessionId, rid);

        resourceForm.loadResourceValue(resource);

        RequestUtils.setResource(request, resource);

        ProductBoss pboss = ContextUtils.getProductBoss(ctx);

        ConfigResponse oldResponse = new ConfigResponse();
        ConfigSchema config = new ConfigSchema();
        String help = null;
        Map helpProps = new HashMap();

        try {
            oldResponse = pboss
                    .getMergedConfigResponse(sessionId,
                                             ProductPlugin.TYPE_PRODUCT, rid,
                                             false);

            config = pboss.getConfigSchema(sessionId, rid,
                                           ProductPlugin.TYPE_PRODUCT,
                                           oldResponse);

            addHelpProperties(helpProps, config, oldResponse);
        } catch (ConfigFetchException e) {
            log.error("cannot retrieve product config", e);
            RequestUtils
                    .setError(request,
                              "resource.common.inventory.error.productConfigNotSet");
            resourceForm.setResourceConfigOptions(new ArrayList());
            request.setAttribute(Constants.PRODUCT_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            resourceForm.setControlConfigOptions(new ArrayList());
            request.setAttribute(Constants.CONTROL_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            resourceForm.setMonitorConfigOptions(new ArrayList());
            request.setAttribute(Constants.MONITOR_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            return null;

        } catch (PluginNotFoundException e) {
            log.error("Plugin not found for the resource ", e);
            RequestUtils
                    .setError(request,
                              "resource.common.inventory.error.PluginNotFound");
            resourceForm.setResourceConfigOptions(new ArrayList());
            request.setAttribute(Constants.PRODUCT_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            resourceForm.setControlConfigOptions(new ArrayList());
            request.setAttribute(Constants.CONTROL_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            resourceForm.setMonitorConfigOptions(new ArrayList());
            request.setAttribute(Constants.MONITOR_CONFIG_OPTIONS_COUNT,
                                 new Integer(0));
            return null;
        }

        //XXX add the options through the builder and get them
        String prefix = ProductPlugin.TYPE_PRODUCT + ".";
        List uiResourceOptions = BizappUtils.buildLoadConfigOptions(prefix, 
                                                        config, oldResponse);

        resourceForm.setResourceConfigOptions(uiResourceOptions);
        request.setAttribute(Constants.PRODUCT_CONFIG_OPTIONS_COUNT,
                                new Integer(uiResourceOptions.size()));

        prefix = ProductPlugin.TYPE_MEASUREMENT + ".";

        config = new ConfigSchema();
        oldResponse = new ConfigResponse();

        try {
            oldResponse = pboss.getMergedConfigResponse(sessionId,
                    ProductPlugin.TYPE_MEASUREMENT, rid, false);
            config = pboss.getConfigSchema(sessionId, rid,
                    ProductPlugin.TYPE_MEASUREMENT, oldResponse);

            addHelpProperties(helpProps, config, oldResponse);

            if(rid.getType() == AppdefEntityConstants.APPDEF_TYPE_SERVER) {
                ServerValue server = (ServerValue)resource;
                if(server.getWasAutodiscovered()) { 
                    request.setAttribute(Constants.SERVER_BASED_AUTO_INVENTORY,
                                                                 new Integer(0));
                } else {
                    request.setAttribute(Constants.SERVER_BASED_AUTO_INVENTORY, 
                                                                new Integer(1));
                    resourceForm.setServerBasedAutoInventory(
                                                server.getRuntimeAutodiscovery());
                }
                BizappUtils.setRuntimeAIMessage(sessionId, request, 
                                                server, boss);
            }

        }catch(PluginNotFoundException e) {
            //do nothing
        }
        
        List uiMonitorOptions = BizappUtils.buildLoadConfigOptions(prefix, config,
                                                                     oldResponse); 

        resourceForm.setMonitorConfigOptions(uiMonitorOptions);
        request.setAttribute(Constants.MONITOR_CONFIG_OPTIONS_COUNT,new Integer(uiMonitorOptions.size()));

        if(rid.getType() != AppdefEntityConstants.APPDEF_TYPE_PLATFORM) {
            setUIOptions(rid, request, resourceForm, uiMonitorOptions, helpProps);
        }

        addExtraHelpProperties(pboss, helpProps);

        try {
            help = pboss.getMonitoringHelp(sessionId, rid, helpProps);
        } catch (Exception e) {
            log.error("Error getting help: " + e.getMessage(), e);
        }

        request.setAttribute(Constants.MONITOR_HELP, help);

        return null;

    }

    protected void setUIOptions(AppdefEntityID aeid,
                                HttpServletRequest request,
                                ResourceConfigForm resourceForm,
                                List uiMonitorOptions,
                                Map helpProps)
        throws Exception {}
}
