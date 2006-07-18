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

package org.hyperic.hq.bizapp.server.trigger.conditional;

import java.text.MessageFormat;

import org.hyperic.hq.appdef.shared.AppdefEntityID;
import org.hyperic.hq.appdef.shared.CPropChangeEvent;
import org.hyperic.hq.bizapp.shared.ConditionalTriggerSchema;
import org.hyperic.hq.common.SystemException;
import org.hyperic.hq.events.AbstractEvent;
import org.hyperic.hq.events.ActionExecuteException;
import org.hyperic.hq.events.AlertCreateException;
import org.hyperic.hq.events.EventConstants;
import org.hyperic.hq.events.EventTypeException;
import org.hyperic.hq.events.InvalidTriggerDataException;
import org.hyperic.hq.events.TriggerFiredEvent;
import org.hyperic.hq.events.ext.AbstractTrigger;
import org.hyperic.hq.events.ext.RegisterableTriggerInterface;
import org.hyperic.hq.events.shared.AlertConditionValue;
import org.hyperic.hq.events.shared.RegisteredTriggerValue;
import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.config.ConfigSchema;
import org.hyperic.util.config.EncodingException;
import org.hyperic.util.config.InvalidOptionException;
import org.hyperic.util.config.InvalidOptionValueException;

/**
 * A simple trigger which fires if a control event occurs.
 */

public class CustomPropertyTrigger
    extends AbstractTrigger
    implements RegisterableTriggerInterface, ConditionalTriggerInterface {
    static {
        // Register the trigger/condition
        ConditionalTriggerInterface.MAP_COND_TRIGGER.put(
            new Integer(EventConstants.TYPE_CUST_PROP),
            CustomPropertyTrigger.class);
    }
    
    public static final MessageFormat MESSAGE_FMT = new MessageFormat
        ("New {0} value ({1}) differs from previous property value ({2}).");

    private AppdefEntityID id;
    private String         customProperty;

    /**
     * @see org.hyperic.hq.events.shared.RegisteredTriggerValue#getInterestedEventTypes()
     */
    public Class[] getInterestedEventTypes(){
        return new Class[] { CPropChangeEvent.class };
    }

    /**
     * @see org.hyperic.hq.events.shared.RegisteredTriggerValue#getInterestedInstanceIDs()
     */
    public Integer[] getInterestedInstanceIDs(Class c){
        return new Integer[] { id.getId() };
    }

    /**
     * @see org.hyperic.hq.events.shared.RegisteredTriggerValue#getConfigSchema()
     */
    public ConfigSchema getConfigSchema(){
        return ConditionalTriggerSchema
            .getConfigSchema(EventConstants.TYPE_CUST_PROP);
    }

    /**
     * @see org.hyperic.hq.bizapp.server.trigger.conditional.ConditionalTriggerInterface#getConfigResponse()
     */
    public ConfigResponse getConfigResponse(AppdefEntityID id,
                                            AlertConditionValue cond)
        throws InvalidOptionException, InvalidOptionValueException {
        if (cond.getType() != EventConstants.TYPE_CUST_PROP)
            throw new InvalidOptionValueException(
                "Condition is not a Custom Property Change Event");

        ConfigResponse resp = new ConfigResponse();
        resp.setValue(CFG_TYPE, String.valueOf(id.getType()));
        resp.setValue(CFG_ID, String.valueOf(id.getID()));
        resp.setValue(CFG_NAME, cond.getName());
        return resp;
    }

    /**
     * @see org.hyperic.hq.events.shared.RegisteredTriggerValue#init()
     */
    public void init(RegisteredTriggerValue tval)
        throws InvalidTriggerDataException {
        ConfigResponse triggerData;
        String sID, sType;

        // Set the trigger value
        setTriggerValue(tval);

        // Decode the configuration
        try {
            triggerData = ConfigResponse.decode(tval.getConfig());
            sType       = triggerData.getValue(CFG_TYPE);
            sID         = triggerData.getValue(CFG_ID);
            this.customProperty = triggerData.getValue(CFG_NAME);
        } catch (EncodingException e) {
            throw new InvalidTriggerDataException(e);
        }

        if(sType == null || sID == null || customProperty == null) {
            throw new InvalidTriggerDataException(
                CFG_TYPE   + " = '" + sType  + "' " +
                CFG_ID     + " = '" + sID    + "' " +
                CFG_NAME   + " = '" + customProperty + "' ");
        }

        try {
            this.id = new AppdefEntityID(Integer.parseInt(sType),
                                         Integer.parseInt(sID));
        } catch(NumberFormatException exc){
            throw new InvalidTriggerDataException(
                "Instance type: " + sType + " or id: " + sID +
                " is not a valid number");
        }
    }

    /**
     * @see org.hyperic.hq.events.AbstractEvent#processEvent()
     */
    public void processEvent(AbstractEvent e)
        throws EventTypeException, ActionExecuteException {
        CPropChangeEvent event;
        boolean fire;

        if(!(e instanceof CPropChangeEvent)){
            throw new EventTypeException("Invalid event type passed, " +
                                         "expected CPropChangeEvent");
        }

        // If we didn't fulfill the condition, then don't fire
        event = (CPropChangeEvent) e;
        
        if (!event.getResource().equals(id)        ||
            !event.getKey().equals(customProperty) ||
            event.getOldValue() == null)
            return;

        try {
            TriggerFiredEvent tfe = new TriggerFiredEvent(getId(), event);
            StringBuffer sb = new StringBuffer();
            MESSAGE_FMT.format(new String[] { event.getKey(),
                                              event.getOldValue(),
                                              event.getNewValue() },
                               sb, null);
            tfe.setMessage(sb.toString());
            super.fireActions(tfe);
        } catch (AlertCreateException exc) {
            throw new ActionExecuteException(exc);
        } catch (ActionExecuteException exc) {
            throw new ActionExecuteException(exc);
        } catch (SystemException exc) {
            throw new ActionExecuteException(exc);
        }
    }

}
