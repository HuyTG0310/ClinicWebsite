/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.doctor.medicalRecord;

import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.*;

@WebServlet(name = "MedicalRecordEditServlet", urlPatterns = {"/doctor/medical-record/edit", "/admin/medical-record/edit"})
public class MedicalRecordEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        // 1. Nhận cả 2 loại ID 
        String appIdRaw = request.getParameter("appointmentId");
        String mrIdRaw = request.getParameter("id"); // Hoặc tên param khác nếu bạn muốn (vd: mrId)

        // Nếu không có ID nào
        if ((appIdRaw == null || appIdRaw.isEmpty()) && (mrIdRaw == null || mrIdRaw.isEmpty())) {
            response.sendRedirect(basePath + "/medical-record/list");
            return;
        }

        try {
            dao.AppointmentDAO appDAO = new dao.AppointmentDAO();
            dao.MedicalRecordDAO mrDAO = new dao.MedicalRecordDAO();

            model.Appointment app = null;
            model.MedicalRecord mr = null;
            int appointmentId = 0;
            int actualRecordId = 0;

            // LUỒNG 1: TRUY CẬP TỪ TRANG HÀNG CHỜ (QUA APPOINTMENT_ID)
            if (appIdRaw != null && !appIdRaw.isEmpty()) {
                appointmentId = Integer.parseInt(appIdRaw);
                app = appDAO.getAppointmentDetailById(appointmentId);

                request.setAttribute("allergies", new PatientDAO().getPatientById(app.getPatientId()).getAllergy());

                if (app == null || "COMPLETED".equals(app.getStatus()) || "CANCELLED".equals(app.getStatus())) {
                    request.getSession().setAttribute("error", "Appointment is not exist or finished!");
                    response.sendRedirect(basePath + "/doctor/queue/list");
                    return;
                }

                // Nếu app có medicalRecordId thì tức là đang lưu nháp
                if (app.getMedicalRecordId() != null && app.getMedicalRecordId() > 0) {
                    mr = mrDAO.getRecordById(app.getMedicalRecordId());
                }
            } // LUỒNG 2: TRUY CẬP TỪ TRANG DANH SÁCH BA (QUA MEDICAL_RECORD_ID)
            else if (mrIdRaw != null && !mrIdRaw.isEmpty()) {
                actualRecordId = Integer.parseInt(mrIdRaw);
                mr = mrDAO.getRecordById(actualRecordId);

                if (mr == null) {
                    request.getSession().setAttribute("error", "Medical record not found!");
                    response.sendRedirect(basePath + "/medical-record/list");
                    return;
                }
            }

            // (Chỉ áp dụng khi có được mr)
            if (mr != null) {
                actualRecordId = mr.getMedicalRecordId();
                model.User currentUser = (model.User) request.getSession().getAttribute("user");
                boolean isAdmin = (currentUser.getRoleId() == 1);

                boolean[] perms = mrDAO.checkPermissionDetail(actualRecordId, currentUser.getUserId(), isAdmin);
                if (!perms[1]) { // perms[1] là cờ canEdit
                    request.getSession().setAttribute("error", "SECURITY: You DON'T HAVE PERMISSION to edit this medical record!");
                    response.sendRedirect(basePath + "/medical-record/list");
                    return;
                }

                request.setAttribute("mr", mr);

                //Cận lâm sàng & Toa thuốc như cũ
                dao.LabTestDAO testDAO = new dao.LabTestDAO();
                dao.MedicineDAO medDAO = new dao.MedicineDAO();

                request.setAttribute("orderedTestIds", testDAO.getOrderedTestIds(actualRecordId));
                request.setAttribute("batches", testDAO.getBatchesForMedicalRecord(actualRecordId));
                request.setAttribute("consolidatedResults", testDAO.getConsolidatedLabResults(actualRecordId));
                request.setAttribute("savedPrescriptions", medDAO.getPrescriptionsByRecordId(actualRecordId));
            }

            // danh mục Thuốc và Xét nghiệm để đổ vào Form Chọn
            dao.MedicineDAO medDAO = new dao.MedicineDAO();
            request.setAttribute("medicines", medDAO.getAllActiveMedicines());
            dao.LabTestDAO testDAO = new dao.LabTestDAO();
            request.setAttribute("labTests", testDAO.getAllActiveTestsForDoctor());

            request.setAttribute("app", app);
            request.setAttribute("pageTitle", "Medical Record Edit");
            request.setAttribute("activePage", "manageMedicalRecord");
            request.setAttribute("contentPage", "/WEB-INF/doctor/medicalrecord/medicalRecordEdit.jsp");

            request.getRequestDispatcher(layout).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(basePath + "/medical-record/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        String layout;
        String basePath;

        if (uri.startsWith(ctx + "/admin")) {
            layout = "/WEB-INF/layout/adminLayout.jsp";
            basePath = ctx + "/admin";
        } else if (uri.startsWith(ctx + "/doctor")) {
            layout = "/WEB-INF/layout/doctorLayout.jsp";
            basePath = ctx + "/doctor";
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("basePath", basePath);

        try {
            HttpSession session = request.getSession(false);
            model.User doctor = (model.User) session.getAttribute("user");

            String appIdRaw = request.getParameter("appointmentId");
            int appointmentId = (appIdRaw != null && !appIdRaw.isEmpty()) ? Integer.parseInt(appIdRaw) : 0;

            String patientIdRaw = request.getParameter("patientId");
            int patientId = (patientIdRaw != null && !patientIdRaw.isEmpty()) ? Integer.parseInt(patientIdRaw) : 0;

            String mrIdRaw = request.getParameter("medicalRecordId");
            int medicalRecordId = (mrIdRaw != null && !mrIdRaw.isEmpty()) ? Integer.parseInt(mrIdRaw) : 0;

            String[] medicineIds = request.getParameterValues("medicineId");
            String[] quantities = request.getParameterValues("quantity");
            String[] dosages = request.getParameterValues("dosage");
            String[] notes = request.getParameterValues("note");

            java.util.List<model.Prescription> prescriptionList = new java.util.ArrayList<>();
            if (medicineIds != null && medicineIds.length > 0) {
                for (int i = 0; i < medicineIds.length; i++) {
                    if (medicineIds[i] != null && !medicineIds[i].trim().isEmpty()) {
                        model.Prescription p = new model.Prescription();
                        p.setMedicineId(Integer.parseInt(medicineIds[i]));
                        p.setQuantity(Integer.parseInt(quantities[i]));
                        p.setDosage(dosages[i] != null ? dosages[i] : "");
                        p.setNote(notes[i] != null ? notes[i] : "");
                        prescriptionList.add(p);
                    }
                }
            }

            model.MedicalRecord mr = new model.MedicalRecord();
            mr.setMedicalRecordId(medicalRecordId);
            mr.setPatientId(patientId);
            mr.setResponsibleDoctorId(doctor.getUserId());
            mr.setPrescriptions(prescriptionList);

            String temp = request.getParameter("temperature");
            if (temp != null && !temp.isEmpty()) {
                mr.setTemperature(Double.parseDouble(temp));
            }
            mr.setBloodPressure(request.getParameter("bloodPressure"));
            String hr = request.getParameter("heartRate");
            if (hr != null && !hr.isEmpty()) {
                mr.setHeartRate(Integer.parseInt(hr));
            }
            String weight = request.getParameter("weight");
            if (weight != null && !weight.isEmpty()) {
                mr.setWeight(Double.parseDouble(weight));
            }
            String height = request.getParameter("height");
            if (height != null && !height.isEmpty()) {
                mr.setHeight(Double.parseDouble(height));
            }

            mr.setSymptom(request.getParameter("symptom"));
            mr.setPhysicalExam(request.getParameter("physicalExam"));
            mr.setDiagnosis(request.getParameter("diagnosis"));
            mr.setTreatmentPlan(request.getParameter("treatmentPlan"));
            mr.setDoctorNotes(request.getParameter("doctorNotes"));

            String followUpStr = request.getParameter("followUpDate");
            if (followUpStr != null && !followUpStr.isEmpty()) {
                mr.setFollowUpDate(java.sql.Date.valueOf(followUpStr));
                mr.setFollowUpStatus("PENDING");
            }

            String action = request.getParameter("action");
            dao.MedicalRecordDAO mrDAO = new dao.MedicalRecordDAO();
            dao.MedicineDAO medDAO = new dao.MedicineDAO();

            // 🔥 XỬ LÝ RIÊNG CHO LUỒNG "LƯU THAY ĐỔI BỆNH ÁN CŨ"
            if ("update_only".equals(action)) {
                mrDAO.updateCompletedMedicalRecord(mr); // Gọi hàm mới tạo
                medDAO.savePrescription(medicalRecordId, medicineIds, quantities, dosages, notes);

                request.getSession().setAttribute("success", "Update medical record successful!");
                // Đá ngược về trang Chi tiết Bệnh án (chứ không phải hàng chờ)
                response.sendRedirect(basePath + "/medical-record/detail?id=" + medicalRecordId);
                return;
            }
            // ==============================================================

            // Chốt ca & Lưu tạm
            String nextStatus = ("complete".equals(action) || "complete_print".equals(action)) ? "COMPLETED" : "IN_PROGRESS";
            if ("complete".equals(action) || "complete_print".equals(action)) {
                //kiểm tra triệu chứng chuẩn đoán
                if (mr.getSymptom() == null || mr.getSymptom().trim().isEmpty() || mr.getDiagnosis() == null || mr.getDiagnosis().trim().isEmpty()) {
                    request.getSession().setAttribute("error", "Please enter the full Symptoms and Diagnosis before finishing the examiniation!");
                    response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
                    return;
                }

                //kiểm tra các chỉ định xn phải completed
                if (medicalRecordId > 0) {
                    dao.LabTestDAO labDao = new dao.LabTestDAO(); // Đảm bảo gọi đúng class DAO chứa hàm của bạn
                    java.util.List<model.LabTestBatch> batches = labDao.getBatchesForMedicalRecord(medicalRecordId);

                    for (model.LabTestBatch batch : batches) {
                        // Nếu có 1 lô nào đó chưa COMPLETED (đang CREATED hoặc IN_PROGRESS)
                        if (!"COMPLETED".equals(batch.getStatus())) {
                            request.getSession().setAttribute("error", "Can not finish examination! Patient has ongoing clinical tests that have not been completed.");
                            response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
                            return;
                        }
                    }
                }
            }

            if (medicalRecordId > 0) {
                mrDAO.updateMedicalRecord(mr, appointmentId, nextStatus);
            } else {
                medicalRecordId = mrDAO.saveMedicalRecord(mr, appointmentId, nextStatus);
            }
            medDAO.savePrescription(medicalRecordId, medicineIds, quantities, dosages, notes);

            if ("complete".equals(action)) {
                request.getSession().setAttribute("success", "Finished examination!");
                response.sendRedirect(basePath + "/queue/list");
            } else if ("complete_print".equals(action)) {
                request.getSession().setAttribute("success", "Finished examination!");
                response.sendRedirect(basePath + "/queue/list?printMrId=" + medicalRecordId);
            } else {
                request.getSession().setAttribute("success", "Saved medical record successful!");
                response.sendRedirect(basePath + "/medical-record/edit?appointmentId=" + appointmentId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Invalid input!");
            response.sendRedirect(basePath + "/medical-record/list");
        }
    }

}
