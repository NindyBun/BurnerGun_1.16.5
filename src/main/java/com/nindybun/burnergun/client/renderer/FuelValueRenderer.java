package com.nindybun.burnergun.client.renderer;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.network.PacketHandler;
import net.minecraft.client.ClientGameSession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, value = Dist.CLIENT)
public class FuelValueRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int base_buffer = com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun.base_use_buffer;
    private static final int base_heat_buffer = com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun.base_heat_buffer;
    @SubscribeEvent
    public static void renderOverlay(@Nonnull RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            ItemStack stack = ItemStack.EMPTY;
            PlayerEntity player = Minecraft.getInstance().player;
            if (player.getMainHandItem().getItem() instanceof com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun)
                stack = player.getMainHandItem();
            else if (player.getOffhandItem().getItem() instanceof com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun)
                stack = player.getOffhandItem();

            if (stack.getItem() instanceof com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun)
                renderFuel(event, stack);

        }

    }

    public static void renderFuel(RenderGameOverlayEvent.Post event, ItemStack stack){
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        int level = stack.getTag().getInt("FuelValue");
        int hLevel = stack.getTag().getInt("HeatValue");
        Color color;
        if (level > base_buffer*3/4)
            color = Color.GREEN;
        else if (level > base_buffer*1/4 && level <= base_buffer*3/4)
            color = Color.ORANGE;
        else
            color = Color.RED;

        if (hLevel > base_heat_buffer*3/4)
            color = Color.RED;
        else if (hLevel > base_heat_buffer*1/4 && hLevel <= base_heat_buffer*3/4)
            color = Color.ORANGE;
        else
            color = Color.GREEN;
        //fontRenderer.drawString(event.getMatrixStack(), "Fuel level: "+level, 6, event.getWindow().getScaledHeight()-12, Color.WHITE.getRGB());
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
        }

    }
}
