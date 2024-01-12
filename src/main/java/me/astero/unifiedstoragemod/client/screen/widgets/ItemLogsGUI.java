package me.astero.unifiedstoragemod.client.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.ICustomWidgetComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class ItemLogsGUI implements ICustomWidgetComponent {


    @Override
    public void tick(GuiGraphics guiGraphics, int leftPos, int topPos) {
        int actualSlotX = 185 + leftPos;
        int actualSlotY = 155 + topPos;


        String text = "x64";
        Font font = Minecraft.getInstance().font;

        guiGraphics.fill(actualSlotX, actualSlotY,  33 + actualSlotX, 8 + actualSlotY,
                0xFF009146);


        float textScale = 0.7f; // Adjust the scale factor as needed
        float scaledX = (actualSlotX) / textScale;
        float scaledY = (actualSlotY) / textScale;

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(textScale, textScale, textScale);

        guiGraphics.drawString(font, text,
                scaledX + 17, scaledY + 2,
                0xFFFFFF, false);



        poseStack.popPose();

        poseStack.pushPose();

        float itemScale = 0.5f;
        scaledX = (actualSlotX) / itemScale;
        scaledY = (actualSlotY) / itemScale;
        poseStack.scale(itemScale, itemScale, itemScale);

        guiGraphics.renderItem(new ItemStack(Items.CHEST), (int) scaledX + 2, (int) scaledY);

        poseStack.popPose();






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

    }
}
