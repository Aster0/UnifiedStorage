package me.astero.mechanicaldrawersmod.registry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.astero.mechanicaldrawersmod.registry.menu.GridControllerMenu;
import me.astero.mechanicaldrawersmod.registry.menu.data.ViewOnlySlot;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.AffineTransform;

public class GridControllerScreen extends AbstractContainerScreen<GridControllerMenu> {








    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ModUtils.MODID, "textures/gui/grid_storage.png");

    public GridControllerScreen(GridControllerMenu menu, Inventory pInventory, Component title) {
        super(menu, pInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;

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

        for(int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = this.menu.slots.get(k);
            renderSlot(guiGraphics, slot);
        }



    }

    @Override
    public boolean mouseClicked(double p_97748_, double p_97749_, int p_97750_) {

        System.out.println(super.mouseClicked(p_97748_, p_97749_, p_97750_));


        return super.mouseClicked(p_97748_, p_97749_, p_97750_);


    }

    @Override
    protected void slotClicked(Slot slot, int p_97779_, int p_97780_, ClickType p_97781_) {

        System.out.println(slot);
        if(slot instanceof ViewOnlySlot) {
            menu.setCarried(new ItemStack(Items.ACACIA_LEAVES));

            return;
        }

        // TODO: Put down logic?

        super.slotClicked(slot, p_97779_, p_97780_, p_97781_);





    }

    @Override
    public void renderSlot(GuiGraphics guiGraphics, Slot slot) {


        if(slot instanceof ViewOnlySlot) {
            var poseStack = guiGraphics.pose();
            poseStack.pushPose();
            ItemStack displayStack = new ItemStack(Items.ACACIA_LEAVES);
            guiGraphics.renderItem(displayStack, leftPos + slot.x, topPos + slot.y);
            guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                    leftPos + slot.x, topPos + slot.y, "");
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0, 0, 200);


            String text = "0";
            float textScale = 0.5f; // Adjust the scale factor as needed
            float scaledX = (leftPos + slot.x) / textScale;
            float scaledY = (topPos + slot.y) / textScale;


            // max 3 length (e.g., 100K, 100M)
            float textWidth = this.font.width(text) == 0 ? 0 :
                    textScale * this.font.width(text) * 2; // Calculate the width of the text after scaling


            poseStack.scale(textScale, textScale, textScale);



            guiGraphics.drawString(this.font, text,
                    scaledX + 1 + 28 - textWidth, scaledY + 1 + 20, 0x000000, false);

            guiGraphics.drawString(this.font, text,
                    scaledX + 30 - textWidth, scaledY + 20, 0xFFFFFF, false);

            guiGraphics.pose().popPose();
        }


    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {


        if(this.hoveredSlot instanceof ViewOnlySlot v) {


            Item item = v.getItem2();
            ItemStack stack = new ItemStack(item);
            guiGraphics.renderTooltip(this.font, stack, x, y);
        }


        super.renderTooltip(guiGraphics, x, y);
    }
}
