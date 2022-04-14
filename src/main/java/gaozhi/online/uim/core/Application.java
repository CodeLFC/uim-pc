package gaozhi.online.uim.core;

import gaozhi.online.uim.core.activity.Context;
import gaozhi.online.uim.core.resource.FontClass;
import gaozhi.online.uim.core.resource.Resources;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

import javax.swing.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 主应用
 * @date 2022/1/25 13:28
 */
public class Application extends Context {
    private final TaskExecutor taskExecutor = new TaskExecutor();


    public Application(Resources resources) {
        super(resources);
        FontClass.loadIndyFont(FontClass.FontExample.SONG);
    }

    public void configSkin(SubstanceSkin substanceSkin) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        taskExecutor.executeInUIThread(() -> {
            //启动页面
            SubstanceLookAndFeel.setSkin(substanceSkin);
        });
    }

}
