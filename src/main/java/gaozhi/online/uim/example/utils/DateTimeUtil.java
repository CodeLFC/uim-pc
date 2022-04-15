package gaozhi.online.uim.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 时间工具
 * @date 2022/3/18 18:54
 */
public class DateTimeUtil {
    private DateTimeUtil() {
    }

    private static SimpleDateFormat birthFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat chatFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
    private static SimpleDateFormat talkFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
    private static SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");

    public static String getBirthTime(long time) {
        return birthFormat.format(new Date(time));
    }

    public static String getChatTime(long time) {
        return chatFormat.format(new Date(time));
    }

    public static String getFileName(long time) {
        return fileNameFormat.format(new Date(time));
    }

    public static String getTalkTime(long time) {
        return talkFormat.format(new Date(time));
    }

    public static String getHMSTime(long time) {
        return hmsFormat.format(new Date(time));
    }

    public static long analyseBirthTime(String time) throws ParseException {
        return birthFormat.parse(time).getTime();
    }
}
