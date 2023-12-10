package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.client.screen.widgets.generic.ICustomWidgetComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu extends AbstractContainerMenu {

    protected Menu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected List<ICustomWidgetComponent> widgets = new ArrayList<>();

    public List<ICustomWidgetComponent> getWidgets() {
        return widgets;
    }

    public abstract void addCustomSlot(Slot slot);




}
