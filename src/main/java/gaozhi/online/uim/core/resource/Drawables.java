package gaozhi.online.uim.core.resource;


/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 图片值
 * @date 2022/3/13 18:19
 */
public class Drawables {
    private final Resources resources;

    public Drawables(Resources resources){
        this.resources = resources;
    }

    public String getDrawable(String key){
        return resources.getString(key);
    }

}
