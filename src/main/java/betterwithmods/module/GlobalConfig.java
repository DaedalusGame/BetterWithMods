package betterwithmods.module;

import net.minecraftforge.common.config.Configuration;

public final class GlobalConfig {
    public static boolean debug;
    public static int maxPlatformBlocks;

    public static void initGlobalConfig() {
        String category = "_global";

        ConfigHelper.needsRestart = ConfigHelper.allNeedRestart = true;

        debug = ConfigHelper.loadPropBool("Debug", category, "Enables debug features", false);
        maxPlatformBlocks = ConfigHelper.loadPropInt("Max Platform Blocks", category, "Max blocks a platform can have", 128);
        ConfigHelper.needsRestart = ConfigHelper.allNeedRestart = false;
    }
    public static void changeConfig(String moduleName, String category, String key, String value, boolean saveToFile) {
        Configuration config = ModuleLoader.config;
        String fullCategory = moduleName;
        if(!category.equals("-"))
            fullCategory += "." + category;

        char type = key.charAt(0);
        key = key.substring(2);

        if(config.hasKey(fullCategory, key)) {
            boolean changed = false;

            try {
                switch(type) {
                    case 'B':
                        boolean b = Boolean.parseBoolean(value);
                        config.get(fullCategory, key, false).setValue(b);
                    case 'I':
                        int i = Integer.parseInt(value);
                        config.get(fullCategory, key, 0).setValue(i);
                    case 'D':
                        double d = Double.parseDouble(value);
                        config.get(fullCategory, key, 0.0).setValue(d);
                    case 'S':
                        config.get(fullCategory, key, "").setValue(value);
                }
            } catch(IllegalArgumentException ignored) {}

            if(config.hasChanged()) {
                ModuleLoader.forEachModule(Module::setupConfig);

                if(saveToFile)
                    config.save();
            }
        }
    }
}
