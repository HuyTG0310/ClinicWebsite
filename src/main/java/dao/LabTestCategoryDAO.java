/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.DBContext;

/**
 *
 * @author huytr
 */
public class LabTestCategoryDAO extends DBContext {

    public List<LabTestCategory> getAllCategories() {
        List<LabTestCategory> list = new ArrayList<>();

        String sql = "SELECT * FROM LabTestCategory ORDER BY SortOrder ASC";
        try (Connection conn = new DBContext().conn; PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                LabTestCategory cat = new LabTestCategory();
                cat.setCategoryId(rs.getInt("CategoryId"));
                cat.setCategoryCode(rs.getString("CategoryCode"));
                cat.setCategoryName(rs.getString("CategoryName"));
                cat.setSortOrder(rs.getInt("SortOrder"));
                list.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
}
