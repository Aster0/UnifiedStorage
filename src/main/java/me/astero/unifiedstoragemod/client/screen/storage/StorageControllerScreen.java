package me.astero.unifiedstoragemod.client.screen.storage;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.unifiedstoragemod.client.screen.widgets.*;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.CustomScrollWheel;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.CustomSearchField;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.ICustomWidgetComponent;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.items.upgrades.IBlockUpdater;
import me.astero.unifiedstoragemod.menu.data.*;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.menu.enums.MouseAction;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.CraftItemEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdateCraftingSlotsEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.NetworkCardInsertedEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.TakeOutFromStorageInventoryEntityPacket;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StorageControllerScreen extends AbstractContainerScreen<StorageControllerMenu> {



    private CustomSearchField searchField;
    private CustomScrollWheel customScrollWheel;

    private int savedPages = -1;





    public static final WidgetSprites STORE_BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/cross_button"),
            new ResourceLocation("widget/cross_button_highlighted"));
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage_crafting.png");


    public StorageControllerScreen(StorageControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 233;
        this.imageHeight = 235;


    }



    @Override
    protected void init() {
        super.init();

        registerSearchField();


        this.inventoryLabelY = 142; // shifting the inventory label

        addRenderableButtons();


    }

    private void addRenderableButtons() {

        ImageButton storeItemsButton = new StoreItemButton<>(this.leftPos + 93,
                this.topPos + 84, 7, 7, STORE_BUTTON_SPRITES, Component
                .translatable("container.unifiedstorage.crafting_clear_tooltip"), this);


        this.addRenderableWidget(storeItemsButton);
    }

    public void clearCraftingGrid() {

        if(!menu.getStorageControllerEntity().isCraftingEnabled())
            return;

        for(int i = 0; i < menu.craftSlots.getContainerSize(); i++) {

            ItemStack stack = menu.craftSlots.getItem(i);

            if(!stack.equals(ItemStack.EMPTY, false)) {

                ModNetwork.sendToServer(new UpdateCraftingSlotsEntityPacket(ItemStack.EMPTY, i,
                        true, stack, false));




            }

        }


    }


    private void registerSearchField() {
        searchField = new CustomSearchField(font, leftPos + 84, topPos + 6, 76, 12) {
            @Override
            public void onPlayerType(String text) {


                if(!text.isEmpty()) {

                    menu.onStorageSearch(text, false, true);
                    savedPages = -1;
                    return;
                }

                customScrollWheel.setPages(menu.getTotalPages());


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


        //new ItemLogsGUI().tick(guiGraphics, leftPos, topPos);

        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0,
                this.imageWidth, this.imageHeight);


        ICustomWidgetComponent.tickAll(menu.getWidgets(), guiGraphics, leftPos, topPos);

        int currentPages = !menu.getStorageSearchData().isSearching() ? menu.getTotalPages() :
                menu.getTotalPages(menu.getStorageSearchData().getSearchedStorageList());

        if((!menu.getStorageSearchData().isSearching() && savedPages != currentPages)
                || (menu.getStorageSearchData().isSearching()
                && savedPages != currentPages)) {




            customScrollWheel = new StorageGUIScrollWheel(this.leftPos + 179,
                    this.topPos + 17, this.topPos + 54, currentPages, menu);


            if(currentPages < savedPages) { // if item is removed that made current pages lesser,



                if(menu.getScrollPage() == savedPages)
                    menu.generateSlots(currentPages);
            }

            savedPages = currentPages;


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
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {



        if(keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {


            for(ItemIdentifier itemIdentifier : menu.getStorageControllerEntity().queueToRemoveItems) {

                int index = menu.getStorageControllerEntity().mergedStorageContents.indexOf(itemIdentifier);

                if(index != -1) {

                    ItemIdentifier existingItem = menu.getStorageControllerEntity().getMergedStorageContents(index);

                    if(existingItem.getCount() <= 0)
                        menu.getStorageControllerEntity().mergedStorageContents.remove(itemIdentifier);
                }




            }

            menu.getStorageControllerEntity().queueToRemoveItems.clear();
            menu.regenerateCurrentPage();
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void removed() {
        super.removed();

        if(!(menu.getPlayerInventory().player.containerMenu instanceof StorageControllerMenu)) {
            menu.getStorageControllerEntity().menu = null;
        }

    }

    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {

        if(customScrollWheel != null)
            customScrollWheel.onMouseRelease();

        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mode) {


        if(customScrollWheel != null)
            customScrollWheel.onMouseClick(mouseX, mouseY, mode);

        if(searchField != null)
            searchField.setFocused(false);

        return super.mouseClicked(mouseX, mouseY, mode);


    }

    @Override
    protected void slotClicked(Slot slot, int slotIndex, int btn, ClickType clickType) {


        if(slot == null) { // somehow it happens sometimes
            super.slotClicked(slot, slotIndex, btn, clickType); // just do normal things

            return;
        }
        else if(slot instanceof ResultSlot) {

            if(clickType == ClickType.PICKUP_ALL)
                clickType = ClickType.PICKUP;

            super.slotClicked(slot, slotIndex, btn, clickType); // just do normal things
            return;
        }


        if(menu.getStorageControllerEntity().isDisabled()) {




            if(slot instanceof ItemVisualSlot) {
                return;
            }
            else if(slot instanceof PlayerSlot && clickType == ClickType.QUICK_MOVE &&
                    !(slot.getItem().getItem() instanceof NetworkItem))
                return;

            if(!(slot.container instanceof TransientCraftingContainer)) { // cant use the crafting if it's not with a network card

                if(slot instanceof NetworkSlot) {



                    if(menu.getCarried().getItem() instanceof NetworkItem networkItem) {

                        if(!(networkItem.getUpgradeType() == UpgradeType.NETWORK))
                            return;

                        menu.getStorageControllerEntity().setDisabled(false);

                        ModNetwork.sendToServer(new
                                NetworkCardInsertedEntityPacket(menu.getStorageControllerEntity().getBlockPos()));
                    }


                }
                super.slotClicked(slot, slotIndex, btn, clickType);
            }




            return;
        }



        // 0 = place down? 1 = place one. clickType cant track this

        MouseAction action = btn == 0 ? MouseAction.LEFT_CLICK : MouseAction.RIGHT_CLICK;

        if(slot instanceof ItemVisualSlot v) {



            if(menu.getCarried().equals(ItemStack.EMPTY, false)) { // means we are taking out smth from the storage



                if(clickType == ClickType.SWAP) // this makes it so, the keys 1,2,3.. etc doesn't trigger taking out stuff from inventory.
                    return;

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

                //menu.interactWithMenu(itemStack, true, modifiedValue, quickMove, 0);




            }
            else { // we want to put things into the storage




                ItemStack itemToPutIn = menu.getCarried();
                int modifiedValue = itemToPutIn.getCount();

                if(action == MouseAction.RIGHT_CLICK)
                    modifiedValue = 1;


                ModNetwork.sendToServer(new TakeOutFromStorageInventoryEntityPacket(itemToPutIn, false,
                        modifiedValue, false));


//                menu.interactWithMenu(itemToPutIn, false,
//                        modifiedValue, false, 0);



            }

            return;

        }
        else if(slot instanceof NetworkSlot) {

            ItemStack slotInStack = slot.getItem();


            if(!(menu.getCarried().getItem() instanceof NetworkItem) &&
                    !menu.getCarried().equals(ItemStack.EMPTY, false)) {

                return;

            }

            if(!slotInStack.equals(ItemStack.EMPTY,false)) {

                if(clickType != ClickType.CLONE) {


                    if(menu.getCarried().getItem() instanceof NetworkItem networkItem) { // if we are swapping, we should update the storage
                        if(networkItem.getUpgradeType() == UpgradeType.NETWORK) {
                            ModNetwork.sendToServer(new
                                    NetworkCardInsertedEntityPacket(menu.getStorageControllerEntity().getBlockPos()));
                        }

                    } // if hand is empty, just taking out the card.
                    else {
                    
                        menu.getStorageControllerEntity().actionWhenNetworkTakenOut(
                                menu.getStorageControllerEntity().getLevel().getPlayerByUUID(
                                        Minecraft.getInstance().player.getUUID()));


                        menu.getStorageControllerEntity().setDisabled(true);
                    }


                }


            }


            super.slotClicked(slot, slotIndex, btn, clickType); // gives the clicking GUI mechanics

            return;
        }
        else if(slot instanceof VisualItemSlot || slot.container instanceof TransientCraftingContainer || slot instanceof UpgradeSlot) {


            if(slot.container instanceof TransientCraftingContainer) {
                if(!menu.getStorageControllerEntity().isCraftingEnabled())
                    return;
            }

            if(slot instanceof UpgradeSlot) {

                IBlockUpdater blockUpdater = null;

                if(slot.getItem().getItem() instanceof IBlockUpdater mBlockUpdater) {
                    blockUpdater = mBlockUpdater;
                }
                else if(menu.getCarried().getItem() instanceof IBlockUpdater mBlockUpdater) {
                    blockUpdater = mBlockUpdater;
                }

                if(blockUpdater != null) {
                    super.slotClicked(slot, slotIndex, btn, clickType);
                    blockUpdater.update(menu.getStorageControllerEntity());

                    return;
                }


            }


            super.slotClicked(slot, slotIndex, btn, clickType);
            return;
        }




        super.slotClicked(slot, slotIndex, btn, clickType);



    }




    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta, double rawDelta) {

        if(!menu.getStorageControllerEntity().isDisabled())
            customScrollWheel.onMouseScrolled(mouseX, mouseY, delta, rawDelta);

        return super.mouseScrolled(mouseX, mouseY, delta, rawDelta);
    }

    private void renderCustomSlot(GuiGraphics guiGraphics) {



        // e.g., 36 (first slot) - 62 (last slot)
        for(int i = menu.getStorageStackStartSlot(); i <
                menu.getStorageStackStartSlot() + StorageControllerMenu.VISIBLE_CONTENT_HEIGHT ; i++) {

            try {



                Slot slot = this.menu.slots.get(i);

                if(slot instanceof ItemVisualSlot itemVisualSlot) { // just to confirm

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



        if(slot instanceof ItemVisualSlot itemVisualSlot) {

            int actualSlotX = leftPos + slotIndex.x;
            int actualSlotY = topPos + slotIndex.y;


            if(menu.getStorageControllerEntity().isDisabled()) {
                renderDisabledSlot(guiGraphics, slotIndex.x, slotIndex.y);

                return;
            }



            if(itemVisualSlot.getActualItem().equals(ItemStack.EMPTY , false)) return;


            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            ItemStack displayStack = itemVisualSlot.getActualItem();


//            FluidStack fluidStack = new FluidStack(Fluids.LAVA, 1);
//
//
//            IClientFluidTypeExtensions client = IClientFluidTypeExtensions.of(fluidStack.getFluid());
//            TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager()
//                    .getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(client.getStillTexture());
//
//            guiGraphics.blit(0, 0, 0, 16, 16, sprite,
//                    FastColor.ABGR32.red(client.getTintColor()) / 255F, FastColor.ABGR32.blue(client.getTintColor()) / 255F,
//                    FastColor.ABGR32.green(client.getTintColor()) / 255F, 1);



            guiGraphics.renderItem(displayStack, actualSlotX, actualSlotY);
            guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                    actualSlotX, actualSlotY, ""); // item durability, etc
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 0, 300); // 300 just to be safe.


            String text = ModUtils.getUnit(itemVisualSlot.getActualItemCount());
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
            if(this.hoveredSlot instanceof ItemVisualSlot itemVisualSlot) { // on our custom inventory

                ItemStack item = itemVisualSlot.getActualItem();

                if(!item.equals(ItemStack.EMPTY, false)) {


                    List<Component> textComponents = item.getTooltipLines(Minecraft.getInstance().player,
                            TooltipFlag.NORMAL);

                    textComponents.addAll(itemVisualSlot.getItemLocations());
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


        this.searchField.renderCustomTooltip(guiGraphics, this.font, x, y, null);





    }
}
