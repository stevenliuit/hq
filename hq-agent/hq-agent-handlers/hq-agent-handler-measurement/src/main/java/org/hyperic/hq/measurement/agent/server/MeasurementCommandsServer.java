/*
 * NOTE: This copyright does *not* cover user programs that use Hyperic
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 *
 * Copyright (C) [2004-2010], VMware, Inc.
 * This file is part of Hyperic.
 *
 * Hyperic is free software; you can redistribute it and/or modify
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

package org.hyperic.hq.measurement.agent.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.agent.*;
import org.hyperic.hq.agent.bizapp.CommandsAPIInfo;
import org.hyperic.hq.agent.bizapp.callback.MeasurementCallbackClient;
import org.hyperic.hq.agent.bizapp.callback.StorageProviderFetcher;
import org.hyperic.hq.agent.handler.measurement.ScheduledMeasurement;
import org.hyperic.hq.agent.server.*;
import org.hyperic.hq.measurement.agent.MeasurementCommandsAPI;
import org.hyperic.hq.measurement.agent.client.MeasurementCommandsClient;
import org.hyperic.hq.measurement.agent.commands.*;
import org.hyperic.hq.measurement.agent.server.ScheduleThread.ParsedTemplate;
import org.hyperic.hq.product.ConfigTrackPluginManager;
import org.hyperic.hq.product.LogTrackPluginManager;
import org.hyperic.hq.product.MeasurementPluginManager;
import org.hyperic.hq.product.ProductPlugin;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MeasurementCommandsServer 
    implements AgentServerHandler, AgentNotificationHandler {
    private static final long THREAD_JOIN_WAIT = 10 * 1000;

    private MeasurementCommandsAPI   verAPI;         // Common API specifics
    private Thread                   scheduleThread; // Thread of scheduler
    private ScheduleThread           scheduleObject; // Our scheduler
    private Thread                   senderThread;   // Thread of sender
    private SenderThread             senderObject;   // Our sender
    private AgentStorageProvider     storage;        // Agent storage
    private Map                      validProps;     // Map of valid props
    private AgentConfig        bootConfig;     // Agent boot config
    private MeasurementSchedule      schedStorage;   // Schedule storage
    private MeasurementPluginManager pluginManager;  // Plugin manager
    private Log                      log;            // Our log

    // Config and Log track
    private ConfigTrackPluginManager ctPluginManager;
    private LogTrackPluginManager    ltPluginManager;
    private Thread                   trackerThread;  // Config and Log tracker thread
    private TrackerThread            trackerObject;  // Config and Log tracker object
    
    private MeasurementCommandsService measurementCommandsService;

    public MeasurementCommandsServer(){
        this.verAPI         = new MeasurementCommandsAPI();
        this.scheduleThread = null;
        this.scheduleObject = null;
        this.senderThread   = null;
        this.senderObject   = null;
        this.storage        = null;
        this.validProps     = Collections.synchronizedMap(new HashMap());
        this.bootConfig     = null;
        this.schedStorage   = null;
        this.pluginManager  = null;
        this.log            = LogFactory.getLog(MeasurementCommandsServer.class);

        this.ctPluginManager  = null;
        this.ltPluginManager  = null;
        this.trackerThread    = null;
        this.trackerObject    = null;
        
        for(int i=0; i<this.verAPI.propSet.length; i++){
            // Simply setup true object values for properties we know about
            this.validProps.put(this.verAPI.propSet[i], this);
        }
    }

    private void spawnThreads(SenderThread senderObject, 
                              ScheduleThread scheduleObject, 
                              TrackerThread trackerObject)
        throws AgentStartException 
    {
        this.senderThread   = new Thread(senderObject, "SenderThread");
        senderThread.setDaemon(true);
        this.scheduleThread = new Thread(scheduleObject,"ScheduleThread");
        scheduleThread.setDaemon(true);
        this.trackerThread = new Thread(trackerObject, "TrackerThread");
        this.trackerThread.setDaemon(true);

        this.senderThread.start();
        this.scheduleThread.start();
        this.trackerThread.start();
    }

    public AgentAPIInfo getAPIInfo(){
        return this.verAPI;
    }

    public String[] getCommandSet(){
        return MeasurementCommandsAPI.commandSet;
    }

    public AgentRemoteValue dispatchCommand(String cmd, AgentRemoteValue args,
                                            InputStream in, OutputStream out)
        throws AgentRemoteException 
    {
        if(cmd.equals(this.verAPI.command_scheduleMeasurements)){            
            ScheduleMeasurements_args sa = 
                new ScheduleMeasurements_args(args);

            measurementCommandsService.scheduleMeasurements(sa);

            return new ScheduleMeasurements_result();
        } else if(cmd.equals(this.verAPI.command_unscheduleMeasurements)){
            UnscheduleMeasurements_args sa = 
                new UnscheduleMeasurements_args(args);

            measurementCommandsService.unscheduleMeasurements(sa);
            
            return new UnscheduleMeasurements_result();
        } else if(cmd.equals(this.verAPI.command_getMeasurements)){
            GetMeasurements_args sa = 
                new GetMeasurements_args(args);

            return measurementCommandsService.getMeasurements(sa);
        } else if(cmd.equals(this.verAPI.command_setProperties)){
            SetProperties_args sa = 
                new SetProperties_args(args);

            measurementCommandsService.setProperties(sa);
            
            return new SetProperties_result();
        } else if(cmd.equals(this.verAPI.command_deleteProperties)){
            DeleteProperties_args sa = 
                new DeleteProperties_args(args);

            measurementCommandsService.deleteProperties(sa);
            
            return new DeleteProperties_result();
        } else if(cmd.equals(this.verAPI.command_trackAdd)) {
            TrackPluginAdd_args ta = new TrackPluginAdd_args(args);
            
            measurementCommandsService.addTrackPlugin(ta);

            return new TrackPluginAdd_result();
        } else if(cmd.equals(this.verAPI.command_trackRemove)) {
            TrackPluginRemove_args ta = new TrackPluginRemove_args(args);

            measurementCommandsService.removeTrackPlugin(ta);
            
            return new TrackPluginRemove_result();
        } else {
            throw new AgentRemoteException("Unknown command: " + cmd);
        }
    }

    public void startup(org.hyperic.hq.agent.server.AgentService agentServer)
        throws AgentStartException 
    {
        Iterator i;

        try {
            this.storage      = agentServer.getStorageProvider();
            this.bootConfig   = agentServer.getBootConfig();
            this.schedStorage = new MeasurementSchedule(this.storage, bootConfig.getBootProperties());
            logMeasurementSchedule(this.schedStorage);
        } catch(AgentRunningException exc){
            throw new AgentAssertionException("Agent should be running here", exc);
        }

        try {
            this.pluginManager =
                (MeasurementPluginManager)agentServer.
                getPluginManager(ProductPlugin.TYPE_MEASUREMENT);
            this.ctPluginManager =
                (ConfigTrackPluginManager)agentServer.
                getPluginManager(ProductPlugin.TYPE_CONFIG_TRACK);
            this.ltPluginManager =
                (LogTrackPluginManager)agentServer.
                getPluginManager(ProductPlugin.TYPE_LOG_TRACK);

        } catch (Exception e) {
            throw new AgentStartException("Unable to get measurement " +
                                          "plugin manager: " + 
                                          e.getMessage());
        }
        
        this.senderObject = new SenderThread(this.bootConfig.getBootProperties(),
                                             this.storage, this.schedStorage);
        
        this.scheduleObject = new ScheduleThread(this.senderObject,
                                                 this.pluginManager,
                                                 this.bootConfig.getBootProperties());
        
        this.trackerObject =
            new TrackerThread(this.ctPluginManager,
                              this.ltPluginManager,
                              this.storage,
                              this.bootConfig.getBootProperties());
        
        this.measurementCommandsService = 
                new MeasurementCommandsService(this.storage, 
                                               this.validProps,
                                               this.schedStorage, 
                                               this.pluginManager, 
                                               this.ltPluginManager,
                                               this.ctPluginManager,
                                               this.scheduleObject);
        
        AgentTransportLifecycle agentTransportLifecycle;
        
        try {
            agentTransportLifecycle = agentServer.getAgentTransportLifecycle();
        } catch (Exception e) {
            throw new AgentStartException("Unable to get agent transport lifecycle: "+
                                            e.getMessage());
        }
        
        log.info("Registering Measurement Commands Service with Agent Transport");
        
        try {
            agentTransportLifecycle.registerService(MeasurementCommandsClient.class, 
                                           measurementCommandsService);
        } catch (Exception e) {
            throw new AgentStartException("Failed to register Measurement Commands Service.", e);
        }

        spawnThreads(this.senderObject, this.scheduleObject, this.trackerObject);

        i = this.schedStorage.getMeasurementList();
        while(i.hasNext()){
            ScheduledMeasurement meas = (ScheduledMeasurement)i.next();
            this.measurementCommandsService.scheduleMeasurement(meas);
        }

        agentServer.registerMonitor("camMetric.schedule", this.scheduleObject);
        agentServer.registerMonitor("camMetric.sender", this.senderObject);

        // If we have don't have a provider, register a handler until
        // we get one
        if(CommandsAPIInfo.getProvider(this.storage) == null){
            agentServer.registerNotifyHandler(this,
                                        CommandsAPIInfo.NOTIFY_SERVER_SET);
        } else {
            this.startConfigPopulator();
        }

        this.log.info("Measurement Commands Server started up");
    }

    public void handleNotification(String msgClass, String msg){
        this.startConfigPopulator();
    }

    private void startConfigPopulator(){
        StorageProviderFetcher fetcher
            = new StorageProviderFetcher(this.storage);
        MeasurementCallbackClient client
            = new MeasurementCallbackClient(fetcher);
        ConfigPopulateThread populator
            = new ConfigPopulateThread(client, this.ltPluginManager,
                                       this.ctPluginManager);
        populator.setDaemon(true);
        populator.start();
    }

    private void interruptThread(Thread t)
        throws InterruptedException
    {
        if(t.isAlive()){
            t.interrupt();
            t.join(THREAD_JOIN_WAIT);
            
            if(t.isAlive()){
                this.log.warn(t.getName() + " did not die within the " +
                              "timeout period.  Killing it");
                t.stop();
            }
        }
    }
    
    private void logMeasurementSchedule(MeasurementSchedule sched) {
        if (this.log.isDebugEnabled()) { 
            try {                
                Iterator scheduleIter = sched.getMeasurementList();
                int scheduleSize = 0;
                
                while (scheduleIter.hasNext()) { 
                    ScheduledMeasurement metric = (ScheduledMeasurement) scheduleIter.next();
                    if (metric != null) {
                        ParsedTemplate templ = ScheduleThread.getParsedTemplate(metric);
                        scheduleSize++;
                        StringBuffer s = new StringBuffer("Measurement Schedule[")
                                .append(scheduleSize)
                                .append("]: entityId=").append(metric.getEntity())
                                .append(", category=").append(metric.getCategory())
                                .append(", interval=").append(metric.getInterval())
                                .append(", derivedId=").append(metric.getDerivedID())
                                .append(", dsnId=").append(metric.getDsnID())
                                .append(", dsn=").append(templ.metric.toDebugString());
                    
                        this.log.debug(s.toString());
                    }
                }
                this.log.debug("Measurement schedule list size=" + scheduleSize);
            } catch (Exception e) {
                // since logging the measurement schedule is in debug mode,
                // also log any exceptions in debug mode
                this.log.debug("Could not display measurement schedule: " + e.getMessage(), e);
            }
        }
    }

    public void shutdown(){
        this.log.info("Measurement Commands Server shutting down");
        logMeasurementSchedule(this.schedStorage);

        this.scheduleObject.die();
        this.senderObject.die();

        try {
            this.interruptThread(this.senderThread);
            this.interruptThread(this.scheduleThread);
        } catch(InterruptedException exc){
            // Someone wants us to die badly .... ok 
            this.log.warn("shutdown interrupted");
        }

        this.log.info("Measurement Commands Server shut down");
    }
}
