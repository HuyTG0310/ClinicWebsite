/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author huytr
 */
import java.util.List;

public class ServiceCategory {

    public static final String EXAM = "Khám bệnh";
    public static final String LAB = "Xét nghiệm";

    public static List<String> getAll() {
        return List.of(EXAM, LAB);
    }
}
