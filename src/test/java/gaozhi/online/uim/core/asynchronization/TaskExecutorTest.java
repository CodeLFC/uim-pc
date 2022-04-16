package gaozhi.online.uim.core.asynchronization;

import gaozhi.online.ugui.core.asynchronization.TaskExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutorTest {
  @Test
    public void test1() throws InterruptedException {
        new TaskExecutor().executeTimerTask(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        },300);
        Thread.sleep(3000);
    }
}