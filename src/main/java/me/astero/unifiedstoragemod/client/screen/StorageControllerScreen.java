package me.astero.unifiedstoragemod.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.client.screen.widgets.*;
import me.astero.unifiedstoragemod.items.data.UpgradeModule;
import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import me.astero.unifiedstoragemod.menu.data.CustomGUISlot;
import me.astero.unifiedstoragemod.menu.data.NetworkSlot;
import me.astero.unifiedstoragemod.menu.data.VisualItemSlot;
import me.astero.unifiedstoragemod.menu.enums.MouseAction;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.CraftItemEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.TakeOutFromStorageInventoryEntityPacket;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StorageControllerScreen extends AbstractContainerScreen<StorageControllerMenu> {



    private CustomSearchField searchField;
    private CustomScrollWheel customScrollWheel;

    private int savedPages = -1;






    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage_crafting.png");


    public StorageControllerScreen(StorageControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 256;
        this.imageHeight = 235;


    }



    @Override
    protected void init() {
        super.init();

        registerSearchField();


        this.inventoryLabelY = 142; // shifting the inventory label


    }



    private void registerSearchField() {
        searchField = new CustomSearchField(font, leftPos + 84, topPos + 6, 76, 12) {
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
    public void resize(Minecraft p_96575_, int p_96576_, int p_96577_) {
        super.resize(p_96575_, p_96576_, p_96577_);


        customScrollWheel = new StorageGUIScrollWheel(this.leftPos + 179,
                this.topPos + 17, this.topPos + 54, menu.getTotalPages(), menu);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        //renderTransparentBackground(guiGraphics);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0,
                this.imageWidth, this.imageHeight);


        ICustomWidgetComponent.tickAll(menu.getWidgets(), guiGraphics, leftPos, topPos);


        if(savedPages != menu.getTotalPages()) {

            customScrollWheel = new StorageGUIScrollWheel(this.leftPos + 179,
                    this.topPos + 17, this.topPos + 54, menu.getTotalPages(), menu);



            savedPages = menu.getTotalPages();


        }


        if(customScrollWheel != null)
            customScrollWheel.tick(guiGraphics, 0,0);



        renderCraftingGridSlot(guiGraphics);


        renderCustomSlot(guiGraphics);





    }






    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {



        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawString
                (this.font, Component.translatable("container.unifiedstorage.crafting_terminal"),
                        leftPos + this.inventoryLabelX, topPos + 74,
                        4210752, false);

        renderTooltip(guiGraphics, mouseX, mouseY);


        if(searchField != null)
            searchField.render(guiGraphics, mouseX, mouseY, partialTicks);









    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {

        Rect2i guiBounds = new Rect2i(
                leftPos, topPos, this.imageWidth, this.imageHeight);


        if (guiBounds.contains(((int) mouseX), (int) mouseY)) {

            if(customScrollWheel!= null)
                customScrollWheel.onMouseDrag(mouseX, mouseY, button, dragX, dragY);
        }


        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {

        if(customScrollWheel != null)
            customScrollWheel.onMouseRelease();

        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_97750_) {


        if(customScrollWheel != null)
            customScrollWheel.onMouseClick(mouseX, mouseY);

        if(searchField != null)
            searchField.setFocused(false);

        return super.mouseClicked(mouseX, mouseY, p_97750_);


    }
    @Override
    protected void slotClicked(Slot slot, int slotIndex, int btn, ClickType clickType) {


        if(slot == null) { // somehow it happens sometimes
            super.slotClicked(slot, slotIndex, btn, clickType); // just do normal things

            return;
        }

        if(menu.getStorageControllerEntity().isDisabled()) {




            if(slot instanceof CustomGUISlot ) {
                return;
            }

            if(!(slot.container instanceof TransientCraftingContainer)) { // cant use the crafting if it's not with a network card
                super.slotClicked(slot, slotIndex, btn, clickType);
            }




            return;
        }


        // 0 = place down? 1 = place one. clickType cant track this

        MouseAction action = btn == 0 ? MouseAction.LEFT_CLICK : MouseAction.RIGHT_CLICK;

        if(slot instanceof CustomGUISlot v) {

            if(menu.getCarried().equals(ItemStack.EMPTY, false)) { // means we are taking out smth from the storage



                if(v.getActualItem().equals(ItemStack.EMPTY))
                    return;


                ItemStack itemStack =  v.getActualItem().copy();
                int modifiedValue = Math.min(v.getActualItemCount(), itemStack.getMaxStackSize());
                boolean quickMove = false;


                if(clickType == ClickType.PICKUP) {


                    if(v.getActualItemCount() > itemStack.getMaxStackSize()) {
                        itemStack.setCount(itemStack.getMaxStackSize());
                    }
                    else {
                        itemStack.setCount(v.getActualItemCount());
                    }


                    if(action == MouseAction.RIGHT_CLICK) { // right click (splitting)

                        int valueToSplit = (int) Math.ceil((double) itemStack.getCount() / 2);
                        modifiedValue = valueToSplit;


                    }

                }
                else if(clickType == ClickType.QUICK_MOVE) {


                    quickMove = true;
                }




                ModNetwork.sendToServer(new TakeOutFromStorageInventoryEntityPacket(itemStack, true,
                        modifiedValue, quickMove));

                menu.interactWithMenu(itemStack, true, modifiedValue, quickMove, 0);




            }
            else { // we want to put things into the storage


                ItemStack itemToPutIn = menu.getCarried();
                int modifiedValue = itemToPutIn.getCount();

                if(action == MouseAction.RIGHT_CLICK)
                    modifiedValue = 1;


                ModNetwork.sendToServer(new TakeOutFromStorageInventoryEntityPacket(itemToPutIn, false,
                        modifiedValue, false));

                menu.interactWithMenu(itemToPutIn, false,
                        modifiedValue, false, 0);



            }

            return;

        }
        else if(slot instanceof NetworkSlot) {



            if(clickType != ClickType.QUICK_MOVE)
                super.slotClicked(slot, slotIndex, btn, clickType); // gives the clicking GUI mechanics

            return;
        }
        else if(slot instanceof VisualItemSlot || slot.container instanceof TransientCraftingContainer) {


            if(slot.container instanceof TransientCraftingContainer) {
                if(!menu.getStorageControllerEntity().isCraftingEnabled())
                    return;
            }


            super.slotClicked(slot, slotIndex, btn, clickType);
            return;
        }
        else if(slot instanceof ResultSlot resultSlot) {

            if(!menu.getStorageControllerEntity().isCraftingEnabled()) // dont allow crafting
                return;

            ItemStack resultStack = resultSlot.getItem();


            boolean quickMove = clickType == ClickType.QUICK_MOVE;

            menu.onItemCrafted(resultStack, quickMove);
            ModNetwork.sendToServer(new CraftItemEntityPacket(resultStack, quickMove));



            return;
        }

        if(clickType == ClickType.QUICK_MOVE) {



            ItemStack itemToPutIn = slot.getItem();
            int modifiedValue = itemToPutIn.getCount();


            ModNetwork.sendToServer(new TakeOutFromStorageInventoryEntityPacket(itemToPutIn, false,
                    modifiedValue, true, slot.getSlotIndex()));

            menu.interactWithMenu(itemToPutIn, false,
                    modifiedValue, true, slot.getSlotIndex());





        }



        super.slotClicked(slot, slotIndex, btn, clickType);



    }




    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta, double rawDelta) {

        customScrollWheel.onMouseScrolled(mouseX, mouseY, delta, rawDelta);
        return super.mouseScrolled(mouseX, mouseY, delta, rawDelta);
    }

    private void renderCustomSlot(GuiGraphics guiGraphics) {




        int startingIndex = (0 * StorageControllerMenu.VISIBLE_CONTENT_HEIGHT)
                + StorageControllerMenu.STARTING_SLOT_INDEX;


        // 36 (first slot) - 62 (last slot)
        for(int i = startingIndex; i <
                startingIndex + StorageControllerMenu.VISIBLE_CONTENT_HEIGHT ; i++) {

            try {



                Slot slot = this.menu.slots.get(i);

                if(slot instanceof CustomGUISlot customGUISlot) { // just to confirm

                    Slot slotIndex = this.menu.slots.get((i -
                            (0 * StorageControllerMenu.VISIBLE_CONTENT_HEIGHT))); // 0 represents scroll page
                    // but with the new system, we don't really need the scroll page anymore
                    // because we are always dealing with a set slots generated.
                    // not deleting first in case I need it again in the future



                    renderCustomSlot(guiGraphics, slot, slotIndex);






                }

            }
            catch(IndexOutOfBoundsException SLOT_DOESNT_EXIST) {


                break;
            }








        }
    }

    private void renderCraftingGridSlot(GuiGraphics guiGraphics) {


        if(menu.getStorageControllerEntity().isCraftingEnabled())
            return;

        for(int i = menu.getCraftSlotIndexStart(); i < menu.getCraftSlotIndexStart() + 9; i++) {

            Slot slot = menu.getSlot(i);


            if(slot.container instanceof TransientCraftingContainer) {
                renderDisabledSlot(guiGraphics, slot.x, slot.y);
            }
        }

    }

    private void renderDisabledSlot(GuiGraphics guiGraphics, int slotX, int slotY) {

        int actualSlotX = leftPos + slotX;
        int actualSlotY = topPos + slotY;

        guiGraphics.fill(actualSlotX, actualSlotY, 16 + actualSlotX, 16 + actualSlotY,
                0xFF686868);
    }

    public void renderCustomSlot(GuiGraphics guiGraphics, Slot slot, Slot slotIndex) {



        if(slot instanceof CustomGUISlot customGUISlot) {

            int actualSlotX = leftPos + slotIndex.x;
            int actualSlotY = topPos + slotIndex.y;



            if(menu.getStorageControllerEntity().isDisabled()) {
                renderDisabledSlot(guiGraphics, slotIndex.x, slotIndex.y);

                return;
            }



            if(customGUISlot.getActualItem().equals(ItemStack.EMPTY , false)) return;






            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            ItemStack displayStack = customGUISlot.getActualItem();



            guiGraphics.renderItem(displayStack, actualSlotX, actualSlotY);
            guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                    actualSlotX, actualSlotY, ""); // item durability, etc
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 0, 300); // 300 just to be safe.


            String text = ModUtils.getUnit(customGUISlot.getActualItemCount());
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



        if(this.hoveredSlot != null) {
            if(this.hoveredSlot instanceof CustomGUISlot customGUISlot) { // on our custom inventory

                ItemStack item = customGUISlot.getActualItem();

                if(!item.equals(ItemStack.EMPTY, false)) {


                    List<Component> textComponents = item.getTooltipLines(Minecraft.getInstance().player,
                            TooltipFlag.NORMAL);

                    textComponents.addAll(customGUISlot.getItemLocations());
                    guiGraphics.renderTooltip(this.font, textComponents, Optional.empty(), item, x, y);

                }


            }
            else if(this.hoveredSlot.container instanceof TransientCraftingContainer) {

                if(!menu.getStorageControllerEntity().isCraftingEnabled()) {

                    List<Component> components = new ArrayList<>();
                    components.addAll(ModUtils.breakComponentLine(Component.translatable("lore."
                            + ModUtils.MODID + ".disabled_crafting")));

                    guiGraphics.renderTooltip(this.font, components, Optional.empty(), x, y);
                }
            }

            ICustomWidgetComponent.renderToolTipAll(menu.getWidgets(), guiGraphics, this.font, x, y, this.hoveredSlot);



            if(!(this.hoveredSlot instanceof VisualItemSlot)
                    && (!(this.hoveredSlot.container instanceof TransientCraftingContainer)
                    || menu.getStorageControllerEntity().isCraftingEnabled()))
                // so it always present the visual item slot's custom tooltip.
                super.renderTooltip(guiGraphics, x, y);
        }





    }
}
