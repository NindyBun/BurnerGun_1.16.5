package com.nindybun.burnergun.client.screens;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class settingsScreen extends Screen implements Slider.ISlider {
    protected settingsScreen() {
        super(new StringTextComponent("Title"));
    }

    @Override
    protected void init() {
    }

    @Override
    public void onChangeSliderValue(Slider slider) {

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float ticks_) {
        //Gives us the darkened background
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, ticks_);
    }
}
