package comm.test.kotlin.tools;


import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StrUtil {

    public static String RoundingDouble(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.parseDouble(str));
    }

    public static boolean checkMobile(String mobile) {
        Pattern pattern = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = pattern.matcher(mobile);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 密码不能纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }

    /**
     * 密码不能纯字母
     *
     * @param data
     * @return
     */
    public static boolean isChar(String data) {
        {
            for (int i = data.length(); --i >= 0; ) {
                char c = data.charAt(i);
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 验证大陆手机格式
     */

    public static boolean isMobileNo(String str) {
        /*
         * 移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、182、183、187、188
         * 联通：130、131、132、152、155、156、176、185、186 电信：133、153、177、180、181、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String regex = "[1][123456789]\\d{9}";// "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static int getRandom() {
        int number = 0;
        while (true) {
            number = (int) (Math.random() * 1000);
            if (number >= 100 && number < 1000) {
                break;
            }
        }
        return number;
    }


    /**
     * 验证str是否为正确的车牌号
     *
     * @param view
     * @return
     */
    public static boolean isPlateNo(EditText view) {
        String no = view.getText().toString().trim();
        if (no == null || no.equals("")) {
            return false;
        }
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String[] str1 = {"京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏",
                "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝",
                "川", "贵", "云", "藏", "陕", "甘", "青", "宁", "新", "农", "台", "中",
                "武", "WJ", "亥", "戌", "酉", "申", "未", "午", "巳", "辰", "卯", "寅",
                "丑", "子", "葵", "壬", "辛", "庚", "己", "戊", "丁", "丙", "乙", "甲",
                "河北", "山西", "北京", "北", "南", "兰", "沈", "济", "成", "广", "海", "空",
                "军", "京V", "使"};

//        if (no.equals("新车")) {
//            return true;
//        }

        if (no.length() == 7) {
            int h = 0;
            for (int r = 0; r < no.length(); r++) {
                if (str.indexOf(no.charAt(r)) != -1) {
                    h++;
                }
            }
            if (h == 7) {
                return true;
            }
        }
        if (no.length() > 1) {

            String jq1 = no.substring(0, 1);
            String jq2 = no.substring(0, 2);

            for (int k = 0; k < str1.length; k++) {
                if (str1[k].equals(jq1)) {
                    if (no.length() <= 8) {
                        return true;
                    }
                }
                if (str1[k].equals(jq2)) {
                    if (no.length() <= 8) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isJson(String str) {
        String regex = "\\{.*\\}";// "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
