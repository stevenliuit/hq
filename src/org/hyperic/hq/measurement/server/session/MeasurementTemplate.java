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

package org.hyperic.hq.measurement.server.session;

import java.util.Collection;
import java.util.ArrayList;

import org.hyperic.hibernate.PersistedObject;
import org.hyperic.hq.measurement.shared.MeasurementTemplateValue;

public class MeasurementTemplate extends PersistedObject
    implements java.io.Serializable {

    // Fields
    private Integer _cid;
    private String _name;
    private String _alias;
    private String _units;
    private int _collectionType;
    private boolean _defaultOn = false;
    private long _defaultInterval;
    private boolean _designate = false;
    private String _template;
    private String _plugin;
    private byte[] _expressionData;
    private long _ctime;
    private long _mtime;
    private MonitorableType _monitorableType;
    private Category _category;
    private Collection _measurementArgs = new ArrayList();
    private Collection _rawMeasurementArgs = new ArrayList();

    // Constructors
    public MeasurementTemplate() {
    }

    public MeasurementTemplate(String name, String alias,
                               int collectionType, boolean defaultOn,
                               long defaultInterval, boolean designate,
                               String template, long ctime, long mtime) {
        _name = name;
        _alias = alias;
        _collectionType = collectionType;
        _defaultOn = defaultOn;
        _defaultInterval = defaultInterval;
        _designate = designate;
        _template = template;
        _ctime = ctime;
        _mtime = mtime;
    }

    public MeasurementTemplate(String name, String alias,
                               String units, int collectionType, 
                               boolean defaultOn, long defaultInterval, 
                               boolean designate, String template,
                               String plugin, byte[] expressionData, 
                               long ctime, long mtime, 
                               MonitorableType monitorableType, 
                               Category category, 
                               Collection measurementArgs) {
        _name = name;
        _alias = alias;
        _units = units;
        _collectionType = collectionType;
        _defaultOn = defaultOn;
        _defaultInterval = defaultInterval;
        _designate = designate;
        _template = template;
        _plugin = plugin;
        _expressionData = expressionData;
        _ctime = ctime;
        _mtime = mtime;
        _monitorableType = monitorableType;
        _category = category;
        _measurementArgs = measurementArgs;
    }
   
    // Property accessors
    public Integer getCid() {
        return _cid;
    }

    protected void setCid(Integer cid) {
        _cid = cid;
    }

    public String getName() {
        return _name;
    }
    
    protected void setName(String name) {
        _name = name;
    }

    public String getAlias() {
        return _alias;
    }
    
    protected void setAlias(String alias) {
        _alias = alias;
    }

    public String getUnits() {
        return _units;
    }
    
    protected void setUnits(String units) {
        _units = units;
    }

    public int getCollectionType() {
        return _collectionType;
    }

    protected void setCollectionType(int collectionType) {
        _collectionType = collectionType;
    }

    public boolean isDefaultOn() {
        return _defaultOn;
    }
    
    protected void setDefaultOn(boolean defaultOn) {
        _defaultOn = defaultOn;
    }

    public long getDefaultInterval() {
        return _defaultInterval;
    }
    
    protected void setDefaultInterval(long defaultInterval) {
        _defaultInterval = defaultInterval;
    }

    public boolean isDesignate() {
        return _designate;
    }
    
    protected void setDesignate(boolean designate) {
        _designate = designate;
    }

    public String getTemplate() {
        return _template;
    }
    
    protected void setTemplate(String template) {
        _template = template;
    }

    public String getPlugin() {
        return _plugin;
    }
    
    protected void setPlugin(String plugin) {
        _plugin = plugin;
    }

    public byte[] getExpressionData() {
        return _expressionData;
    }
    
    protected void setExpressionData(byte[] expressionData) {
        _expressionData = expressionData;
    }

    public long getCtime() {
        return _ctime;
    }
    
    protected void setCtime(long ctime) {
        _ctime = ctime;
    }

    public long getMtime() {
        return _mtime;
    }
    
    protected void setMtime(long mtime) {
        _mtime = mtime;
    }

    public MonitorableType getMonitorableType() {
        return _monitorableType;
    }
    
    protected void setMonitorableType(MonitorableType monitorableType) {
        _monitorableType = monitorableType;
    }

    public Category getCategory() {
        return _category;
    }
    
    protected void setCategory(Category category) {
        _category = category;
    }

    public Collection getMeasurementArgs() {
        return _measurementArgs;
    }
    
    protected void setMeasurementArgs(Collection measurementArgs) {
        _measurementArgs = measurementArgs;
    }

    public Collection getRawMeasurementArgs() {
        return _rawMeasurementArgs;
    }

    protected void setRawMeasurementArgs(Collection measurementArgs) {
        _rawMeasurementArgs = measurementArgs;
    }

    /**
     * Legacy EJB DTO pattern
     * @deprecated Use (this) MeasurementTemplate object instead
     */
    public MeasurementTemplateValue getMeasurementTemplateValue() {
        MeasurementTemplateValue value = new MeasurementTemplateValue();
        value.setId(getId());
        value.setName(getName());
        value.setAlias(getAlias());
        value.setUnits(getUnits());
        value.setCollectionType(getCollectionType());
        value.setDefaultOn(isDefaultOn());
        value.setDefaultInterval(getDefaultInterval());
        value.setDesignate(isDesignate());
        value.setTemplate(getTemplate());
        value.setExpressionData(getExpressionData());
        value.setPlugin(getPlugin());
        value.setCtime(getCtime());
        value.setMtime(getMtime());

        value.setMonitorableType(getMonitorableType().getMonitorableTypeValue());
        value.setCategory(getCategory().getCategoryValue());

        return value;
    }
}
