package me.astero.unifiedstoragemod.client.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class CustomEditBox extends EditBox {

    private static final ResourceLocation TEXT_FIELD =
            new ResourceLocation(ModUtils.MODID, "textures/gui/slots.png");
    private MutableComponent hint;


    private boolean showHint = false;


    private String text = "";

    public CustomEditBox(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, null);

        setBordered(false);

    }




    public void setHint(MutableComponent hint) {
        this.hint = hint;

        showHint = true;

    }

    @Override
    public void render(GuiGraphics guiGraphics, int p_93658_, int p_93659_, float p_93660_) {
        super.render(guiGraphics, p_93658_, p_93659_, p_93660_);


        guiGraphics.blit(TEXT_FIELD, getX()- 5, getY() - 2,   122, 3,
                this.width + 15, this.height);

        if(!text.equals(getValue())) { // char type doesn't work. gives the previous text before char :( makeshift right here.

            text = getValue();

            showHint = text.isEmpty();

            onPlayerType(text);
        }


        if(hint != null && showHint) { // if there's a hint set

            int color = 0xFFFFFF;

            if(this.isFocused()) {

                color = 0xFFD3D3D3;
            }

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();


            float textScale = 0.9f;
            float scaledX = getX() / textScale;
            float scaledY = getY() / textScale;

            poseStack.scale(textScale, textScale, textScale);



            guiGraphics.drawString(Minecraft.getInstance().font,
                    hint.getString(),
                    scaledX + 0.3f,
                    scaledY + 0.7f,
                    color, false);




            poseStack.popPose();
        }
    }

    public abstract void onPlayerType(String text);
}
