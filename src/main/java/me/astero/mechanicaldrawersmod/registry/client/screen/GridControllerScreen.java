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



//        guiGraphics.pose().pushPose();
//
//        guiGraphics.renderItem(new ItemStack(Items.ACACIA_PLANKS),
//                (this.leftPos + 8),  this.topPos + 18);
//
//
//
//        guiGraphics.renderItemDecorations(this.font, new ItemStack(Items.ACACIA_PLANKS),
//                (this.leftPos + 8),  this.topPos + 17, "0");

//        // Same as the Vanilla method, just with dynamic width and height
//        guiGraphics.fillGradient(RenderType.guiOverlay(), 1, 1, 1 + 5, 1 + 5,
//                0x80ffffff, 0x80ffffff, 1);









//        // Draw the quantity string with shadow
//        String quantityString = String.valueOf("0");
//        int textWidth = font.width(quantityString);
//        int slotWidth = 18; // Adjust this based on your slot width

        var poseStack = guiGraphics.pose();
        poseStack.pushPose();

        ItemStack displayStack = new ItemStack(Items.ACACIA_LEAVES);
        guiGraphics.renderItem(displayStack, (this.leftPos + 8), this.topPos + 18);
        guiGraphics.renderItemDecorations(minecraft.font, displayStack,
                (this.leftPos + 8), this.topPos + 18, "");

        poseStack.popPose();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 200);
//
        guiGraphics.drawString(this.font, "0",
                (this.leftPos + 18),  this.topPos + 26, 0xFFFFFF, false);





        guiGraphics.pose().popPose();



//        for (int row = 0; row < 3; row++) {
//
//            for (int column = 0; column < 9; column++) {
//
//                int x = leftPos + 18 + (column % 9) * 18;
//                int y = topPos + 18 + (column / 9) * 18;
//
//
//
//                guiGraphics.pose().pushPose();
//
//
//
//
//                // Draw the quantity string with shadow
//                String quantityString = String.valueOf("0");
//                int textWidth = font.width(quantityString);
//                int slotWidth = 18; // Adjust this based on your slot width
//
//                guiGraphics.drawString(this.font, quantityString,
//                        this.leftPos + (slotWidth * (column + 1)), y + 7 + 1, 0x000000, false);
//
//                guiGraphics.drawString(this.font, quantityString,
//                        this.leftPos + (slotWidth * (column + 1)), y + 7, 0xFFFFFF, false);
//
//                guiGraphics.pose().popPose();
//
//            }
//        }

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
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {


        if(this.hoveredSlot instanceof ViewOnlySlot v) {


            Item item = v.getItem2();
            ItemStack stack = new ItemStack(item);
            guiGraphics.renderTooltip(this.font, stack, x, y);
        }


        super.renderTooltip(guiGraphics, x, y);
    }
}
