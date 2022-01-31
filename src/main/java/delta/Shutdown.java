package delta;

public class Shutdown extends Thread {
    public void run() {
        saveConfig();
    }

    public static void saveConfig() {
        try {
            DeltaCore.configManager.saveModule();
        } catch (Exception e) {
            // fuck
        }
    }
}
