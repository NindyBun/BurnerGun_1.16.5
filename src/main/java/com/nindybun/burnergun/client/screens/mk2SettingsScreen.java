package com.nindybun.burnergun.client.screens;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.nindybun.burnergun.client.screens.buttons.ToggleButton;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1InfoProvider;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketChangeVolume;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mk2SettingsScreen extends Screen implements Slider.ISlider {
    private static BurnerGunMK1Info info;
    private static final Logger LOGGER = LogManager.getLogger();
    private List<Upgrade> toggleableList = new ArrayList<>();
    private HashMap<Upgrade, ToggleButton> upgradeButtons = new HashMap<>();
    private CompoundNBT compoundInfo = new CompoundNBT();
    private int raycastRange,
                vertical,
                horizontal;
    private float volume;
    private boolean trashFilterWhiteList = true;
    private boolean smeltFilterWhiteList = true;
    private Slider  raycastSlider,
                    volumeSlider,
                    verticalSlider,
                    horizontalSlider;

    protected mk2SettingsScreen(ItemStack gun) {
        super(new StringTextComponent("Title"));
        this.info = gun.getCapability(BurnerGunMK1InfoProvider.burnerGunInfoMK1Capability, null).orElseThrow(()->new IllegalArgumentException("No capability found!"));
        this.volume = info.getVolume();
    }

    @Override
    protected void init() {
        List<Widget> settings = new ArrayList<>();
        int midX = width/2;
        int midY = height/2;

        settings.add(volumeSlider = new Slider(midX-135, 20, 125, 20, new TranslationTextComponent("tooltip." + BurnerGun.MOD_ID + ".screen.volume"), new StringTextComponent("%"), 0, 100, Math.min(100, this.volume*100), false, true, slider -> {}, this));

        for (int i = 0; i < settings.size(); i++) {
            settings.get(i).y = (80)+(i*25);
            this.addButton(settings.get(i));
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        PacketHandler.sendToServer(new PacketChangeVolume(this.volume));
        super.removed();
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        volumeSlider.dragging = false;
        return false;
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if( volumeSlider.isMouseOver(mouseX, mouseY) ) {
            volumeSlider.sliderValue += (.01f * (delta > 0 ? 1 : -1));
            volumeSlider.updateSlider();
        }
        return false;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        InputMappings.Input key = InputMappings.getKey(p_231046_1_, p_231046_2_);
        if (p_231046_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(key)){
            onClose();
            return true;
        }
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks_) {
        //Gives us the darkened background
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, ticks_);
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        if (slider.equals(volumeSlider)){
            this.volume = slider.getValueInt()/100f;
        }
    }
}
