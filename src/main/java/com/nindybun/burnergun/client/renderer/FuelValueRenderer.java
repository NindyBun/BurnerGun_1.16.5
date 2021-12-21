package com.nindybun.burnergun.client.renderer;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1InfoProvider;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, value = Dist.CLIENT)
public class FuelValueRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int base_buffer = BurnerGunMK1.base_use_buffer;
    private static final int base_heat_buffer = BurnerGunMK1.base_heat_buffer;
    private static final int base_use = 100;

    @SubscribeEvent
    public static void renderOverlay(@Nonnull RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            ItemStack stack = ItemStack.EMPTY;
            Minecraft mc = Minecraft.getInstance();
            ClientPlayerEntity player = mc.player;
            if (player.getMainHandItem().getItem() instanceof BurnerGunMK1)
                stack = player.getMainHandItem();
            else if (player.getOffhandItem().getItem() instanceof BurnerGunMK1)
                stack = player.getOffhandItem();
            if (stack.getItem() instanceof BurnerGunMK1)
                renderFuel(event, stack);

        }

    }

    public static List<UpgradeCard> getUpgrades(ItemStack stack){
        List<UpgradeCard> upgrades = new ArrayList<>();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("No Item Handler Found!"));
        for (int index  = 1; index < handler.getSlots()-1; index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgrades.add((UpgradeCard)handler.getStackInSlot(index).getItem());
            }
        }
        return upgrades;
    }

    //Returns the upgrade card by upgrade
    public static Upgrade getUpgradeByUpgrade(ItemStack stack, Upgrade upgrade){
        List<UpgradeCard> upgrades = getUpgrades(stack);
        for (UpgradeCard upgradeCard : upgrades) {
            if (upgradeCard.getUpgrade().getBaseName().equals(upgrade.getBaseName())){
                return upgradeCard.getUpgrade();
            }
        }
        return null;
    }

    public static double getFuelEfficiency(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.FUEL_EFFICIENCY_1) != null ? getUpgradeByUpgrade(stack, Upgrade.FUEL_EFFICIENCY_1).getExtraValue() : 0.00;
    }
    public static double getHeatEfficiency(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.HEAT_EFFICIENCY_1) != null ? getUpgradeByUpgrade(stack, Upgrade.HEAT_EFFICIENCY_1).getExtraValue() : 0.00;
    }

    public static double getUseValue(ItemStack stack){
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("No Item Handler Found!"));
        int extraUse = 0;
        List<UpgradeCard> upgrades = getUpgrades(stack);
        if (!upgrades.isEmpty()){
            extraUse = upgrades.stream().mapToInt(upgradeCard -> upgradeCard.getUpgrade().getCost()).sum();
        }
        if (handler.getStackInSlot(0).getItem().equals(Upgrade.REACTOR.getCard().getItem())) {
            return (base_use + extraUse) - ((base_use + extraUse) * getHeatEfficiency(stack));
        }
        return (base_use + extraUse) - ((base_use + extraUse) * getFuelEfficiency(stack));
    }

    public static void renderFuel(RenderGameOverlayEvent.Post event, ItemStack stack){
        BurnerGunMK1Info info = stack.getCapability(BurnerGunMK1InfoProvider.burnerGunInfoCapability, null).orElseThrow(()->new IllegalArgumentException("No capability found!"));
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        int level = info.getFuelValue();
        int hLevel = info.getHeatValue();
        Color color = Color.GREEN;
        if (!handler.getStackInSlot(0).getItem().equals(Upgrade.REACTOR.getCard().getItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            if (level > base_buffer*3/4)
                color = Color.GREEN;
            else if (level > base_buffer*1/4 && level <= base_buffer*3/4)
                color = Color.ORANGE;
            else
                color = Color.RED;
        }else if (handler.getStackInSlot(0).getItem().equals(Upgrade.REACTOR.getCard().getItem())){
            if (hLevel > base_heat_buffer*3/4)
                color = Color.RED;
            else if (hLevel > base_heat_buffer*1/4 && hLevel <= base_heat_buffer*3/4)
                color = Color.ORANGE;
            else
                color = Color.GREEN;
        }

        //fontRenderer.draw(event.getMatrixStack(), "Fuel usage: " + getUseValue(stack), 6, event.getWindow().getGuiScaledHeight()-22, Color.WHITE.getRGB());
        if (!handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())
            && !handler.getStackInSlot(0).getItem().equals(Upgrade.REACTOR.getCard().getItem())){
            fontRenderer.draw(event.getMatrixStack(), "Fuel level: ", 6, event.getWindow().getGuiScaledHeight()-12, Color.WHITE.getRGB());
            fontRenderer.draw(event.getMatrixStack(), level+"", 61, event.getWindow().getGuiScaledHeight()-12, color.getRGB());
        }else if (handler.getStackInSlot(0).getItem().equals(Upgrade.REACTOR.getCard().getItem())){
            double heat = (double)hLevel/base_heat_buffer*100;
            String heatString = heat+"";
            int deci = heatString.lastIndexOf(".");
            heatString = heatString.substring(0, deci+2);
            fontRenderer.draw(event.getMatrixStack(), "Heat level: ", 6, event.getWindow().getGuiScaledHeight()-12, Color.WHITE.getRGB());
            fontRenderer.draw(event.getMatrixStack(), heatString+"%", 61, event.getWindow().getGuiScaledHeight()-12, color.getRGB());
        }else if (handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            fontRenderer.draw(event.getMatrixStack(), "Source: ", 6, event.getWindow().getGuiScaledHeight()-12, Color.WHITE.getRGB());
            fontRenderer.draw(event.getMatrixStack(), new StringTextComponent("Universe").withStyle(TextFormatting.OBFUSCATED), 50, event.getWindow().getGuiScaledHeight()-12, color.getRGB());
         }

    }
}
