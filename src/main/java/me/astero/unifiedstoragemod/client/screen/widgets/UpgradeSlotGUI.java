package me.astero.unifiedstoragemod.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.BaseUpgradeSlot;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.ICustomWidgetComponent;
import me.astero.unifiedstoragemod.menu.Menu;
import me.astero.unifiedstoragemod.menu.data.NetworkSlot;
import me.astero.unifiedstoragemod.menu.data.UpgradeSlot;
import me.astero.unifiedstoragemod.menu.data.VisualItemSlot;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;


public class UpgradeSlotGUI<T extends Menu> extends BaseUpgradeSlot implements ICustomWidgetComponent {

    private static final ResourceLocation UPGRADE_START =
            new ResourceLocation(ModUtils.MODID, "textures/gui/slots.png");

    int numberOfSlots = 1, x, y, rawX, rawY;

    private ItemStackHandler itemStackHandler;

    protected SlotType slotType;

    private Slot slot;

    public UpgradeSlotGUI(int numberOfSlots, int x, int y, int rawX, int rawY, ItemStackHandler itemStackHandler,
                          SlotType slotType) {

        this.numberOfSlots = numberOfSlots;
        this.x = x;
        this.y = y;
        this.rawX = rawX;
        this.rawY = rawY;
        this.itemStackHandler = itemStackHandler;

        this.slotType = slotType;


    }

    public void create(T menu) {


        int nextY = this.rawY + 7;

        if(itemStackHandler == null)
            itemStackHandler = new ItemStackHandler(1);

        if(slotType == SlotType.NETWORK) {
            numberOfSlots = 1; // there should only be one network slot
            // we force it to be one.
        }


        for(int i = 0; i < numberOfSlots; i++) {

                slot = new UpgradeSlot(itemStackHandler,
                    i, this.rawX + 8, nextY);

            if(slotType == SlotType.NETWORK) {
                slot = new NetworkSlot(itemStackHandler,
                        i, this.rawX + 8, nextY);
            }
            else if(slotType == SlotType.VISUAL_BLOCK) {
                slot = new VisualItemSlot(itemStackHandler,
                        i, this.rawX + 8, nextY);
            }

            menu.addCustomSlot(slot);

            nextY += 20;

        }



    }

    @Override
    public void tick(GuiGraphics guiGraphics, int leftPos, int topPos) {




        guiGraphics.blit(UPGRADE_START, leftPos + x, topPos + y,   0, 0,
                32, 30);


        int nextYForIcon = topPos + y + 8;

        RenderSystem.enableBlend(); // Enable alpha blending
        RenderSystem.defaultBlendFunc(); // Set the default blend function

        renderIcon(guiGraphics, leftPos, nextYForIcon);

        int nextY = topPos + y;

        for(int i = 1; i < numberOfSlots; i++) {
            if(i == 1) nextY += 25;
            else nextY +=20;

            guiGraphics.blit(UPGRADE_START, leftPos + x, nextY,   42, 0,
                    31, 25);


            nextYForIcon +=20;


            renderIcon(guiGraphics, leftPos, nextYForIcon);





        }

        RenderSystem.disableBlend(); // Disable alpha blending after rendering




    }

    private void renderIcon(GuiGraphics guiGraphics, int leftPos, int nextYForIcon) {

        if(slotType.getCustomItemStack() == null) {

            if(slot.getItem().equals(ItemStack.EMPTY, false))
                guiGraphics.blit(UPGRADE_START, leftPos + x + slotType.getOffsetX(), nextYForIcon,
                        slotType.getIconHeight(), slotType.getIconWidth(),
                        16, 16); // shadow
        }

        else
            guiGraphics.renderFakeItem(new ItemStack(ItemRegistry.SHADOW_UPGRADE_CARD.get()),
                    leftPos + x + slotType.getOffsetX(),
                    nextYForIcon + slotType.getOffsetY());
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

        if(slot instanceof UpgradeSlot) {

            if(!slot.getItem().isEmpty())
                return;

            List<Component> componentList = new ArrayList<>();
            componentList.add(Component.translatable("container.unifiedstorage.upgradeSlotTitle"));
            componentList.addAll(ModUtils.breakComponentLine(Component.translatable(
                    "container.unifiedstorage.upgradeSlotDescription")));

            super.renderCustomTooltip(componentList, guiGraphics, font, x, y, slot);
        }

    }
}
