package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.common.registry.KilnInteraction;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.*;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.tweaks.MobSpawning;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class BWIMCHandler {
    public static void processIMC(ImmutableList<IMCMessage> message) {
        Logger log = BWMod.logger;
        for (IMCMessage m : message) {
            try {
                String k = m.key;
                log.debug("[BWIMCHandler] %s from %s", k, m.getSender());

                if ("addCauldronRecipe".equals(k)) {
                    addBulkRecipe(m, CraftingManagerCauldron.getInstance());
                } else if ("addStokedCauldronRecipe".equals(k)) {
                    addBulkRecipe(m, CraftingManagerCauldronStoked.getInstance());
                } else if ("addCrucibleRecipe".equals(k)) {
                    addBulkRecipe(m, CraftingManagerCrucible.getInstance());
                } else if ("addStokedCrucibleRecipe".equals(k)) {
                    addBulkRecipe(m, CraftingManagerCrucibleStoked.getInstance());
                } else if ("addMillRecipe".equals(k)) {
                    addBulkRecipe(m, CraftingManagerMill.getInstance());
                } else if ("addKilnRecipe".equals(k)) {
                    if (m.isNBTMessage()) {
                        NBTTagCompound tag = m.getNBTValue();
                        ItemStack blockStack = new ItemStack(tag.getCompoundTag("Input"));
                        Block block = null;
                        int meta = 0;
                        if (blockStack.getItem() instanceof ItemBlock) {
                            block = ((ItemBlock) blockStack.getItem()).getBlock();
                            meta = blockStack.getItemDamage();
                        }
                        if (block != null) {
                            if (meta != OreDictionary.WILDCARD_VALUE) {
                                KilnInteraction.INSTANCE.addRecipe(block, meta,
                                        new ItemStack(tag.getCompoundTag("Output")));
                            } else
                                KilnInteraction.INSTANCE.addRecipe(block, 0,
                                        new ItemStack(tag.getCompoundTag("Output")));
                        }
                    }
                } else if ("addTurntableRecipe".equals(k)) {
                    if (m.isNBTMessage()) {
                        NBTTagCompound tag = m.getNBTValue();
                        ItemStack blockStack = new ItemStack(tag.getCompoundTag("Input"));
                        IBlockState state = Blocks.AIR.getDefaultState();
                        int meta = 0;
                        if (blockStack.getItem() instanceof ItemBlock) {
                            ItemBlock itemBlock = (ItemBlock) blockStack.getItem();
                            if (blockStack.getItemDamage() != OreDictionary.WILDCARD_VALUE)
                                state = itemBlock.getBlock().getStateFromMeta(blockStack.getItemDamage());
                            else {
                                state = itemBlock.getBlock().getDefaultState();
                                meta = OreDictionary.WILDCARD_VALUE;
                            }
                        }
                        IBlockState output;
                        if (tag.hasKey("Output")) {
                            ItemStack outputStack = new ItemStack(tag.getCompoundTag("Output"));
                            if (outputStack.getItem() instanceof ItemBlock) {
                                if (blockStack.getItemDamage() != OreDictionary.WILDCARD_VALUE)
                                    output = ((ItemBlock) outputStack.getItem()).getBlock()
                                            .getStateFromMeta(blockStack.getItemDamage());
                                else
                                    output = ((ItemBlock) outputStack.getItem()).getBlock().getDefaultState();
                            }
                        }
                        ItemStack[] scraps;
                        if (tag.hasKey("Scrap")) {
                            NBTTagCompound scrapTag = tag.getCompoundTag("Scrap");
                            scraps = new ItemStack[scrapTag.getInteger("ScrapCount")];
                            for (int i = 0; i < scraps.length; i++) {
                                if (tag.hasKey("Item_" + i))
                                    scraps[i] = new ItemStack
                                            (scrapTag.getCompoundTag("Item_" + i).copy());
                            }
                        }
                        //TODO Turntable IMC
                        if (state.getBlock() != Blocks.AIR) {
                            if (meta != OreDictionary.WILDCARD_VALUE) {
//                                TurntableInteraction.addBlockRecipe(state, output, scraps);
                            } else {
//                                TurntableInteraction.addBlockRecipe(state.getBlock().getDefaultState(), output, scraps);
                            }
                        }
                    }
                } else if ("addHeatRegistry".equals(k)) {
                    if (m.isNBTMessage()) {
                        NBTTagCompound tag = m.getNBTValue();
                        ItemStack blockStack = new ItemStack(tag.getCompoundTag("Input"));
                        Block block = null;
                        int meta = 0;
                        int value = tag.getInteger("HeatValue");
                        if (blockStack.getItem() instanceof ItemBlock) {
                            block = ((ItemBlock) blockStack.getItem()).getBlock();
                            meta = blockStack.getItemDamage();
                        }
                        if (block != null) {
                            if (meta != OreDictionary.WILDCARD_VALUE) {
                                BWMHeatRegistry.setBlockHeatRegistry(block, meta, value);
                            } else
                                BWMHeatRegistry.setBlockHeatRegistry(block, value);
                        }
                    }
                } else if ("addNetherSpawnBlock".equals(k)) {
                    if (m.isNBTMessage()) {
                        NBTTagCompound tag = m.getNBTValue();
                        ItemStack blockStack = new ItemStack(tag.getCompoundTag("Spawn"));
                        Block block = null;
                        int meta = 0;
                        if (blockStack.getItem() instanceof ItemBlock) {
                            block = ((ItemBlock) blockStack.getItem()).getBlock();
                            meta = blockStack.getItemDamage();
                        }
                        if (block != null) {
                            if (meta != OreDictionary.WILDCARD_VALUE)
                                MobSpawning.NetherSpawnWhitelist.addBlock(block, meta);
                            else
                                MobSpawning.NetherSpawnWhitelist.addBlock(block);
                        }
                    }
                }
            } catch (Throwable t) {
                bigWarning(log, Level.ERROR, "Bad IMC message (%s)\nfrom %s", m.key, m.getSender());
                log.catching(t);
            }
        }
    }

    private static void addBulkRecipe(IMCMessage m, CraftingManagerBulk craft) {
        if (m.isNBTMessage()) {
            NBTTagCompound tag = m.getNBTValue();
            NBTTagCompound outputs = tag.getCompoundTag("Outputs");
            ItemStack output = new ItemStack(outputs.getCompoundTag("Output"));
            ItemStack second = null;
            if (outputs.hasKey("Secondary"))
                second = new ItemStack(outputs.getCompoundTag("Secondary"));
            NBTTagCompound inputs = tag.getCompoundTag("Inputs");
            Object[] inputStacks = new Object[inputs.getInteger("InputLength")];
            for (int i = 0; i < inputs.getInteger("InputLength"); i++) {
                NBTBase obj = inputs.getTag("Input_" + i);
                if (obj instanceof NBTTagCompound)
                    inputStacks[i] = new ItemStack(inputs.getCompoundTag("Input_" + i));
                else if (obj instanceof NBTTagString) {
                    if (i + 1 < inputs.getInteger("InputLength")) {
                        NBTBase nextObj = inputs.getTag("Input_" + (i + 1));
                        if (nextObj instanceof NBTTagInt) {
                            inputStacks[i] = new OreStack(inputs.getString("Input_" + i),
                                    inputs.getInteger("Input_" + (i + 1)));
                            i++;
                        } else
                            inputStacks[i] = new OreStack(inputs.getString("Input_" + i), 1);
                    } else
                        inputStacks[i] = new OreStack(inputs.getString("Input_" + i), 1);
                }
            }
            craft.addOreRecipe(output, second, inputStacks);
        }
    }

    public static void bigWarning(Logger log, Level level, String format, Object... data) {
        String o = String.format(format, data);
        String err = "************************";
        err += err;
        log.log(level, err);
        log.log(level, err);
        for (String str : o.split("\n", 0))
            log.log(level, str);
        log.log(level, err);
        log.log(level, err);
    }
}
