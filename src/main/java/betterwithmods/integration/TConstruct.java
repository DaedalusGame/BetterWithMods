package betterwithmods.integration;

import betterwithmods.BWMod;
import betterwithmods.integration.tcon.TraitMending;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerMaterials;

import java.util.List;

public class TConstruct
{
    public static final Material soulforgedSteel = mat("soulforgedSteel", 5066061);
    public static final Material hellfire = mat("hellfire", 14426647);

    public static AbstractTrait mending;

    public static FluidMolten soulforgeFluid;
    public static FluidMolten hellfireFluid;

    public static void init()
    {
        mending = new TraitMending();
        if(BWMod.proxy.isClientside())
            registerRenderInfo(soulforgedSteel, 5066061, 0.1F, 0.3F, 0.1F);
        soulforgeFluid = fluidMetal("soulforged_steel", 5066061);
        soulforgeFluid.setTemperature(681);
        soulforgedSteel.addItem("ingotSoulforgedSteel", 1, Material.VALUE_Ingot);
        soulforgedSteel.addTrait(mending);
        if(BWMod.proxy.isClientside())
            registerRenderInfo(hellfire, 14426647, 0.0F, 0.2F, 0.0F);
        hellfireFluid = fluidMetal("hellfire", 14426647);
        hellfireFluid.setTemperature(850);
        hellfire.addItem("ingotHellfire", 1, Material.VALUE_Ingot);
        hellfire.addTrait(TinkerMaterials.autosmelt);

        TinkerRegistry.addMaterialStats(soulforgedSteel, new HeadMaterialStats(875, 12.0F, 6.0F, HarvestLevels.OBSIDIAN), new HandleMaterialStats(1.0F, 225), new ExtraMaterialStats(50));
        TinkerRegistry.addMaterialStats(hellfire, new HeadMaterialStats(325, 8.0F, 4.0F, HarvestLevels.DIAMOND), new HandleMaterialStats(0.75F, 75), new ExtraMaterialStats(25));
        registerMaterial(soulforgedSteel, soulforgeFluid, "SoulforgedSteel");
        registerMaterial(hellfire, hellfireFluid, "Hellfire");
        fixHellfireDust();
        netherWhitelist();
    }

    private static void netherWhitelist()
    {
        Block ore = Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "ore"));
        NetherSpawnWhitelist.addBlock(ore, 0);
        NetherSpawnWhitelist.addBlock(ore, 1);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_congealed")), 3);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_congealed")), 4);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_dirt")), 3);
        Block slimeGrass = Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_grass"));
        NetherSpawnWhitelist.addBlock(slimeGrass, 4);
        NetherSpawnWhitelist.addBlock(slimeGrass, 9);
        NetherSpawnWhitelist.addBlock(slimeGrass, 14);
    }

    private static void registerMaterial(Material material, Fluid fluid, String oreSuffix)
    {
        MaterialIntegration mat = new MaterialIntegration(material, fluid, oreSuffix).setRepresentativeItem("ingot" + oreSuffix);
        mat.integrate();
        mat.integrateRecipes();
        mat.registerRepresentativeItem();
    }

    private static Material mat(String name, int color)
    {
        Material mat = new Material(name, color);
        TinkerMaterials.materials.add(mat);
        return mat;
    }

    private static FluidMolten fluidMetal(String name, int color)
    {
        FluidMolten fluid = new FluidMolten(name, color);
        return registerFluid(fluid);
    }

    private static <T extends Fluid> T registerFluid(T fluid)
    {
        fluid.setUnlocalizedName("betterwithmods:" + fluid.getName());
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    @SideOnly(Side.CLIENT)
    private static void registerRenderInfo(Material material, int color, float shininess, float brightness, float hueshift)
    {
        material.setRenderInfo(new MaterialRenderInfo.Metal(color, shininess, brightness, hueshift));
    }

    private static void fixHellfireDust()
    {
        Pair<List<ItemStack>, Integer> dustOre = Pair.of(OreDictionary.getOres("powderedHellfire"), Material.VALUE_Ingot / 8);
        TinkerRegistry.registerMelting(new MeltingRecipe(RecipeMatch.of(dustOre.getLeft(), dustOre.getRight()), hellfireFluid));
    }
}
