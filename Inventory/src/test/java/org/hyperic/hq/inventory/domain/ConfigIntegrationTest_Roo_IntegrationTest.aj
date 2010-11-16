// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hyperic.hq.inventory.domain;

import org.hyperic.hq.inventory.domain.ConfigDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ConfigIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ConfigIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ConfigIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: ConfigIntegrationTest: @Transactional;
    
    @Autowired
    private ConfigDataOnDemand ConfigIntegrationTest.dod;
    
    @Test
    public void ConfigIntegrationTest.testCountConfigs() {
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", dod.getRandomConfig());
        long count = org.hyperic.hq.inventory.domain.Config.countConfigs();
        org.junit.Assert.assertTrue("Counter for 'Config' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ConfigIntegrationTest.testFindConfig() {
        org.hyperic.hq.inventory.domain.Config obj = dod.getRandomConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to provide an identifier", id);
        obj = org.hyperic.hq.inventory.domain.Config.findConfig(id);
        org.junit.Assert.assertNotNull("Find method for 'Config' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Config' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ConfigIntegrationTest.testFindAllConfigs() {
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", dod.getRandomConfig());
        long count = org.hyperic.hq.inventory.domain.Config.countConfigs();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Config', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<org.hyperic.hq.inventory.domain.Config> result = org.hyperic.hq.inventory.domain.Config.findAllConfigs();
        org.junit.Assert.assertNotNull("Find all method for 'Config' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Config' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ConfigIntegrationTest.testFindConfigEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", dod.getRandomConfig());
        long count = org.hyperic.hq.inventory.domain.Config.countConfigs();
        if (count > 20) count = 20;
        java.util.List<org.hyperic.hq.inventory.domain.Config> result = org.hyperic.hq.inventory.domain.Config.findConfigEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Config' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Config' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ConfigIntegrationTest.testFlush() {
        org.hyperic.hq.inventory.domain.Config obj = dod.getRandomConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to provide an identifier", id);
        obj = org.hyperic.hq.inventory.domain.Config.findConfig(id);
        org.junit.Assert.assertNotNull("Find method for 'Config' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyConfig(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Config' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ConfigIntegrationTest.testMerge() {
        org.hyperic.hq.inventory.domain.Config obj = dod.getRandomConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to provide an identifier", id);
        obj = org.hyperic.hq.inventory.domain.Config.findConfig(id);
        boolean modified =  dod.modifyConfig(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        org.hyperic.hq.inventory.domain.Config merged = (org.hyperic.hq.inventory.domain.Config) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Config' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ConfigIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", dod.getRandomConfig());
        org.hyperic.hq.inventory.domain.Config obj = dod.getNewTransientConfig(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Config' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'Config' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ConfigIntegrationTest.testRemove() {
        org.hyperic.hq.inventory.domain.Config obj = dod.getRandomConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Config' failed to provide an identifier", id);
        obj = org.hyperic.hq.inventory.domain.Config.findConfig(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Config' with identifier '" + id + "'", org.hyperic.hq.inventory.domain.Config.findConfig(id));
    }
    
}