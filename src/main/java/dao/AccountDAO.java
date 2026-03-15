/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DBContext;

/**
 *
 * @author Tai Loi
 */
public class AccountDAO extends DBContext {

    public boolean checkEmailExist(String email) {

        String sql = "SELECT Email FROM [User] WHERE LOWER(Email) = LOWER(?)";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.trim());

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void updatePassword(String email, String password) {

        String sql = "UPDATE [User] SET PasswordHash = ? WHERE Email = ?";

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, password);
            ps.setString(2, email);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
