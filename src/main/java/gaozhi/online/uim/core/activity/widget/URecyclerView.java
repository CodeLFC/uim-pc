package gaozhi.online.uim.core.activity.widget;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.activity.widget.ui.UScrollBarUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BiConsumer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 自定义列表
 * @date 2022/3/16 15:26
 */

public class URecyclerView<T> extends JPanel implements MouseWheelListener {
    private Component header;
    private Component bottom;

    private final JList<T> centerList;
    private final JScrollBar centerScrollBarV;
    private int isNeedBottom;
    private int lastX;
    private int lastY;
    public URecyclerView() {
        setLayout(new BorderLayout());

        centerList = new JList<>();

        JScrollPane centerPanelScroll = new JScrollPane(centerList);
        centerPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centerPanelScroll.getHorizontalScrollBar().setUI(new UScrollBarUI());
        centerPanelScroll.getVerticalScrollBar().setUI(new UScrollBarUI());
        centerPanelScroll.setBorder(null);
        centerScrollBarV = centerPanelScroll.getVerticalScrollBar();
        add(centerPanelScroll);
        centerList.addMouseWheelListener(this);

        centerScrollBarV.addAdjustmentListener(evt -> {
            if (evt.getAdjustmentType() == AdjustmentEvent.TRACK && isNeedBottom <= 3) {
                centerScrollBarV.setValue(centerScrollBarV.getModel().getMaximum() - centerScrollBarV.getModel().getExtent());
                isNeedBottom++;
            }
        });
    }

    /**
     * @description: TODO 添加点击事件
     * @author LiFucheng
     * @date 2022/4/13 13:42
     * @version 1.0
     */
    public void addItemClickedListener(BiConsumer<Integer, T> itemClickedListener) {
        if (itemClickedListener == null) {
            return;
        }
        centerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(centerList.getSelectedValue()!=null) {
                    itemClickedListener.accept(centerList.getSelectedIndex(), centerList.getSelectedValue());
                }
            }
        });
    }

    public Component getHeader() {
        return header;
    }

    public void setHeader(Component header) {
        if (this.header != null)
            remove(this.header);
        this.header = header;
        add(header, BorderLayout.NORTH);
    }

    public Component getBottom() {
        return bottom;
    }

    public void setBottom(Component bottom) {
        if (this.bottom != null)
            remove(this.bottom);
        this.bottom = bottom;
        add(bottom, BorderLayout.SOUTH);
    }

    public void setListModel(ListModel<T> model) {
        centerList.setModel(model);
    }

    public void setCellRender(ListCellRenderer<T> cellRender) {
        centerList.setCellRenderer(cellRender);
    }

    public void move2End() {
        isNeedBottom = 0;
        // centerScrollBarV.setValue(centerScrollBarV.getModel().getMaximum());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int size = 50;
        if (e.getWheelRotation() > 0) {
            centerScrollBarV.setValue(centerScrollBarV.getValue() + size);
            return;
        }
        if (e.getWheelRotation() < 0) {
            centerScrollBarV.setValue(centerScrollBarV.getValue() - size);
        }
    }
}
