package gaozhi.online.uim.core.activity.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 导航栏
 * @date 2022/4/12 20:45
 */
public class UNavigationBar extends UPanel implements ActionListener {
    private final GridLayout navigationPanelGridLayout = new GridLayout();
    private Color selectedColor = Color.RED;
    private Color unSelectedColor = Color.BLACK;
    private int selected;
    private UItemSelectedListener uItemSelectedListener;

    public UNavigationBar() {
        navigationPanelGridLayout.setRows(1);
        setLayout(navigationPanelGridLayout);
        setBackground(Color.WHITE);
    }

    public void setItems(String[] items) {
        removeAll();
        navigationPanelGridLayout.setColumns(items.length);
        for (int i = 0; i < items.length; i++) {
            UItemView item = new UItemView(i, items[i]);
            item.setHorizontalAlignment(JLabel.CENTER);
            item.setForeground(unSelectedColor);
            item.setActionListener(this);
            add(item);
        }
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        updateColor();
    }

    public Color getUnSelectedColor() {
        return unSelectedColor;
    }

    public void setUnSelectedColor(Color unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
        updateColor();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        UItemView itemView = updateColor();
        actionPerformed(new ActionEvent(itemView, itemView.getIndex(), null));
    }

    public UItemSelectedListener getuItemSelectedListener() {
        return uItemSelectedListener;
    }

    public void setuItemSelectedListener(UItemSelectedListener uItemSelectedListener) {
        this.uItemSelectedListener = uItemSelectedListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof UItemView)) {
            return;
        }

        UItemView itemView = (UItemView) e.getSource();
        selected = itemView.getIndex();
        updateColor();
        if (uItemSelectedListener == null) {
            return;
        }
        uItemSelectedListener.onClick(itemView);
    }

    /**
     * @description: TODO 更新显示的颜色
     * @author LiFucheng
     * @date 2022/4/12 21:09
     * @version 1.0
     */
    private UItemView updateColor() {
        UItemView selectedView = null;
        for (Component component : getComponents()) {
            if (component instanceof UItemView) {
                if (((UItemView) component).index != selected) {
                    component.setForeground(unSelectedColor);
                } else {
                    component.setForeground(selectedColor);
                    selectedView = (UItemView) component;
                }
            }
        }
        return selectedView;
    }

    @FunctionalInterface
    public interface UItemSelectedListener {
        void onClick(UItemView itemView);
    }

    public static class UItemView extends ULabel {
        private final int index;
        private final String item;

        public UItemView(int index, String item) {
            this.index = index;
            this.item = item;
            setText(item);
        }

        public int getIndex() {
            return index;
        }

        public String getItem() {
            return item;
        }
    }
}
