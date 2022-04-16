package gaozhi.online.uim.ui.conversation.chat.msg;

import gaozhi.online.uim.im.conversation.IMMessage;
import gaozhi.online.uim.im.conversation.message.IMMsgType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 显示文本消息的自定义视图
 * @date 2022/3/18 19:59
 */
public class TextMsgView extends IMMsgView {
    private JTextArea textArea;
    private int strokeThickness = 3;
    private int radius = 10;
    private int arrowSize = 6;
    private int padding = strokeThickness / 2;
    private int rowLen = 60;
    private boolean isSelf;

    public TextMsgView() {
        textArea = new JTextArea();
        textArea.setBorder(new EmptyBorder(6, 6, 6, 6));
        add(textArea);
        textArea.setOpaque(false);
    }

    @Override
    void bindView(IMMessage msg, boolean isSelf) {
        textArea.setColumns(rowLen);
        IMMessage.DataCoder<String> dataCoder = IMMessage.Codec.getDataCoder(IMMsgType.TEXT);
        textArea.setText(dataCoder.parse2T(msg.getData()));
        textArea.setLineWrap(true);
        this.isSelf = isSelf;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        if (isSelf) {
            paintRight(g2d);
        } else {
            paintLeft(g2d);
        }
    }

    private void paintLeft(final Graphics2D g2d) {
        g2d.setColor(new Color(1f, 1f, 1f));
        int x = padding + strokeThickness + arrowSize;
        int width = getWidth() - arrowSize - (strokeThickness * 2);
        int bottomLineY = getHeight() - strokeThickness;
        g2d.fillRect(x, padding, width, bottomLineY);
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setStroke(new BasicStroke(strokeThickness));
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x, padding, width, bottomLineY, radius, radius);
        Polygon arrow = new Polygon();
        int offset_y = getHeight() / 2 - 10;
        arrow.addPoint(20, 8 + offset_y);
        arrow.addPoint(0, 10 + offset_y);
        arrow.addPoint(20, 12 + offset_y);
        Area area = new Area(rect);
        area.add(new Area(arrow));
        g2d.draw(area);
    }

    private void paintRight(final Graphics2D g2d) {
        g2d.setColor(new Color(149, 193, 239));
        int bottomLineY = getHeight() - strokeThickness;
        int width = getWidth() - arrowSize - (strokeThickness * 2);
        g2d.fillRect(padding, padding, width, bottomLineY);
        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(padding, padding, width, bottomLineY, radius, radius);
        Polygon arrow = new Polygon();
        int offset_y = getHeight() / 2 - 10;
        arrow.addPoint(width, 8 + offset_y);
        arrow.addPoint(width + arrowSize, 10 + offset_y);
        arrow.addPoint(width, 12 + offset_y);
        Area area = new Area(rect);
        area.add(new Area(arrow));
        g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setStroke(new BasicStroke(strokeThickness));
        g2d.draw(area);
    }
}
