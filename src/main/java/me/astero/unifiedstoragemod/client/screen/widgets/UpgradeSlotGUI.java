package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.Menu;
import me.astero.unifiedstoragemod.menu.data.NetworkSlot;
import me.astero.unifiedstoragemod.menu.data.UpgradeSlot;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



enum SlotType {
    UPGRADE,
    NETWORK,
    FILTER
}

public class UpgradeSlotGUI<T extends Menu> implements ICustomWidgetComponent, IUpgradeSlot {

    private static final ResourceLocation UPGRADE_START =
            new ResourceLocation(ModUtils.MODID, "textures/gui/slots.png");
    int numberOfSlots = 1, x, y, rawX, rawY;

    protected SlotType slotType;

    public UpgradeSlotGUI(int numberOfSlots, int x, int y, int rawX, int rawY) {

        this.numberOfSlots = numberOfSlots;
        this.x = x;
        this.y = y;
        this.rawX = rawX;
        this.rawY = rawY;

    }

    public void create(T menu) {


        int nextY = this.rawY + 7;

        Slot slot = new UpgradeSlot(new ItemStackHandler(1),
                0, this.rawX + 8, nextY);

        if(slotType == SlotType.NETWORK) {
            slot = new NetworkSlot(new ItemStackHandler(1),
                    0, this.rawX + 8, nextY);
        }





        for(int i = 0; i < numberOfSlots; i++) {

            menu.addCustomSlot(slot);

            nextY += 20;

        }



    }

    @Override
    public void tick(GuiGraphics guiGraphics) {

        guiGraphics.blit(UPGRADE_START, x, y,   0, 0,
                32, 30);


        int nextY = y;
        for(int i = 1; i < numberOfSlots; i++) {
            if(i == 1) nextY += 25;
            else nextY +=20;

            guiGraphics.blit(UPGRADE_START, x, nextY,   42, 0,
                    31, 25);
        }
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY) {

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
    public void renderCustomTooltip(GuiGraphics guiGraphics, Font font) {
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.literal("HI!!"));
        componentList.add(Component.literal("§7HI!!"));

        guiGraphics.renderTooltip(font, componentList, Optional.empty(), x, y);

    }
}
