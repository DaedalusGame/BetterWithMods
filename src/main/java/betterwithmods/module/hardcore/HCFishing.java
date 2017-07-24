package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.common.BWMItems;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 7/23/17.
 */
public class HCFishing extends Feature {
    private static final ResourceLocation BAITED_FISHING_ROD = new ResourceLocation(BWMod.MODID, "baited_fishing_rod");
    private static final Ingredient BAIT = Ingredient.fromStacks(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(BWMItems.CREEPER_OYSTER), new ItemStack(Items.FISH, 1, 2), new ItemStack(Items.FISH, 1, 3));

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(FishingBait.class, new CapabilityFishingRod(), FishingBait::new);
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.FISHING_ROD), "  I", " IS", "I N", 'S', "string", 'I', "stickWood", 'N', "nuggetIron").setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "fishing_rod")));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addHardcoreRecipe(new BaitingRecipe());
    }

    @Override
    public String getFeatureDescription() {
        return "Change Fishing Rods to require being Baited with certain items to entice fish, they won't nibble without it!";
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ItemFishingRod) {
            event.addCapability(BAITED_FISHING_ROD, new FishingBait());
        }
    }

    @SubscribeEvent
    public void onFished(ItemFishedEvent event) {
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
        if (isFishingRod(stack)) {
            FishingBait cap = stack.getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            if (cap.hasBait()) {
                cap.setBait(false);
                NBTTagCompound tag = stack.getTagCompound();
                if (tag != null && tag.hasKey("bait")) {
                    tag.setBoolean("bait", false);
                }
            }
        }
    }

    @SubscribeEvent
    public void useFishingRod(PlayerInteractEvent.RightClickItem event) {
        if (isFishingRod(event.getItemStack())) {
            FishingBait cap = event.getItemStack().getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            if (!cap.hasBait()) {
                event.setCanceled(true);
                if (!event.getWorld().isRemote && event.getHand() == EnumHand.MAIN_HAND)
                    event.getEntityPlayer().sendMessage(new TextComponentTranslation("bwm.message.needs_bait"));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (isFishingRod(stack)) {
            FishingBait cap = event.getItemStack().getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            boolean bait = cap.hasBait();
            String tooltip = bait ? "Baited" : "Unbaited";
            if (!bait) {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag != null && tag.hasKey("bait")) {
                    tooltip = tag.getBoolean("bait") ? "Baited" : "Unbaited";
                }
            }
            event.getToolTip().add(tooltip);
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    public static boolean isFishingRod(ItemStack stack) {
        return stack.getItem() instanceof ItemFishingRod && stack.hasCapability(FISHING_ROD_CAP, EnumFacing.UP);
    }

    public static boolean isBaited(ItemStack stack, boolean baited) {
        return isFishingRod(stack) && stack.getCapability(FISHING_ROD_CAP, EnumFacing.UP).hasBait() == baited;
    }

    public static ItemStack setBaited(ItemStack rod, boolean baited) {
        if (rod.hasCapability(FISHING_ROD_CAP, EnumFacing.UP)) {
            FishingBait cap = rod.getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            cap.setBait(baited);
        }
        if (rod.getTagCompound() == null) {
            rod.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = rod.getTagCompound();
        tag.setBoolean("bait", baited);
        return new ItemStack(rod.serializeNBT());
    }

    public class BaitingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
        protected ResourceLocation group;
        protected Predicate<ItemStack> isTool;
        protected Ingredient input;

        public BaitingRecipe() {
            this.group = new ResourceLocation("baiting_recipe");
            this.input = BAIT;
            this.isTool = stack -> isBaited(stack, false);
            setRegistryName(this.getGroup());
        }

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            return isMatch(inv);
        }

        public ItemStack findRod(InventoryCrafting inv) {
            for (int x = 0; x < inv.getSizeInventory(); x++) {
                ItemStack slot = inv.getStackInSlot(x);
                if (isTool.test(slot)) {
                    return slot;
                }
            }
            return ItemStack.EMPTY;
        }

        public boolean isMatch(IInventory inv) {
            boolean hasTool = false, hasInput = false;
            for (int x = 0; x < inv.getSizeInventory(); x++) {
                boolean inRecipe = false;
                ItemStack slot = inv.getStackInSlot(x);

                if (!slot.isEmpty()) {
                    if (isTool.test(slot)) {
                        if (!hasTool) {
                            hasTool = true;
                            inRecipe = true;
                        } else
                            return false;
                    } else if (OreDictionary.containsMatch(true, InvUtils.asNonnullList(input.getMatchingStacks()), slot)) {
                        if (!hasInput) {
                            hasInput = true;
                            inRecipe = true;
                        } else
                            return false;
                    }
                    if (!inRecipe)
                        return false;
                }
            }
            return hasTool && hasInput;
        }

        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            return ForgeHooks.defaultRecipeGetRemainingItems(inv);
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inv) {
            ItemStack rod = findRod(inv);
            if (!rod.isEmpty())
                return setBaited(rod, true);
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canFit(int width, int height) {
            return width * height >= 2;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.EMPTY;
        }

        @Override
        public String getGroup() {
            return group.toString();
        }

        @Override
        public boolean isHidden() {
            return true;
        }
    }


    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(FishingBait.class)
    public static Capability<FishingBait> FISHING_ROD_CAP = null;

    public static class CapabilityFishingRod implements Capability.IStorage<FishingBait> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<FishingBait> capability, FishingBait instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<FishingBait> capability, FishingBait instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }

    public static class FishingBait implements ICapabilitySerializable<NBTTagCompound> {
        private boolean bait;

        public FishingBait() {

        }

        public boolean hasBait() {
            return bait;
        }

        public void setBait(boolean bait) {
            this.bait = bait;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == FISHING_ROD_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == FISHING_ROD_CAP)
                return FISHING_ROD_CAP.cast(this);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("bait", hasBait());
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            setBait(nbt.getBoolean("bait"));
        }
    }
}
