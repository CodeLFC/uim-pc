package gaozhi.online.uim.core.activity.widget;

import gaozhi.online.uim.core.activity.Context;

import javax.swing.*;
import java.awt.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  卡片
 * @date 2022/4/13 9:38
 */
public abstract class BaseCard<T> extends UPanel {
    private final String key;
    private final Context context;
    public BaseCard(String key, Context context, T data) {
        this.key = key;
        this.context =context;
        initParam(data);
        initUI();
        doBusiness();
    }

    public abstract void initParam(T data);

    public abstract void initUI();

    public abstract void doBusiness();

    public String getKey() {
        return key;
    }

    public Context getContext() {
        return context;
    }

    public abstract void releaseResource();
}
