package gaozhi.online.uim.example.im.service;

import java.util.logging.Logger;

public interface UService {
    default Logger getLogger() {
        return Logger.getGlobal();
    }

    void setIMApplication(IMServiceApplication imApplication);

    void startService();

    void closeService();
}
