package delta.managers;

import delta.DeltaCore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    ExecutorService service = Executors.newFixedThreadPool(2);

    public void execute(Runnable command) {
        service.execute(command);
    }

    public ThreadManager() {
        new Thread(() -> {
            while (true) {
                DeltaCore.moduleManager.onThread();
            }
        }).start();
    }
}
