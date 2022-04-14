package gaozhi.online.uim.core.asynchronization;


/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  暂未完成
 * @date 2022/3/14 19:42
 */
public class Handler {
    private TaskExecutor taskExecutor = new TaskExecutor();

    public void sendMessage(Message message) {
        // 模拟发送到GUI线程
        taskExecutor.executeInUIThread(() -> handleMessage(message));
    }

    /**
     * 处理者
     */
    public interface Worker {
        void handleMessage(Message msg);
    }

    private Worker worker;

    public Handler(Worker worker) {
        this.worker = worker;
    }

    public void handleMessage(Message msg) {
        if (worker != null) {
            worker.handleMessage(msg);
        }
    }

    public static class Message {
        public int what;
        public Object obj;
    }
}
