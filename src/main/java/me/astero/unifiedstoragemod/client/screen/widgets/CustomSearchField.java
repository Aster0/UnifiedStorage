package me.astero.unifiedstoragemod.client.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;

public class CustomSearchField extends CustomEditBox {

    public CustomSearchField(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height);

        super.setHint(Component.translatable("container." + ModUtils.MODID + ".searchField"),
                4.8f, 3);

    }

    @Override
    public void onPlayerType(String text) {


    }


    public CustomSearchField giveMaxLength(int length) {

        super.setMaxLength(length);

        return this;
    }




    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_E) {
                // Handle 'E' key press without closing the GUI
                return true;
            }
        }


        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void setCanLoseFocus(boolean p_94191_) {
        System.out.println(p_94191_);
        super.setCanLoseFocus(p_94191_);
    }
}
