package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Patient;
import util.DBContext;

/**
 *
 * @author Chi Duong
 */
public class PatientDAO extends DBContext {

    
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT TOP (1000) [PatientId], [FullName], [Phone], [DateOfBirth], "
                + "[Gender], [Address], [MedicalHistory], [Allergy], [CreatedAt] "
                + "FROM Patient";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("PatientId"));
                patient.setFullName(rs.getString("FullName"));
                patient.setPhone(rs.getString("Phone"));
                patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                patient.setGender(rs.getString("Gender"));
                patient.setAddress(rs.getString("Address"));
                patient.setMedicalHistory(rs.getString("MedicalHistory"));
                patient.setAllergy(rs.getString("Allergy"));
                patient.setCreatedAt(rs.getDate("CreatedAt"));

                patients.add(patient);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error getting all patients: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }

    
    public Patient getPatientById(int patientId) {
        Patient patient = null;
        String query = "SELECT [PatientId], [FullName], [Phone], [DateOfBirth], "
                + "[Gender], [Address], [MedicalHistory], [Allergy], [CreatedAt] "
                + "FROM Patient WHERE [PatientId] = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                patient = new Patient();
                patient.setPatientId(rs.getInt("PatientId"));
                patient.setFullName(rs.getString("FullName"));
                patient.setPhone(rs.getString("Phone"));
                patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                patient.setGender(rs.getString("Gender"));
                patient.setAddress(rs.getString("Address"));
                patient.setMedicalHistory(rs.getString("MedicalHistory"));
                patient.setAllergy(rs.getString("Allergy"));
                patient.setCreatedAt(rs.getDate("CreatedAt"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error getting patient by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return patient;
    }

   
    public List<Patient> searchPatientByName(String fullName) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT [PatientId], [FullName], [Phone], [DateOfBirth], "
                + "[Gender], [Address], [MedicalHistory], [Allergy], [CreatedAt] "
                + "FROM Patient WHERE [FullName] LIKE ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + fullName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("PatientId"));
                patient.setFullName(rs.getString("FullName"));
                patient.setPhone(rs.getString("Phone"));
                patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                patient.setGender(rs.getString("Gender"));
                patient.setAddress(rs.getString("Address"));
                patient.setMedicalHistory(rs.getString("MedicalHistory"));
                patient.setAllergy(rs.getString("Allergy"));
                patient.setCreatedAt(rs.getDate("CreatedAt"));

                patients.add(patient);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error searching patient by name: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }

   
    public boolean addPatient(Patient patient) {
        String query = "INSERT INTO Patient ([FullName], [Phone], [DateOfBirth], "
                + "[Gender], [Address], [MedicalHistory], [Allergy], [CreatedAt]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, patient.getFullName());
            ps.setString(2, patient.getPhone());
            ps.setDate(3, patient.getDateOfBirth());
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getMedicalHistory());
            ps.setString(7, patient.getAllergy());
            ps.setDate(8, new java.sql.Date(System.currentTimeMillis()));

            int result = ps.executeUpdate();
            ps.close();

            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

  
    public boolean isPhoneExists(String phone, Integer excludePatientId) {
        String query = excludePatientId != null
                ? "SELECT COUNT(*) as count FROM Patient WHERE [Phone] = ? AND [PatientId] != ?"
                : "SELECT COUNT(*) as count FROM Patient WHERE [Phone] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, phone);
            if (excludePatientId != null) {
                ps.setInt(2, excludePatientId);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                ps.close();
                return count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra số điện thoại: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

  
    public boolean isPhoneExists(String phone) {
        String query = "SELECT COUNT(*) as count FROM Patient WHERE [Phone] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                ps.close();
                return count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error checking if phone exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean isDuplicatePatient(String fullName, java.sql.Date dateOfBirth, Integer excludePatientId) {
        String query = excludePatientId != null
                ? "SELECT COUNT(*) as count FROM Patient WHERE [FullName] = ? AND [DateOfBirth] = ? AND [PatientId] != ?"
                : "SELECT COUNT(*) as count FROM Patient WHERE [FullName] = ? AND [DateOfBirth] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setDate(2, dateOfBirth);
            if (excludePatientId != null) {
                ps.setInt(3, excludePatientId);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                ps.close();
                return count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra bệnh nhân trùng lặp: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

   
    public boolean isDuplicatePatient(String fullName, java.sql.Date dateOfBirth) {
        String query = "SELECT COUNT(*) as count FROM Patient WHERE [FullName] = ? AND [DateOfBirth] = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setDate(2, dateOfBirth);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                ps.close();
                return count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error checking for duplicate patient: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

   
    public List<Patient> searchPatientByMultiFields(String keyword) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT TOP (1000) [PatientId], [FullName], [Phone], [DateOfBirth], "
                + "[Gender], [Address], [MedicalHistory], [Allergy], [CreatedAt] "
                + "FROM Patient "
                + "WHERE [FullName] LIKE ? OR [Phone] LIKE ? OR [Address] LIKE ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            String searchKeyword = "%" + keyword.trim() + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(rs.getInt("PatientId"));
                patient.setFullName(rs.getString("FullName"));
                patient.setPhone(rs.getString("Phone"));
                patient.setDateOfBirth(rs.getDate("DateOfBirth"));
                patient.setGender(rs.getString("Gender"));
                patient.setAddress(rs.getString("Address"));
                patient.setMedicalHistory(rs.getString("MedicalHistory"));
                patient.setAllergy(rs.getString("Allergy"));
                patient.setCreatedAt(rs.getDate("CreatedAt"));

                patients.add(patient);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error searching patients: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }

        public boolean updatePatient(Patient patient) {
        String query = "UPDATE Patient "
                + "SET [FullName] = ?, [Phone] = ?, [DateOfBirth] = ?, [Gender] = ?, "
                + "[Address] = ?, [MedicalHistory] = ?, [Allergy] = ? "
                + "WHERE [PatientId] = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, patient.getFullName());
            ps.setString(2, patient.getPhone());
            ps.setDate(3, patient.getDateOfBirth());
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getMedicalHistory());
            ps.setString(7, patient.getAllergy());
            ps.setInt(8, patient.getPatientId());

            int result = ps.executeUpdate();
            ps.close();

            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

   
    public boolean deletePatient(int patientId) {
        String query = "DELETE FROM Patient WHERE [PatientId] = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, patientId);

            int result = ps.executeUpdate();
            ps.close();

            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
