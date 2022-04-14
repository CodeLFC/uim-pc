package gaozhi.online.uim.core.asynchronization;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 多线程
 * @date 2022/1/25 22:47
 */
public class TaskExecutor {
    /**
     * @description: TODO  当应用程序线程需要更新GUI时，应该使用此方法
     * @author LiFucheng
     * @date 2022/1/25 22:53
     * @version 1.0
     */
    public void executeInUIThread(Runnable runnable) {
        if (!EventQueue.isDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            runnable.run();
        }
    }

    /**
     * @description: TODO 当需要执行异步任务时，应该使用此方法
     * @author LiFucheng
     * @date 2022/3/13 14:38
     * @version 1.0
     */
    public void executeInBackThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public Timer executeTimerTask(Runnable runnable, long period) {
        return executeTimerTask(runnable, 0, period);
    }

    public Timer executeTimerTask(Runnable runnable, long delay, long period) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay, period);
        return timer;
    }

    public Timer executeDelayTask(Runnable runnable, long delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                cancel();
            }
        }, delay, 100);
        return timer;
    }
}
