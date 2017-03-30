package betterwithmods.integration.jei;

import betterwithmods.common.BWMBlocks;
import betterwithmods.client.container.bulk.GuiCauldron;
import betterwithmods.client.container.bulk.GuiCrucible;
import betterwithmods.client.container.bulk.GuiMill;
import betterwithmods.client.container.anvil.GuiSteelAnvil;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.common.registry.KilnInteraction;
import betterwithmods.common.registry.SawInteraction;
import betterwithmods.common.registry.TurntableInteraction;
import betterwithmods.common.registry.bulk.*;
import betterwithmods.common.registry.steelanvil.SteelCraftingManager;
import betterwithmods.integration.jei.category.*;
import betterwithmods.integration.jei.handler.*;
import com.google.common.collect.Lists;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class BWMJEIPlugin extends BlankModPlugin {
    public static IJeiHelpers helper;

    @Override
    public void register(@Nonnull IModRegistry reg) {
        helper = reg.getJeiHelpers();
            IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(new CauldronRecipeCategory(guiHelper), new StokedCauldronRecipeCategory(guiHelper), new CrucibleRecipeCategory(guiHelper),
                new StokedCrucibleRecipeCategory(guiHelper), new MillRecipeCategory(guiHelper), new SawRecipeCategory(guiHelper), new KilnRecipeCategory(guiHelper),
                new TurntableRecipeCategory(guiHelper), new HopperRecipeCategory(guiHelper), new SoulUrnCategory(guiHelper), new SteelAnvilRecipeCategory(guiHelper));

        reg.addRecipeHandlers(new BulkRecipeHandler(), new BlockMetaRecipeHandler(), new TurntableHandler(), new HopperRecipeHandler(),
              new SteelShapedRecipeHandler(helper), new SteelShapelessRecipeHandler(helper));

        reg.addRecipes(CraftingManagerCauldron.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCauldronStoked.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCrucible.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCrucibleStoked.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerMill.getInstance().getRecipes());
        reg.addRecipes(SawInteraction.INSTANCE.getRecipes());
        reg.addRecipes(KilnInteraction.INSTANCE.getRecipes());
        reg.addRecipes(TurntableInteraction.INSTANCE.getRecipes());
        reg.addRecipes(HopperInteractions.recipes);
        reg.addRecipes(SteelCraftingManager.getInstance().getRecipeList());

        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 0), "bwm.mill");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 2), "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4), "bwm.hopper", "bwm.hopper.soul_urn");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5), "bwm.turntable");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SAW), "bwm.saw");
        reg.addRecipeCategoryCraftingItem(new ItemStack(Blocks.BRICK_BLOCK), "bwm.kiln");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.STEEL_ANVIL), "bwm.steel_anvil");

        reg.addRecipeClickArea(GuiCauldron.class, 81, 19, 14, 14, "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeClickArea(GuiCrucible.class, 81, 19, 14, 14, "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeClickArea(GuiMill.class, 81, 19, 14, 14, "bwm.mill");
        reg.addRecipeClickArea(GuiSteelAnvil.class, 88, 41, 28, 23, "bwm.steel_anvil");
        registerAnvil(reg);
    }

    private void registerAnvil(IModRegistry reg) {
        List<ItemStack> tools = Lists.newArrayList(new ItemStack(BWMItems.STEEL_AXE), new ItemStack(BWMItems.STEEL_BATTLEAXE), new ItemStack(BWMItems.STEEL_BOOTS), new ItemStack(BWMItems.STEEL_CHEST), new ItemStack(BWMItems.STEEL_HELMET), new ItemStack(BWMItems.STEEL_HOE), new ItemStack(BWMItems.STEEL_MATTOCK),
                new ItemStack(BWMItems.STEEL_PANTS), new ItemStack(BWMItems.STEEL_PICKAXE), new ItemStack(BWMItems.STEEL_SHOVEL), new ItemStack(BWMItems.STEEL_SWORD));
        for (ItemStack stack : tools) {
            ItemStack dam1 = stack.copy();
            dam1.setItemDamage(dam1.getMaxDamage());
            ItemStack dam2 = stack.copy();
            dam2.setItemDamage(dam2.getMaxDamage() * 3 / 4);
            ItemStack dam3 = stack.copy();
            dam3.setItemDamage(dam3.getMaxDamage() * 2 / 4);
            reg.addAnvilRecipe(dam1, Collections.singletonList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL)), Collections.singletonList(dam2));
            reg.addAnvilRecipe(dam2, Collections.singletonList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL)), Collections.singletonList(dam3));
        }
    }
}
