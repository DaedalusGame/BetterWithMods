package betterwithmods.module.hardcore;

import betterwithmods.BWMod;
import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.ToolDamageRecipe;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 7/23/17.
 */
public class HCFishing extends Feature {
    private static final ResourceLocation BAITED_FISHING_ROD = new ResourceLocation(BWMod.MODID, "baited_fishing_rod");
    private static final Ingredient BAIT = Ingredient.fromStacks(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(BWMItems.CREEPER_OYSTER), new ItemStack(Items.FISH, 2), new ItemStack(Items.FISH, 3));

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(FishingBait.class, new CapabilityFishingRod(), FishingBait::new);
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.FISHING_ROD), "  I"," IS","I N", 'S', "string", 'I', "stickWood",'N', "nuggetIron").setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "fishing_rod")));
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
            if (cap.hasBait())
                cap.setBait(false);
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
            } else {
                System.out.println("has bait!");
            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (isFishingRod(event.getItemStack())) {
            FishingBait cap = event.getItemStack().getCapability(FISHING_ROD_CAP, EnumFacing.UP);
            String tooltip = cap.hasBait() ? "Baited" : "Unbaited";
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


    public static ItemStack getBaitedRod(boolean baited) {
        ItemStack rod = new ItemStack(Items.FISHING_ROD);
        if(rod.hasCapability(FISHING_ROD_CAP,EnumFacing.UP))
            rod.getCapability(FISHING_ROD_CAP, EnumFacing.UP).setBait(baited);
        return new ItemStack(rod.serializeNBT());
    }

    public class BaitingRecipe extends ToolDamageRecipe {

        public BaitingRecipe() {
            super(new ResourceLocation("baiting_recipe"), getBaitedRod(true), BAIT, stack -> isBaited(stack, false));
            setRegistryName(this.getGroup());
        }

        @Override
        public ItemStack getExampleStack() {
            return getBaitedRod(false);
        }

        @Override
        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
            return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
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
