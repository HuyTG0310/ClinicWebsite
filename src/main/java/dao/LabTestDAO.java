/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

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

    public boolean editLabOrders(int batchId, int patientId, int medicalRecordId, int doctorId, String[] newTestIds) {
        // 1. NẾU BÁC SĨ BỎ TICK HẾT -> Tự động gọi hàm Hủy toàn bộ Lô
        if (newTestIds == null || newTestIds.length == 0) {
            return cancelLabBatch(batchId); // Gọi lại hàm lúc nãy anh em mình viết!
        }

        java.sql.Connection conn = null;
        java.sql.PreparedStatement stGetCurrent = null;
        java.sql.PreparedStatement stCancelTest = null;
        java.sql.PreparedStatement stCancelOrder = null;
        java.sql.PreparedStatement stGetPrice = null;
        java.sql.PreparedStatement stOrder = null;
        java.sql.PreparedStatement stOrderTest = null;

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false); // Vẫn phải dùng Transaction để Kế toán không bị lệch

            // 2. LẤY DANH SÁCH CÁC CHỈ SỐ CŨ (Đang có trong Lô này)
            java.util.List<Integer> currentTestIds = new java.util.ArrayList<>();
            java.util.Map<Integer, Integer> testToOrderMap = new java.util.HashMap<>(); // Lưu cặp <TestId, ServiceOrderId> để lát Hủy hóa đơn

            String sqlCurrent = "SELECT LabTestId, ServiceOrderId FROM LabOrderTest WHERE BatchId = ? AND Status != 'CANCELLED'";
            stGetCurrent = conn.prepareStatement(sqlCurrent);
            stGetCurrent.setInt(1, batchId);
            try (java.sql.ResultSet rs = stGetCurrent.executeQuery()) {
                while (rs.next()) {
                    currentTestIds.add(rs.getInt("LabTestId"));
                    testToOrderMap.put(rs.getInt("LabTestId"), rs.getInt("ServiceOrderId"));
                }
            }

            // 3. PHÂN LOẠI: CÁI NÀO CẦN XÓA? CÁI NÀO CẦN THÊM?
            java.util.List<Integer> newIdsList = new java.util.ArrayList<>();
            for (String id : newTestIds) {
                newIdsList.add(Integer.parseInt(id));
            }

            // Danh sách Xóa = Cũ - Mới (Có trong cũ nhưng Bác sĩ đã bỏ tick)
            java.util.List<Integer> testsToRemove = new java.util.ArrayList<>(currentTestIds);
            testsToRemove.removeAll(newIdsList);

            // Danh sách Thêm = Mới - Cũ (Có trong mới nhưng Cũ chưa có)
            java.util.List<Integer> testsToAdd = new java.util.ArrayList<>(newIdsList);
            testsToAdd.removeAll(currentTestIds);

            // ==========================================
            // 4. THỰC THI XÓA (Cập nhật thành CANCELLED)
            // ==========================================
            if (!testsToRemove.isEmpty()) {
                String sqlCancelTest = "UPDATE LabOrderTest SET Status = 'CANCELLED' WHERE BatchId = ? AND LabTestId = ?";
                String sqlCancelOrder = "UPDATE ServiceOrder SET Status = 'CANCELLED' WHERE ServiceOrderId = ?";
                stCancelTest = conn.prepareStatement(sqlCancelTest);
                stCancelOrder = conn.prepareStatement(sqlCancelOrder);

                for (int testId : testsToRemove) {
                    stCancelTest.setInt(1, batchId);
                    stCancelTest.setInt(2, testId);
                    stCancelTest.addBatch();

                    stCancelOrder.setInt(1, testToOrderMap.get(testId)); // Xóa đúng cái Hóa đơn của Test này
                    stCancelOrder.addBatch();
                }
                stCancelTest.executeBatch();
                stCancelOrder.executeBatch();
            }

            // ==========================================
            // 5. THỰC THI THÊM MỚI (Copy logic y hệt hàm Create của bạn)
            // ==========================================
            if (!testsToAdd.isEmpty()) {
                String sqlPrice = "SELECT t.ServiceId, s.CurrentPrice FROM LabTest t JOIN Service s ON t.ServiceId = s.ServiceId WHERE t.LabTestId = ?";
                stGetPrice = conn.prepareStatement(sqlPrice);

                String sqlOrder = "INSERT INTO ServiceOrder (PatientId, MedicalRecordId, ServiceId, AssignedById, PriceAtTime, Status) VALUES (?, ?, ?, ?, ?, 'UNPAID')";
                stOrder = conn.prepareStatement(sqlOrder, java.sql.Statement.RETURN_GENERATED_KEYS);

                String sqlOrderTest = "INSERT INTO LabOrderTest (ServiceOrderId, LabTestId, BatchId, Status) VALUES (?, ?, ?, 'ORDERED')";
                stOrderTest = conn.prepareStatement(sqlOrderTest);

                for (int labTestId : testsToAdd) {
                    stGetPrice.setInt(1, labTestId);
                    try (java.sql.ResultSet rsPrice = stGetPrice.executeQuery()) {
                        if (rsPrice.next()) {
                            int serviceId = rsPrice.getInt("ServiceId");
                            double price = rsPrice.getDouble("CurrentPrice");

                            // Tạo hóa đơn
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

                            // Liên kết
                            if (orderId != -1) {
                                stOrderTest.setInt(1, orderId);
                                stOrderTest.setInt(2, labTestId);
                                stOrderTest.setInt(3, batchId);
                                stOrderTest.addBatch();
                            }
                        }
                    }
                }
                stOrderTest.executeBatch();
            }

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
            // Nhớ đóng các Statement ở đây nhé (mình rút gọn cho đỡ dài)
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
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
            String sqlCancelBatch = "UPDATE LabTestBatch SET Status = 'CANCELLED' WHERE BatchId = ?";
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

    //cập nhật trạng thái của 1 chỉ định xét nghiệm (LabOrderTest)
//    public boolean updateLabTestStatus(int labOrderTestId, String status, String rejectReason) {
//        String sqlTest = "UPDATE LabOrderTest SET Status = ?, RejectReason = ? WHERE LabOrderTestId = ?";
//
//        // Cú pháp thần thánh: Tìm Hóa đơn thông qua ID của LabOrderTest để Hủy
//        String sqlOrder = "UPDATE ServiceOrder SET Status = 'CANCELLED' "
//                + "WHERE ServiceOrderId = (SELECT ServiceOrderId FROM LabOrderTest WHERE LabOrderTestId = ?)";
//
//        java.sql.Connection conn = null;
//        java.sql.PreparedStatement stTest = null;
//        java.sql.PreparedStatement stOrder = null;
//
//        try {
//            conn = new DBContext().conn;
//            conn.setAutoCommit(false); // 🔥 Bật khiên Transaction lên
//
//            // 1. Cập nhật bảng Xét nghiệm
//            stTest = conn.prepareStatement(sqlTest);
//            stTest.setString(1, status);
//            if (rejectReason != null && !rejectReason.trim().isEmpty()) {
//                stTest.setString(2, rejectReason);
//            } else {
//                stTest.setNull(2, java.sql.Types.NVARCHAR);
//            }
//            stTest.setInt(3, labOrderTestId);
//            stTest.executeUpdate();
//
//            // 2. Nếu là REJECTED -> Chém luôn cái Hóa đơn (ServiceOrder) tương ứng
//            if ("REJECTED".equals(status)) {
//                stOrder = conn.prepareStatement(sqlOrder);
//                stOrder.setInt(1, labOrderTestId);
//                stOrder.executeUpdate();
//            }
//
//            conn.commit(); // Chốt sổ 2 bảng cùng lúc!
//            return true;
//
//        } catch (Exception e) {
//            if (conn != null) {
//                try {
//                    conn.rollback();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            e.printStackTrace();
//        } finally {
//            try {
//                if (stTest != null) {
//                    stTest.close();
//                }
//            } catch (Exception e) {
//            }
//            try {
//                if (stOrder != null) {
//                    stOrder.close();
//                }
//            } catch (Exception e) {
//            }
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return false;
//    }
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

//    public java.util.List<java.util.Map<String, Object>> getTestsForProcessing(int medicalRecordId) {
//        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
//
//        // Join với LabReferenceRange dựa trên Tuổi và Giới tính bệnh nhân
//        String sql = "SELECT "
//                + "c.CategoryName, "
//                + "b.BatchId, "
//                + "lot.LabOrderTestId, "
//                + "lot.Status AS TestStatus, "
//                + "lot.RejectReason, "
//                + "t.TestName, "
//                + "p.ParameterId, "
//                + "p.ParameterName, "
//                + "COALESCE(r.Unit, p.Unit) AS Unit, "
//                // Ưu tiên lấy RefMin/Max từ bảng LabResult nếu đã nhập. Nếu chưa nhập thì lấy từ quy tắc phù hợp (rr)
//                + "COALESCE(r.RefMin, rr.RefMin) AS RefMin, "
//                + "COALESCE(r.RefMax, rr.RefMax) AS RefMax, "
//                + "r.ResultId, "
//                + "r.ResultValue, "
//                + "r.Flag "
//                + "FROM LabTestBatch b "
//                + "JOIN Patient pat ON b.PatientId = pat.PatientId " // Cần bảng Patient để lấy Tuổi, Giới tính
//                + "JOIN LabOrderTest lot ON b.BatchId = lot.BatchId "
//                + "JOIN ServiceOrder so ON lot.ServiceOrderId = so.ServiceOrderId "
//                + "JOIN LabTest t ON lot.LabTestId = t.LabTestId "
//                + "JOIN LabTestCategory c ON t.CategoryId = c.CategoryId "
//                + "JOIN LabTestParameter p ON t.LabTestId = p.LabTestId "
//                // 💎 LEFT JOIN Quyết định: Tìm Range phù hợp với bệnh nhân này
//                + "LEFT JOIN LabReferenceRange rr ON p.ParameterId = rr.ParameterId "
//                + "     AND rr.IsActive = 1 "
//                + "     AND ("
//                + "         rr.Gender = 'ALL' "
//                + "         OR (rr.Gender = 'M' AND pat.Gender IN ('Nam', 'Male', 'M')) "
//                + "         OR (rr.Gender = 'F' AND pat.Gender IN ('Nữ', 'Nu', 'Female', 'F')) "
//                + "     ) "
//                + "     AND DATEDIFF(day, pat.DateOfBirth, GETDATE()) BETWEEN rr.AgeMinDays AND rr.AgeMaxDays "
//                + "LEFT JOIN LabResult r ON lot.LabOrderTestId = r.LabOrderTestId AND p.ParameterId = r.ParameterId "
//                + "WHERE b.MedicalRecordId = ? "
//                + "AND so.Status = 'PAID' "
//                + "AND b.Status != 'CANCELLED' "
//                + "AND lot.Status != 'CANCELLED' "
//                + "AND (t.IsActive = 1 OR r.ResultValue IS NOT NULL) "
//                + "AND ( (p.IsActive = 1 AND lot.Status != 'COMPLETED') OR r.ResultId IS NOT NULL ) "
//                + "ORDER BY c.SortOrder ASC, t.SortOrder ASC, p.SortOrder ASC";
//
//        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {
//
//            st.setInt(1, medicalRecordId);
//            try (java.sql.ResultSet rs = st.executeQuery()) {
//                while (rs.next()) {
//                    java.util.Map<String, Object> map = new java.util.HashMap<>();
//                    map.put("batchId", rs.getInt("BatchId"));
//                    map.put("categoryName", rs.getString("CategoryName"));
//                    map.put("labOrderTestId", rs.getInt("LabOrderTestId"));
//                    map.put("status", rs.getString("TestStatus"));
//                    map.put("rejectReason", rs.getString("RejectReason"));
//                    map.put("testName", rs.getString("TestName"));
//                    map.put("parameterId", rs.getInt("ParameterId"));
//                    map.put("parameterName", rs.getString("ParameterName"));
//                    map.put("unit", rs.getString("Unit"));
//
//                    // Nối dải tham chiếu bình thường (VD: 4.0 - 5.5)
//                    Object refMin = rs.getObject("RefMin");
//                    Object refMax = rs.getObject("RefMax");
//                    String normalRange = (refMin != null ? refMin.toString() : "") + " - " + (refMax != null ? refMax.toString() : "");
//                    map.put("normalRange", normalRange.equals(" - ") ? "___" : normalRange);
//
//                    map.put("isNumeric", (refMin != null || refMax != null));
//                    map.put("refMin", refMin);
//                    map.put("refMax", refMax);
//
//                    // Lấy kết quả (nếu đã nhập)
//                    map.put("resultId", rs.getObject("ResultId"));
//                    map.put("resultValue", rs.getString("ResultValue"));
//
//                    // Cờ bất thường (Flag)
//                    String flag = rs.getString("Flag");
//                    map.put("isAbnormal", (flag != null && !flag.trim().isEmpty()));
//                    map.put("flag", flag);
//
//                    list.add(map);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
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
    public boolean saveLabResults(TestResult testResult) {
        java.sql.Connection conn = null;

        // 🔥 SỬA LẠI CÂU LỆNH LẤY SNAPSHOT: Truyền MedicalRecordId để truy vết ra Bệnh nhân, từ đó tìm đúng Range
        String sqlGetSnapshot = "SELECT TOP 1 rr.RefMin, rr.RefMax, p.Unit "
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
                // Ưu tiên Rule càng hẹp (có giới tính cụ thể) càng tốt
                + "ORDER BY CASE WHEN rr.Gender = 'ALL' THEN 2 ELSE 1 END ASC";

        String sqlCheckResult = "SELECT ResultId FROM LabResult WHERE LabOrderTestId = ? AND ParameterId = ?";
        String sqlInsertResult = "INSERT INTO LabResult (LabOrderTestId, ParameterId, ResultValue, Flag, RefMin, RefMax, Unit, ResultTime) VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";
        String sqlUpdateResult = "UPDATE LabResult SET ResultValue = ?, Flag = ?, RefMin = ?, RefMax = ?, Unit = ?, ResultTime = GETDATE() WHERE ResultId = ?";
        String sqlUpdateOrderTest = "UPDATE LabOrderTest SET Status = 'COMPLETED' WHERE LabOrderTestId = ?";
        String sqlUpdateBatch = "UPDATE LabTestBatch SET Status = 'COMPLETED', TechnicianId = ? "
                + "WHERE BatchId IN (SELECT BatchId FROM LabOrderTest WHERE LabOrderTestId = ?) "
                + "AND NOT EXISTS ("
                + "    SELECT 1 FROM LabOrderTest lot2 "
                + "    WHERE lot2.BatchId = (SELECT BatchId FROM LabOrderTest WHERE LabOrderTestId = ?) "
                + "    AND lot2.Status != 'COMPLETED'"
                + "    AND lot2.Status != 'CANCELLED'"
                + ")";

        try {
            conn = new DBContext().conn;
            conn.setAutoCommit(false);

            java.sql.PreparedStatement stGetSnapshot = conn.prepareStatement(sqlGetSnapshot);
            java.sql.PreparedStatement stCheckResult = conn.prepareStatement(sqlCheckResult);
            java.sql.PreparedStatement stInsert = conn.prepareStatement(sqlInsertResult);
            java.sql.PreparedStatement stUpdateResult = conn.prepareStatement(sqlUpdateResult);
            java.sql.PreparedStatement stUpdateOrder = conn.prepareStatement(sqlUpdateOrderTest);
            java.sql.PreparedStatement stUpdateBatch = conn.prepareStatement(sqlUpdateBatch);

            boolean hasInsert = false;
            boolean hasUpdate = false;
            boolean hasStatusUpdate = false;

            String[] orderTestIds = testResult.getOrderTestIds();
            String[] paramIds = testResult.getParamIds();
            if (orderTestIds != null && paramIds != null) {
                for (int i = 0; i < orderTestIds.length; i++) {
                    String orderTestId = orderTestIds[i];
                    String paramId = paramIds[i];

                    String[] resultValues = testResult.getParameterMap().get("result_" + orderTestId + "_" + paramId);
                    String resultValue = (resultValues != null && resultValues.length > 0) ? resultValues[0] : "";

                    if (resultValue != null && !resultValue.trim().isEmpty()) {

                        java.math.BigDecimal refMin = null;
                        java.math.BigDecimal refMax = null;
                        String unit = null;

                        stGetSnapshot.setInt(1, testResult.getMedicalRecordId());
                        stGetSnapshot.setInt(2, Integer.parseInt(paramId));

                        try (java.sql.ResultSet rsSnap = stGetSnapshot.executeQuery()) {
                            if (rsSnap.next()) {
                                refMin = rsSnap.getBigDecimal("RefMin");
                                refMax = rsSnap.getBigDecimal("RefMax");
                                unit = rsSnap.getString("Unit");
                            }
                        }

                        // AUTO FLAG LOGIC (Giữ nguyên của bạn)
                        String finalFlag = null;
                        try {
                            double val = Double.parseDouble(resultValue.replace(",", ".").trim());
                            if (refMin != null && val < refMin.doubleValue()) {
                                finalFlag = "L";
                            } else if (refMax != null && val > refMax.doubleValue()) {
                                finalFlag = "H";
                            }
                        } catch (Exception e) {
                        }

                        if (finalFlag == null) {
                            String[] flags = testResult.getParameterMap().get("flag_" + orderTestId + "_" + paramId);
                            if (flags != null && flags.length > 0 && flags[0] != null) {
                                String uiFlag = flags[0].trim().toLowerCase();
                                if (uiFlag.equals("on") || uiFlag.equals("true") || uiFlag.equals("y") || uiFlag.equals("1")) {
                                    finalFlag = "Y";
                                }
                            }
                        }

                        Integer existResultId = null;
                        stCheckResult.setInt(1, Integer.parseInt(orderTestId));
                        stCheckResult.setInt(2, Integer.parseInt(paramId));
                        try (java.sql.ResultSet rsCheck = stCheckResult.executeQuery()) {
                            if (rsCheck.next()) {
                                existResultId = rsCheck.getInt("ResultId");
                            }
                        }

                        if (existResultId == null) {
                            stInsert.setInt(1, Integer.parseInt(orderTestId));
                            stInsert.setInt(2, Integer.parseInt(paramId));
                            stInsert.setString(3, resultValue.trim());
                            stInsert.setString(4, finalFlag);
                            stInsert.setBigDecimal(5, refMin);
                            stInsert.setBigDecimal(6, refMax);
                            stInsert.setString(7, unit);
                            stInsert.addBatch();
                            hasInsert = true;
                        } else {
                            stUpdateResult.setString(1, resultValue.trim());
                            stUpdateResult.setString(2, finalFlag);
                            stUpdateResult.setBigDecimal(3, refMin);
                            stUpdateResult.setBigDecimal(4, refMax);
                            stUpdateResult.setString(5, unit);
                            stUpdateResult.setInt(6, existResultId);
                            stUpdateResult.addBatch();
                            hasUpdate = true;
                        }

                        stUpdateOrder.setInt(1, Integer.parseInt(orderTestId));
                        stUpdateOrder.addBatch();

                        stUpdateBatch.setInt(1, testResult.getTechnicianId());
                        stUpdateBatch.setInt(2, Integer.parseInt(orderTestId));
                        stUpdateBatch.setInt(3, Integer.parseInt(orderTestId));
                        stUpdateBatch.addBatch();
                        hasStatusUpdate = true;
                    }
                }
            }

            if (hasInsert) {
                stInsert.executeBatch();
            }
            if (hasUpdate) {
                stUpdateResult.executeBatch();
            }
            if (hasStatusUpdate) {
                stUpdateOrder.executeBatch();
                stUpdateBatch.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            // (Bạn copy lại đoạn Finally đóng PreparedStatement vào nhé)
        }
    }

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
