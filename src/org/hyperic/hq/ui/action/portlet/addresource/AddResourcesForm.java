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

package org.hyperic.hq.ui.action.portlet.addresource;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.hyperic.hq.ui.Constants;
import org.hyperic.hq.ui.action.resource.ResourceForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.action.ActionMapping;


/**
 * A subclass of <code>ResourceForm</code> representing the
 * <em>AddGroupResources</em> form. The purpose of this form is
 * to add AppdefResourceValues to a AppdefGroupValue
 */
public class AddResourcesForm extends ResourceForm  {

    //-------------------------------------instance variables

    private String[] availableResources;
    private String[] pendingResources;
    private Integer psa;
    private Integer psp;
    private List availResourceTypes;
    /** Holds value of property key. */
    private String key;
    
    /** Holds value of property ft. */
    private Integer ft;
    
    /** Holds value of property ff. */
    private Integer ff;
    
    /** Holds value of property functions. */
    private List functions;
    
    /** Holds value of property types. */
    private List types;
    
    //-------------------------------------constructors

    public AddResourcesForm() {
        super();
        setDefaults();
    }

    //-------------------------------------public methods

    public String[] getAvailableResource() {
	   return this.availableResources;
    }

    public String[] getAvailableResources() {
        return getAvailableResource();
    }

    public void setAvailableResource(String[] availableResource) {
        this.availableResources = availableResource;
    }

    public void setAvailableResources(String[] availableResources) {
        setAvailableResource(availableResources);
    }

    public String[] getPendingResource() {
	return this.pendingResources;
    }

    public String[] getPendingResources() {
        return getPendingResource();
    }

    public void setPendingResource(String[] pendingResource) {
        this.pendingResources = pendingResource;
    }

    public void setPendingResources(String[] pendingResources) {
        setPendingResource(pendingResources);
    }

    public Integer getPsa() {
        return this.psa;
    }

    public void setPsa(Integer ps) {
        this.psa = ps;
    }

    public Integer getPsp() {
        return this.psp;
    }

    public void setPsp(Integer ps) {
        this.psp = ps;
    }

    public void reset(ActionMapping mapping,
                      HttpServletRequest request) {
        this.availableResources = new String[0];
        this.pendingResources = new String[0];
        this.psa = null;
        this.psp = null;
        super.reset(mapping, request);
    }

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append("psa=" + psa + " ");
        s.append("psp=" + psp + " ");

        s.append("availableResources={");
        listToString(s, availableResources);
        s.append("} ");

        s.append("pendingResources={");
        listToString(s, pendingResources);
        s.append("}");

        return s.toString();
    }

    private void listToString(StringBuffer s, String[] l) {
        if (l != null) {
            for (int i=0; i<l.length; i++) {
                s.append(l[i]);
                if (i<l.length-1) {
                    s.append(", ");
                }
            }
        }
    }
    
    /**
     * @return List
     */
    public List getAvailResourceTypes() {
        return availResourceTypes;
    }

    /**
     * Sets the availResourceTypes.
     * @param availResourceTypes The availResourceTypes to set
     */
    public void setAvailResourceTypes(List availResourceTypes) {
        this.availResourceTypes = availResourceTypes;
    }

    /** Getter for property key.
     * @return Value of property key.
     *
     */
    public String getKey() {
        return this.key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     *
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /** Getter for property ft.
     * @return Value of property ft.
     *
     */
    public Integer getFt() {
        return this.ft;
    }
    
    /** Setter for property ft.
     * @param ft New value of property ft.
     *
     */
    public void setFt(Integer ft) {
        this.ft = ft;
    }
    
    /** Getter for property ff.
     * @return Value of property ff.
     *
     */
    public Integer getFf() {
        return this.ff;
    }
    
    /** Setter for property ff.
     * @param ff New value of property ff.
     *
     */
    public void setFf(Integer ff) {
        this.ff = ff;
    }
    
    /** Getter for property functions.
     * @return Value of property functions.
     *
     */
    public List getFunctions() {
        return this.functions;
    }
    
    /** Setter for property functions.
     * @param functions New value of property functions.
     *
     */
    public void setFunctions(List functions) {
        this.functions = functions;
    }
    
    public void addFunction(LabelValueBean b) {
        if (this.functions != null) {
            this.functions.add(b);
        }
    }
    
    
    /** Getter for property types.
     * @return Value of property types.
     *
     */
    public List getTypes() {
        return this.types;
    }
    
    /** Setter for property types.
     * @param types New value of property types.
     *
     */
    public void setTypes(List types) {
        this.types = types;
    }
    
    public void addType(LabelValueBean b) {
        if (this.types != null) {
            this.types.add(b);
        }
    }
    
// ******************** support methods ***********************************
    
    private void setDefaults() {
        ff = null;
        ft = new Integer(-1);
        functions = new ArrayList();        
        types = new ArrayList();
    }
}
