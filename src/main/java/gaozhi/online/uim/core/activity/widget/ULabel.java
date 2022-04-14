package gaozhi.online.uim.core.activity.widget;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 重写JLabel
 * @date 2022/3/24 10:28
 */
public class ULabel extends JLabel implements MouseListener {
    private ActionListener actionListener;

    public ULabel() {
        this(null);
    }

    public ULabel(String text) {
        super(text);
        addMouseListener(this);
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void doClick() {
        mouseClicked(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (actionListener != null) {
            ActionEvent actionEvent = new ActionEvent(this, 0, null);
            actionListener.actionPerformed(actionEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
