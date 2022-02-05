package delta.managers;

import delta.DeltaCore;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.FileUtils;
import delta.util.Wrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager implements Wrapper {
    File folder = new File(mc.gameDir.getAbsoluteFile() + "/delta");

    public void saveModule() throws IOException {
        for (Module module : DeltaCore.moduleManager.getModules()) {

            if (folder.mkdir()) {
                System.out.println("noatmc logger - file created");
            } else {
                System.out.println("noatmc logger - file already existed");
            }

            File temp = new File(folder + "/" + module.getName() + "/");
            if (temp.mkdir()) {
                System.out.println("noatmc logger - file created");
            } else {
                System.out.println("noatmc logger - file already existed");
            }
//
//            File file2 = new File(folder + "/" + module.getName() + "/Bind.txt");
//            if (file2.createNewFile()) {
//                FileWriter writer = new FileWriter(file2);
//                writer.write(module.getKey());
//                writer.close();
//            }  // cope

            File file3 = new File(folder + "/" + module.getName() + "/Enabled.txt");
            if (file3.createNewFile()) {
                FileWriter writer = new FileWriter(file3);
                writer.write(String.valueOf(module.isToggled()));
                writer.close();
            }  // cope

            for (Setting setting : module.getSettings()) {
                File file = new File(String.valueOf(Paths.get(folder + "/" + module.getName() + "/" + setting.getName() + ".txt")));
                if (file.createNewFile()) {
                    System.out.println("noatmc logger - file created");
                } else {
                    System.out.println("noatmc logger - file already existed");
                }
                FileWriter writer = new FileWriter(file.getPath());
                if (setting.isBVal()) {
                    writer.write(String.valueOf(setting.getBVal()));
                } else if (setting.isDVal()) {
                    writer.write(String.valueOf(setting.getDVal()));
                } else if (setting.isMVal()) {
                    writer.write(String.valueOf(setting.getMode()));
                } else if (setting.isIntVal()) {
                    writer.write(String.valueOf(setting.getDVal()));
                }
                writer.close();
            }
        }
    }

    public void loadModule() throws IOException {
        for (Module module : DeltaCore.moduleManager.getModules()) {
            File enabled = new File(folder.getAbsolutePath() + "/" + module.getName() + "/Enabled.txt");
//            File bind = new File(folder.getAbsolutePath() + "/" + module.getName() + "/Bind.txt");
//            if (bind.exists()) {
//                module.setKey(Integer.parseInt(FileUtils.getContentFromFile(bind)));
//            }
            if (enabled.exists()) {
                module.setToggled(Boolean.parseBoolean(FileUtils.getContentFromFile(enabled)));
            }
            for (Setting setting : module.getSettings()) {
                File file = new File(folder.getAbsolutePath() + "/" + module.getName() + "/" + setting.getName());
                if (file.exists()) {
                    String content = FileUtils.getContentFromFile(file);
                    if (setting.isMVal()) {
                        setting.setMode(content);
                    } else if (setting.isBVal()) {
                        setting.setBVal(Boolean.parseBoolean(content));
                    } else if (setting.isDVal()) {
                        setting.setDVal(Double.parseDouble(content));
                    } else if (setting.isIntVal()) {
                        setting.setDVal(Double.parseDouble(content));
                    }
                }
            }
        }
    }
}
