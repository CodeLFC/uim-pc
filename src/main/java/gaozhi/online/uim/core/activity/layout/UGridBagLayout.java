package gaozhi.online.uim.core.activity.layout;

import java.awt.*;

/**
 * @author LiFucheng   布局
 * @version 1.0
 * @description: TODO
 * @date 2022/3/15 15:34
 */

public class UGridBagLayout extends GridBagLayout {
    //4.创建GridBagConstraints对象
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private final Container container;

    public UGridBagLayout(Container container) {
        this.container = container;
        this.container.setLayout(this);
        setFill(GridBagConstraints.BOTH);
    }

    /**
     * @description: TODO 设置所有的GridBagConstraints对象的fill属性为 GridBagConstraints.BOTH,当有空白区域时，组件自动扩大占满空白区域
     * @author LiFucheng
     * @date 2022/3/15 15:44
     * @version 1.0
     */
    public void setFill(int fill) {
        gridBagConstraints.fill = fill;
    }

    /**
     * @description: TODO 设置GridBagConstraints对象的weightx设置为h,表示横向扩展比例为h weighty为v,表示纵向扩展比例为v
     * @author LiFucheng
     * @date 2022/3/15 15:44
     * @version 1.0
     */
    public void setWeight(double h, double v) {
        gridBagConstraints.weightx = h;
        gridBagConstraints.weighty = v;
    }

    /**
     * @description: TODO 把GridBagConstraints的gridwidth设置为GridBagConstraints.REMAINDER,则表明当前组件是横向最后一个组件
     * @author LiFucheng
     * @date 2022/3/15 15:46
     * @version 1.0
     */
    public void setGrid(int width, int height) {
        gridBagConstraints.gridheight = width;
        gridBagConstraints.gridwidth = height;
    }

    public void addComponent(Component component) {
        setConstraints(component, gridBagConstraints);
        container.add(component);
    }

    public void placeholder(double h, double v) {
        //占位
        double h_temp = gridBagConstraints.weightx;
        double v_temp = gridBagConstraints.weighty;
        setWeight(h, v);
        addComponent(new Container());
        setWeight(h_temp, v_temp);
    }
}
