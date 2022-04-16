package gaozhi.online.uim.im.service;

import gaozhi.online.uim.im.entity.UServer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/3/20 18:52
 */
public class IMServiceApplication {
    private static final IMServiceApplication instance = new IMServiceApplication();
    private UServer imServer;

    private IMServiceApplication() {
        if (instance != null) {
            throw new UnsupportedOperationException("单例不允许重复创建");
        }
    }

    public UServer getImServer() {
        return imServer;
    }

    public void setImServer(UServer imServer) {
        this.imServer = imServer;
    }

    public static IMServiceApplication getInstance() {
        return instance;
    }

    private final Map<String, UService> imUService = new HashMap<>();

    public <T extends UService> void registerService(T instance) {
        imUService.put(instance.getClass().getName(), instance);
        instance.setIMApplication(this);
    }

    public <T extends UService> T getServiceInstance(Class<T> klass) {
        return (T) imUService.get(klass.getName());
    }

}
