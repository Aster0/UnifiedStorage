package me.astero.unifiedstoragemod.client.screen.widgets.generic;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Optional;

public abstract class CustomEditBox extends EditBox implements ICustomWidgetComponent {

    private static final ResourceLocation TEXT_FIELD =
            new ResourceLocation(ModUtils.MODID, "textures/gui/slots.png");
    private MutableComponent hint;


    private boolean showHint = false;


    private String text = "";

    private final List<Component> lore;

    public CustomEditBox(Font font, int x, int y, int width, int height, List<Component> lore) {
        super(font, x, y, width, height, null);

        setBordered(false);
        this.lore = lore;


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

    @Override
    public void tick(GuiGraphics guiGraphics, int leftPos, int topPos) {

    }

    @Override
    public void onMouseClick(double mouseX, double mouseY, int mode) {


        
    }

    @Override
    public void onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY) {

    }

    @Override
    public void onMouseScrolled(double mouseX, double mouseY, double delta, double rawDelta) {

    }

    @Override
    public void onMouseRelease() {

    }


    @Override
    public void renderCustomTooltip(GuiGraphics guiGraphics, Font font, int x, int y, Slot slot) {

        if(x >= (double)this.getX() && x < (double)(this.getX() + this.width)
                && y >= (double)this.getY()
                && y < (double)(this.getY() + this.height)) {
            guiGraphics.renderTooltip(font, lore, Optional.empty(), x, y + 10);
        }


    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mode) {




        if(this.clicked(mouseX, mouseY)) {

            if(mode == 1) { // right click

                setValue("");
            }

        }
        
        return super.mouseClicked(mouseX, mouseY, mode);
    }
}
