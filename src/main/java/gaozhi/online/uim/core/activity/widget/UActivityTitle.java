package gaozhi.online.uim.core.activity.widget;

import gaozhi.online.uim.core.activity.Activity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 标题
 * @date 2022/4/14 18:58
 */
public class UActivityTitle extends UPanel implements ActionListener {
    private UImageView image_icon;
    private ULabel label_title;
    UPanel panel_right;
    private ULabel label_close;
    private ULabel label_min;
    private ULabel label_max;
    private Activity parent;
    private boolean fullScreen;
    private Rectangle initBounds;

    public UActivityTitle(Activity parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        image_icon = new UImageView();
        image_icon.setBorder(new EmptyBorder(0, 10, 0, 10));
        label_title = new ULabel();
        label_title.setBorder(new EmptyBorder(0, 10, 0, 10));
        label_min = new ULabel("-");
        label_min.setBorder(new EmptyBorder(0, 5, 0, 5));
        label_max = new ULabel("[]");
        label_max.setBorder(new EmptyBorder(0, 5, 0, 5));
        label_close = new ULabel("X");
        label_close.setBorder(new EmptyBorder(0, 5, 0, 5));
        add(image_icon, BorderLayout.WEST);
        add(label_title);

        panel_right = new UPanel();
        panel_right.add(label_min);
        panel_right.add(label_max);
        panel_right.add(label_close);
        add(panel_right, BorderLayout.EAST);

        label_max.setActionListener(this);
        label_min.setActionListener(this);
        label_close.setActionListener(this);

        fullScreen = false;
    }

    public UImageView getImageIcon() {
        return image_icon;
    }

    public ULabel getLabelTitle() {
        return label_title;
    }

    public void setResizable(boolean resizable) {
        label_max.setVisible(resizable);
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        label_title.addMouseListener(l);
        image_icon.addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        label_title.addMouseMotionListener(l);
        image_icon.addMouseMotionListener(l);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == label_min) {
            parent.setExtendedState(JFrame.ICONIFIED);
            return;
        }
        //放大缩小
        if (e.getSource() == label_max && label_max.isVisible()) {
            if (fullScreen) {
                parent.setBounds(initBounds);
            } else {
                initBounds = parent.getBounds();
                parent.setLocation(0, 0);
                parent.setSize(Activity.FULL_SCREEN, Activity.FULL_SCREEN);
            }
            fullScreen = !fullScreen;
            return;
        }
        if (e.getSource() == label_close) {
            parent.dispose();
        }
    }
}
