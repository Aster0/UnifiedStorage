package me.astero.unifiedstoragemod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.data.ViewOnlySlot;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GridControllerScreen extends AbstractContainerScreen<GridControllerMenu> {








    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage_crafting.png");

    public GridControllerScreen(GridControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 199;
        this.imageHeight = 235;

    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        renderTransparentBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);







    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {



        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);


        for(int i = 0; i < this.menu.slots.size(); i++) {

            Slot slot = this.menu.slots.get(i);
            renderCustomSlot(guiGraphics, slot);
        }






    }

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {




        return super.mouseClicked(p_97748_, p_97749_, p_97750_);


    }

    @Override
    protected void slotClicked(Slot slot, int p_97779_, int p_97780_, ClickType p_97781_) {


        if(slot instanceof ViewOnlySlot viewOnlySlot) {
//            menu.setCarried(new ItemStack(((ViewOnlySlot) slot).getItem2().getItem(),
//                    ((ViewOnlySlot) slot).getItem2().getCount()));

            return;
        }

        // TODO: Put down logic?

        super.slotClicked(slot, p_97779_, p_97780_, p_97781_);





    }


    public void renderCustomSlot(GuiGraphics guiGraphics, Slot slot) {



        if(slot instanceof ViewOnlySlot viewOnlySlot) {






            if(viewOnlySlot.getActualItem().equals(ItemStack.EMPTY , false)) return;



            int actualSlotX = leftPos + slot.x;
            int actualSlotY = topPos + slot.y;

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            ItemStack displayStack = new ItemStack(viewOnlySlot.getItemRepresentative(), 1);
            guiGraphics.renderItem(displayStack, actualSlotX, actualSlotY);
            guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                    leftPos + slot.x, topPos + slot.y, "");
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 0, 200);


            String text = String.valueOf(viewOnlySlot.getActualItemCount());
            float textScale = 0.5f; // Adjust the scale factor as needed
            float scaledX = (actualSlotX) / textScale;
            float scaledY = (actualSlotY) / textScale;


            // max 3 length (e.g., 100K, 100M)
            float textWidth = this.font.width(text) == 0 ? 0 :
                    textScale * this.font.width(text) * 2; // Calculate the width of the text after scaling


            poseStack.scale(textScale, textScale, textScale);



            guiGraphics.drawString(this.font, text,
                    scaledX + 1 + 28 - textWidth, scaledY + 1 + 22, 0x000000, false);

            guiGraphics.drawString(this.font, text,
                    scaledX + 30 - textWidth, scaledY + 22, 0xFFFFFF, false);

            guiGraphics.pose().popPose();



        }






    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {


        if(this.hoveredSlot instanceof ViewOnlySlot viewOnlySlot) {


            ItemStack item = viewOnlySlot.getActualItem();

            if(!item.equals(ItemStack.EMPTY, false)) {

                guiGraphics.renderTooltip(this.font, item, x, y);
            }

        }


        super.renderTooltip(guiGraphics, x, y);
    }
}
