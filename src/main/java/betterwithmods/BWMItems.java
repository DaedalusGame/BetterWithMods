package betterwithmods;

import betterwithmods.api.block.IBTWBlock;
import betterwithmods.blocks.mini.ItemBlockMini;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.client.BWStateMapper;
import betterwithmods.items.*;
import betterwithmods.items.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BWMItems {
	private BWMItems() {
	}

	public static final ToolMaterial SOULFORGED_STEEL = EnumHelper.addToolMaterial("soulforged_steel", 3, 1561, 8, 3,
			22);

	public static final Item material = new ItemMaterial().setRegistryName("material");
	public static final Item windmill = new ItemMechanical().setRegistryName("windmill");
	public static final Item bark = new ItemBark().setRegistryName("bark");
	public static final Item donut = new ItemFood(2, 0.5F, false).setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("donut");
	public static final Item knife = new ItemKnife(ToolMaterial.IRON).setRegistryName("knife");
	public static final Item dynamite = new ItemDynamite().setRegistryName("dynamite");
	public static final Item fertilizer = new ItemFertilizer().setRegistryName("fertilizer");
	public static final Item steelAxe = new ItemSoulforgedAxe().setRegistryName("steel_axe");
	public static final Item steelHoe = new ItemSoulforgedHoe().setRegistryName("steel_hoe");
	public static final Item steelPickaxe = new ItemSoulforgedPickaxe().setRegistryName("steel_pickaxe");
	public static final Item steelShovel = new ItemSoulforgedShovel().setRegistryName("steel_shovel");
	public static final Item steelSword = new ItemSoulforgedSword().setRegistryName("steel_sword");
	public static final Item creeperOyster = (new ItemFood(2, 0.5F, false).setPotionEffect(new PotionEffect(MobEffects.POISON,5,0),1)).setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("creeper_oyster");
	public static final Item enderSpectacles = new ItemEnderSpectacles().setRegistryName("ender_spectacles");

	public static void registerItems() {
		registerItem(material);
		registerItem(windmill);
		registerItem(bark);
		registerItem(donut);
		registerItem(knife);
		registerItem(dynamite);
		registerItem(fertilizer);
		registerItem(steelAxe);
		registerItem(steelHoe);
		registerItem(steelPickaxe);
		registerItem(steelShovel);
		registerItem(steelSword);
        registerItem(creeperOyster);
		registerItem(enderSpectacles);
	}

	public static void linkItemModels() {
		setInventoryModel(material);
		setInventoryModel(windmill);
		setInventoryModel(bark);
		setInventoryModel(donut);
		setInventoryModel(knife);
		setInventoryModel(dynamite);
		setInventoryModel(fertilizer);
		setInventoryModel(steelAxe);
		setInventoryModel(steelHoe);
		setInventoryModel(steelPickaxe);
		setInventoryModel(steelShovel);
		setInventoryModel(steelSword);
        setInventoryModel(creeperOyster);
		setInventoryModel(enderSpectacles);
	}

	/**
	 * Register an Item.
	 * 
	 * @param item Item instance to register.
	 * @return Registered item.
	 */
	public static Item registerItem(Item item) {
        item.setUnlocalizedName("bwm"+ item.getRegistryName().toString().substring(BWMod.MODID.length()));
		return GameRegistry.register(item);
	}

	private static void setModelLocation(Item item, int meta, String variantSettings) {
		setModelLocation(item, meta, item.getRegistryName().toString(), variantSettings);
	}

	private static void setModelLocation(Item item, int meta, String location, String variantSettings) {
		if (meta == OreDictionary.WILDCARD_VALUE) {
			ModelLoader.setCustomMeshDefinition(item,
					stack -> new ModelResourceLocation(location, variantSettings));
		} else {
			ModelLoader.setCustomModelResourceLocation(item, meta,
					new ModelResourceLocation(location, variantSettings));
		}
	}

	private static void setInventoryModel(ItemBlock item) {
		Block block = item.getBlock();
		if (item instanceof ItemBlockMini) {
			registerMiniBlockNBT((ItemBlockMini) item);
			ModelLoader.setCustomStateMapper(block, new BWStateMapper(block.getRegistryName().toString()));
		} else if (block instanceof IBTWBlock) {
			ModelLoader.setCustomStateMapper(block, new BWStateMapper(block.getRegistryName().toString()));
			String[] variants = ((IBTWBlock) block).getVariants();
			for (int meta = 0; meta < variants.length; meta++) {
				setModelLocation(item, meta, variants[meta]);
			}
		} else if (block instanceof IBWMItem) {// TODO A block that implements IBWMItem sounds weird.
			for (int i = 0; i < ((IBWMItem) block).getMaxMeta(); i++) {
				final String location = ((IBWMItem) block).getLocation(i);
				setModelLocation(item, i, location, "inventory");
			}
		} else {
			setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
		}
	}

	public static void setInventoryModel(Item item) {
		if (item.isDamageable()) {
			setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
		} else if (item instanceof IBWMItem) {
			IBWMItem bwmItem = (IBWMItem) item;
			for (int i = 0; i < bwmItem.getMaxMeta(); i++) {
				String location = bwmItem.getLocation(i);
				setModelLocation(item, i, location, "inventory");
			}
		} else if (item instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock) item;
			setInventoryModel(itemBlock);
		} else {
			setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
		}
	}

	private static void registerMiniBlockNBT(ItemBlockMini item) {
		//TODO use more of the BlockPlanks.EnumType instead of metadata
		ModelLoader.setCustomMeshDefinition(item,
				stack -> (stack.hasTagCompound() && stack.getTagCompound().hasKey("type")) ? new ModelResourceLocation(
						item.getRegistryName() + "_"
								+ BlockPlanks.EnumType.byMetadata(stack.getTagCompound().getInteger("type")).getName(),
						"inventory")
						: stack.getItemDamage() < 6
								? new ModelResourceLocation(
										item.getRegistryName() + "_"
												+ BlockPlanks.EnumType.byMetadata(stack.getItemDamage()).getName(),
										"inventory")
								: new ModelResourceLocation(item.getRegistryName() + "_oak", "inventory"));
		ModelResourceLocation[] resource = new ModelResourceLocation[6];
		for (int i = 0; i < 6; i++)
			resource[i] = new ModelResourceLocation(item.getRegistryName() + "_" + BlockPlanks.EnumType.byMetadata(i),
					"inventory");
		ModelBakery.registerItemVariants(item, resource);
	}
}
