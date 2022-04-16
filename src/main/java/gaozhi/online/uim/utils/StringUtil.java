package gaozhi.online.uim.utils;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 字符串工具
 * @date 2022/4/6 10:42
 */
public class StringUtil {
    public static boolean isPhoneNum(String phone) {
        if(isEmpty(phone)){
            return false;
        }
        if(phone.length()!=11){
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String src) {
        if (src == null)
            return true;
        return src.isEmpty();
    }

    public static boolean isBlank(String src) {
        if (src == null)
            return true;
        return src.isBlank();
    }
    public static boolean startWith(String src,String prefix){
        if(src==null)
            return true;
        return src.startsWith(prefix);
    }
}
