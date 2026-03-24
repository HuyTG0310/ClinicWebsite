/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.*;
import model.LabTest;
import util.DBContext;
import java.util.*;
import model.*;

/**
 *
 * @author huytr
 */
public class LabTestDAO extends DBContext {

    public void loadTestsForBatch(LabTestBatch batch) {

        String sql = "SELECT t.LabTestId, t.TestName "
                + "FROM LabOrderTest lot "
                + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
                + "WHERE lot.BatchId = ? "
                + "AND lot.Status != 'CANCELLED' "
                + "AND lot.Status != 'REJECTED'";

        List<Integer> testIds = new ArrayList<>();
        List<String> testNames = new ArrayList<>();

        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, batch.getBatchId());

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    testIds.add(rs.getInt("LabTestId"));
                    testNames.add(rs.getString("TestName"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        batch.setTestIds(testIds);
        batch.setTestNames(testNames);
    }

    private static final String SQL_GET_SNAPSHOT
            = "SELECT TOP 1 rr.RefMin, rr.RefMax, p.Unit "
            + "FROM LabTestParameter p "
            + "LEFT JOIN LabReferenceRange rr ON p.ParameterId = rr.ParameterId AND rr.IsActive = 1 "
            + "JOIN MedicalRecord mr ON mr.MedicalRecordId = ? "
            + "JOIN Patient pat ON mr.PatientId = pat.PatientId "
            + "WHERE p.ParameterId = ? "
            + "AND ("
            + "     rr.Gender IS NULL "
            + "     OR rr.Gender = 'ALL' "
            + "     OR (rr.Gender = 'M' AND pat.Gender IN ('Nam', 'Male', 'M')) "
            + "     OR (rr.Gender = 'F' AND pat.Gender IN ('Nữ', 'Nu', 'Female', 'F')) "
            + ") "
            + "AND (rr.AgeMinDays IS NULL OR DATEDIFF(day, pat.DateOfBirth, GETDATE()) BETWEEN rr.AgeMinDays AND rr.AgeMaxDays) "
            + "ORDER BY CASE WHEN rr.Gender = 'ALL' THEN 2 ELSE 1 END ASC";

    private static final String SQL_CHECK_RESULT
            = "SELECT ResultId FROM LabResult WHERE LabOrderTestId = ? AND ParameterId = ?";

    private static final String SQL_INSERT_RESULT
            = "INSERT INTO LabResult (LabOrderTestId, ParameterId, ResultValue, Flag, RefMin, RefMax, Unit, ResultTime) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";

    private static final String SQL_UPDATE_RESULT
            = "UPDATE LabResult SET ResultValue = ?, Flag = ?, RefMin = ?, RefMax = ?, Unit = ?, ResultTime = GETDATE() "
            + "WHERE ResultId = ?";

    private static final String SQL_UPDATE_ORDER
            = "UPDATE LabOrderTest SET Status = 'COMPLETED' WHERE LabOrderTestId = ?";

    private static final String SQL_UPDATE_BATCH
            = "UPDATE LabTestBatch SET Status = 'COMPLETED', TechnicianId = ? "
            + "WHERE BatchId IN (SELECT BatchId FROM LabOrderTest WHERE LabOrderTestId = ?) "
            + "AND NOT EXISTS ("
            + "    SELECT 1 FROM LabOrderTest lot2 "
            + "    WHERE lot2.BatchId = (SELECT BatchId FROM LabOrderTest WHERE LabOrderTestId = ?) "
            + "    AND lot2.Status != 'COMPLETED' "
            + "    AND lot2.Status != 'CANCELLED'"
            + ")";

    public boolean saveLabResults(TestResult testResult) {
        Connection conn = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            processSaveResults(conn, testResult);

            conn.commit();
            return true;

        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
            return false;
        } finally {
            close(conn);
        }
    }

    private void processSaveResults(Connection conn, TestResult testResult) throws Exception {

        PreparedStatement stGetSnapshot = conn.prepareStatement(SQL_GET_SNAPSHOT);
        PreparedStatement stCheck = conn.prepareStatement(SQL_CHECK_RESULT);
        PreparedStatement stInsert = conn.prepareStatement(SQL_INSERT_RESULT);
        PreparedStatement stUpdate = conn.prepareStatement(SQL_UPDATE_RESULT);
        PreparedStatement stUpdateOrder = conn.prepareStatement(SQL_UPDATE_ORDER);
        PreparedStatement stUpdateBatch = conn.prepareStatement(SQL_UPDATE_BATCH);

        boolean hasInsert = false;
        boolean hasUpdate = false;
        boolean hasStatus = false;

        String[] orderTestIds = testResult.getOrderTestIds();
        String[] paramIds = testResult.getParamIds();

        for (int i = 0; i < orderTestIds.length; i++) {

            String orderTestId = orderTestIds[i];
            String paramId = paramIds[i];

            String value = getResultValue(testResult, orderTestId, paramId);
            if (isEmpty(value)) {
                continue;
            }

            Snapshot snap = getSnapshot(stGetSnapshot, testResult, paramId);

            String flag = calculateFlag(value, snap, testResult, orderTestId, paramId);

            Integer resultId = checkExistingResult(stCheck, orderTestId, paramId);

            if (resultId == null) {
                insertResult(stInsert, orderTestId, paramId, value, flag, snap);
                hasInsert = true;
            } else {
                updateResult(stUpdate, resultId, value, flag, snap);
                hasUpdate = true;
            }

            updateStatus(stUpdateOrder, stUpdateBatch, testResult, orderTestId);
            hasStatus = true;
        }

        if (hasInsert) {
            stInsert.executeBatch();
        }
        if (hasUpdate) {
            stUpdate.executeBatch();
        }
        if (hasStatus) {
            stUpdateOrder.executeBatch();
            stUpdateBatch.executeBatch();
        }
    }

    private String getResultValue(TestResult testResult, String orderTestId, String paramId) {
        String[] values = testResult.getParameterMap()
                .get("result_" + orderTestId + "_" + paramId);

        return (values != null && values.length > 0) ? values[0].trim() : "";
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Snapshot getSnapshot(PreparedStatement st, TestResult testResult, String paramId) throws Exception {
        st.setInt(1, testResult.getMedicalRecordId());
        st.setInt(2, Integer.parseInt(paramId));

        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return new Snapshot(
                        rs.getBigDecimal("RefMin"),
                        rs.getBigDecimal("RefMax"),
                        rs.getString("Unit")
                );
            }
        }
        return new Snapshot(null, null, null);
    }

    private String calculateFlag(String value, Snapshot s,
            TestResult testResult, String orderTestId, String paramId) {

        try {
            double val = Double.parseDouble(value.replace(",", ".").trim());

            if (s.refMin != null && val < s.refMin.doubleValue()) {
                return "L";
            }
            if (s.refMax != null && val > s.refMax.doubleValue()) {
                return "H";
            }

        } catch (Exception e) {
        }

        String[] flags = testResult.getParameterMap()
                .get("flag_" + orderTestId + "_" + paramId);

        if (flags != null && flags.length > 0) {
            String f = flags[0].toLowerCase();
            if (f.equals("on") || f.equals("true") || f.equals("1")) {
                return "Y";
            }
        }

        return null;
    }

    private Integer checkExistingResult(PreparedStatement st, String orderTestId, String paramId) throws Exception {
        st.setInt(1, Integer.parseInt(orderTestId));
        st.setInt(2, Integer.parseInt(paramId));

        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ResultId");
            }
        }
        return null;
    }

    private void insertResult(PreparedStatement st, String orderTestId, String paramId,
            String value, String flag, Snapshot s) throws Exception {

        st.setInt(1, Integer.parseInt(orderTestId));
        st.setInt(2, Integer.parseInt(paramId));
        st.setString(3, value);
        st.setString(4, flag);
        st.setBigDecimal(5, s.refMin);
        st.setBigDecimal(6, s.refMax);
        st.setString(7, s.unit);

        st.addBatch();
    }

    private void updateResult(PreparedStatement st, int id,
            String value, String flag, Snapshot s) throws Exception {

        st.setString(1, value);
        st.setString(2, flag);
        st.setBigDecimal(3, s.refMin);
        st.setBigDecimal(4, s.refMax);
        st.setString(5, s.unit);
        st.setInt(6, id);

        st.addBatch();
    }

    private void updateStatus(PreparedStatement stOrder, PreparedStatement stBatch,
            TestResult testResult, String orderTestId) throws Exception {

        stOrder.setInt(1, Integer.parseInt(orderTestId));
        stOrder.addBatch();

        stBatch.setInt(1, testResult.getTechnicianId());
        stBatch.setInt(2, Integer.parseInt(orderTestId));
        stBatch.setInt(3, Integer.parseInt(orderTestId));
        stBatch.addBatch();
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (Exception e) {
        }
    }

    private void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }

    class Snapshot {

        BigDecimal refMin;
        BigDecimal refMax;
        String unit;

        public Snapshot(BigDecimal min, BigDecimal max, String unit) {
            this.refMin = min;
            this.refMax = max;
            this.unit = unit;
        }
    }

    public boolean insertFullLabTest(Service service, LabTest labTest, List<LabTestParameter> parameters) {
        Connection conn = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            int serviceId = insertService(conn, service);
            int labTestId = insertLabTest(conn, serviceId, labTest);

            insertParametersWithRanges(conn, labTestId, parameters);

            conn.commit();
            return true;

        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
            return false;
        } finally {
            close(conn);
        }
    }

    private int insertService(Connection conn, Service service) throws Exception {
        String sql = "INSERT INTO Service (ServiceName, Category, CurrentPrice, IsActive) VALUES (?, ?, ?, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, service.getServiceName());
            ps.setString(2, "Xét nghiệm");
            ps.setBigDecimal(3, service.getCurrentPrice());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new RuntimeException("Insert Service failed");
    }

    private int insertLabTest(Connection conn, int serviceId, LabTest labTest) throws Exception {
        String sql = "INSERT INTO LabTest (ServiceId, TestCode, TestName, CategoryId, IsPanel, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";

        int nextSortOrder = getNextSortOrder(conn, labTest.getCategoryId());

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, serviceId);
            ps.setString(2, labTest.getTestCode());
            ps.setString(3, labTest.getTestName());
            ps.setInt(4, labTest.getCategoryId());
            ps.setBoolean(5, labTest.isIsPanel());
            ps.setInt(6, nextSortOrder);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new RuntimeException("Insert LabTest failed");
    }

    private int getNextSortOrder(Connection conn, int categoryId) throws Exception {
        String sql = "SELECT ISNULL(MAX(SortOrder), 0) + 1 AS NextOrder FROM LabTest WHERE CategoryId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("NextOrder");
                }
            }
        }
        return 1; // Mặc định trả về 1 nếu bảng đang trống
    }

    private void insertParametersWithRanges(Connection conn, int labTestId, List<LabTestParameter> params) throws Exception {

        String sqlParam = "INSERT INTO LabTestParameter (LabTestId, ParameterCode, ParameterName, Unit, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlRange = "INSERT INTO LabReferenceRange (ParameterId, Gender, AgeMinDays, AgeMaxDays, RefMin, RefMax, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (
                PreparedStatement psParam = conn.prepareStatement(sqlParam, Statement.RETURN_GENERATED_KEYS); PreparedStatement psRange = conn.prepareStatement(sqlRange)) {

            int sortOrder = 1;

            for (LabTestParameter p : params) {

                int paramId = insertParameter(psParam, labTestId, p, sortOrder++);

                insertRanges(psRange, paramId, p.getReferenceRanges());
            }

            psRange.executeBatch();
        }
    }

    private int insertParameter(PreparedStatement ps, int labTestId, LabTestParameter p, int sortOrder) throws Exception {

        ps.setInt(1, labTestId);
        ps.setString(2, p.getParameterCode());
        ps.setString(3, p.getParameterName());
        ps.setString(4, p.getUnit());
        ps.setInt(5, sortOrder);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        throw new RuntimeException("Insert Parameter failed");
    }

    private void insertRanges(PreparedStatement ps, int paramId, List<LabReferenceRange> ranges) throws Exception {

        if (ranges == null || ranges.isEmpty()) {
            return;
        }

        for (LabReferenceRange r : ranges) {

            ps.setInt(1, paramId);
            ps.setString(2, r.getGender());
            ps.setInt(3, r.getAgeMinDays());
            ps.setInt(4, r.getAgeMaxDays());

            if (r.getRefMin() != null) {
                ps.setDouble(5, r.getRefMin());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }

            if (r.getRefMax() != null) {
                ps.setDouble(6, r.getRefMax());
            } else {
                ps.setNull(6, Types.DECIMAL);
            }

            ps.addBatch();
        }
    }

//    public boolean createLabOrders(int patientId, int medicalRecordId, int doctorId, String[] labTestIds) {
//
//        if (labTestIds == null || labTestIds.length == 0) {
//            return false;
//        }
//
//        Connection conn = null;
//
//        try {
//            conn = new DBContext().conn;
//            conn.setAutoCommit(false);
//
//            int batchId = createBatch(conn, patientId, medicalRecordId, doctorId);
//
//            for (String idStr : labTestIds) {
//                int labTestId = Integer.parseInt(idStr);
//
//                processSingleLabOrder(conn, batchId, patientId, medicalRecordId, doctorId, labTestId);
//            }
//
//            conn.commit();
//            return true;
//
//        } catch (Exception e) {
//            rollback(conn);
//            e.printStackTrace();
//            return false;
//        } finally {
//            close(conn);
//        }
//    }
    public boolean createLabOrders(model.LabTestBatch batchOrder) {

        // Rút danh sách ID ra bằng getTestIds()
        java.util.List<Integer> testIds = batchOrder.getTestIds();

        if (testIds == null || testIds.isEmpty()) {
            return false;
        }

        java.sql.Connection conn = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            // Gọi các hàm Get từ Model
            int batchId = createBatch(conn, batchOrder.getPatientId(), batchOrder.getMedicalRecordId(), batchOrder.getCreatedByDoctorId());

            // Duyệt vòng lặp
            for (Integer labTestId : testIds) {
                processSingleLabOrder(conn, batchId, batchOrder.getPatientId(), batchOrder.getMedicalRecordId(), batchOrder.getCreatedByDoctorId(), labTestId);
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
            return false;
        } finally {
            close(conn);
        }
    }

    private int createBatch(Connection conn, int patientId, int medicalRecordId, int doctorId) throws Exception {

        String sql = "INSERT INTO LabTestBatch (PatientId, MedicalRecordId, CreatedByDoctorId, Status) VALUES (?, ?, ?, 'CREATED')";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, patientId);
            ps.setInt(2, medicalRecordId);
            ps.setInt(3, doctorId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new RuntimeException("Create batch failed");
    }

    private void processSingleLabOrder(Connection conn,
            int batchId,
            int patientId,
            int medicalRecordId,
            int doctorId,
            int labTestId) throws Exception {

        PriceInfo priceInfo = getPriceInfo(conn, labTestId);

        int orderId = createServiceOrder(conn, patientId, medicalRecordId, doctorId,
                priceInfo.serviceId, priceInfo.price);

        insertLabOrderTest(conn, orderId, labTestId, batchId);
    }

    private PriceInfo getPriceInfo(Connection conn, int labTestId) throws Exception {

        String sql = "SELECT t.ServiceId, s.CurrentPrice "
                + "FROM LabTest t "
                + "JOIN Service s ON t.ServiceId = s.ServiceId "
                + "WHERE t.LabTestId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, labTestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PriceInfo(
                            rs.getInt("ServiceId"),
                            rs.getDouble("CurrentPrice")
                    );
                }
            }
        }

        throw new RuntimeException("Price not found for LabTestId = " + labTestId);
    }

    private int createServiceOrder(Connection conn,
            int patientId,
            int medicalRecordId,
            int doctorId,
            int serviceId,
            double price) throws Exception {

        String sql = "INSERT INTO ServiceOrder (PatientId, MedicalRecordId, ServiceId, AssignedById, PriceAtTime, Status) "
                + "VALUES (?, ?, ?, ?, ?, 'UNPAID')";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, patientId);
            ps.setInt(2, medicalRecordId);
            ps.setInt(3, serviceId);
            ps.setInt(4, doctorId);
            ps.setDouble(5, price);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new RuntimeException("Create ServiceOrder failed");
    }

    private void insertLabOrderTest(Connection conn, int orderId, int labTestId, int batchId) throws Exception {

        String sql = "INSERT INTO LabOrderTest (ServiceOrderId, LabTestId, BatchId, Status) "
                + "VALUES (?, ?, ?, 'ORDERED')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, labTestId);
            ps.setInt(3, batchId);

            ps.executeUpdate();
        }
    }

    class PriceInfo {

        int serviceId;
        double price;

        public PriceInfo(int serviceId, double price) {
            this.serviceId = serviceId;
            this.price = price;
        }
    }

    public boolean updateFullLabTest(Service service, LabTest labTest, List<LabTestParameter> parameters) {

        Connection conn = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            updateService(conn, service);
            updateLabTest(conn, labTest);

            softDeleteRemovedParams(conn, labTest.getLabTestId(), parameters);

            processParametersUpdate(conn, labTest.getLabTestId(), parameters);

            conn.commit();
            return true;

        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
            return false;
        } finally {
            close(conn);
        }
    }

    private void updateService(Connection conn, Service service) throws Exception {

        String sql = "UPDATE Service SET ServiceName = ?, CurrentPrice = ?, IsActive = ? WHERE ServiceId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setBigDecimal(2, service.getCurrentPrice());
            ps.setBoolean(3, service.isIsActive());
            ps.setInt(4, service.getServiceId());

            ps.executeUpdate();
        }
    }

    private void updateLabTest(Connection conn, LabTest labTest) throws Exception {

        String sql = "UPDATE LabTest SET TestCode = ?, TestName = ?, CategoryId = ?, IsPanel = ?, IsActive = ? WHERE LabTestId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, labTest.getTestCode());
            ps.setString(2, labTest.getTestName());
            ps.setInt(3, labTest.getCategoryId());
            ps.setBoolean(4, labTest.isIsPanel());
            ps.setBoolean(5, labTest.isIsActive());
            ps.setInt(6, labTest.getLabTestId());

            ps.executeUpdate();
        }
    }

    private void softDeleteRemovedParams(Connection conn, int labTestId, List<LabTestParameter> parameters) throws Exception {

        StringBuilder keptIds = new StringBuilder("0");

        for (LabTestParameter p : parameters) {
            if (p.getParameterId() > 0) {
                keptIds.append(",").append(p.getParameterId());
            }
        }

        String sql = "UPDATE LabTestParameter SET IsActive = 0 WHERE LabTestId = ? AND ParameterId NOT IN (" + keptIds + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, labTestId);
            ps.executeUpdate();
        }
    }

    private void processParametersUpdate(Connection conn, int labTestId, List<LabTestParameter> parameters) throws Exception {

        String sqlUpdate = "UPDATE LabTestParameter SET ParameterCode=?, ParameterName=?, Unit=?, SortOrder=?, IsActive=1 WHERE ParameterId=?";
        String sqlInsert = "INSERT INTO LabTestParameter (LabTestId, ParameterCode, ParameterName, Unit, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlDeleteRange = "DELETE FROM LabReferenceRange WHERE ParameterId = ?";
        String sqlInsertRange = "INSERT INTO LabReferenceRange (ParameterId, Gender, AgeMinDays, AgeMaxDays, RefMin, RefMax, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate); PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS); PreparedStatement psDeleteRange = conn.prepareStatement(sqlDeleteRange); PreparedStatement psInsertRange = conn.prepareStatement(sqlInsertRange)) {

            int sortOrder = 1;

            for (LabTestParameter p : parameters) {

                int paramId;

                if (p.getParameterId() > 0) {
                    paramId = updateParameter(psUpdate, p, sortOrder++);
                    deleteOldRanges(psDeleteRange, paramId);
                } else {
                    paramId = insertNewParameter(psInsert, labTestId, p, sortOrder++);
                }

                insertRanges(psInsertRange, paramId, p.getReferenceRanges());
            }

            psInsertRange.executeBatch();
        }
    }

    private int updateParameter(PreparedStatement ps, LabTestParameter p, int sortOrder) throws Exception {

        ps.setString(1, p.getParameterCode());
        ps.setString(2, p.getParameterName());
        ps.setString(3, p.getUnit());
        ps.setInt(4, sortOrder);
        ps.setInt(5, p.getParameterId());

        ps.executeUpdate();
        return p.getParameterId();
    }

    private int insertNewParameter(PreparedStatement ps, int labTestId, LabTestParameter p, int sortOrder) throws Exception {

        ps.setInt(1, labTestId);
        ps.setString(2, p.getParameterCode());
        ps.setString(3, p.getParameterName());
        ps.setString(4, p.getUnit());
        ps.setInt(5, sortOrder);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }

        throw new RuntimeException("Insert new param failed");
    }

    private void deleteOldRanges(PreparedStatement ps, int paramId) throws Exception {
        ps.setInt(1, paramId);
        ps.executeUpdate();
    }

//    public boolean editLabOrders(int batchId, int patientId, int medicalRecordId, int doctorId, String[] newTestIds) {
//
//        if (newTestIds == null || newTestIds.length == 0) {
//            return cancelLabBatch(batchId);
//        }
//
//        Connection conn = null;
//
//        try {
//            conn = new DBContext().conn;
//            conn.setAutoCommit(false);
//
//            Map<Integer, Integer> currentMap = getCurrentTests(conn, batchId);
//            List<Integer> currentIds = new ArrayList<>(currentMap.keySet());
//
//            List<Integer> newIds = parseIds(newTestIds);
//
//            List<Integer> toRemove = getTestsToRemove(currentIds, newIds);
//            List<Integer> toAdd = getTestsToAdd(currentIds, newIds);
//
//            if (!toRemove.isEmpty()) {
//                cancelTests(conn, batchId, toRemove, currentMap);
//            }
//
//            if (!toAdd.isEmpty()) {
//                addNewTests(conn, batchId, patientId, medicalRecordId, doctorId, toAdd);
//            }
//
//            conn.commit();
//            return true;
//
//        } catch (Exception e) {
//            rollback(conn);
//            e.printStackTrace();
//            return false;
//        } finally {
//            close(conn);
//        }
//    }
    public boolean editLabOrders(model.LabTestBatch batchUpdate) {

        // Rút các thông tin cần thiết từ Model ra
        int batchId = batchUpdate.getBatchId();
        int patientId = batchUpdate.getPatientId();
        int medicalRecordId = batchUpdate.getMedicalRecordId();
        int doctorId = batchUpdate.getCreatedByDoctorId();
        List<Integer> newIds = batchUpdate.getTestIds(); // Lấy List ID mới

        // Nếu rỗng -> Hủy luôn cái Lô đó
        if (newIds == null || newIds.isEmpty()) {
            return cancelLabBatch(batchId);
        }

        Connection conn = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            // Lấy danh sách ID hiện tại từ DB
            Map<Integer, Integer> currentMap = getCurrentTests(conn, batchId);
            List<Integer> currentIds = new ArrayList<>(currentMap.keySet());

            // Hàm parseIds không cần nữa vì testIds đã là List<Integer> sẵn rồi
            // Tìm ra những ID cần thêm và cần xóa
            List<Integer> toRemove = getTestsToRemove(currentIds, newIds);
            List<Integer> toAdd = getTestsToAdd(currentIds, newIds);

            // Xử lý Hủy/Thêm
            if (!toRemove.isEmpty()) {
                cancelTests(conn, batchId, toRemove, currentMap);
            }

            if (!toAdd.isEmpty()) {
                addNewTests(conn, batchId, patientId, medicalRecordId, doctorId, toAdd);
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
            return false;
        } finally {
            close(conn);
        }
    }

    private Map<Integer, Integer> getCurrentTests(Connection conn, int batchId) throws Exception {

        Map<Integer, Integer> map = new HashMap<>();

        String sql = "SELECT LabTestId, ServiceOrderId FROM LabOrderTest WHERE BatchId = ? AND Status != 'CANCELLED'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, batchId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("LabTestId"), rs.getInt("ServiceOrderId"));
                }
            }
        }

        return map;
    }

    private List<Integer> parseIds(String[] ids) {
        List<Integer> list = new ArrayList<>();
        for (String id : ids) {
            list.add(Integer.parseInt(id));
        }
        return list;
    }

    private List<Integer> getTestsToRemove(List<Integer> current, List<Integer> newer) {
        List<Integer> result = new ArrayList<>(current);
        result.removeAll(newer);
        return result;
    }

    private List<Integer> getTestsToAdd(List<Integer> current, List<Integer> newer) {
        List<Integer> result = new ArrayList<>(newer);
        result.removeAll(current);
        return result;
    }

    private void cancelTests(Connection conn, int batchId, List<Integer> testIds, Map<Integer, Integer> testToOrderMap) throws Exception {

        String sqlTest = "UPDATE LabOrderTest SET Status = 'CANCELLED' WHERE BatchId = ? AND LabTestId = ?";
        String sqlOrder = "UPDATE ServiceOrder SET Status = 'CANCELLED' WHERE ServiceOrderId = ?";

        try (
                PreparedStatement psTest = conn.prepareStatement(sqlTest); PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {

            for (int testId : testIds) {

                psTest.setInt(1, batchId);
                psTest.setInt(2, testId);
                psTest.addBatch();

                psOrder.setInt(1, testToOrderMap.get(testId));
                psOrder.addBatch();
            }

            psTest.executeBatch();
            psOrder.executeBatch();
        }
    }

    private void addNewTests(Connection conn, int batchId,
            int patientId, int medicalRecordId, int doctorId,
            List<Integer> testIds) throws Exception {

        for (int labTestId : testIds) {

            PriceInfo priceInfo = getPriceInfo(conn, labTestId);

            int orderId = createServiceOrder(conn,
                    patientId, medicalRecordId, doctorId,
                    priceInfo.serviceId, priceInfo.price);

            insertLabOrderTest(conn, orderId, labTestId, batchId);
        }
    }

    public boolean cancelLabBatch(int batchId) {
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stCheck = null;
        java.sql.PreparedStatement stCancelOrders = null;
        java.sql.PreparedStatement stCancelOrderTests = null;
        java.sql.PreparedStatement stCancelBatch = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            // 1. KIỂM TRA BẢO MẬT: Đảm bảo không có hóa đơn nào trong Lô này đã được thanh toán
            String sqlCheck = "SELECT COUNT(*) FROM LabOrderTest lot "
                    + "JOIN ServiceOrder so ON lot.ServiceOrderId = so.ServiceOrderId "
                    + "WHERE lot.BatchId = ? AND so.Status = 'PAID'";
            stCheck = conn.prepareStatement(sqlCheck);
            stCheck.setInt(1, batchId);
            try (java.sql.ResultSet rs = stCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Đã đóng tiền rồi -> Không cho Hủy!
                }
            }

            // 2. HỦY TẤT CẢ HÓA ĐƠN (ServiceOrder) liên quan đến Lô này
            // Phép thuật SQL: Dùng IN kết hợp SubQuery để tìm ra đúng hóa đơn của Lô
            String sqlCancelOrders = "UPDATE ServiceOrder SET Status = 'CANCELLED' "
                    + "WHERE ServiceOrderId IN (SELECT ServiceOrderId FROM LabOrderTest WHERE BatchId = ?)";
            stCancelOrders = conn.prepareStatement(sqlCancelOrders);
            stCancelOrders.setInt(1, batchId);
            stCancelOrders.executeUpdate();

            // 3. HỦY TẤT CẢ CHI TIẾT XÉT NGHIỆM (LabOrderTest)
            String sqlCancelOrderTests = "UPDATE LabOrderTest SET Status = 'CANCELLED' WHERE BatchId = ?";
            stCancelOrderTests = conn.prepareStatement(sqlCancelOrderTests);
            stCancelOrderTests.setInt(1, batchId);
            stCancelOrderTests.executeUpdate();

            // 4. HỦY LÔ (LabTestBatch)
            String sqlCancelBatch = "UPDATE LabTestBatch SET Status = 'CANCELLED', TechnicianId = 4 WHERE BatchId = ?";
            stCancelBatch = conn.prepareStatement(sqlCancelBatch);
            stCancelBatch.setInt(1, batchId);
            stCancelBatch.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            return false;
        } finally {
            try {
                if (stCheck != null) {
                    stCheck.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stCancelOrders != null) {
                    stCancelOrders.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stCancelOrderTests != null) {
                    stCancelOrderTests.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stCancelBatch != null) {
                    stCancelBatch.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public LabTest getLabTestByServiceId(int serviceId) {
        String sql = "SELECT lt.*, c.CategoryName "
                + "FROM LabTest lt "
                + "LEFT JOIN LabTestCategory c ON lt.CategoryId = c.CategoryId "
                + "WHERE lt.ServiceId = ?";

        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    model.LabTest test = new model.LabTest();
                    test.setLabTestId(rs.getInt("LabTestId"));
                    test.setServiceId(rs.getInt("ServiceId"));
                    test.setTestCode(rs.getString("TestCode"));
                    test.setTestName(rs.getString("TestName"));
                    test.setCategoryId(rs.getInt("CategoryId"));
                    test.setCategoryName(rs.getString("CategoryName"));
                    test.setIsPanel(rs.getBoolean("IsPanel"));
                    test.setSortOrder(rs.getInt("SortOrder"));
                    test.setIsActive(rs.getBoolean("IsActive"));
                    dao.LabTestDAO labDao = new dao.LabTestDAO();
                    test.setParameters(labDao.getParametersByLabTestId(test.getLabTestId()));
                    return test;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

// Hàm lấy danh sách các chỉ số của xét nghiệm đó
    public java.util.List<model.LabTestParameter> getParametersByLabTestId(int labTestId) {
        java.util.Map<Integer, model.LabTestParameter> paramMap = new java.util.LinkedHashMap<>();

        // 🔥 LEFT JOIN: Lấy Parameter và tất cả các Range của nó (nếu có)
        String sql = "SELECT p.*, r.RangeId, r.Gender, r.AgeMinDays, r.AgeMaxDays, r.RefMin, r.RefMax "
                + "FROM LabTestParameter p "
                + "LEFT JOIN LabReferenceRange r ON p.ParameterId = r.ParameterId AND r.IsActive = 1 "
                + "WHERE p.LabTestId = ? AND p.IsActive = 1 "
                + "ORDER BY p.SortOrder ASC, r.Gender DESC, r.AgeMinDays ASC";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, labTestId);

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int paramId = rs.getInt("ParameterId");

                    // 1. Kiểm tra xem Parameter này đã được tạo trong Map chưa?
                    model.LabTestParameter p = paramMap.get(paramId);
                    if (p == null) {
                        // Lần đầu tiên gặp Parameter này -> Tạo mới
                        p = new model.LabTestParameter();
                        p.setParameterId(paramId);
                        p.setLabTestId(rs.getInt("LabTestId"));
                        p.setParameterCode(rs.getString("ParameterCode"));
                        p.setParameterName(rs.getString("ParameterName"));
                        p.setUnit(rs.getString("Unit"));
                        p.setSortOrder(rs.getInt("SortOrder"));
                        p.setActive(rs.getBoolean("IsActive"));

                        // Khởi tạo danh sách Range rỗng
                        p.setReferenceRanges(new java.util.ArrayList<>());

                        // Cất vào Map
                        paramMap.put(paramId, p);
                    }

                    // 2. Đọc thông tin Range (Nếu LEFT JOIN có dữ liệu)
                    int rangeId = rs.getInt("RangeId");
                    if (!rs.wasNull() && rangeId > 0) { // Đảm bảo Range thực sự tồn tại
                        model.LabReferenceRange r = new model.LabReferenceRange();
                        r.setRangeId(rangeId);
                        r.setParameterId(paramId);
                        r.setGender(rs.getString("Gender"));
                        r.setAgeMinDays(rs.getInt("AgeMinDays"));
                        r.setAgeMaxDays(rs.getInt("AgeMaxDays"));

                        // Lấy Min/Max an toàn vì có thể NULL
                        r.setRefMin(rs.getObject("RefMin") != null ? rs.getDouble("RefMin") : null);
                        r.setRefMax(rs.getObject("RefMax") != null ? rs.getDouble("RefMax") : null);

                        // Nhét Range này vào bụng thằng Parameter mẹ
                        p.getReferenceRanges().add(r);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Trả về danh sách các Parameter đã được gộp đầy đủ Range
        return new java.util.ArrayList<>(paramMap.values());
    }

    public java.util.List<java.util.Map<String, Object>> getConsolidatedLabResults(int medicalRecordId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();

        String sql = "SELECT "
                + "c.CategoryName, "
                + "b.BatchId, "
                + "t.TestName, "
                + "t.IsPanel, "
                + "p.ParameterName, "
                // 🔥 SNAPSHOT LOGIC: Ưu tiên LabResult (đã lưu). Nếu chưa lưu, mò vào Quy tắc tham chiếu (rr)
                + "COALESCE(r.Unit, p.Unit) AS Unit, "
                + "COALESCE(r.RefMin, rr.RefMin) AS RefMin, "
                + "COALESCE(r.RefMax, rr.RefMax) AS RefMax, "
                + "r.ResultValue, "
                + "r.Flag, "
                + "r.ResultTime, " // THỜI GIAN KTV BẤM LƯU KẾT QUẢ
                + "lot.Status, "
                + "lot.RejectReason "
                + "FROM LabTestBatch b "
                + "JOIN Patient pat ON b.PatientId = pat.PatientId " // 🔥 Cần bảng Patient để tính Tuổi, Giới tính
                + "JOIN LabOrderTest lot ON b.BatchId = lot.BatchId "
                + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
                + "JOIN LabTestCategory c ON t.CategoryId = c.CategoryId "
                + "JOIN LabTestParameter p ON t.LabTestId = p.LabTestId "
                // 🔥 LEFT JOIN Quyết định: Tìm Range phù hợp nếu chưa có Snapshot
                + "LEFT JOIN LabReferenceRange rr ON p.ParameterId = rr.ParameterId "
                + "     AND rr.IsActive = 1 "
                + "     AND ("
                + "         rr.Gender = 'ALL' "
                + "         OR (rr.Gender = 'M' AND pat.Gender IN ('Nam', 'Male', 'M')) "
                + "         OR (rr.Gender = 'F' AND pat.Gender IN ('Nữ', 'Nu', 'Female', 'F')) "
                + "     ) "
                + "     AND DATEDIFF(day, pat.DateOfBirth, GETDATE()) BETWEEN rr.AgeMinDays AND rr.AgeMaxDays "
                + "LEFT JOIN LabResult r ON lot.LabOrderTestId = r.LabOrderTestId AND p.ParameterId = r.ParameterId "
                + "WHERE b.MedicalRecordId = ? "
                + "AND lot.Status != 'CANCELLED' "
                // CHẶN RÁC LỊCH SỬ: Chỉ hiện chỉ số Active nếu phiếu chưa COMPLETED. Nếu đã COMPLETED thì dứt khoát phải có ResultValue mới hiện.
                + "AND ( (p.IsActive = 1 AND lot.Status != 'COMPLETED') OR r.ResultValue IS NOT NULL ) "
                + "ORDER BY c.SortOrder ASC, t.SortOrder ASC, p.SortOrder ASC";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();

                    // Giữ nguyên 100% logic Map Data của Chủ tịch
                    map.put("categoryName", rs.getString("CategoryName"));
                    map.put("batchId", rs.getInt("BatchId"));
                    map.put("testName", rs.getString("TestName"));
                    map.put("isPanel", rs.getBoolean("IsPanel"));
                    map.put("parameterName", rs.getString("ParameterName"));
                    map.put("unit", rs.getString("Unit"));
                    map.put("resultValue", rs.getString("ResultValue"));
                    map.put("status", rs.getString("Status"));
                    map.put("rejectReason", rs.getString("RejectReason"));

                    // Xử lý chuỗi Normal Range 
                    Object refMin = rs.getObject("RefMin");
                    Object refMax = rs.getObject("RefMax");
                    String normalRange = (refMin != null ? refMin.toString() : "") + " - " + (refMax != null ? refMax.toString() : "");
                    map.put("normalRange", normalRange.equals(" - ") ? "___" : normalRange);

                    // Xử lý Cờ bất thường (Flag)
                    String flag = rs.getString("Flag");
                    map.put("isAbnormal", (flag != null && !flag.trim().isEmpty()));

                    // Lấy thời gian trả kết quả
                    map.put("resultTime", rs.getTimestamp("ResultTime")); // Lưu ý lấy Timestamp để giữ nguyên giờ phút
                    map.put("flag", flag != null ? flag.trim().toUpperCase() : "");
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getOrderedTestIds(int medicalRecordId) {
        List<Integer> list = new java.util.ArrayList<>();
        // Truy vấn qua bảng trung gian Batch và OrderTest
        String sql = "SELECT lot.LabTestId "
                + "FROM LabOrderTest lot "
                + "JOIN LabTestBatch ltb ON lot.BatchId = ltb.BatchId "
                + "WHERE ltb.MedicalRecordId = ?"
                + "AND lot.Status != 'CANCELLED' AND lot.Status != 'REJECTED'";
        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, medicalRecordId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("LabTestId"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<model.LabTestBatch> getBatchesForMedicalRecord(int medicalRecordId) {
        java.util.List<model.LabTestBatch> list = new java.util.ArrayList<>();
        java.sql.Connection conn = null;
        java.sql.PreparedStatement stBatch = null;
        java.sql.PreparedStatement stTest = null;

        try {
            conn = new DBContext().conn;

            // 🔥 1. THÊM b.Status AS PhysicalStatus ĐỂ LẤY TRẠNG THÁI GỐC CỦA LỄ TÂN
            String sqlBatch = "SELECT b.BatchId, b.Status AS PhysicalStatus, u.FullName AS DoctorName, b.CreatedAt, "
                    + "SUM(CASE WHEN lot.Status != 'REJECTED' AND lot.Status != 'CANCELLED' THEN 1 ELSE 0 END) AS TotalValid, "
                    + "SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) AS TotalCompleted "
                    + "FROM LabTestBatch b "
                    + "LEFT JOIN [User] u ON b.CreatedByDoctorId = u.UserId "
                    + "LEFT JOIN LabOrderTest lot ON b.BatchId = lot.BatchId "
                    + "WHERE b.MedicalRecordId = ? AND b.Status != 'CANCELLED' "
                    + "GROUP BY b.BatchId, b.Status, u.FullName, b.CreatedAt " // Nhớ thêm b.Status vào GROUP BY
                    + "ORDER BY b.BatchId DESC";

            stBatch = conn.prepareStatement(sqlBatch);
            stBatch.setInt(1, medicalRecordId);
            java.sql.ResultSet rsBatch = stBatch.executeQuery();

            String sqlTest = "SELECT t.LabTestId, t.TestName FROM LabOrderTest lot "
                    + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
                    + "WHERE lot.BatchId = ? "
                    + "AND lot.Status != 'CANCELLED' "
                    + "AND lot.Status != 'REJECTED'";

            stTest = conn.prepareStatement(sqlTest);

            while (rsBatch.next()) {
                model.LabTestBatch batch = new model.LabTestBatch();
                batch.setBatchId(rsBatch.getInt("BatchId"));
                batch.setDoctorName(rsBatch.getString("DoctorName"));
                batch.setOrderTime(rsBatch.getTimestamp("CreatedAt"));

                // =========================================================
                // 🔥 LOGIC TÍNH TOÁN TRẠNG THÁI LAI (HYBRID STATUS)
                // =========================================================
                int totalValid = rsBatch.getInt("TotalValid");
                int totalCompleted = rsBatch.getInt("TotalCompleted");
                String physicalStatus = rsBatch.getString("PhysicalStatus"); // Lấy trạng thái thực tế từ DB

                String finalStatus;

                // Cảnh 1: Nếu đã làm xong 100% các test hợp lệ (hoặc Lab từ chối sạch sành sanh)
                if (totalValid == 0 || totalCompleted == totalValid) {
                    finalStatus = "COMPLETED";
                } // Cảnh 2: Nếu chưa xong, lấy đúng trạng thái gốc dưới DB 
                // (Chưa đóng tiền = CREATED, Đã đóng tiền = IN_PROGRESS)
                else {
                    finalStatus = physicalStatus;
                }

                batch.setStatus(finalStatus);
                // =========================================================

                java.util.List<Integer> testIds = new java.util.ArrayList<>();
                java.util.List<String> testNames = new java.util.ArrayList<>();
                stTest.setInt(1, batch.getBatchId());
                java.sql.ResultSet rsTest = stTest.executeQuery();
                while (rsTest.next()) {
                    testIds.add(rsTest.getInt("LabTestId"));
                    testNames.add(rsTest.getString("TestName"));
                }
                batch.setTestIds(testIds);
                batch.setTestNames(testNames);

                list.add(batch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stTest != null) {
                    stTest.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stBatch != null) {
                    stBatch.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    // 1. Lấy danh sách xét nghiệm cho Bác sĩ Tick chọn (Sắp xếp theo Nhóm)
    public List<LabTest> getAllActiveTestsForDoctor() {
        List<LabTest> list = new ArrayList<>();
        // Query join 3 bảng để lấy Tên XN, Tên Nhóm và Giá Tiền
        String sql = "SELECT t.LabTestId, t.TestCode, t.TestName, t.IsPanel, t.ServiceId, "
                + "c.CategoryName, s.CurrentPrice "
                + "FROM LabTest t "
                + "JOIN LabTestCategory c ON t.CategoryId = c.CategoryId "
                + "JOIN Service s ON t.ServiceId = s.ServiceId "
                + "WHERE t.IsActive = 1 AND s.IsActive = 1 "
                + "ORDER BY c.SortOrder ASC, t.SortOrder ASC";

        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                LabTest test = new LabTest();
                test.setLabTestId(rs.getInt("LabTestId"));
                test.setTestCode(rs.getString("TestCode"));
                test.setTestName(rs.getString("TestName"));
                test.setIsPanel(rs.getBoolean("IsPanel"));
                test.setServiceId(rs.getInt("ServiceId"));
                test.setCategoryName(rs.getString("CategoryName"));
                test.setCurrentPrice(rs.getDouble("CurrentPrice"));
                list.add(test);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<model.LabTest> getLabTestsByBatchId(int batchId) {
        java.util.List<model.LabTest> list = new java.util.ArrayList<>();

        // Cần JOIN 3 bảng: Bảng trung gian (LabOrderTest), Bảng XN (LabTest) và Bảng Giá (Service)
        String sql = "SELECT t.LabTestId, t.TestCode, t.TestName, t.IsPanel, s.CurrentPrice "
                + "FROM LabOrderTest lot "
                + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
                + "JOIN Service s ON t.ServiceId = s.ServiceId " // Nối để lấy giá tiền
                + "WHERE lot.BatchId = ?"
                + "AND lot.Status != 'CANCELLED' AND lot.Status != 'REJECTED'";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, batchId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    model.LabTest test = new model.LabTest();

                    // Gán các giá trị cần thiết để in ra phiếu
                    test.setLabTestId(rs.getInt("LabTestId"));
                    test.setTestCode(rs.getString("TestCode"));
                    test.setTestName(rs.getString("TestName"));
                    test.setIsPanel(rs.getBoolean("IsPanel"));
                    test.setCurrentPrice(rs.getDouble("CurrentPrice"));

                    // Nếu trên phiếu in cần thêm nhóm (Category) thì bạn Join thêm bảng Category và set vào đây nhé
                    list.add(test);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại getLabTestsByBatchId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public java.util.Map<String, String> getPrintInvoiceInfo(int batchId) {
        java.util.Map<String, String> info = new java.util.HashMap<>();

        // Nối 4 bảng: Lô XN + Bệnh nhân + Bệnh án + Bác sĩ (Bảng User)
        String sql = "SELECT p.FullName AS PatientName, p.Gender, YEAR(p.DateOfBirth) AS YOB, p.Address, "
                + "mr.Diagnosis, u.FullName AS DoctorName "
                + "FROM LabTestBatch b "
                + "JOIN Patient p ON b.PatientId = p.PatientId "
                + "JOIN MedicalRecord mr ON b.MedicalRecordId = mr.MedicalRecordId "
                + "JOIN [User] u ON b.CreatedByDoctorId = u.UserId "
                + "WHERE b.BatchId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, batchId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    info.put("PatientName", rs.getString("PatientName"));
                    info.put("Gender", rs.getString("Gender"));
                    info.put("YOB", rs.getString("YOB"));
                    // Xử lý null cho Address và Diagnosis tránh bị lỗi chữ "null" trên giấy
                    info.put("Address", rs.getString("Address") != null ? rs.getString("Address") : "Chưa cập nhật");
                    info.put("Diagnosis", rs.getString("Diagnosis") != null ? rs.getString("Diagnosis") : "Chưa có chẩn đoán");
                    info.put("DoctorName", rs.getString("DoctorName"));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại getPrintInvoiceInfo: " + e.getMessage());
            e.printStackTrace();
        }
        return info;
    }

    // SQL GỐC CHO DANH SÁCH TEST
    private final String BASE_SQL_QUEUE
            = "SELECT "
            + "mr.MedicalRecordId, "
            + "p.PatientId, "
            + "p.FullName AS PatientName, "
            + "p.Gender, "
            + "(YEAR(GETDATE()) - YEAR(p.DateOfBirth)) AS Age, "
            + "u.FullName AS DoctorName, "
            + "SUM(CASE WHEN lot.Status != 'REJECTED' THEN 1 ELSE 0 END) AS TotalValidTests, "
            + "SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) AS CompletedTests "
            + "FROM MedicalRecord mr "
            + "JOIN Patient p ON mr.PatientId = p.PatientId "
            + "JOIN [User] u ON mr.ResponsibleDoctorId = u.UserId "
            + "JOIN LabTestBatch b ON mr.MedicalRecordId = b.MedicalRecordId "
            + "JOIN LabOrderTest lot ON b.BatchId = lot.BatchId "
            + "JOIN ServiceOrder so ON lot.ServiceOrderId = so.ServiceOrderId "
            + "WHERE so.Status = 'PAID' AND b.Status != 'CANCELLED' ";

    private final String GROUP_BY_QUEUE = "GROUP BY mr.MedicalRecordId, p.PatientId, p.FullName, p.Gender, p.DateOfBirth, u.FullName ";

    // HÀM LẤY TẤT CẢ DANH SÁCH TEST
    public java.util.List<model.TestResult> getAllTestResults(String status) {
        java.util.List<model.TestResult> queue = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SQL_QUEUE);

        sql.append(GROUP_BY_QUEUE);

        if ("COMPLETED".equals(status)) {
            sql.append("HAVING SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) = "
                    + "SUM(CASE WHEN lot.Status != 'REJECTED' THEN 1 ELSE 0 END) ");
        } else if ("PENDING".equals(status)) {
            sql.append("HAVING SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) < "
                    + "SUM(CASE WHEN lot.Status != 'REJECTED' THEN 1 ELSE 0 END) ");
        }

        sql.append("ORDER BY mr.MedicalRecordId DESC");

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString()); java.sql.ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                queue.add(mapResultSetToTestResult(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queue;
    }

    // HÀM TÌM KIẾM DANH SÁCH TEST
    public java.util.List<model.TestResult> searchTestResults(String searchKeyword, String status) {
        java.util.List<model.TestResult> queue = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SQL_QUEUE);

        boolean hasKeyword = searchKeyword != null && !searchKeyword.trim().isEmpty();

        if (hasKeyword) {
            sql.append("AND (p.FullName LIKE ? OR CAST(mr.MedicalRecordId AS VARCHAR) = ?) ");
        }

        sql.append(GROUP_BY_QUEUE);

        if ("COMPLETED".equals(status)) {
            sql.append("HAVING SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) = "
                    + "SUM(CASE WHEN lot.Status != 'REJECTED' THEN 1 ELSE 0 END) ");
        } else if ("PENDING".equals(status)) {
            sql.append("HAVING SUM(CASE WHEN lot.Status = 'COMPLETED' THEN 1 ELSE 0 END) < "
                    + "SUM(CASE WHEN lot.Status != 'REJECTED' THEN 1 ELSE 0 END) ");
        }

        sql.append("ORDER BY mr.MedicalRecordId DESC");

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql.toString())) {

            if (hasKeyword) {
                st.setString(1, "%" + searchKeyword.trim() + "%");
                st.setString(2, searchKeyword.trim());
            }

            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    queue.add(mapResultSetToTestResult(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queue;
    }

    // =========================================================================
    // 🔥 HÀM TIỆN ÍCH CHUẨN HÓA DỮ LIỆU
    // =========================================================================
    private model.TestResult mapResultSetToTestResult(java.sql.ResultSet rs) throws Exception {
        model.TestResult row = new model.TestResult();
        row.setMedicalRecordId(rs.getInt("MedicalRecordId"));
        row.setPatientId(rs.getInt("PatientId"));
        row.setPatientName(rs.getString("PatientName"));
        row.setGender(rs.getString("Gender"));
        row.setAge(rs.getInt("Age"));
        row.setDoctorName(rs.getString("DoctorName"));

        int totalValid = rs.getInt("TotalValidTests");
        int completed = rs.getInt("CompletedTests");
        row.setTotalTests(totalValid);
        row.setCompletedTests(completed);
        row.setProgress(completed + "/" + totalValid);

        boolean isFullyCompleted = (totalValid == completed);
        row.setIsFullyCompleted(isFullyCompleted);

        return row;
    }

    public boolean updateLabTestStatus(int labOrderTestId, String status, String rejectReason, int technicianId) {
        String sqlTest = "UPDATE LabOrderTest SET Status = ?, RejectReason = ? WHERE LabOrderTestId = ?";

        //: Tìm Hóa đơn thông qua ID của LabOrderTest để Hủy
        String sqlOrder = "UPDATE ServiceOrder SET Status = 'CANCELLED' "
                + "WHERE ServiceOrderId = (SELECT ServiceOrderId FROM LabOrderTest WHERE LabOrderTestId = ?)";

        // MỚI: Cập nhật KTV vào Batch
        String sqlBatch = "UPDATE LabTestBatch SET TechnicianId = ? "
                + "WHERE BatchId = (SELECT BatchId FROM LabOrderTest WHERE LabOrderTestId = ?)";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement stTest = null;
        java.sql.PreparedStatement stOrder = null;
        java.sql.PreparedStatement stBatch = null; // Mới

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // 🔥 Bật khiên Transaction lên

            // 1. Cập nhật bảng Xét nghiệm (LabOrderTest)
            stTest = conn.prepareStatement(sqlTest);
            stTest.setString(1, status);
            if (rejectReason != null && !rejectReason.trim().isEmpty()) {
                stTest.setString(2, rejectReason);
            } else {
                stTest.setNull(2, java.sql.Types.NVARCHAR);
            }
            stTest.setInt(3, labOrderTestId);
            stTest.executeUpdate();

            // 2. Cập nhật Bảng Batch: Đóng dấu ID Kỹ thuật viên thao tác
            stBatch = conn.prepareStatement(sqlBatch);
            stBatch.setInt(1, technicianId);
            stBatch.setInt(2, labOrderTestId);
            stBatch.executeUpdate();

            // 3. Nếu là REJECTED -> Chém luôn cái Hóa đơn (ServiceOrder)
            if ("REJECTED".equals(status)) {
                stOrder = conn.prepareStatement(sqlOrder);
                stOrder.setInt(1, labOrderTestId);
                stOrder.executeUpdate();
            }

            conn.commit(); // Chốt sổ 3 bảng cùng lúc!
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stTest != null) {
                    stTest.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stOrder != null) {
                    stOrder.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stBatch != null) {
                    stBatch.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    //lấy danh sách các dịch vụ xét nghiệm của một bệnh án để làm thủ tục checkin
    public List<Map<String, Object>> getOrderedServicesForCheckin(int medicalRecordId) {
        List<Map<String, Object>> list = new ArrayList<>();
        // Lấy thông tin dịch vụ, nhóm theo Category để dễ nhìn
        String sql = "SELECT lot.LabOrderTestId, lt.TestName, lt.TestCode, ltc.CategoryName, lot.Status, lot.RejectReason "
                + "FROM LabOrderTest lot "
                + "JOIN LabTest lt ON lot.LabTestId = lt.LabTestId "
                + "JOIN LabTestCategory ltc ON lt.CategoryId = ltc.CategoryId "
                + "JOIN LabTestBatch ltb ON lot.BatchId = ltb.BatchId "
                + "WHERE ltb.MedicalRecordId = ? AND ltb.Status != 'CANCELLED' AND lot.status != 'CANCELLED'"
                + "ORDER BY ltc.SortOrder, lt.SortOrder";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("labOrderTestId", rs.getInt("LabOrderTestId"));
                    map.put("testName", rs.getString("TestName"));
                    map.put("testCode", rs.getString("TestCode"));
                    map.put("categoryName", rs.getString("CategoryName"));
                    map.put("status", rs.getString("Status"));
                    map.put("rejectReason", rs.getString("RejectReason"));
                    list.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //kiểm tra xem bệnh án này còn dịch vụ nào đang "ORDERED" (chờ nhận mẫu) không
    public boolean requiresCheckin(int medicalRecordId) {
        // Đếm số lượng Test đang ở trạng thái ORDERED của bệnh án này
        String sql = "SELECT COUNT(*) FROM LabOrderTest lot "
                + "JOIN LabTestBatch ltb ON lot.BatchId = ltb.BatchId " // Đã sửa đúng tên bảng LabTestBatch
                + "WHERE ltb.MedicalRecordId = ? AND lot.Status = 'ORDERED'"
                + "AND ltb.Status != 'CANCELLED'";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu > 0 nghĩa là BẮT BUỘC phải qua trang Check-in
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public java.util.List<TestResultDetail> getTestsForProcessing(int medicalRecordId) {
        java.util.List<TestResultDetail> list = new java.util.ArrayList<>();

        // Join với LabReferenceRange dựa trên Tuổi và Giới tính bệnh nhân
        String sql = "SELECT "
                + "c.CategoryName, "
                + "b.BatchId, "
                + "lot.LabOrderTestId, "
                + "lot.Status AS TestStatus, "
                + "lot.RejectReason, "
                + "t.TestName, "
                + "p.ParameterId, "
                + "p.ParameterName, "
                + "COALESCE(r.Unit, p.Unit) AS Unit, "
                // Ưu tiên lấy RefMin/Max từ bảng LabResult nếu đã nhập. Nếu chưa nhập thì lấy từ quy tắc phù hợp (rr)
                + "COALESCE(r.RefMin, rr.RefMin) AS RefMin, "
                + "COALESCE(r.RefMax, rr.RefMax) AS RefMax, "
                + "r.ResultId, "
                + "r.ResultValue, "
                + "r.Flag "
                + "FROM LabTestBatch b "
                + "JOIN Patient pat ON b.PatientId = pat.PatientId " // Cần bảng Patient để lấy Tuổi, Giới tính
                + "JOIN LabOrderTest lot ON b.BatchId = lot.BatchId "
                + "JOIN ServiceOrder so ON lot.ServiceOrderId = so.ServiceOrderId "
                + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
                + "JOIN LabTestCategory c ON t.CategoryId = c.CategoryId "
                + "JOIN LabTestParameter p ON t.LabTestId = p.LabTestId "
                // 💎 LEFT JOIN Quyết định: Tìm Range phù hợp với bệnh nhân này
                + "LEFT JOIN LabReferenceRange rr ON p.ParameterId = rr.ParameterId "
                + "     AND rr.IsActive = 1 "
                + "     AND ("
                + "         rr.Gender = 'ALL' "
                + "         OR (rr.Gender = 'M' AND pat.Gender IN ('Nam', 'Male', 'M')) "
                + "         OR (rr.Gender = 'F' AND pat.Gender IN ('Nữ', 'Nu', 'Female', 'F')) "
                + "     ) "
                + "     AND DATEDIFF(day, pat.DateOfBirth, GETDATE()) BETWEEN rr.AgeMinDays AND rr.AgeMaxDays "
                + "LEFT JOIN LabResult r ON lot.LabOrderTestId = r.LabOrderTestId AND p.ParameterId = r.ParameterId "
                + "WHERE b.MedicalRecordId = ? "
                + "AND so.Status = 'PAID' "
                + "AND b.Status != 'CANCELLED' "
                + "AND lot.Status != 'CANCELLED' "
                + "AND (t.IsActive = 1 OR r.ResultValue IS NOT NULL) "
                + "AND ( (p.IsActive = 1 AND lot.Status != 'COMPLETED') OR r.ResultId IS NOT NULL ) "
                + "ORDER BY c.SortOrder ASC, t.SortOrder ASC, p.SortOrder ASC";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
//                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    TestResultDetail result = new TestResultDetail();
                    result.setBatchId(rs.getInt("BatchId"));
                    result.setCategoryName(rs.getString("CategoryName"));
                    result.setLabOrderTestId(rs.getInt("LabOrderTestId"));
                    result.setStatus(rs.getString("TestStatus"));
                    result.setRejectReason(rs.getString("RejectReason"));
                    result.setTestName(rs.getString("TestName"));
                    result.setParameterId(rs.getInt("ParameterId"));
                    result.setParameterName(rs.getString("ParameterName"));
                    result.setUnit(rs.getString("Unit"));

                    // Nối dải tham chiếu bình thường (VD: 4.0 - 5.5)
                    Object refMin = rs.getObject("RefMin");
                    Object refMax = rs.getObject("RefMax");
                    String normalRange = (refMin != null ? refMin.toString() : "") + " - " + (refMax != null ? refMax.toString() : "");
                    result.setNormalRange(normalRange.equals(" - ") ? "___" : normalRange);
                    result.setIsNumeric((refMin != null || refMax != null));

                    result.setRefMin(refMin);
                    result.setRefMax(refMax);

                    // Lấy kết quả (nếu đã nhập)
                    result.setResultId(rs.getObject("ResultId"));
                    result.setResultValue(rs.getString("ResultValue"));

                    // Cờ bất thường (Flag)
                    String flag = rs.getString("Flag");
                    result.setIsAbnormal((flag != null && !flag.trim().isEmpty()));
                    result.setFlag(flag);

                    list.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //hàm lưu kết quả xét nghiệm
    public int getInChargeLabTechinicianId(int mrId) {
        String sql = "SELECT TOP 1 (TechnicianId) FROM LabTestBatch WHERE MedicalRecordId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mrId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TechnicianId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(new LabTestDAO().getInChargeLabTechinicianId(24));
    }

}
