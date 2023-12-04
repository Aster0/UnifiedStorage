package me.astero.unifiedstoragemod.client.screen.widgets.generic;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class CustomSearchField extends CustomEditBox {

    public CustomSearchField(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height);

        super.setHint(Component.translatable("container." + ModUtils.MODID + ".searchField"));

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

        super.setCanLoseFocus(p_94191_);
    }
}
