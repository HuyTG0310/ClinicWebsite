/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import model.LabTest;
import util.DBContext;
import java.util.*;

/**
 *
 * @author huytr
 */
public class LabTestDAO extends DBContext {

    public boolean insertFullLabTest(model.Service service, model.LabTest labTest, java.util.List<model.LabTestParameter> parameters) {
        String sqlService = "INSERT INTO Service (ServiceName, Category, CurrentPrice, IsActive) VALUES (?, ?, ?, 1)";
        String sqlLabTest = "INSERT INTO LabTest (ServiceId, TestCode, TestName, CategoryId, IsPanel, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";
        String sqlParam = "INSERT INTO LabTestParameter (LabTestId, ParameterCode, ParameterName, Unit, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlRange = "INSERT INTO LabReferenceRange (ParameterId, Gender, AgeMinDays, AgeMaxDays, RefMin, RefMax, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement psService = null;
        java.sql.PreparedStatement psLabTest = null;
        java.sql.PreparedStatement psParam = null;
        java.sql.PreparedStatement psRange = null; // Mới
        java.sql.ResultSet rsService = null;
        java.sql.ResultSet rsLabTest = null;
        java.sql.ResultSet rsParam = null; // Mới

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            // 1. INSERT VÀO BẢNG SERVICE
            psService = conn.prepareStatement(sqlService, java.sql.PreparedStatement.RETURN_GENERATED_KEYS);
            psService.setString(1, service.getServiceName());
            psService.setString(2, "Xét nghiệm");
            psService.setBigDecimal(3, service.getCurrentPrice());
            psService.executeUpdate();

            rsService = psService.getGeneratedKeys();
            int serviceId = 0;
            if (rsService.next()) {
                serviceId = rsService.getInt(1);
            }

            // 2. INSERT VÀO BẢNG LABTEST
            psLabTest = conn.prepareStatement(sqlLabTest, java.sql.PreparedStatement.RETURN_GENERATED_KEYS);
            psLabTest.setInt(1, serviceId);
            psLabTest.setString(2, labTest.getTestCode());
            psLabTest.setString(3, labTest.getTestName());
            psLabTest.setInt(4, labTest.getCategoryId());
            psLabTest.setBoolean(5, labTest.isIsPanel());
            psLabTest.setInt(6, 1);
            psLabTest.executeUpdate();

            rsLabTest = psLabTest.getGeneratedKeys();
            int labTestId = 0;
            if (rsLabTest.next()) {
                labTestId = rsLabTest.getInt(1);
            }

            // 3. INSERT VÀO BẢNG LABTEST PARAMETER & REFERENCE RANGES
            psParam = conn.prepareStatement(sqlParam, java.sql.PreparedStatement.RETURN_GENERATED_KEYS); // Cần sinh ID
            psRange = conn.prepareStatement(sqlRange);

            int sortOrder = 1;
            for (model.LabTestParameter p : parameters) {
                psParam.setInt(1, labTestId);
                psParam.setString(2, p.getParameterCode());
                psParam.setString(3, p.getParameterName());
                psParam.setString(4, p.getUnit());
                psParam.setInt(5, sortOrder++);
                psParam.executeUpdate(); // Chạy từng dòng thay vì Batch để lấy ID

                rsParam = psParam.getGeneratedKeys();
                int paramId = 0;
                if (rsParam.next()) {
                    paramId = rsParam.getInt(1);
                }

                // DUYỆT VÀ INSERT TẤT CẢ RANGES CỦA PARAMETER NÀY
                if (p.getReferenceRanges() != null && !p.getReferenceRanges().isEmpty()) {
                    for (model.LabReferenceRange r : p.getReferenceRanges()) {
                        psRange.setInt(1, paramId);
                        psRange.setString(2, r.getGender());
                        psRange.setInt(3, r.getAgeMinDays());
                        psRange.setInt(4, r.getAgeMaxDays());

                        if (r.getRefMin() != null) {
                            psRange.setDouble(5, r.getRefMin());
                        } else {
                            psRange.setNull(5, java.sql.Types.DECIMAL);
                        }

                        if (r.getRefMax() != null) {
                            psRange.setDouble(6, r.getRefMax());
                        } else {
                            psRange.setNull(6, java.sql.Types.DECIMAL);
                        }

                        psRange.addBatch();
                    }
                }
            }
            psRange.executeBatch();

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
                if (rsService != null) {
                    rsService.close();
                }
            } catch (Exception e) {
            }
            try {
                if (rsLabTest != null) {
                    rsLabTest.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psService != null) {
                    psService.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psLabTest != null) {
                    psLabTest.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psParam != null) {
                    psParam.close();
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
    }

    public boolean updateFullLabTest(model.Service service, model.LabTest labTest, java.util.List<model.LabTestParameter> parameters) {
        String sqlUpdateService = "UPDATE Service SET ServiceName = ?, CurrentPrice = ?, IsActive = ? WHERE ServiceId = ?";
        String sqlUpdateLabTest = "UPDATE LabTest SET TestCode = ?, TestName = ?, CategoryId = ?, IsPanel = ?, IsActive = ? WHERE LabTestId = ?";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement psUpdateService = null;
        java.sql.PreparedStatement psUpdateLabTest = null;
        java.sql.PreparedStatement psDeleteParams = null;
        java.sql.PreparedStatement psUpdateParam = null;
        java.sql.PreparedStatement psInsertParam = null;
        java.sql.PreparedStatement psDeleteOldRanges = null;
        java.sql.PreparedStatement psInsertNewRange = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            // 1 & 2. Update Service & LabTest
            psUpdateService = conn.prepareStatement(sqlUpdateService);
            psUpdateService.setString(1, service.getServiceName());
            psUpdateService.setBigDecimal(2, service.getCurrentPrice());
            psUpdateService.setBoolean(3, service.isIsActive());
            psUpdateService.setInt(4, service.getServiceId());
            psUpdateService.executeUpdate();

            psUpdateLabTest = conn.prepareStatement(sqlUpdateLabTest);
            psUpdateLabTest.setString(1, labTest.getTestCode());
            psUpdateLabTest.setString(2, labTest.getTestName());
            psUpdateLabTest.setInt(3, labTest.getCategoryId());
            psUpdateLabTest.setBoolean(4, labTest.isIsPanel());
            psUpdateLabTest.setBoolean(5, labTest.isIsActive());
            psUpdateLabTest.setInt(6, labTest.getLabTestId());
            psUpdateLabTest.executeUpdate();

            // 3. XỬ LÝ CHỈ SỐ VÀ KHOẢNG THAM CHIẾU
            StringBuilder keptIds = new StringBuilder("0");
            for (model.LabTestParameter p : parameters) {
                if (p.getParameterId() > 0) {
                    keptIds.append(",").append(p.getParameterId());
                }
            }

            // Soft delete các Param bị xóa
            String sqlSoftDelete = "UPDATE LabTestParameter SET IsActive = 0 WHERE LabTestId = ? AND ParameterId NOT IN (" + keptIds.toString() + ")";
            try (java.sql.PreparedStatement psSoftDelete = conn.prepareStatement(sqlSoftDelete)) {
                psSoftDelete.setInt(1, labTest.getLabTestId());
                psSoftDelete.executeUpdate();
            }

            String sqlUpdateP = "UPDATE LabTestParameter SET ParameterCode=?, ParameterName=?, Unit=?, SortOrder=?, IsActive=1 WHERE ParameterId=?";
            String sqlInsertP = "INSERT INTO LabTestParameter (LabTestId, ParameterCode, ParameterName, Unit, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, 1)";

            //Xóa sạch Range cũ của Param đó và Insert lại toàn bộ Range mới
            String sqlDelRange = "DELETE FROM LabReferenceRange WHERE ParameterId = ?";
            String sqlInsRange = "INSERT INTO LabReferenceRange (ParameterId, Gender, AgeMinDays, AgeMaxDays, RefMin, RefMax, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";

            psUpdateParam = conn.prepareStatement(sqlUpdateP);
            psInsertParam = conn.prepareStatement(sqlInsertP, java.sql.PreparedStatement.RETURN_GENERATED_KEYS);
            psDeleteOldRanges = conn.prepareStatement(sqlDelRange);
            psInsertNewRange = conn.prepareStatement(sqlInsRange);

            int sortOrder = 1;
            for (model.LabTestParameter p : parameters) {
                int currentParamId = p.getParameterId();

                if (currentParamId > 0) {
                    // Cập nhật thông tin Param
                    psUpdateParam.setString(1, p.getParameterCode());
                    psUpdateParam.setString(2, p.getParameterName());
                    psUpdateParam.setString(3, p.getUnit());
                    psUpdateParam.setInt(4, sortOrder++);
                    psUpdateParam.setInt(5, currentParamId);
                    psUpdateParam.executeUpdate();

                    // Xóa Range cũ
                    psDeleteOldRanges.setInt(1, currentParamId);
                    psDeleteOldRanges.executeUpdate();
                } else {
                    // Thêm Param mới
                    psInsertParam.setInt(1, labTest.getLabTestId());
                    psInsertParam.setString(2, p.getParameterCode());
                    psInsertParam.setString(3, p.getParameterName());
                    psInsertParam.setString(4, p.getUnit());
                    psInsertParam.setInt(5, sortOrder++);
                    psInsertParam.executeUpdate();

                    java.sql.ResultSet rsNewParam = psInsertParam.getGeneratedKeys();
                    if (rsNewParam.next()) {
                        currentParamId = rsNewParam.getInt(1);
                    }
                }

                // Insert lại Range mới
                if (p.getReferenceRanges() != null && !p.getReferenceRanges().isEmpty()) {
                    for (model.LabReferenceRange r : p.getReferenceRanges()) {
                        psInsertNewRange.setInt(1, currentParamId);
                        psInsertNewRange.setString(2, r.getGender());
                        psInsertNewRange.setInt(3, r.getAgeMinDays());
                        psInsertNewRange.setInt(4, r.getAgeMaxDays());

                        if (r.getRefMin() != null) {
                            psInsertNewRange.setDouble(5, r.getRefMin());
                        } else {
                            psInsertNewRange.setNull(5, java.sql.Types.DECIMAL);
                        }

                        if (r.getRefMax() != null) {
                            psInsertNewRange.setDouble(6, r.getRefMax());
                        } else {
                            psInsertNewRange.setNull(6, java.sql.Types.DECIMAL);
                        }

                        psInsertNewRange.addBatch();
                    }
                }
            }
            psInsertNewRange.executeBatch();

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
                if (psUpdateService != null) {
                    psUpdateService.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psUpdateLabTest != null) {
                    psUpdateLabTest.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psDeleteParams != null) {
                    psDeleteParams.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psInsertParam != null) {
                    psInsertParam.close();
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

    public boolean createLabOrders(int patientId, int medicalRecordId, int doctorId, String[] labTestIds) {
        if (labTestIds == null || labTestIds.length == 0) {
            return false;
        }

        java.sql.Connection conn = null;
        java.sql.PreparedStatement stBatch = null, stOrder = null, stOrderTest = null, stGetPrice = null;

        try {
            // Lưu ý: Nếu DBContext của bạn gọi getConnection() thì sửa lại chỗ này nhé
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // Bắt đầu Transaction riêng cho Xét nghiệm

            // 1. Tạo Lô (Batch)
            String sqlBatch = "INSERT INTO LabTestBatch (PatientId, MedicalRecordId, CreatedByDoctorId, Status) VALUES (?, ?, ?, 'CREATED')";
            stBatch = conn.prepareStatement(sqlBatch, java.sql.Statement.RETURN_GENERATED_KEYS);
            stBatch.setInt(1, patientId);
            stBatch.setInt(2, medicalRecordId);
            stBatch.setInt(3, doctorId);
            stBatch.executeUpdate();

            int batchId = -1;
            try (java.sql.ResultSet rs = stBatch.getGeneratedKeys()) {
                if (rs.next()) {
                    batchId = rs.getInt(1);
                }
            }
            if (batchId == -1) {
                conn.rollback();
                return false;
            }

            // Chuẩn bị SQL
            String sqlPrice = "SELECT t.ServiceId, s.CurrentPrice FROM LabTest t JOIN Service s ON t.ServiceId = s.ServiceId WHERE t.LabTestId = ?";
            stGetPrice = conn.prepareStatement(sqlPrice);

            String sqlOrder = "INSERT INTO ServiceOrder (PatientId, MedicalRecordId, ServiceId, AssignedById, PriceAtTime, Status) VALUES (?, ?, ?, ?, ?, 'UNPAID')";
            stOrder = conn.prepareStatement(sqlOrder, java.sql.Statement.RETURN_GENERATED_KEYS);

            String sqlOrderTest = "INSERT INTO LabOrderTest (ServiceOrderId, LabTestId, BatchId, Status) VALUES (?, ?, ?, 'ORDERED')";
            stOrderTest = conn.prepareStatement(sqlOrderTest);

            // 2. Duyệt qua mảng checkbox Bác sĩ chọn
            for (String idStr : labTestIds) {
                int labTestId = Integer.parseInt(idStr);

                // Lấy giá thực tế
                stGetPrice.setInt(1, labTestId);
                try (java.sql.ResultSet rsPrice = stGetPrice.executeQuery()) {

                    // 🔥 FIX BUG: Phải tìm thấy Dịch vụ & Giá tiền thì mới bắt đầu Insert hóa đơn
                    if (rsPrice.next()) {
                        int serviceId = rsPrice.getInt("ServiceId");
                        double price = rsPrice.getDouble("CurrentPrice");

                        // Tạo Hóa đơn (UNPAID)
                        stOrder.setInt(1, patientId);
                        stOrder.setInt(2, medicalRecordId);
                        stOrder.setInt(3, serviceId);
                        stOrder.setInt(4, doctorId);
                        stOrder.setDouble(5, price);
                        stOrder.executeUpdate();

                        int orderId = -1;
                        try (java.sql.ResultSet rsOrder = stOrder.getGeneratedKeys()) {
                            if (rsOrder.next()) {
                                orderId = rsOrder.getInt(1);
                            }
                        }

                        // Liên kết Xét nghiệm vào Lô VÀ Hóa đơn
                        if (orderId != -1) {
                            stOrderTest.setInt(1, orderId);
                            stOrderTest.setInt(2, labTestId);
                            stOrderTest.setInt(3, batchId);
                            stOrderTest.addBatch();
                        }
                    }
                }
            }

            // Thực thi loạt Insert chi tiết
            stOrderTest.executeBatch();
            conn.commit(); // Chốt đơn thành công!
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
            // Đóng các resource an toàn
            try {
                if (stOrderTest != null) {
                    stOrderTest.close();
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
                if (stGetPrice != null) {
                    stGetPrice.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stBatch != null) {
                    stBatch.close();
                }
            } catch (Exception e) {
            }
            // CỰC KỲ QUAN TRỌNG: Phải trả lại AutoCommit cho Connection Pool
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
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

}
