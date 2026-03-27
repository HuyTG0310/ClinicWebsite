/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.*;
import util.DBContext;

/**
 *
 * @author Tan Vinh
 */
public class DoctorSpecialtyDAO extends DBContext {

    public List<Integer> getSpecialtyIdsByDoctor(int doctorId) {

        List<Integer> list = new ArrayList<>();

        String sql = "            SELECT SpecialtyId\n"
                + "            FROM DoctorSpecialty\n"
                + "            WHERE UserId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("SpecialtyId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public Integer getPrimarySpecialtyId(int doctorId) {

        String sql = "            SELECT SpecialtyId\n"
                + "            FROM DoctorSpecialty\n"
                + "            WHERE UserId = ? AND IsPrimary = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("SpecialtyId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteByDoctor(int doctorId) {

        String sql = "DELETE FROM DoctorSpecialty WHERE UserId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insert(int doctorId, int specialtyId, boolean isPrimary) {

        String sql = "            INSERT INTO DoctorSpecialty (UserId, SpecialtyId, IsPrimary)\n"
                + "            VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, specialtyId);
            ps.setBoolean(3, isPrimary);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
