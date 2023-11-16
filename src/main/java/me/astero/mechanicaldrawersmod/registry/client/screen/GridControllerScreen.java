package me.astero.mechanicaldrawersmod.registry.client.screen;

import me.astero.mechanicaldrawersmod.registry.menu.GridControllerMenu;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GridControllerScreen extends AbstractContainerScreen<GridControllerMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage.png");

    public GridControllerScreen(GridControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        renderTransparentBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

}
