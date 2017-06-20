package betterwithmods.module;

import betterwithmods.client.gui.GuiStatus;
import betterwithmods.common.blocks.BlockHemp;
import net.minecraftforge.common.config.Configuration;

public final class GlobalConfig {
    public static boolean debug;
    public static int maxPlatformBlocks;

    public static void initGlobalConfig() {
        String category = "_global";

        ConfigHelper.needsRestart = ConfigHelper.allNeedRestart = true;

        debug = ConfigHelper.loadPropBool("Debug", category, "Enables debug features", false);
        maxPlatformBlocks = ConfigHelper.loadPropInt("Max Platform Blocks", category, "Max blocks a platform can have", 128);
        GuiStatus.offsetY = ConfigHelper.loadPropInt("Status Effect Offset Y", "gui", "Y Offset for the Hunger, Injury and Gloom Status effects.", 0);
        GuiStatus.offsetX = ConfigHelper.loadPropInt("Status Effect Offset X", "gui", "X Offset for the Hunger, Injury and Gloom Status effects.", 0);
        ConfigHelper.needsRestart = ConfigHelper.allNeedRestart = false;

        BlockHemp.growthChance = ConfigHelper.loadPropDouble("Growth Chance","Hemp","Hemp has a 1/X chance of growing where X is this value, the following modifiers divide this value", 15D);
        BlockHemp.fertileModifier = ConfigHelper.loadPropDouble("Fertile Modifier","Hemp","Modifies Hemp Growth Chance when planted on Fertile Farmland", 1.33);
        BlockHemp.lampModifier = ConfigHelper.loadPropDouble("Light Block Modifier","Hemp","Modifies Hemp Growth Chance when a Light Block is two blocks above the Hemp",  1.5D);
        BlockHemp.neighborModifier = ConfigHelper.loadPropDouble("Neighbor Modifier","Hemp","Modifies Hemp Growth Chance for each other crop next to it ",  1.1D);


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
