package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Certification {

    private int certificationId;
    private int userId;

    private String certificateName;
    private String certificateNumber;

    private String fullName;
    private String phoneNumber;
    private String roleName;
    private Date issueDate;
    private Date expiryDate;

    private String filePath;
    private String status;

    private Integer verifiedBy;
    private Timestamp verifiedAt;

    private String rejectionNote;

    public Certification() {
    }

    public Certification(int certificationId, int userId, String certificateName,
            String certificateNumber, Date issueDate, Date expiryDate,
            String filePath, String status, Integer verifiedBy,
            Timestamp verifiedAt) {

        this.certificationId = certificationId;
        this.userId = userId;
        this.certificateName = certificateName;
        this.certificateNumber = certificateNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.filePath = filePath;
        this.status = status;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = verifiedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Timestamp getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Timestamp verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
