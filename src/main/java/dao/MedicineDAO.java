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

    /* =====================================================
       5. DELETE SOFT (IsActive = 0)
       ===================================================== */
//    public void updateIsActive(int medicineId, boolean isActive) {
//
//        String sql = "UPDATE Medicine "
//                + "SET IsActive = ? "
//                + "WHERE MedicineId = ?";
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setBoolean(1, isActive);
//            ps.setInt(2, medicineId);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args) {
        Medicine m = new Medicine(1, "pARAMACE", "Vỉ", "Ko", "ko", "ko", true);
        new MedicineDAO().update(m);

        for (Medicine me : new MedicineDAO().search("a", "")) {
            System.out.println(me);
        }
    }
}
