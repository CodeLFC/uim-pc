package gaozhi.online.uim.core.activity;

import gaozhi.online.uim.core.activity.widget.UActivityTitle;
import gaozhi.online.uim.core.activity.widget.UPanel;
import gaozhi.online.uim.core.activity.widget.URoundRectPanel;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.core.utils.ImageUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 基础窗口
 * @date 2022/1/25 13:30
 */
public abstract class Activity extends JFrame implements ActionListener, Runnable {

    public static final int FULL_SCREEN = -1;
    //标题中的图标大小
    protected int TITLE_ICON_SIZE = 20;
    //上下文环境
    private final Context context;
    private Intent intent;
    //总布局
    private final GridLayout gridLayout = new GridLayout();
    //总日志
    protected static final Logger logger = Logger.getLogger(Activity.class.getName());
    //rootPanel
    private final URoundRectPanel contentPanel;
    //centerPanel
    private final UPanel centerPanel;
    private UActivityTitle titleView;
    //拖拽响应
    private final DraggedResizeListener draggedResizeListener;
    //唯一标识
    private final long id;
    private static long ID = 0;
    //闪烁
    private Timer twinkleTimer;
    private boolean twinkle;
    private boolean twinkleImage;
    private Image twinkleLogo;

    public Activity(Context context, Intent intent, String title, long id) {
        this.context = context;
        this.intent = intent;
        this.id = id;
        // 添加拖拽响应
        draggedResizeListener = new DraggedResizeListener(this);
        addMouseListener(draggedResizeListener);
        addMouseMotionListener(draggedResizeListener);

        contentPanel = new URoundRectPanel();
        setContentPane(contentPanel);
        contentPanel.setLayout(new BorderLayout());
        //添加自定义标题
        setTitleView(new UActivityTitle(this));
        setTitle(title);
        //添加中间布局
        centerPanel = new UPanel();
        centerPanel.setLayout(gridLayout);
        contentPanel.add(centerPanel);

        //监听活动的生命周期
        context.listenActivityLife(this);

        //图标
        try {
            twinkleLogo = context.getDrawable("app_logo");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIconImage(twinkleLogo);
        // 去掉java自带边框
        setUndecorated(true);
        //背景透明
        setOpacity(0.98f);

        //设置默认大小
        Dimension screenSize = screenSize();
        setSize(screenSize.width * 4 / 9, screenSize.height * 4 / 9);
        //设置窗口居中
        setLocationRelativeTo(null);
        //监听大小变化
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                logger.info(getSize().toString());
                contentPanel.setActivitySize(getSize());
            }
        });
        //启动页面
        setVisible(true);
        //设置窗口生命周期结束
        // setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initParam(this.intent);
        initUI();
        doBusiness();

    }

    public void setTitleView(UActivityTitle title) {
        if (title == null) {
            return;
        }
        this.titleView = title;
        super.getContentPane().add(title, BorderLayout.NORTH);
        titleView.addMouseListener(draggedResizeListener);
        titleView.addMouseMotionListener(draggedResizeListener);
    }

    public UActivityTitle getTitleView() {
        return titleView;
    }

    @Override
    public Container getContentPane() {
        return centerPanel;
    }

    @Override
    public void setIconImage(Image image) {
        super.setIconImage(image);
        image = ImageUtil.getScaleImage(image, TITLE_ICON_SIZE);
        titleView.getImageIcon().setImage(image);
    }

    @Override
    public Image getIconImage() {
        return titleView.getImageIcon().getImage();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        titleView.getLabelTitle().setText(title);
    }

    @Override
    public String getTitle() {
        return titleView.getLabelTitle().getText();
    }

    @Override
    public void setResizable(boolean resizable) {
        super.setResizable(resizable);
        titleView.setResizable(resizable);
    }

    public abstract void initParam(Intent intent);

    public abstract void initUI();

    public abstract void doBusiness();

    public Context getContext() {
        return context;
    }

    /**
     * @description: TODO 生成id
     * @author LiFucheng
     * @date 2022/4/15 12:40
     * @version 1.0
     */
    public static synchronized long generateId() {
        return ID++;
    }

    public long getId() {
        return id;
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
     * @description: TODO 显示
     * @author LiFucheng
     * @date 2022/4/15 14:28
     * @version 1.0
     */
    public void showActivity() {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
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
        initParam(intent);
    }

    public void setRootGridLayout(final int rows, final int cols) {
        gridLayout.setRows(rows);
        gridLayout.setColumns(cols);
        getContentPane().removeAll();
        int size = rows * cols;
        while (size-- > 0) {
            getContentPane().add(new UPanel());
        }
    }

    public void setVGap(int gap) {
        gridLayout.setVgap(gap);
    }

    public void setHGap(int gap) {
        gridLayout.setHgap(gap);
    }

    public UPanel getChildPanel(int row, int column) {
        return (UPanel) getContentPane().getComponent((row - 1) * gridLayout.getColumns() + column - 1);
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

    public boolean isIconified() {
        return getExtendedState() == ICONIFIED;
    }

    public void setTwinkleLogo(Image twinkleLogo) {
        this.twinkleLogo = twinkleLogo;
    }

    public void startTwinkle(long period) {
        twinkle = true;
        twinkleTimer = new TaskExecutor().executeTimerTask(this, period);
    }

    public boolean isTwinkle() {
        return twinkle;
    }

    public void stopTwinkle() {
        if (twinkleTimer != null) {
            twinkleTimer.cancel();
        }
        twinkle = false;
    }

    @Override
    public void run() {
        Image head = getTitleView().getImageIcon().getImage();
        twinkleImage = !twinkleImage;
        super.setIconImage(twinkleImage ? twinkleLogo : head);
    }

    /**
     * @description: TODO 释放资源
     * @author LiFucheng
     * @date 2022/4/6 19:42
     * @version 1.0
     */
    public void releaseResource() {
        stopTwinkle();
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 拖拽响应
     * @date 2022/4/14 19:48
     */
    public static class DraggedResizeListener extends MouseAdapter {

        //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
        int newX, newY, oldX, oldY;
        //这两个坐标为组件当前的坐标
        int startX, startY;

        private Component dragged;

        public DraggedResizeListener(Component dragged) {
            this.dragged = dragged;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //此为得到事件源组件
            //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
            startX = dragged.getX();
            startY = dragged.getY();
            oldX = e.getXOnScreen();
            oldY = e.getYOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //拖动的时候记录新坐标
            newX = e.getXOnScreen();
            newY = e.getYOnScreen();
            //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
            dragged.setBounds(startX + (newX - oldX), startY + (newY - oldY), dragged.getWidth(),
                    dragged.getHeight());
        }
    }
}
