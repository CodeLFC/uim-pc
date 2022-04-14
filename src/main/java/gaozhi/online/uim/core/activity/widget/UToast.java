package gaozhi.online.uim.core.activity.widget;

import javax.swing.*;
import java.awt.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/2 18:36
 */
public class UToast {
    public static void show(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message);
    }
}
