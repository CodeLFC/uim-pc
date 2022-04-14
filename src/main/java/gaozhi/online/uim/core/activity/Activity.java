package gaozhi.online.uim.core.activity;

import gaozhi.online.ubtb.core.util.StringUtils;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.core.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 基础窗口
 * @date 2022/1/25 13:30
 */
public abstract class Activity extends JFrame implements ActionListener {
    //上下文环境
    private final Context context;
    private Intent intent;
    //总布局
    private final GridLayout gridLayout = new GridLayout();
    //总日志
    protected static final Logger logger = Logger.getLogger(Activity.class.getName());

    public Activity(Context context, Intent intent, String title) {
        setTitle(title);
        this.context = context;
        this.intent = intent;
        JPanel contentPanel = new JPanel();
        setContentPane(contentPanel);
        contentPanel.setLayout(gridLayout);
        //监听活动的生命周期
        context.listenActivityLife(this);
        //图标
        try {
            setIconImage(context.getDrawable("app_logo"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置大小
        Dimension screenSize = screenSize();
        setLocation(screenSize.width / 3, screenSize.height / 3);
        setSize(screenSize.width / 3, screenSize.height / 3);
        //启动页面
        setVisible(true);
        //设置窗口生命周期结束
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initParam(this.intent);
        initUI();
        doBusiness();

    }

    public abstract void initParam(Intent intent);

    public abstract void initUI();

    public abstract void doBusiness();

    public Context getContext() {
        return context;
    }

    @Override
    public void setSize(int width, int height) {
        Dimension dimension = screenSize();
        if (width == -1) {
            width = dimension.width;
        }
        if (height == -1) {
            height = dimension.height;
        }
        super.setSize(width, height);
    }

    /**
     * @description: TODO 获取屏幕大小
     * @author LiFucheng
     * @date 2022/1/25 21:02
     * @version 1.0
     */
    public static Dimension screenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setRootGridLayout(final int rows, final int cols) {
        gridLayout.setRows(rows);
        gridLayout.setColumns(cols);
        getContentPane().removeAll();
        int size = rows * cols;
        while (size-- > 0) {
            getContentPane().add(new JPanel());
        }
    }

    public void setVGap(int gap) {
        gridLayout.setVgap(gap);
    }

    public void setHGap(int gap) {
        gridLayout.setHgap(gap);
    }

    public JPanel getChildPanel(int row, int column) {
        return (JPanel) getContentPane().getComponent((row - 1) * gridLayout.getColumns() + column - 1);
    }

    /**
     * @description: TODO  根据url设置图标
     * @author LiFucheng
     * @date 2022/4/2 19:26
     * @version 1.0
     */
    public void setIcon(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        new TaskExecutor().executeInBackThread(() -> {
            try {
                Image head = ImageUtil.readURL(url);
                new TaskExecutor().executeInUIThread(() -> {
                    setIconImage(head);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * @description: TODO 释放资源
     * @author LiFucheng
     * @date 2022/4/6 19:42
     * @version 1.0
     */
    public void releaseResource(){

    }
}
