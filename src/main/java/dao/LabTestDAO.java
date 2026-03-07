/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DBContext;

/**
 *
 * @author huytr
 */
public class LabTestDAO extends DBContext {

    public boolean insertFullLabTest(model.Service service, model.LabTest labTest, java.util.List<model.LabTestParameter> parameters) {
        String sqlService = "INSERT INTO Service (ServiceName, Category, CurrentPrice, IsActive) VALUES (?, ?, ?, 1)";
        String sqlLabTest = "INSERT INTO LabTest (ServiceId, TestCode, TestName, CategoryId, IsPanel, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, ?, 1)";
        // ĐÃ BỎ RefMin, RefMax
        String sqlParam = "INSERT INTO LabTestParameter (LabTestId, ParameterCode, ParameterName, Unit, SortOrder, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
        // 🔥 THÊM MỚI: Câu lệnh Insert Range
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

                // 🔥 DUYỆT VÀ INSERT TẤT CẢ RANGES CỦA PARAMETER NÀY
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
            psRange.executeBatch(); // Quất 1 lần toàn bộ Ranges

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

            // 1 & 2. Update Service & LabTest (Giống cũ)
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

            // Chiến thuật "Bàn tay sắt": Xóa sạch Range cũ của Param đó và Insert lại toàn bộ Range mới (nhanh và an toàn nhất)
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

                    // Móc thêm danh sách Parameters
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

}
