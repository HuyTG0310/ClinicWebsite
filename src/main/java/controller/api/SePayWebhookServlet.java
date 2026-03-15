/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author huytr
 */
@WebServlet(name = "SePayWebhookServlet", urlPatterns = {"/api/sepay-webhook"})
public class SePayWebhookServlet extends HttpServlet {

    public static ConcurrentHashMap<String, Double> paymentAlerts = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("========== SEPAY WEBHOOK BẮT ĐẦU CHẠY ==========");

        // 1. Đọc dữ liệu JSON do SePay ném vào
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String payload = buffer.toString();
        System.out.println("1. RAW PAYLOAD NHẬN ĐƯỢC: " + payload);

        // 2. Trích xuất Nội dung CK và Số tiền
        int transferAmount = extractInt(payload, "\"transferAmount\"\\s*:\\s*(\\d+)");
        String content = extractString(payload, "\"content\"\\s*:\\s*\"([^\"]+)\"");

        System.out.println("2. ĐÃ TRÍCH XUẤT -> NỘI DUNG: " + content + " | SỐ TIỀN: " + transferAmount);

        if (content == null || content.isEmpty()) {
            response.setStatus(400);
            return;
        }

        // Ép tất cả thành chữ hoa và XÓA SẠCH khoảng trắng
        String normalizedContent = content.toUpperCase().replace(" ", "");
        dao.ServiceOrderDAO dao = new dao.ServiceOrderDAO();
        int botCashierId = 1; // ID Lễ tân tự động (1)

        try {
            //THANH TOÁN BỆNH ÁN (XÉT NGHIỆM)
            if (normalizedContent.contains("THANHTOANBA")) {
                int mrId = Integer.parseInt(normalizedContent.replaceAll(".*THANHTOANBA(\\d+).*", "$1"));
                String alertKey = "BA" + mrId;
                System.out.println("3. TÌM THẤY MÃ BỆNH ÁN: " + mrId);

                double expectedAmount = dao.getUnpaidTotalAmount(mrId);

                if (expectedAmount > 0 && transferAmount > 0) {
                    if (transferAmount < expectedAmount) {
                        System.out.println("-> ❌ BÁO ĐỘNG: Chuyển thiếu tiền!");
                        paymentAlerts.put(alertKey, -(double) transferAmount);
                        response.setStatus(200); // Vẫn trả 200 OK để SePay không bắn lại
                        response.getWriter().write("{\"success\": true, \"note\": \"Underpaid\"}");
                        return;
                    } else {
                        if (transferAmount > expectedAmount) {
                            System.out.println("-> ⚠️ KHÁCH CHUYỂN DƯ TIỀN!");
                            paymentAlerts.put(alertKey, transferAmount - expectedAmount);
                        }

                        // Chốt đơn toàn bộ Bệnh Án
                        boolean isSuccess = dao.checkoutServiceOrders(mrId, botCashierId, "BANKING");
                        if (isSuccess) {
                            System.out.println("4. ✅ CẬP NHẬT DB THÀNH CÔNG BỆNH ÁN: " + mrId);
                            response.setStatus(200);
                            response.getWriter().write("{\"success\": true}");
                            return;
                        }
                    }
                }

                //THANH TOÁN PHIẾU KHÁM LẺ (CHƯA CÓ BỆNH ÁN)
            } else if (normalizedContent.contains("THANHTOANPK")) {
                int soId = Integer.parseInt(normalizedContent.replaceAll(".*THANHTOANPK(\\d+).*", "$1"));
                String alertKey = "PK" + soId;
                System.out.println("3. TÌM THẤY MÃ PHIẾU KHÁM: " + soId);

                model.ServiceOrder order = dao.getServiceOrderById(soId);
                double expectedAmount = (order != null && "UNPAID".equals(order.getStatus())) ? order.getPriceAtTime() : 0;

                if (expectedAmount > 0 && transferAmount > 0) {
                    if (transferAmount < expectedAmount) {
                        System.out.println("-> ❌ BÁO ĐỘNG: Chuyển thiếu tiền!");
                        paymentAlerts.put(alertKey, -(double) transferAmount);
                        response.setStatus(200);
                        response.getWriter().write("{\"success\": true, \"note\": \"Underpaid\"}");
                        return;
                    } else {
                        if (transferAmount > expectedAmount) {
                            System.out.println("-> ⚠ KHÁCH CHUYỂN DƯ TIỀN!");
                            paymentAlerts.put(alertKey, transferAmount - expectedAmount);
                        }

                        // Chốt đơn duy nhất 1 Phiếu Khám lẻ
                        boolean isSuccess = dao.checkoutSingleServiceOrder(soId, botCashierId, "BANKING");
                        if (isSuccess) {
                            System.out.println("4. ✅ CẬP NHẬT DB THÀNH CÔNG PHIẾU KHÁM: " + soId);
                            response.setStatus(200);
                            response.getWriter().write("{\"success\": true}");
                            return;
                        }
                    }
                }

            } else {
                System.out.println("LỖI: Không tìm thấy mã THANHTOANBA hay THANHTOANPK hợp lệ.");
            }

        } catch (Exception e) {
            System.out.println("LỖI TRONG QUÁ TRÌNH XỬ LÝ: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("========== SEPAY WEBHOOK KẾT THÚC BẰNG LỖI 400 ==========");
        response.setStatus(400); // Chỉ trả 400 khi lỗi nặng hoặc mã rác
    }

    
    private int extractInt(String json, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String extractString(String json, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
