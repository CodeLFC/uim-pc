package gaozhi.online.uim.core.activity.widget;

import gaozhi.online.ubtb.core.util.StringUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 重写 JTextField
 * @date 3/14/2022 10:05 AM
 */
public class UTextField extends JTextField {
    private final ReminderFocusAdapter reminderFocusAdapter = new ReminderFocusAdapter(this, null);

    {
        addFocusListener(reminderFocusAdapter);
    }

    public void setHint(String hint) {
        reminderFocusAdapter.setReminder(hint);
    }

    public String getUText() {
        if (reminderFocusAdapter.isHintState) {
            return "";
        }
        return getText();
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 提示词
     * @date 3/14/2022 10:04 AM
     */
    private static class ReminderFocusAdapter extends FocusAdapter {  //JTextField提示文字通用方法
        private JTextField txt;
        private String reminder;
        private Color foreground;
        private boolean isHintState;

        //初始化
        public ReminderFocusAdapter(JTextField txt_, String reminderString_) {
            // TODO Auto-generated constructor stub
            txt = txt_;
            reminder = reminderString_;
            foreground = txt.getForeground();
            isHintState = false;
        }

        //焦点获得
        @Override
        public void focusGained(FocusEvent e) {
            String tempString = txt.getText();
            if (tempString.equals(reminder)) {
                txt.setText("");
                txt.setForeground(foreground);
                isHintState = false;
            }
        }

        //焦点失去
        @Override
        public void focusLost(FocusEvent e) {
            String tempString = txt.getText();
            if (tempString.equals("")) {
                txt.setForeground(Color.GRAY);
                txt.setText(reminder);
                isHintState = true;
            }
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }
    }
}
