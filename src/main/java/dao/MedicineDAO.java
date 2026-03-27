/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Medicine;
import util.DBContext;

/**
 *
 * @author TRUONGTHINHNGUYEN
 */
public class MedicineDAO extends DBContext {

    /* =====================================================
       1. VIEW + SEARCH MEDICINE LIST
       ===================================================== */
    public List<Medicine> search(String keyword, String status) {
        List<Medicine> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT MedicineId, MedicineName, Unit, IsActive "
                + "FROM Medicine "
                + "WHERE MedicineName LIKE ? "
        );

        // Nếu không phải all thì mới filter IsActive
        if (status != null && !status.equalsIgnoreCase("all")) {
            sql.append("AND IsActive = ? ");
        }

        sql.append("ORDER BY MedicineId");

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, "%" + keyword + "%");

            if (status != null && !status.equalsIgnoreCase("all")) {
                boolean isActive = status.equalsIgnoreCase("active");
                ps.setBoolean(2, isActive);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("MedicineId"));
                m.setMedicineName(rs.getString("MedicineName"));
                m.setUnit(rs.getString("Unit"));
                m.setIsActive(rs.getBoolean("IsActive"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Medicine> getAll() {
        List<Medicine> list = new ArrayList<>();

        String sql = "            SELECT MedicineId, MedicineName, Unit, IsActive\n"
                + "            FROM Medicine\n"
                + "            ORDER BY MedicineId";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("MedicineId"));
                m.setMedicineName(rs.getString("MedicineName"));
                m.setUnit(rs.getString("Unit"));
                m.setIsActive(rs.getBoolean("IsActive"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /* =====================================================
       2. ADD MEDICINE
       ===================================================== */
    public void insert(Medicine m) {

        String sql = "INSERT INTO Medicine\n"
                + "            (MedicineName, Unit, Ingredients, Usage, Contraindication, IsActive)\n"
                + "            VALUES (?, ?, ?, ?, ?, 1)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getMedicineName());
            ps.setString(2, m.getUnit());
            ps.setString(3, m.getIngredients());
            ps.setString(4, m.getUsage());
            ps.setString(5, m.getContraindication());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       3. GET MEDICINE BY ID (DETAIL + EDIT)
       ===================================================== */
    public Medicine getById(int id) {

        String sql = "SELECT *\n"
                + "            FROM Medicine\n"
                + "            WHERE MedicineId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("MedicineId"));
                m.setMedicineName(rs.getString("MedicineName"));
                m.setUnit(rs.getString("Unit"));
                m.setIngredients(rs.getString("Ingredients"));
                m.setUsage(rs.getString("Usage"));
                m.setContraindication(rs.getString("Contraindication"));
                m.setIsActive(rs.getBoolean("IsActive"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* =====================================================
       4. UPDATE MEDICINE
       ===================================================== */
    public void update(Medicine m) {

        String sql = " UPDATE Medicine\n"
                + "SET MedicineName = ?, "
                + "Unit = ?, "
                + "Ingredients = ?, "
                + "Usage = ?, "
                + "Contraindication = ?, "
                + "IsActive = ? "
                + "WHERE MedicineId = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, m.getMedicineName());
            ps.setString(2, m.getUnit());
            ps.setString(3, m.getIngredients());
            ps.setString(4, m.getUsage());
            ps.setString(5, m.getContraindication());
            ps.setBoolean(6, m.isIsActive());
            ps.setInt(7, m.getMedicineId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<model.Prescription> getPrescriptionsByRecordId(int medicalRecordId) {
        java.util.List<model.Prescription> list = new java.util.ArrayList<>();
        String sql = "SELECT MedicineId, Quantity, Dosage, Note "
                + "FROM Prescription WHERE MedicalRecordId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    model.Prescription p = new model.Prescription();
                    p.setMedicineId(rs.getInt("MedicineId"));
                    p.setQuantity(rs.getInt("Quantity"));
                    p.setDosage(rs.getString("Dosage"));
                    p.setNote(rs.getString("Note"));
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Medicine> getAllActiveMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM Medicine WHERE IsActive = 1 ORDER BY MedicineName ASC";
        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("MedicineId"));
                m.setMedicineName(rs.getString("MedicineName"));
                m.setUnit(rs.getString("Unit"));
                m.setUsage(rs.getString("Usage"));
                m.setContraindication(rs.getString("Contraindication"));
                m.setIngredients(rs.getString("Ingredients"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public java.util.List<model.PrescriptionItem> getPrescriptionItems(int medicalRecordId) {
        java.util.List<model.PrescriptionItem> list = new java.util.ArrayList<>();

        String sql = "SELECT m.MedicineName, m.Unit, pd.Quantity, pd.Dosage, pd.Note "
                + "FROM Prescription pd "
                + "JOIN Medicine m ON pd.MedicineId = m.MedicineId "
                + "WHERE pd.MedicalRecordId = ?";

        try (java.sql.Connection conn = new DBContext().conn; java.sql.PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, medicalRecordId);
            try (java.sql.ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    model.PrescriptionItem item = new model.PrescriptionItem();
                    item.setMedicineName(rs.getString("MedicineName"));
                    item.setUnit(rs.getString("Unit"));
                    item.setQuantity(rs.getInt("Quantity"));
                    item.setDosage(rs.getString("Dosage"));
                    item.setNote(rs.getString("Note"));
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkExistName(String medicineName) {
        String sql = "SELECT 1 FROM Medicine WHERE MedicineName = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, medicineName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        Medicine m = new Medicine(1, "pARAMACE", "Vỉ", "Ko", "ko", "ko", true);
        new MedicineDAO().update(m);

        for (Medicine me : new MedicineDAO().search("a", "")) {
            System.out.println(me);
        }
    }
}
