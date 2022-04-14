package gaozhi.online.uim.core.activity;

import gaozhi.online.uim.core.Application;
import gaozhi.online.uim.core.resource.*;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.example.utils.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Objects;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 全局上下文
 * @date 2022/1/25 22:13
 */
public class Context {
    //字符串常量
    private final Strings app_strings;
    //activity栈
    private final ActivityList activityList;
    //多线程工具
    private final TaskExecutor taskExecutor;

    //字符串
    private final Strings strings;
    //数值
    private final Dimens dimens;
    //图像
    private final Drawables drawables;
    //颜色
    private final Colors color;

    public Context(Resources resources) {

        app_strings = new Strings(resources);
        activityList = new ActivityList();
        taskExecutor = new TaskExecutor();

        strings = new Strings(new Resources(getAppString("strings")));
        dimens = new Dimens(new Resources(getAppString("dimens")));
        drawables = new Drawables(new Resources(getAppString("drawables")));
        color = new Colors(new Resources(getAppString("colors")));

    }

    /**
     * @description: TODO 监听活动的生命周期
     * @author LiFucheng
     * @date 2022/1/25 23:23
     * @version 1.0
     */
    void listenActivityLife(Activity activity) {
        activity.addWindowListener(activityList);
    }

    protected String getAppString(String key) {
        return app_strings.getString(key);
    }

    public int getDimen(String key, int defaultValue) {
        return dimens.getDimen(key, defaultValue);
    }

    public String getString(String key) {
        return strings.getString(key);
    }

    public Image getDrawable(String key) throws IOException {
        String urlKey = drawables.getDrawable(key);
        URL url =getClass().getResource(urlKey);
        return ImageIO.read(Objects.requireNonNull(url));
    }

    public int getColor(String key, int defaultValue) {
        return color.getColor(key, defaultValue);
    }

    public ActivityList getActivityList() {
        return activityList;
    }

    public <T extends Activity> void startActivity(Class<T> activityClass) {
        startActivity(activityClass, null);
    }

    public <T extends Activity> void startActivity(Class<T> activityClass, Intent intent) {
        startActivity(activityClass, intent, getString("app_name"));
    }

    public <T extends Activity> void startActivity(Class<T> activityClass, Intent intent, String title) {
        taskExecutor.executeInUIThread(() -> {
            try {
                Constructor<T> constructor = activityClass.getConstructor(Context.class, Intent.class, String.class);
                constructor.newInstance(Context.this, intent, title);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

}
