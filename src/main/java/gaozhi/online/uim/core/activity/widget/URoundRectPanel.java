package gaozhi.online.uim.core.activity.widget;

import javax.swing.*;
import java.awt.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 圆角
 * @date 2022/4/14 20:16
 */
public class URoundRectPanel extends UPanel {

    private int arc = 10;
    private Color startColor = Color.WHITE;
    private Color endColor = new Color(149, 193, 219);
    private Dimension activitySize = new Dimension(100,100);

    public URoundRectPanel() {
        setBackground(new Color(0,0,0,0));
    }

    public int getArc() {
        return arc;
    }

    public void setArc(int arc) {
        this.arc = arc;
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public Dimension getActivitySize() {
        return activitySize;
    }

    public void setActivitySize(Dimension activitySize) {
        this.activitySize = activitySize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D) g).setPaint(new GradientPaint(0, 0, startColor, 0, activitySize.height, endColor));
        g.fillRoundRect(0, 0, activitySize.width, activitySize.height, arc, arc);
       // System.out.println("重绘制："+activitySize.toString());
        super.paintComponent(g);
    }

}
