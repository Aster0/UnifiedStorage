package me.astero.unifiedstoragemod.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.client.screen.widgets.CustomScrollWheel;
import me.astero.unifiedstoragemod.client.screen.widgets.CustomSearchField;
import me.astero.unifiedstoragemod.client.screen.widgets.StorageGUIScrollWheel;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.data.ViewOnlySlot;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdatePlayerInventoryEntityPacket;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
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



    private CustomSearchField searchField;
    private CustomScrollWheel customScrollWheel;

    private int scrollbarPosition = 0;
    private int scrollbarHeight = 0;
    private int totalContentHeight = 27;
    private int visibleContentHeight = 7; // Adjust this based on your needs







    private Map<ViewOnlySlot, ViewOnlySlot> allViewOnlySlots = new HashMap<>();



    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage_crafting.png");



    public GridControllerScreen(GridControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 199;
        this.imageHeight = 235;


    }



    @Override
    protected void init() {
        super.init();

        registerSearchField();

        customScrollWheel = new StorageGUIScrollWheel(this.leftPos + 179,
                this.topPos + 17, this.topPos + 54, menu.getTotalPages(), menu);

    }



    private void registerSearchField() {
        searchField = new CustomSearchField(font, leftPos + 90, topPos + 3, 100, 12) {
            @Override
            public void onPlayerType(String text) {


                if(!text.isEmpty()) {

                    menu.onStorageSearch(text);
                    return;
                }

                menu.onStorageSearchStop();



            }
        }
                .giveMaxLength(50);



        addWidget(searchField);



    }
    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        renderTransparentBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0,
                this.imageWidth, this.imageHeight);



        customScrollWheel.tick(guiGraphics);




    }




    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {



        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);

        searchField.render(guiGraphics, mouseX, mouseY, partialTicks);






        int startingIndex = (0 * GridControllerMenu.VISIBLE_CONTENT_HEIGHT)
                + GridControllerMenu.STARTING_SLOT_INDEX;



        // 36 (first slot) - 62 (last slot)
        for(int i = startingIndex; i <
                startingIndex + GridControllerMenu.VISIBLE_CONTENT_HEIGHT ; i++) {

            try {


                Slot slot = this.menu.slots.get(i);

                if(slot instanceof ViewOnlySlot viewOnlySlot) { // just to confirm

                    Slot slotIndex = this.menu.slots.get((i -
                            (0 * GridControllerMenu.VISIBLE_CONTENT_HEIGHT))); // 0 represents scroll page
                    // but with the new system, we don't really need the scroll page anymore
                    // because we are always dealing with a set slots generated.
                    // not deleting first in case I need it again in the future



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




    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {

        Rect2i guiBounds = new Rect2i(
                leftPos, topPos, this.imageWidth, this.imageHeight);


        if (guiBounds.contains(((int) mouseX), (int) mouseY)) {
            customScrollWheel.onMouseDrag(mouseX, mouseY, button, dragX, dragY);
        }


        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {

        customScrollWheel.onMouseRelease();
        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_97750_) {


        customScrollWheel.onMouseClick(mouseX, mouseY);
        searchField.setFocused(false);

        return super.mouseClicked(mouseX, mouseY, p_97750_);


    }

    @Override
    protected void slotClicked(Slot slot, int p_97779_, int p_97780_, ClickType p_97781_) {




        if(slot instanceof ViewOnlySlot viewOnlySlot) {


            menu.setCarried(viewOnlySlot.getActualItem());
            ModNetwork.sendToServer(new UpdatePlayerInventoryEntityPacket(viewOnlySlot.getActualItem(),
                    slot.getSlotIndex(), false));




            return;
        }
        else {

            if(!menu.getCarried().equals(ItemStack.EMPTY)) {


                System.out.println(slot);
                // TODO FIX THE BUG
                if(slot != null) {

                    System.out.println("yea");
                    menu.setCarried(ItemStack.EMPTY);
                    ModNetwork.sendToServer(new UpdatePlayerInventoryEntityPacket(menu.getCarried(),
                            slot.getSlotIndex(), true));


                    slot.safeInsert(menu.getCarried());
                }

            }
        }



        // TODO: Put down logic?

        super.slotClicked(slot, p_97779_, p_97780_, p_97781_);


    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta, double rawDelta) {

        customScrollWheel.onMouseScrolled(mouseX, mouseY, delta, rawDelta);
        return super.mouseScrolled(mouseX, mouseY, delta, rawDelta);
    }

    public void renderCustomSlot(GuiGraphics guiGraphics, Slot slot, Slot slotIndex) {



        if(slot instanceof ViewOnlySlot viewOnlySlot) {






            if(viewOnlySlot.getActualItem().equals(ItemStack.EMPTY , false)) return;



            int actualSlotX = leftPos + slotIndex.x;
            int actualSlotY = topPos + slotIndex.y;

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            ItemStack displayStack = viewOnlySlot.getActualItem();



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



            ItemStack item = viewOnlySlot.getActualItem();



            if(!item.equals(ItemStack.EMPTY, false)) {

                guiGraphics.renderTooltip(this.font, item, x, y);

            }




        }


        super.renderTooltip(guiGraphics, x, y);
    }
}
