package gaozhi.online.uim.core.resource;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO dimen值
 * @date 2022/1/25 21:26
 */
public class Dimens {
    private final Resources resources;

    public Dimens(Resources resources) {
        this.resources = resources;
    }

    public int getDimen(String key,int defaultValue) {
        String res = resources.getString(key);
        if(res == null){
            return defaultValue;
        }
        return Integer.parseInt(res);
    }
}
