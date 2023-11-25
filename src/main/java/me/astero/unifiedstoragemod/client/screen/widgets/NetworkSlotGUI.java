package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.Menu;
import me.astero.unifiedstoragemod.menu.data.UpgradeSlot;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NetworkSlotGUI<T extends Menu> extends UpgradeSlotGUI<T> {

    private static final ResourceLocation UPGRADE_START =
            new ResourceLocation(ModUtils.MODID, "textures/gui/slots.png");
    int numberOfSlots = 1, x, y, rawX, rawY;

    public NetworkSlotGUI(int numberOfSlots, int x, int y, int rawX, int rawY) {
        super(numberOfSlots, x, y, rawX, rawY);

        this.numberOfSlots = numberOfSlots;
        this.x = x;
        this.y = y;
        this.rawX = rawX;
        this.rawY = rawY;

        this.slotType = SlotType.NETWORK;

    }



    @Override
    public void renderCustomTooltip(GuiGraphics guiGraphics, Font font, int x, int y) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.translatable("container.unifiedstorage.networkSlotTitle"));
        componentList.addAll(ModUtils.breakComponentLine(Component.translatable(
                "container.unifiedstorage.networkSlotDescription")));


        super.renderCustomTooltip(componentList, guiGraphics, font, x, y);

    }
}
