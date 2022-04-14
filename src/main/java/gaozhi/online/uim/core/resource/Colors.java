package gaozhi.online.uim.core.resource;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 颜色值
 * @date 2022/3/13 18:17
 */
public class Colors {
    private final Resources resources;

    public Colors(Resources resources) {
        this.resources = resources;
    }

    public int getColor(String key,int defaultValue) {
        String res = resources.getString(key);
        if(res == null){
            return defaultValue;
        }
        return Integer.getInteger(res);
    }
}
