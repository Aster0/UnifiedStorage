package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.Menu;
import me.astero.unifiedstoragemod.menu.data.NetworkSlot;
import me.astero.unifiedstoragemod.menu.data.VisualBlockSlot;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class VisualItemSlotGUI<T extends Menu> extends UpgradeSlotGUI<T> {

    int numberOfSlots = 1, x, y, rawX, rawY;

    public VisualItemSlotGUI(int numberOfSlots, int x, int y, int rawX, int rawY, ItemStackHandler itemStackHandler) {
        super(numberOfSlots, x, y, rawX, rawY, itemStackHandler, SlotType.VISUAL_BLOCK);

        this.numberOfSlots = numberOfSlots;
        this.x = x;
        this.y = y;
        this.rawX = rawX;
        this.rawY = rawY;


    }



    @Override
    public void renderCustomTooltip(GuiGraphics guiGraphics, Font font, int x, int y, Slot slot) {

        if(slot instanceof VisualBlockSlot) {

            if(!slot.getItem().isEmpty())
                return;

            List<Component> componentList = new ArrayList<>();
            componentList.add(Component.translatable("container.unifiedstorage.visualItemSlotTitle"));
            componentList.addAll(ModUtils.breakComponentLine(Component.translatable(
                    "container.unifiedstorage.visualItemSlotDescription")));


            super.renderCustomTooltip(componentList, guiGraphics, font, x, y, slot);
        }


    }
}
