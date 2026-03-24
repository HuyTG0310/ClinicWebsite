/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import model.Service;
import util.DBContext;

/**
 *
 * @author Gia Huy
 */
public class ServiceDAO extends DBContext {

    public List<Service> getAll() {
        List<Service> list = new ArrayList<>();

        String sql = "            SELECT ServiceId,\n"
                + "                   ServiceName,\n"
                + "                   Category,\n"
                + "                   CurrentPrice,\n"
                + "                   IsActive\n"
                + "            FROM Service\n"
                + "            ORDER BY ServiceId";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getInt("ServiceId"));
                s.setServiceName(rs.getString("ServiceName"));
                s.setCategory(rs.getString("Category"));
                s.setCurrentPrice(rs.getBigDecimal("CurrentPrice"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Service getById(int serviceId) {

        String sql = "        SELECT ServiceId,\n"
                + "               ServiceName,\n"
                + "               Category,\n"
                + "               CurrentPrice,\n"
                + "               IsActive\n"
                + "        FROM Service\n"
                + "        WHERE ServiceId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getInt("ServiceId"));
                s.setServiceName(rs.getString("ServiceName"));
                s.setCategory(rs.getString("Category"));
                s.setCurrentPrice(rs.getBigDecimal("CurrentPrice"));
                s.setIsActive(rs.getBoolean("IsActive"));
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(Service s) {

        String sql = "INSERT INTO Service (ServiceName, Category, CurrentPrice, IsActive) VALUES (?, ?, ?, 1)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCategory());
            ps.setBigDecimal(3, s.getCurrentPrice());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Service s) {

        String sql = "        UPDATE Service\n"
                + "        SET ServiceName = ?,\n"
                + "            Category = ?,\n"
                + "            CurrentPrice = ?,\n"
                + "            IsActive = ?\n"
                + "        WHERE ServiceId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCategory());
            ps.setBigDecimal(3, s.getCurrentPrice());
            ps.setBoolean(4, s.isIsActive());
            ps.setInt(5, s.getServiceId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Service> search(String keyword, String status) {

        List<Service> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("        SELECT ServiceId,\n"
                + "               ServiceName,\n"
                + "               Category,\n"
                + "               CurrentPrice,\n"
                + "               IsActive\n"
                + "        FROM Service\n"
                + "        WHERE 1 = 1");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND ServiceName LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND IsActive = ?");
            params.add(status.equals("1"));
        }

        sql.append(" ORDER BY ServiceId");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getInt("ServiceId"));
                s.setServiceName(rs.getString("ServiceName"));
                s.setCategory(rs.getString("Category"));
                s.setCurrentPrice(rs.getBigDecimal("CurrentPrice"));
                s.setIsActive(rs.getBoolean("IsActive"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
        Service s = new Service();
        s.setServiceName("ABC");
        s.setCategory("ABC");
        s.setCurrentPrice(new BigDecimal(10000));
        new ServiceDAO().insert(s);
    }

}
