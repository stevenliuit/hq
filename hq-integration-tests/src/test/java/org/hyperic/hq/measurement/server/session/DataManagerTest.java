/**
 * 
 */
package org.hyperic.hq.measurement.server.session;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hyperic.hq.appdef.server.session.Platform;
import org.hyperic.hq.appdef.shared.AIPlatformValue;
import org.hyperic.hq.appdef.shared.AgentManager;
import org.hyperic.hq.appdef.shared.PlatformManager;
import org.hyperic.hq.authz.server.session.AuthzSubject;
import org.hyperic.hq.authz.shared.AuthzSubjectManager;
import org.hyperic.hq.authz.shared.ResourceGroupManager;
import org.hyperic.hq.authz.shared.ResourceManager;
import org.hyperic.hq.bizapp.shared.AppdefBoss;
import org.hyperic.hq.common.ApplicationException;
import org.hyperic.hq.common.NotFoundException;
import org.hyperic.hq.context.IntegrationTestContextLoader;
import org.hyperic.hq.measurement.shared.AvailabilityManager;
import org.hyperic.hq.measurement.shared.DataManager;
import org.hyperic.hq.measurement.shared.MeasRangeObj;
import org.hyperic.hq.measurement.shared.MeasurementManager;
import org.hyperic.util.config.ConfigResponse;
import org.hyperic.util.jdbc.DBUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the {@link DataManagerImpl}
 * @author iperumal
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = IntegrationTestContextLoader.class, locations = { "classpath*:META-INF/spring/*-context.xml" })
@Transactional
public class DataManagerTest {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private DBUtil dbUtil;

    @Autowired
    private MetricDataCache metricDataCache;

    @Autowired
    private AgentManager agentManager;

    @Autowired
    private AppdefBoss appDefBoss;

    @Autowired
    private ResourceManager resourceManager;

    @Autowired
    private PlatformManager platformManager;

    @Autowired
    private AuthzSubjectManager authzSubjectManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MeasurementManager measurementManager;

    @Autowired
    private AvailabilityManager availabilityManager;

    @Autowired
    private ResourceGroupManager resourceGroupManager;

    private Platform testPlatform;

    private List<Measurement> measurements;

    private List<DataPoint> randomDataPoints;

    private List<DataPoint> measDataPoints;

    private List<DataPoint> availDataPoints;
    
    private List<DataPoint> pointsToClean;

    private void createTestPlatform() throws ApplicationException, NotFoundException {
        // Setup Agent
        String agentToken = "agentToken123";
        agentManager.createLegacyAgent("127.0.0.1", 2144, "authToken", agentToken, "5.0");
        // Create PlatformType
        String platformType = "Linux";
        platformManager.createPlatformType(platformType, "Test Plugin");
        // Create test platform
        AIPlatformValue aiPlatform = new AIPlatformValue();
        aiPlatform.setCpuCount(2);
        aiPlatform.setPlatformTypeName(platformType);
        aiPlatform.setAgentToken(agentToken);
        aiPlatform.setFqdn("Test Platform");
        platformManager.createPlatform(authzSubjectManager.getOverlordPojo(), aiPlatform);
        sessionFactory.getCurrentSession().flush();
    }

    private List<Measurement> createMeasurements() throws ApplicationException {
        AuthzSubject overlord = authzSubjectManager.getOverlordPojo();
        Platform platform = platformManager.findPlatformByFqdn(overlord, "Test Platform");
        int appDefType = platform.getResource().getResourceType().getAppdefType();
        MonitorableType monitor_Type = new MonitorableType("Platform monitor", appDefType, "test");
        Category cate = new Category("Test Category");
        sessionFactory.getCurrentSession().save(monitor_Type);
        sessionFactory.getCurrentSession().save(cate);
        MeasurementTemplate availTempl = new MeasurementTemplate("AvailabilityTemplate", "avail",
            "percentage", 1, true, 60000l, true, "Availability:avail", monitor_Type, cate, "test");
        MeasurementTemplate metric1Templ = new MeasurementTemplate("Metric1Template", "metric1",
            "percentage", 1, true, 60000l, true, "Metric1:metric1", monitor_Type, cate, "test");
        MeasurementTemplate metric2Templ = new MeasurementTemplate("Metric2Template", "metric2",
            "percentage", 1, true, 60000l, true, "Metric2:metric2", monitor_Type, cate, "test");
        MeasurementTemplate metric3Templ = new MeasurementTemplate("Metric3Template", "metric3",
            "percentage", 1, true, 60000l, true, "Metric3:metric3", monitor_Type, cate, "test");
        MeasurementTemplate metric4Templ = new MeasurementTemplate("Metric4Template", "metric4",
            "percentage", 1, true, 60000l, true, "Metric4:metric4", monitor_Type, cate, "test");
        MeasurementTemplate metric5Templ = new MeasurementTemplate("Metric5Template", "metric5",
            "percentage", 1, true, 60000l, true, "Metric5:metric5", monitor_Type, cate, "test");
        sessionFactory.getCurrentSession().save(availTempl);
        sessionFactory.getCurrentSession().save(metric1Templ);
        sessionFactory.getCurrentSession().save(metric2Templ);
        sessionFactory.getCurrentSession().save(metric3Templ);
        sessionFactory.getCurrentSession().save(metric4Templ);
        sessionFactory.getCurrentSession().save(metric5Templ);
        Integer[] templateIds = new Integer[] { availTempl.getId(),
                                               metric1Templ.getId(),
                                               metric2Templ.getId(),
                                               metric3Templ.getId(),
                                               metric4Templ.getId(),
                                               metric5Templ.getId() };
        long[] intervals = new long[] { 60000l, 60000l, 60000l, 60000l, 60000l, 60000l };
        List<Measurement> meas = measurementManager.createMeasurements(platform.getEntityId(),
            templateIds, intervals, new ConfigResponse());
        // Add availability data
        // availabilityManager.addData(meas.get(0).getId(), new MetricValue(new
        // Double(1), System
        // .currentTimeMillis()));
        return meas;
    }

    private List<DataPoint> createRandomDataPoints() {
        int metricId;
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        // Create some data points with predefined metric values
        for (int i = 1200; i < 1300; i++) {
            metricId = i;
            dataPoints.add(new DataPoint(metricId, i % 10, System.currentTimeMillis()));
        }
        return dataPoints;
    }

    private List<DataPoint> createRealisticDataPoints() {
        int metricId, i = 0;
        List<DataPoint> dataPoints = new ArrayList<DataPoint>(measurements.size());
        // Create data points with the given measurement ids
        for (Measurement meas : measurements) {
            metricId = meas.getId();
            // metricId = metric value
            dataPoints.add(new DataPoint(metricId, ++i, System.currentTimeMillis()));
        }
        return dataPoints;
    }

    @Before
    public void initializeTestData() throws ApplicationException, NotFoundException {
        createTestPlatform();
        measurements = createMeasurements();
         randomDataPoints = createRandomDataPoints();
        measDataPoints = createRealisticDataPoints();
    }

    // Cleanup the datapoints committed in a separate transaction by DataManagerImpl
    @AfterTransaction
    public void cleanupDataPoints() throws SQLException {
        if(this.pointsToClean == null) {
            return;
        }
        Connection conn = null;
        boolean succeeded = false;
        try {
            conn = dbUtil.getConnection();
        } catch (SQLException e) {
            fail("Failed to retrieve connection from data source");
        }
        try {
            boolean autocommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);
                succeeded = performDeletion(conn, pointsToClean);
                if (succeeded) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(autocommit);
            }
        } finally {
            DBUtil.closeConnection(DataManagerTest.class.getName(), conn);
        }
        
    }

    private boolean performDeletion(Connection conn, List<DataPoint> data) {
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, List<DataPoint>> buckets = MeasRangeObj.getInstance().bucketData(data);
        try {
            for (Iterator<Map.Entry<String, List<DataPoint>>> it = buckets.entrySet().iterator(); it
                .hasNext();) {
                Map.Entry<String, List<DataPoint>> entry = it.next();
                String table = entry.getKey();
                List<DataPoint> dpts = entry.getValue();

                StringBuilder values = new StringBuilder();
                int rowsToUpdate = 0;
                for (Iterator<DataPoint> i = dpts.iterator(); i.hasNext();) {
                    DataPoint pt = i.next();
                    Integer metricId = pt.getMetricId();
                    rowsToUpdate++;
                    values.append(metricId.intValue()).append(",");
                }
                String sql = "delete from " + table + " where measurement_id in (" +
                             values.substring(0, values.length() - 1) + " )";

                stmt = conn.createStatement();
                int rows = stmt.executeUpdate(sql);
                if (rows < rowsToUpdate)
                    return false;
            }
        } catch (SQLException e) {
            fail("Transaction failed while performing deletion of data points");
            return false;
        } finally {
            DBUtil.closeJDBCObjects(DataManagerTest.class.getName(), null, stmt, rs);
        }
        return true;
    }

    private void cleanupMetricDataCache(List<DataPoint> dataPoints) {
        for (DataPoint dpts : dataPoints) {
            metricDataCache.remove(dpts.getMetricId());
        }
    }

    /**
     * Test method for
     * {@link org.hyperic.hq.measurement.shared.DataManager#addData(java.lang.Integer, org.hyperic.hq.product.MetricValue, boolean)}
     * .
     * 
     @Test public final void testAddDataIntegerMetricValueBoolean() {
     *       fail("Not yet implemented"); // TODO }/
     */

    /**
     * Test method for
     * {@link org.hyperic.hq.measurement.shared.DataManager#addData(java.util.List)}
     * .
     */
    @Test
    public final void testAddDataListOfDataPoint() {
        // assertTrue(dataManager.addData(randomDataPoints));
    }

    /*
     * Verify the MetricDataCache update
     */
    @Test
    public final void testVerifyMetricDataCacheUpdate() {
        assertTrue(dataManager.addData(randomDataPoints));
        Integer metricId;
        Long timeStamp;
        for (DataPoint dp : randomDataPoints) {
            metricId = dp.getMetricId();
            timeStamp = dp.getTimestamp();
            // Compare the metricDataCache with the test data points
            assertTrue(metricDataCache.get(metricId, timeStamp).equals(dp.getMetricValue()));
        }
        this.pointsToClean = randomDataPoints;
        // Cleanup metricData ehcache to avoid conflict in further usecases
        cleanupMetricDataCache(randomDataPoints);
    }

    /**
     * Adding EmptyList
     */
    @Test
    public final void testAddDataListOfEmptyDataPoint() {
        // Create an Empty List<DataPoint>
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        assertTrue(dataManager.addData(dataPoints));
    }

//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#addData(java.util.List, boolean)}
//     * .
//     * 
//     @Test public final void testAddDataListOfDataPointBoolean() {
//     *       fail("Not yet implemented"); // TODO }/
//     * 
//     *       /** Test method for
//     *       {@link org.hyperic.hq.measurement.shared.DataManager#getHistoricalData(org.hyperic.hq.measurement.server.session.Measurement, long, long, org.hyperic.util.pager.PageControl, boolean)}
//     *       .
//     */
//    @Test
//    public final void testGetHistoricalDataMeasurementLongLongPageControlBoolean() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getHistoricalData(org.hyperic.hq.measurement.server.session.Measurement, long, long, org.hyperic.util.pager.PageControl)}
//     * .
//     */
//    @Test
//    public final void testGetHistoricalDataMeasurementLongLongPageControl() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getAggregateData(java.util.List, long, long)}
//     * .
//     */
//    @Test
//    public final void testGetAggregateData() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getAggregateDataByTemplate(java.util.List, long, long)}
//     * .
//     */
//    @Test
//    public final void testGetAggregateDataByTemplate() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getHistoricalData(java.util.List, long, long, long, int, boolean, org.hyperic.util.pager.PageControl)}
//     * .
//     */
//    @Test
//    public final void testGetHistoricalDataListOfMeasurementLongLongLongIntBooleanPageControl() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getLastHistoricalData(org.hyperic.hq.measurement.server.session.Measurement)}
//     * .
//     */
//    @Test
//    public final void testGetLastHistoricalData() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getLastDataPoints(java.util.List, long)}
//     * .
//     */
//    @Test
//    public final void testGetLastDataPoints() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getCachedDataPoints(java.lang.Integer[], java.util.Map, long)}
//     * .
//     */
//    @Test
//    public final void testGetCachedDataPoints() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getBaselineData(org.hyperic.hq.measurement.server.session.Measurement, long, long)}
//     * .
//     */
//    @Test
//    public final void testGetBaselineData() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getAggregateDataByMetric(java.lang.Integer[], java.lang.Integer[], long, long, boolean)}
//     * .
//     */
//    @Test
//    public final void testGetAggregateDataByMetricIntegerArrayIntegerArrayLongLongBoolean() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    /**
//     * Test method for
//     * {@link org.hyperic.hq.measurement.shared.DataManager#getAggregateDataByMetric(java.util.List, long, long, boolean)}
//     * .
//     */
//    @Test
//    public final void testGetAggregateDataByMetricListOfMeasurementLongLongBoolean() {
//        fail("Not yet implemented"); // TODO
//    }

}
