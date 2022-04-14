package gaozhi.online.uim.core.resource;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 资源加载类
 * @date 2022/1/25 21:47
 */
public class Resources {
    private final Locale locale;
    private final ResourceBundle resources;
    private final Logger logger = Logger.getGlobal();
    public Resources(String baseName) {
        this(baseName, Locale.getDefault());
        logger.info(baseName+""+locale);
    }

    public Resources(String baseName, Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle(baseName, locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public String getString(String key) {
        return resources.getString(key);
    }
}
