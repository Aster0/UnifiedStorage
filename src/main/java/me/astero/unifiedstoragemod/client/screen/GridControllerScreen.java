package me.astero.unifiedstoragemod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
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
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class GridControllerScreen extends AbstractContainerScreen<GridControllerMenu> {




    private int scrollbarPosition = 0;
    private int scrollbarHeight = 0;
    private int totalContentHeight = 27;
    private int visibleContentHeight = 7; // Adjust this based on your needs

    private int scrollPage = 1;





    private Map<ViewOnlySlot, ViewOnlySlot> allViewOnlySlots = new HashMap<>();



    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage_crafting.png");

    private static final ResourceLocation SCROLLBAR_TEXTURE = new ResourceLocation("minecraft",
            "textures/gui/container/creative_inventory/tabs.png");

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



        scrollPage = 0;


        int startingIndex = (scrollPage * GridControllerMenu.VISIBLE_CONTENT_HEIGHT)
                + GridControllerMenu.STARTING_SLOT_INDEX;



        // 36 (first slot) - 62 (last slot)
        for(int i = startingIndex; i <
                startingIndex + GridControllerMenu.VISIBLE_CONTENT_HEIGHT ; i++) {

            try {


                Slot slot = this.menu.slots.get(i);

                if(slot instanceof ViewOnlySlot viewOnlySlot) { // just to confirm

                    Slot slotIndex = this.menu.slots.get((i -
                            (scrollPage * GridControllerMenu.VISIBLE_CONTENT_HEIGHT)));



                    renderCustomSlot(guiGraphics, slot, slotIndex);

                    allViewOnlySlots.putIfAbsent((ViewOnlySlot) slotIndex, viewOnlySlot);
                    // so when we hover when it's not page 1,
                    // we can tie this slot position to the visual slot that we need.




                }

            }
            catch(IndexOutOfBoundsException SLOT_DOESNT_EXIST) {


                break;
            }








        }

        renderScrollbar(guiGraphics);



    }

    private void changePage(int page) {

        this.scrollPage = page;



    }

    private void renderScrollbar(GuiGraphics guiGraphics) {
        if (totalContentHeight <= visibleContentHeight) {
            return; // No need for a scrollbar if content fits in the visible area
        }

        int maxScrollbarPosition = Math.max(0, 27 - visibleContentHeight);



        if (maxScrollbarPosition == 0) {
            return;
        }


        scrollbarPosition = Math.min(scrollbarPosition, maxScrollbarPosition);

        int scrollbarX = leftPos + imageWidth + 10; // Adjust this based on your GUI layout
        int scrollbarY = topPos + 6; // Adjust this based on your GUI layout
        int scrollbarHeight = 100;




        // Render the scrollbar
        guiGraphics.fill(scrollbarX, scrollbarY , scrollbarX + 10, scrollbarY
                + scrollbarHeight, 0xFF000000);

        // Calculate the position and dimensions of the scrollbar thumb
        int thumbY = scrollbarY + scrollbarPosition * (visibleContentHeight - scrollbarHeight) / maxScrollbarPosition;
        int thumbHeight = Math.min(scrollbarHeight, visibleContentHeight - thumbY + scrollbarY);

        guiGraphics.fill(scrollbarX, thumbY , scrollbarX + 10, thumbHeight, 0xFFFFFFFF);

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


            menu.generateSlots(2);
            return;
        }

        // TODO: Put down logic?

        super.slotClicked(slot, p_97779_, p_97780_, p_97781_);





    }


    public void renderCustomSlot(GuiGraphics guiGraphics, Slot slot, Slot slotIndex) {



        if(slot instanceof ViewOnlySlot viewOnlySlot) {






            if(viewOnlySlot.getActualItem().equals(ItemStack.EMPTY , false)) return;



            int actualSlotX = leftPos + slotIndex.x;
            int actualSlotY = topPos + slotIndex.y;

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            ItemStack displayStack = new ItemStack(viewOnlySlot.getItemRepresentative(), 1);
            guiGraphics.renderItem(displayStack, actualSlotX, actualSlotY);
            guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                    actualSlotX, actualSlotY, "");
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




        if(this.hoveredSlot instanceof ViewOnlySlot viewOnlySlot) { // on our custom inventory




            ViewOnlySlot hoveredSlot = allViewOnlySlots.get(viewOnlySlot);


            if(hoveredSlot != null) {

                ItemStack item = hoveredSlot.getActualItem();

                if(!item.equals(ItemStack.EMPTY, false)) {

                        guiGraphics.renderTooltip(this.font, item, x, y);

                }

            }


        }


        super.renderTooltip(guiGraphics, x, y);
    }
}
