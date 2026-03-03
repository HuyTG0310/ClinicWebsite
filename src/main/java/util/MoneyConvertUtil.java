/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


public class MoneyConvertUtil {

    private static final String[] units = {"", "Một", "Hai", "Ba", "Bốn", "Năm", "Sáu", "Bảy", "Tám", "Chín"};
    private static final String[] places = {"", "Nghìn", "Triệu", "Tỷ", "Nghìn tỷ"};

    public static String convertToVietnameseWords(long amount) {
        if (amount == 0) {
            return "Không đồng chẵn.";
        }
        String sNumber = Long.toString(amount);
        String result = "";
        int placeValue = 0;

        while (sNumber.length() > 0) {
            int length = sNumber.length();
            String group = sNumber.substring(Math.max(0, length - 3), length);
            sNumber = sNumber.substring(0, Math.max(0, length - 3));

            int groupInt = Integer.parseInt(group);
            if (groupInt > 0) {
                String groupWord = readGroupOfThree(groupInt, sNumber.length() == 0);
                result = groupWord + " " + places[placeValue] + " " + result;
            }
            placeValue++;
        }
        result = result.trim().replaceAll("\\s+", " ");
        return result.substring(0, 1).toUpperCase() + result.substring(1) + " đồng chẵn.";
    }

    private static String readGroupOfThree(int number, boolean isFirstGroup) {
        int hundred = number / 100;
        int ten = (number % 100) / 10;
        int unit = number % 10;
        String res = "";

        if (hundred > 0 || !isFirstGroup) {
            res += units[hundred] + " Trăm ";
        }
        if (ten > 1) {
            res += units[ten] + " Mươi ";
            if (unit == 1) {
                res += "Mốt";
            } else if (unit == 5) {
                res += "Lăm";
            } else if (unit > 0) {
                res += units[unit];
            }
        } else if (ten == 1) {
            res += "Mười ";
            if (unit == 5) {
                res += "Lăm";
            } else if (unit > 0) {
                res += units[unit];
            }
        } else if (ten == 0 && unit > 0) {
            if (hundred > 0 || !isFirstGroup) {
                res += "Lẻ ";
            }
            res += units[unit];
        }
        return res.trim();
    }
}
