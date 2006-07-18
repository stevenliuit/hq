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

package org.hyperic.image.widget.test;

import org.hyperic.image.*;
import org.hyperic.image.widget.*;
import org.hyperic.util.data.*;
import org.hyperic.util.units.*;

import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.util.*;
import org.hyperic.util.pager.PageList;
import org.hyperic.util.TimeUtil;

/**
 * A basic JFC 1.1 based application.
 */
public class WidgetTest extends javax.swing.JFrame
{
    public WebImage m_image;
    
	public WidgetTest()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		setTitle("JFC Application");
		setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(972,490);
		setVisible(false);
		JPanel1.setLayout(null);
		getContentPane().add(BorderLayout.CENTER, JPanel1);
		JPanel1.setBackground(new java.awt.Color(226,226,226));
		JPanel1.setFont(new Font("SansSerif", Font.BOLD, 11));
		try {
			imageViewer.setStyle(symantec.itools.multimedia.ImageViewer.IMAGE_NORMAL);
		}
		catch(java.beans.PropertyVetoException e) { }
		JPanel1.add(imageViewer);
		imageViewer.setBounds(216,0,755,500);
		JPanel1.add(textFieldBorderR);
		textFieldBorderR.setBounds(108,120,26,20);
		label1.setText("Border Color:");
		JPanel1.add(label1);
		label1.setBounds(12,120,74,20);
		radioButtonLineChart.setCheckboxGroup(Group1);
		radioButtonLineChart.setState(true);
		radioButtonLineChart.setLabel("AvailabilityReport");
		JPanel1.add(radioButtonLineChart);
		radioButtonLineChart.setBounds(12,36,80,20);
		radioButtonColumnChart.setCheckboxGroup(Group1);
		radioButtonColumnChart.setLabel("ResourceTree");
		JPanel1.add(radioButtonColumnChart);
		radioButtonColumnChart.setBounds(110,36,100,20);
		label2.setText("Chart Type:");
		JPanel1.add(label2);
		label2.setBounds(12,12,100,20);
		label3.setText("Colors:");
		JPanel1.add(label3);
		label3.setBounds(12,96,60,20);
		label4.setText("Bkgnd Color:");
		JPanel1.add(label4);
		label4.setBounds(12,144,72,20);
		JPanel1.add(textFieldBorderG);
		textFieldBorderG.setBounds(144,120,26,20);
		JPanel1.add(textFieldBorderB);
		textFieldBorderB.setBounds(180,120,26,20);
		JPanel1.add(textFieldBkgndR);
		textFieldBkgndR.setBounds(108,144,26,20);
		JPanel1.add(textFieldBkgndG);
		textFieldBkgndG.setBounds(144,144,26,20);
		JPanel1.add(textFieldBkgndB);
		textFieldBkgndB.setBounds(180,144,26,20);
		label5.setText("Line Color:");
		JPanel1.add(label5);
		label5.setBounds(12,168,72,20);
		JPanel1.add(textFieldLineR);
		textFieldLineR.setBounds(108,168,26,20);
		JPanel1.add(textFieldLineG);
		textFieldLineG.setBounds(144,168,26,20);
		JPanel1.add(textFieldLineB);
		textFieldLineB.setBounds(180,168,26,20);
		label6.setText("Bar Color:");
		JPanel1.add(label6);
		label6.setBounds(12,192,72,20);
		JPanel1.add(textFieldBarR);
		textFieldBarR.setBounds(108,192,26,20);
		JPanel1.add(textFieldBarG);
		textFieldBarG.setBounds(144,192,26,20);
		JPanel1.add(textFieldBarB);
		textFieldBarB.setBounds(180,192,26,20);
		label7.setText("Sizes:");
		JPanel1.add(label7);
		label7.setBounds(12,324,100,20);
		label8.setText("Border Width:");
		JPanel1.add(label8);
		label8.setBounds(12,348,84,20);
		JPanel1.add(textFieldBorderWidth);
		textFieldBorderWidth.setBounds(120,348,26,20);
		label9.setText("Line Width:");
		JPanel1.add(label9);
		label9.setBounds(12,372,72,20);
		JPanel1.add(textFieldLineWidth);
		textFieldLineWidth.setBounds(120,372,26,20);
		label10.setText("Horizontal Lines:");
		JPanel1.add(label10);
		label10.setBounds(12,396,100,20);
		JPanel1.add(textFieldHorizLines);
		textFieldHorizLines.setBounds(120,396,26,20);
		label11.setText("Line Overhang:");
		JPanel1.add(label11);
		label11.setBounds(12,420,100,20);
		JPanel1.add(textFieldLineOverhang);
		textFieldLineOverhang.setBounds(120,420,26,20);
		label12.setText("Chart Color:");
		JPanel1.add(label12);
		label12.setBounds(12,216,72,20);
		JPanel1.add(textFieldChartR);
		textFieldChartR.setBounds(108,216,26,20);
		JPanel1.add(textFieldChartG);
		textFieldChartG.setBounds(144,216,26,20);
		JPanel1.add(textFieldChartB);
		textFieldChartB.setBounds(180,216,26,20);
		label13.setText("Avg Line Color:");
		JPanel1.add(label13);
		label13.setBounds(12,240,85,20);
		JPanel1.add(textFieldAvgLineR);
		textFieldAvgLineR.setBounds(108,240,26,20);
		JPanel1.add(textFieldAvgLineG);
		textFieldAvgLineG.setBounds(144,240,26,20);
		JPanel1.add(textFieldAvgLineB);
		textFieldAvgLineB.setBounds(180,240,26,20);
		label14.setText("Low Line Color:");
		JPanel1.add(label14);
		label14.setBounds(12,264,90,20);
		JPanel1.add(textFieldLowLineR);
		textFieldLowLineR.setBounds(108,264,26,20);
		JPanel1.add(textFieldLowLineG);
		textFieldLowLineG.setBounds(144,264,26,20);
		JPanel1.add(textFieldLowLineB);
		textFieldLowLineB.setBounds(180,264,26,20);
		label15.setText("Peak Line Color:");
		JPanel1.add(label15);
		label15.setBounds(12,288,90,20);
		JPanel1.add(textFieldPeakLineR);
		textFieldPeakLineR.setBounds(108,288,26,20);
		JPanel1.add(textFieldPeakLineG);
		textFieldPeakLineG.setBounds(144,288,26,20);
		JPanel1.add(textFieldPeakLineB);
		textFieldPeakLineB.setBounds(180,288,26,20);
		radioButtonColumnLineChart.setCheckboxGroup(Group1);
		radioButtonColumnLineChart.setLabel("Availability Chart");
		JPanel1.add(radioButtonColumnLineChart);
		radioButtonColumnLineChart.setBounds(12,60,130,20);
		buttonSave.setLabel("Save");
		JPanel1.add(buttonSave);
		buttonSave.setBackground(java.awt.Color.lightGray);
		buttonSave.setBounds(12,456,60,24);
		//}}

		//{{INIT_MENUS
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymItem lSymItem = new SymItem();
		radioButtonLineChart.addItemListener(lSymItem);
		radioButtonColumnChart.addItemListener(lSymItem);
		SymFocus aSymFocus = new SymFocus();
		textFieldBorderR.addFocusListener(aSymFocus);
		textFieldBorderG.addFocusListener(aSymFocus);
		textFieldBorderB.addFocusListener(aSymFocus);
		textFieldBkgndR.addFocusListener(aSymFocus);
		textFieldBkgndG.addFocusListener(aSymFocus);
		textFieldBkgndB.addFocusListener(aSymFocus);
		textFieldLineR.addFocusListener(aSymFocus);
		textFieldLineG.addFocusListener(aSymFocus);
		textFieldLineB.addFocusListener(aSymFocus);
		textFieldBarR.addFocusListener(aSymFocus);
		textFieldBarG.addFocusListener(aSymFocus);
		textFieldBarB.addFocusListener(aSymFocus);
		textFieldBorderWidth.addFocusListener(aSymFocus);
		textFieldHorizLines.addFocusListener(aSymFocus);
		textFieldLineOverhang.addFocusListener(aSymFocus);
		textFieldChartR.addFocusListener(aSymFocus);
		textFieldChartG.addFocusListener(aSymFocus);
		textFieldChartB.addFocusListener(aSymFocus);
		textFieldAvgLineR.addFocusListener(aSymFocus);
		textFieldAvgLineG.addFocusListener(aSymFocus);
		textFieldAvgLineB.addFocusListener(aSymFocus);
		textFieldLowLineR.addFocusListener(aSymFocus);
		textFieldLowLineG.addFocusListener(aSymFocus);
		textFieldLowLineB.addFocusListener(aSymFocus);
		textFieldPeakLineR.addFocusListener(aSymFocus);
		textFieldPeakLineG.addFocusListener(aSymFocus);
		textFieldPeakLineB.addFocusListener(aSymFocus);
		radioButtonColumnLineChart.addItemListener(lSymItem);
		SymAction lSymAction = new SymAction();
		buttonSave.addActionListener(lSymAction);
		//}}
	
    	this.createChart();

        Color color;
		//Color color = this.m_image.getBarColor();
		//this.textFieldBarR.setText(String.valueOf(color.getRed()));
		//this.textFieldBarG.setText(String.valueOf(color.getGreen()));
		//this.textFieldBarB.setText(String.valueOf(color.getBlue()));

		//color = this.m_image.getBarBorderColor();
		//this.textFieldBorderR.setText(String.valueOf(color.getRed()));
		//this.textFieldBorderG.setText(String.valueOf(color.getGreen()));
		//this.textFieldBorderB.setText(String.valueOf(color.getBlue()));

		color = this.m_image.backgroundColor;
		this.textFieldBkgndR.setText(String.valueOf(color.getRed()));
		this.textFieldBkgndG.setText(String.valueOf(color.getGreen()));
		this.textFieldBkgndB.setText(String.valueOf(color.getBlue()));

		//this.textFieldBorderHeight.Value = chart.BorderHeight;
		this.textFieldBorderWidth.setText(String.valueOf(this.m_image.leftBorder));

		//this.labelFontName.Text		  = this.m_image.Font.FontFamily.Name;
		//this.labelFontSize.Text		  = this.m_image.Font.SizeInPoints.ToString() + " pt.";
		//this.labelFontColor.BackColor = this.m_image.TextColor;
	}

    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     * @see #JFrame1()
     */
	public WidgetTest(String sTitle)
	{
		this();
		setTitle(sTitle);
	}
	
	/**
	 * The entry point for this application.
	 * Sets the Look and Feel to the System Look and Feel.
	 * Creates a new JFrame1 and makes it visible.
	 */
	static public void main(String args[])
	{
		try {
		    // Add the following code if you want the Look and Feel
		    // to be set to the Look and Feel of the native system.
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } 
		    catch (Exception e) { 
		    }

			//Create a new instance of our application's frame, and make it visible.
			(new WidgetTest()).setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}

	public void createChart()
	{
		if(this.radioButtonLineChart.getState() == true) {
            ResourceTree tree = new ResourceTree(700);
            this.m_image = tree;
            
            IResourceTreeNode[] apps = {
                new ResourceTreeNode("Online Store", "Application", false, ResourceTreeNode.RESOURCE)
            };
            
            ResourceTreeNode service = null;            
            IResourceTreeNode[] services = {
                //new ResourceTreeNode("WebLogic Admin 7.0 Webapp", "WebLogic Admin 7.0 Webapp Auto-group", ResourceTreeNode.RESOURCE),
                service = new ResourceTreeNode("This is a really long WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.RESOURCE),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER),
//                new ResourceTreeNode("WebLogic Admin 7.0 JMS Destination", "WebLogic Admin 7.0 JMS Destination", ResourceTreeNode.CLUSTER)
            };
//            
//            IResourceTreeNode[] child = {
//                new ResourceTreeNode("EJB1", "WebLogic 8.1 EJB", ResourceTreeNode.RESOURCE),
//                new ResourceTreeNode("EJB2", "WebLogic 8.1 EJB", ResourceTreeNode.RESOURCE),
//                new ResourceTreeNode("EJB3", "WebLogic 8.1 EJB", ResourceTreeNode.RESOURCE)                    
//            };
//            
//            //ejb.addDownChildren(child);

            ResourceTreeNode server = null;
            IResourceTreeNode[] servers = {
//                new ResourceTreeNode("Apache 2.0 Servers", "Apache 2.0 Server", ResourceTreeNode.AUTO_GROUP),
//                new ResourceTreeNode("WebLogic 8.1 Servers", "WebLogic 8.1 Server", ResourceTreeNode.AUTO_GROUP),
                server = new ResourceTreeNode("Oracle 9i Server", "Oracle 9i Database Server", true, ResourceTreeNode.RESOURCE)
            };
            
            ResourceTreeNode platform = null; 
            IResourceTreeNode[] platforms = {
                platform = new ResourceTreeNode("Linux Platforms", "Linux Platform", ResourceTreeNode.RESOURCE),   
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE)
            };

            //platform.addUpChildren(servers);
            server.addUpChildren(services);
            server.addDownChildren(platforms);
            ///service.addDownChildren(servers);
                        
            tree.addLevel(apps);
            tree.addLevel(services);
            tree.addLevel(servers);
            tree.addLevel(platforms);
        } else if(this.radioButtonColumnChart.getState() == true) {
            ResourceTree tree = new ResourceTree(700);
            this.m_image = tree;
            
            IResourceTreeNode[] apps = {
                new ResourceTreeNode("Online Store", "Application", false, ResourceTreeNode.RESOURCE),
                //new ResourceTreeNode("Credit Card Processing", "Application", false, ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Credit Card Processing", "Application", false, ResourceTreeNode.RESOURCE)
            };
            
            ResourceTreeNode ejb = new ResourceTreeNode("WebLogic 8.1 EJBs", "WebLogic 8.1 EJB Group", true, ResourceTreeNode.AUTO_GROUP);            

            IResourceTreeNode[] downChild = {
                new ResourceTreeNode("EJB1", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB2", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB3", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB4", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB5", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB6", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB7", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB8", "Foo", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("EJB9", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB10", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB11", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB12", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB13", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB14", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB15", "Foo", ResourceTreeNode.RESOURCE),                    
                new ResourceTreeNode("EJB16", "Foo", ResourceTreeNode.RESOURCE)                    
            };
            ejb.addDownChildren(downChild);

            IResourceTreeNode[] services = {
                ejb,
                //new ResourceTreeNode("Apache 2.0 VHosts", "Apache 2.0 VHost Group", ResourceTreeNode.CLUSTER),
                //new ResourceTreeNode("Apache 2.0 VHosts", "Apache 2.0 VHost Group", ResourceTreeNode.CLUSTER)
            };
            
            
            IResourceTreeNode[] servers = {
                new ResourceTreeNode("Apache 2.0 Servers", "Apache 2.0 Server", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("WebLogic 8.1 Servers", "WebLogic 8.1 Server", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("Oracle 9i Server", "Oracle 9i Database Server", ResourceTreeNode.RESOURCE)
            };
            
            IResourceTreeNode[] platforms = {
                new ResourceTreeNode("Linux Platforms", "Linux Platform", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE)
            };
            
            //tree.addLevel(apps);
            tree.addLevel(services);
            //tree.addLevel(servers);
            //tree.addLevel(platforms);
        } else if(this.radioButtonColumnLineChart.getState() == true) {
            ResourceTree tree = new ResourceTree(700);
            this.m_image = tree;
            
            IResourceTreeNode[] apps = {
                new ResourceTreeNode("gg", ResourceTreeNode.RESOURCE),
            };
            
            ResourceTreeNode service;
            IResourceTreeNode[] services = {
                service = new ResourceTreeNode("legolas CAM Agent service", "CAM Agent 1.0 Service", ResourceTreeNode.RESOURCE)
            };

            IResourceTreeNode[] upChild = {
                new ResourceTreeNode("Online Store", "Application", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("BankApp", "Application", ResourceTreeNode.RESOURCE)
            };
            service.addUpChildren(upChild);
            
            IResourceTreeNode[] downChild = {
                new ResourceTreeNode("Server1", "WebLogic 8.1 Server", ResourceTreeNode.RESOURCE),
                new ResourceTreeNode("Server2", "WebLogic 8.1 Server", ResourceTreeNode.RESOURCE),
            };
            service.addDownChildren(downChild);
            
            IResourceTreeNode[] servers = {
                new ResourceTreeNode("My very powerful Apache 2.0 Servers", "Apache 2.0 Server", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("My very powerful WebLogic 8.1 Servers", "WebLogic 8.1 Server", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("My very powerful WebLogic 8.1 Servers", "WebLogic 8.1 Server", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("My very powerful Oracle 9i Server", "Oracle 9i Database Server", ResourceTreeNode.RESOURCE)
            };
            
            IResourceTreeNode[] platforms = {
                new ResourceTreeNode("Linux Platforms", "Linux Platform", ResourceTreeNode.AUTO_GROUP),
                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE)
            };
            
            tree.addLevel(apps);
            tree.addLevel(services);
            tree.addLevel(servers);
            tree.addLevel(platforms);

//            ResourceTree tree = new ResourceTree(700, 350);
//            this.m_image = tree;
//            
//            IResourceTreeNode[] apps = {
//                new ResourceTreeNode("Online Store", "Application", ResourceTreeNode.RESOURCE)
//            };
//            
//            IResourceTreeNode[] services = {
//                new ResourceTreeNode("WebLogic 8.1 EJBs", "WebLogic 8.1 EJB Group", ResourceTreeNode.AUTO_GROUP),
//                new ResourceTreeNode("Apache 2.0 VHosts", "Apache 2.0 VHost Group", ResourceTreeNode.CLUSTER)
//            };
//            
//            IResourceTreeNode[] servers = {
//                new ResourceTreeNode("Apache 2.0 Servers", "Apache 2.0 Server", ResourceTreeNode.AUTO_GROUP),
//                new ResourceTreeNode("WebLogic 8.1 Servers", "WebLogic 8.1 Server", ResourceTreeNode.AUTO_GROUP),
//                new ResourceTreeNode("Oracle 9i Server", "Oracle 9i Database Server", ResourceTreeNode.RESOURCE)
//            };
//            
//            ResourceTreeNode linux = new ResourceTreeNode("Linux Platforms", "Linux Platform", true, ResourceTreeNode.AUTO_GROUP);
//            IResourceTreeNode[] platforms = {
//                linux,
//                new ResourceTreeNode("Solaris Platform", "Solaris Platform", ResourceTreeNode.RESOURCE)
//            };
//            IResourceTreeNode[] child = {
//                new ResourceTreeNode("heimdal", "Linux Platform", ResourceTreeNode.RESOURCE),
//                new ResourceTreeNode("panic", "Linux Platform", ResourceTreeNode.RESOURCE),
//            };
//            linux.addDownChildren(child);
//            
//            tree.addLevel(apps);
//            tree.addLevel(services);
//            tree.addLevel(servers);
//            tree.addLevel(platforms);

//            AvailabilityReport report = new AvailabilityReport();
//            this.m_image = report;
//                
//            report.Available   = 111;
//            report.Unavailable = 222;
//            report.Unknown     = 333;
        } else {
        }
                		
        try {
		    this.imageViewer.setImage(this.m_image.getImage());
		    this.imageViewer.repaint();
		} catch(Exception e) {
		    System.out.println(e);
		}
	}

    /**
     * Notifies this component that it has been added to a container
     * This method should be called by <code>Container.add</code>, and 
     * not by user code directly.
     * Overridden here to adjust the size of the frame if needed.
     * @see java.awt.Container#removeNotify
     */
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();
		
		super.addNotify();
		
		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;
		
		// Adjust size of frame according to the insets and menu bar
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
		    menuBarHeight = menuBar.getPreferredSize().height;
		Insets insets = getInsets();
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JPanel JPanel1 = new javax.swing.JPanel();
	symantec.itools.multimedia.ImageViewer imageViewer = new symantec.itools.multimedia.ImageViewer();
	java.awt.TextField textFieldBorderR = new java.awt.TextField();
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Checkbox radioButtonLineChart = new java.awt.Checkbox();
	java.awt.CheckboxGroup Group1 = new java.awt.CheckboxGroup();
	java.awt.Checkbox radioButtonColumnChart = new java.awt.Checkbox();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.Label label4 = new java.awt.Label();
	java.awt.TextField textFieldBorderG = new java.awt.TextField();
	java.awt.TextField textFieldBorderB = new java.awt.TextField();
	java.awt.TextField textFieldBkgndR = new java.awt.TextField();
	java.awt.TextField textFieldBkgndG = new java.awt.TextField();
	java.awt.TextField textFieldBkgndB = new java.awt.TextField();
	java.awt.Label label5 = new java.awt.Label();
	java.awt.TextField textFieldLineR = new java.awt.TextField();
	java.awt.TextField textFieldLineG = new java.awt.TextField();
	java.awt.TextField textFieldLineB = new java.awt.TextField();
	java.awt.Label label6 = new java.awt.Label();
	java.awt.TextField textFieldBarR = new java.awt.TextField();
	java.awt.TextField textFieldBarG = new java.awt.TextField();
	java.awt.TextField textFieldBarB = new java.awt.TextField();
	java.awt.Label label7 = new java.awt.Label();
	java.awt.Label label8 = new java.awt.Label();
	java.awt.TextField textFieldBorderWidth = new java.awt.TextField();
	java.awt.Label label9 = new java.awt.Label();
	java.awt.TextField textFieldLineWidth = new java.awt.TextField();
	java.awt.Label label10 = new java.awt.Label();
	java.awt.TextField textFieldHorizLines = new java.awt.TextField();
	java.awt.Label label11 = new java.awt.Label();
	java.awt.TextField textFieldLineOverhang = new java.awt.TextField();
	java.awt.Label label12 = new java.awt.Label();
	java.awt.TextField textFieldChartR = new java.awt.TextField();
	java.awt.TextField textFieldChartG = new java.awt.TextField();
	java.awt.TextField textFieldChartB = new java.awt.TextField();
	java.awt.Label label13 = new java.awt.Label();
	java.awt.TextField textFieldAvgLineR = new java.awt.TextField();
	java.awt.TextField textFieldAvgLineG = new java.awt.TextField();
	java.awt.TextField textFieldAvgLineB = new java.awt.TextField();
	java.awt.Label label14 = new java.awt.Label();
	java.awt.TextField textFieldLowLineR = new java.awt.TextField();
	java.awt.TextField textFieldLowLineG = new java.awt.TextField();
	java.awt.TextField textFieldLowLineB = new java.awt.TextField();
	java.awt.Label label15 = new java.awt.Label();
	java.awt.TextField textFieldPeakLineR = new java.awt.TextField();
	java.awt.TextField textFieldPeakLineG = new java.awt.TextField();
	java.awt.TextField textFieldPeakLineB = new java.awt.TextField();
	java.awt.Checkbox radioButtonColumnLineChart = new java.awt.Checkbox();
	java.awt.Button buttonSave = new java.awt.Button();
	//}}

	//{{DECLARE_MENUS
	//}}

	void exitApplication()
	{
    	this.setVisible(false);    // hide the Frame
    	this.dispose();            // free the system resources
    	System.exit(0);            // close the application
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == WidgetTest.this)
				JFrame1_windowClosing(event);
		}
	}

	void JFrame1_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
			 
		JFrame1_windowClosing_Interaction1(event);
	}

	void JFrame1_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
		try {
			this.exitApplication();
		} catch (Exception e) {
		}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == radioButtonLineChart)
				radioButtonChart_ItemStateChanged(event);
			else if (object == radioButtonColumnChart)
				radioButtonChart_ItemStateChanged(event);
			else if (object == radioButtonColumnLineChart)
				radioButtonColumnLineChart_ItemStateChanged(event);
		}
	}

	void radioButtonChart_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		this.createChart();
	}

	class SymFocus extends java.awt.event.FocusAdapter
	{
		public void focusLost(java.awt.event.FocusEvent event)
		{
			Object object = event.getSource();
			if (object == textFieldBorderR)
				textFieldBorder_FocusLost(event);
			else if (object == textFieldBorderG)
				textFieldBorder_FocusLost(event);
			else if (object == textFieldBorderB)
				textFieldBorder_FocusLost(event);
			else if (object == textFieldBkgndR)
				textFieldBkgnd_FocusLost(event);
			else if (object == textFieldBkgndG)
				textFieldBkgnd_FocusLost(event);
			else if (object == textFieldBkgndB)
				textFieldBkgnd_FocusLost(event);
			else if (object == textFieldLineR)
				textFieldLine_FocusLost(event);
			else if (object == textFieldLineG)
				textFieldLine_FocusLost(event);
			else if (object == textFieldLineB)
				textFieldLine_FocusLost(event);
			else if (object == textFieldBarR)
				textFieldBar_FocusLost(event);
			else if (object == textFieldBarG)
				textFieldBar_FocusLost(event);
			else if (object == textFieldBarB)
				textFieldBar_FocusLost(event);
			else if (object == textFieldBorderWidth)
				textFieldBorderWidth_FocusLost(event);
			else if (object == textFieldHorizLines)
				textFieldHorizLines_FocusLost(event);
			else if (object == textFieldLineOverhang)
				textFieldLineOverhang_FocusLost(event);
			else if (object == textFieldChartR)
				textFieldChart_FocusLost(event);
			else if (object == textFieldChartG)
				textFieldChart_FocusLost(event);
			else if (object == textFieldChartB)
				textFieldChart_FocusLost(event);
			else if (object == textFieldAvgLineR)
				textFieldAvgLine_FocusLost(event);
			else if (object == textFieldAvgLineG)
				textFieldAvgLine_FocusLost(event);
			else if (object == textFieldAvgLineB)
				textFieldAvgLine_FocusLost(event);
			else if (object == textFieldLowLineR)
				textFieldLowLine_FocusLost(event);
			else if (object == textFieldLowLineG)
				textFieldLowLine_FocusLost(event);
			else if (object == textFieldLowLineB)
				textFieldLowLine_FocusLost(event);
			else if (object == textFieldPeakLineR)
				textFieldPeakLine_FocusLost(event);
			else if (object == textFieldPeakLineG)
				textFieldPeakLine_FocusLost(event);
			else if (object == textFieldPeakLineB)
				textFieldPeakLine_FocusLost(event);
		}
	}

	void textFieldBorder_FocusLost(java.awt.event.FocusEvent event)
	{
	    /*
	    int R = Integer.valueOf(this.textFieldBorderR.getText()).intValue();
	    int G = Integer.valueOf(this.textFieldBorderG.getText()).intValue();
	    int B = Integer.valueOf(this.textFieldBorderB.getText()).intValue();
	    
	    Color clr = this.m_image.getBarBorderColor();
	    
	    if(R != clr.getRed() || G != clr.getGreen() || B != clr.getBlue())
	    {
    		this.m_image.setBarBorderColor(new Color(R, G, B));

	        try
	        {
		        this.imageViewer.setImage(this.m_image.getImage());
		    }
		    catch(Exception e)
		    {
		    }
		
		    this.imageViewer.repaint();
		}
		*/
	}

	void textFieldBkgnd_FocusLost(java.awt.event.FocusEvent event)
	{
	    int R = Integer.valueOf(this.textFieldBkgndR.getText()).intValue();
	    int G = Integer.valueOf(this.textFieldBkgndG.getText()).intValue();
	    int B = Integer.valueOf(this.textFieldBkgndB.getText()).intValue();
	    
	    Color clr = this.m_image.backgroundColor;
	    
	    if(R != clr.getRed() || G != clr.getGreen() || B != clr.getBlue())
	    {
    		this.m_image.backgroundColor = new Color(R, G, B);

	        try {
		        this.imageViewer.setImage(this.m_image.getImage());
		    } catch(Exception e) {}
		
		    this.imageViewer.repaint();
		}
	}

	void textFieldLine_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldBar_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldBorderWidth_FocusLost(java.awt.event.FocusEvent event)
	{
	    int iWidth = Integer.valueOf(this.textFieldBorderWidth.getText()).intValue();
        
        if(iWidth != this.m_image.leftBorder) {
            this.m_image.setBorder(iWidth);
            
            try {
                this.imageViewer.setImage(this.m_image.getImage());
            } catch(Exception e) {}
            
            this.imageViewer.repaint();
        }
	}

	void textFieldHorizLines_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldLineOverhang_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldChart_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldAvgLine_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldLowLine_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void textFieldPeakLine_FocusLost(java.awt.event.FocusEvent event)
	{
	}

	void radioButtonColumnLineChart_ItemStateChanged(java.awt.event.ItemEvent event)
	{
	    this.createChart();
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == buttonSave)
				buttonSave_ActionPerformed(event);
		}
	}

	void buttonSave_ActionPerformed(java.awt.event.ActionEvent event)
	{
	    try {
                String userDir = System.getProperty("user.dir");
                if (userDir.charAt(userDir.length() - 1) != '/') {
                    userDir += '/';
                }
                this.m_image.writeJpegImage(userDir + "test.jpg");
                this.m_image.writePngImage(userDir + "test.png");
                System.out.println("Wrote test images to " + userDir);
            } catch (Exception e) {
                System.out.println(e);
            }
	}
}
