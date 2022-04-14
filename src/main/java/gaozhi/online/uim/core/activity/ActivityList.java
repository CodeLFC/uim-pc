package gaozhi.online.uim.core.activity;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 窗口管理器,栈的方式管理窗口的生命周期
 * @date 2022/1/25 15:10
 */
public class ActivityList implements WindowListener {
    //活跃活动的栈
    private final List<Activity> activityList = new LinkedList<>();
    private final Logger logger = Logger.getLogger(ActivityList.class.getName());

    @Override
    public void windowOpened(WindowEvent e) {
        //窗口首次打开时调用
        Activity activity = (Activity) e.getWindow();
        // logger.info("windowOpened:" + activity.getClass().getName());
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //从系统菜单中关闭窗体时调用
        Activity activity = (Activity) e.getWindow();
        //logger.info("windowClosing:" + activity.getClass().getName());
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //对窗口调用 dispose 而将其关闭时调用。
        Activity activity = (Activity) e.getWindow();
        activity.releaseResource();
        activityList.remove(activity);
        logger.info("windowClosed:" + activity.getClass().getName());

        if (activityList.size() == 0) {
            System.exit(0);
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //窗体变为最小化时调用
        Activity activity = (Activity) e.getWindow();
        //logger.info("windowIconified:" + activity.getClass().getName());
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //窗体变为正常状态事调用
        Activity activity = (Activity) e.getWindow();
        // logger.info("windowDeiconified:" + activity.getClass().getName());
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //窗体被激活时调用
        Activity activity = (Activity) e.getWindow();
        if (!activityList.contains(activity)) {
            activityList.add(activity);
            logger.info("windowActivated:" + activity.getClass().getName());
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // 当 Window 不再是活动 Window 时调用。
        Activity activity = (Activity) e.getWindow();
        //logger.info("windowDeactivated:" + activity.getClass().getName());
    }

    /**
     * @description: TODO 获取顶部activity
     * @author LiFucheng
     * @date 2022/3/13 16:25
     * @version 1.0
     */
    public Activity top() {
        return activityList.get(activityList.size() - 1);
    }

    public int size() {
        return activityList.size();
    }

    public <T extends Activity> List<Activity> getActivityList(Class<T> klass) {
        List<Activity> res = new LinkedList<>();
        for (Activity activity : activityList) {
            if (klass == activity.getClass()) {
                res.add(activity);
            }
        }
        return res;
    }
}
