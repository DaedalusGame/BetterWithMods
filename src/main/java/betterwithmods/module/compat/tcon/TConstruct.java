package betterwithmods.module.compat.tcon;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockBWMFluid;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.tweaks.MobSpawning;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.smeltery.OreCastingRecipe;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTraits;

import java.util.List;
import java.util.Set;

import static slimeknights.tconstruct.smeltery.TinkerSmeltery.*;

public class TConstruct extends CompatFeature {
    public final Material MATERIAL_SOULFORGED_STEEL = newTinkerMaterial("soulforged_steel", 5066061);
    public final Material MATERIAL_CONCENTRATED_HELLFIRE = newTinkerMaterial("hellfire", 14426647);
    public AbstractTrait mending;
    public FluidMolten FLUID_SOULFORGED_STEEL;
    public FluidMolten FLUID_CONCENTRATED_HELLFIRE;
    public Block BLOCK_FLUID_SOULFORGED_STEEL;
    public Block BLOCK_FLUID_HELLFIRE;

    public TConstruct() {
        super("tconstruct");
    }

    @Override
    public void preInit(FMLPreInitializationEvent evt) {
        FLUID_SOULFORGED_STEEL = fluidMetal("soulforged_steel", 5066061);
        FLUID_SOULFORGED_STEEL.setTemperature(681);
        BLOCK_FLUID_SOULFORGED_STEEL = new BlockBWMFluid(FLUID_SOULFORGED_STEEL, net.minecraft.block.material.Material.LAVA).setRegistryName("soulforged_steel");

        FLUID_CONCENTRATED_HELLFIRE = fluidMetal("hellfire", 14426647);
        FLUID_CONCENTRATED_HELLFIRE.setTemperature(850);
        BLOCK_FLUID_HELLFIRE = new BlockBWMFluid(FLUID_CONCENTRATED_HELLFIRE, net.minecraft.block.material.Material.LAVA).setRegistryName("hellfire");
        BWMBlocks.registerBlock(BLOCK_FLUID_SOULFORGED_STEEL);
        BWMBlocks.registerBlock(BLOCK_FLUID_HELLFIRE);
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        BWMBlocks.registerFluidModels(FLUID_CONCENTRATED_HELLFIRE);
        BWMBlocks.registerFluidModels(FLUID_SOULFORGED_STEEL);
    }

    @Override
    public void init(FMLInitializationEvent evt) {
        mending = new TraitMending();
        MATERIAL_SOULFORGED_STEEL.addTrait(mending);
        MATERIAL_SOULFORGED_STEEL.addItem("nuggetSoulforgedSteel", 1, Material.VALUE_Nugget);
        MATERIAL_SOULFORGED_STEEL.addItem("ingotSoulforgedSteel", 1, Material.VALUE_Ingot);
        MATERIAL_SOULFORGED_STEEL.addItem("blockSoulforgedSteel", 1, Material.VALUE_Block);
        TinkerRegistry.addMaterialStats(MATERIAL_SOULFORGED_STEEL, new HeadMaterialStats(875, 12.0F, 6.0F, HarvestLevels.OBSIDIAN), new HandleMaterialStats(1.0F, 225), new ExtraMaterialStats(50));
        registerMaterial(MATERIAL_SOULFORGED_STEEL, FLUID_SOULFORGED_STEEL, "SoulforgedSteel", 16);

        MATERIAL_CONCENTRATED_HELLFIRE.addTrait(TinkerTraits.autosmelt);
        MATERIAL_CONCENTRATED_HELLFIRE.addItem("ingotConcentratedHellfire", 1, Material.VALUE_Ingot);
        MATERIAL_CONCENTRATED_HELLFIRE.addItem("blockConcentratedHellfire", 1, Material.VALUE_Block);

        TinkerRegistry.addMaterialStats(MATERIAL_CONCENTRATED_HELLFIRE, new HeadMaterialStats(325, 8.0F, 4.0F, HarvestLevels.DIAMOND), new HandleMaterialStats(0.75F, 75), new ExtraMaterialStats(25));
        registerMaterial(MATERIAL_CONCENTRATED_HELLFIRE, FLUID_CONCENTRATED_HELLFIRE, "ConcentratedHellfire", 9, 8);
        netherWhitelist();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient(FMLInitializationEvent evt) {
        registerRenderInfo(MATERIAL_SOULFORGED_STEEL, 5066061, 0.1F, 0.3F, 0.1F);
        registerRenderInfo(MATERIAL_CONCENTRATED_HELLFIRE, 14426647, 0.0F, 0.2F, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    private void registerRenderInfo(Material material, int color, float shininess, float brightness, float hueshift) {
        material.setRenderInfo(new MaterialRenderInfo.Metal(color, shininess, brightness, hueshift));
    }

    private void netherWhitelist() {
        Block ore = Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "ore"));
        MobSpawning.NetherSpawnWhitelist.addBlock(ore, 0);
        MobSpawning.NetherSpawnWhitelist.addBlock(ore, 1);
        MobSpawning.NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_congealed")), 3);
        MobSpawning.NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_congealed")), 4);
        MobSpawning.NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_dirt")), 3);
        Block slimeGrass = Block.REGISTRY.getObject(new ResourceLocation("tconstruct", "slime_grass"));
        MobSpawning.NetherSpawnWhitelist.addBlock(slimeGrass, 4);
        MobSpawning.NetherSpawnWhitelist.addBlock(slimeGrass, 9);
        MobSpawning.NetherSpawnWhitelist.addBlock(slimeGrass, 14);
    }

    private void registerMaterial(Material material, Fluid fluid, String oreSuffix, int blockValue) {
        registerMaterial(material, fluid, oreSuffix, blockValue, 1);
    }

    private void registerMaterial(Material material, Fluid fluid, String oreSuffix, int blockValue, int dustDivision) {
        MaterialIntegration mat = new MaterialIntegration(material, fluid, oreSuffix).setRepresentativeItem("ingot" + oreSuffix);
        mat.integrate();
        registerMeltingStuff(oreSuffix, fluid, blockValue, dustDivision);
        TinkerSmeltery.registerToolpartMeltingCasting(material);
        mat.registerRepresentativeItem();
    }

    private Material newTinkerMaterial(String name, int color) {
        Material mat = new Material(name, color);
        TinkerMaterials.materials.add(mat);
        return mat;
    }

    private FluidMolten fluidMetal(String name, int color) {
        FluidMolten fluid = new FluidMolten(name, color);
        return registerFluid(fluid);
    }

    private <T extends Fluid> T registerFluid(T fluid) {
        fluid.setUnlocalizedName(BWMod.MODID + ":" + fluid.getName());
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    /*Abridged version of TinkerSmeltery.registerOredictMeltingCasting
    Since both Hellfire and Soulforged Steel are technically alloys, there's no need to look for ore variants.
     */
    private void registerMeltingStuff(String ore, Fluid fluid, int blockValue, int dustDivision) {
        ImmutableSet.Builder<Pair<List<ItemStack>, Integer>> builder = ImmutableSet.builder();
        Pair<List<ItemStack>, Integer> nuggetOre = Pair.of(OreDictionary.getOres("nugget" + ore), Material.VALUE_Nugget);
        Pair<List<ItemStack>, Integer> ingotOre = Pair.of(OreDictionary.getOres("ingot" + ore), Material.VALUE_Ingot);
        Pair<List<ItemStack>, Integer> plateOre = Pair.of(OreDictionary.getOres("plate" + ore), Material.VALUE_Ingot);
        Pair<List<ItemStack>, Integer> gearOre = Pair.of(OreDictionary.getOres("gear" + ore), Material.VALUE_Ingot * 4);
        Pair<List<ItemStack>, Integer> blockOre = Pair.of(OreDictionary.getOres("block" + ore), Material.VALUE_Ingot * blockValue);
        Pair<List<ItemStack>, Integer> dustOre = Pair.of(OreDictionary.getOres("dust" + ore), Material.VALUE_Ingot / dustDivision);

        builder.add(nuggetOre, ingotOre, plateOre, gearOre, blockOre, dustOre);
        Set<Pair<List<ItemStack>, Integer>> knownOres = builder.build();


        // register oredicts
        for (Pair<List<ItemStack>, Integer> pair : knownOres) {
            TinkerRegistry.registerMelting(new MeltingRecipe(RecipeMatch.of(pair.getLeft(), pair.getRight()), fluid));
        }

        // register oredict castings!
        // ingot casting
        TinkerRegistry.registerTableCasting(new OreCastingRecipe(ingotOre.getLeft(),
                RecipeMatch.ofNBT(castIngot),
                fluid,
                ingotOre.getRight()));
        // nugget casting
        TinkerRegistry.registerTableCasting(new OreCastingRecipe(nuggetOre.getLeft(),
                RecipeMatch.ofNBT(castNugget),
                fluid,
                nuggetOre.getRight()));
        // block casting
        TinkerRegistry.registerBasinCasting(new OreCastingRecipe(blockOre.getLeft(),
                null, // no cast
                fluid,
                blockOre.getRight()));
        // plate casting
        TinkerRegistry.registerTableCasting(new OreCastingRecipe(plateOre.getLeft(),
                RecipeMatch.ofNBT(castPlate),
                fluid,
                plateOre.getRight()));
        // gear casting
        TinkerRegistry.registerTableCasting(new OreCastingRecipe(gearOre.getLeft(),
                RecipeMatch.ofNBT(castGear),
                fluid,
                gearOre.getRight()));

        // and also cast creation!
        for (FluidStack fs : castCreationFluids) {
            TinkerRegistry.registerTableCasting(new CastingRecipe(castIngot, RecipeMatch.of(ingotOre.getLeft()), fs, true, true));
            TinkerRegistry.registerTableCasting(new CastingRecipe(castNugget, RecipeMatch.of(nuggetOre.getLeft()), fs, true, true));
            TinkerRegistry.registerTableCasting(new CastingRecipe(castPlate, RecipeMatch.of(plateOre.getLeft()), fs, true, true));
            TinkerRegistry.registerTableCasting(new CastingRecipe(castGear, RecipeMatch.of(gearOre.getLeft()), fs, true, true));
        }
    }


}
