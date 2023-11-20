package me.astero.unifiedstoragemod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.astero.unifiedstoragemod.blocks.entity.DrawerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DrawerItemRenderer implements BlockEntityRenderer<DrawerBlockEntity> {

    private BlockEntityRendererProvider.Context context;

    public DrawerItemRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;


    }

    @Override
    public void render(DrawerBlockEntity drawerBlockEntity,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {




        ItemStack itemStack = drawerBlockEntity.getInventory().getStackInSlot(0);


        ItemRenderer itemRenderer = context.getItemRenderer();


//        itemRenderer.render(itemStack, ItemDisplayContext.FIXED,
//                false, poseStack, buffer, combinedOverlay, packedLight,null);




        poseStack.pushPose();


//        // Get the player's rotation angles
//        Player localPlayer = Minecraft.getInstance().player;
//        float playerYaw = localPlayer.getYRot();
//        float playerPitch = localPlayer.getXRot();
//
//        // Rotate the item to face the player
//        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - playerYaw));
//        poseStack.mulPose(Axis.XP.rotationDegrees(-playerPitch));

        // Get the block's rotation
        Direction blockFacing = drawerBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        // Translate the rendering position to the center of the block
        poseStack.translate(0.5, 0.5, 0.5);


        // Adjust the rendering position based on the block's facing direction
        float offset = 1f;
        float scale = 0.5f;


        if(itemStack.getItem() instanceof BlockItem) {

            // if it's an item, we move the offset in a little

            offset = 0.5f;
            scale = 0.8f;
        }



        poseStack.scale(scale, scale, scale);

        poseStack.translate(offset * blockFacing.getStepX(),
                offset * blockFacing.getStepY(),
                offset * blockFacing.getStepZ());

        // Rotate both the item and font to face the same direction as the block
        float rotationYaw = blockFacing.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationYaw));


        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, drawerBlockEntity.getLevel(), 1);

        // Reset the scale before rendering text
        poseStack.scale(1 / scale, 1 / scale, 1 / scale);




        renderTextOnBlock(drawerBlockEntity.getItemsFromDrawer(0), poseStack, buffer, packedLight,
                blockFacing);



        poseStack.popPose();



    }


    private void renderTextOnBlock(String text, PoseStack poseStack, MultiBufferSource buffer,
                                   int packedLight, Direction blockDirection) {

        if (text != null && !text.isEmpty()) {
            Font fontRenderer = Minecraft.getInstance().font;

            // Calculate the width and height of the text
            float textWidth = fontRenderer.width(text) * 0.02f; // Scale factor is applied

            // Calculate the X position to center the text within the block
            float textOffsetX = (3 - textWidth) / 2.0f;

            float scaleX = 0.02f, scaleY = 0.02f, scaleZ = 0.02f;

            if (blockDirection == Direction.WEST || blockDirection == Direction.EAST) {
                scaleX = -scaleX;
                scaleY = -scaleY;
                scaleZ = -scaleZ;
            } else {
                scaleY = -scaleY;
            }

            poseStack.scale(scaleX, scaleY, scaleZ);

            // Calculate the position based on screen space and maxWidth
            float textOffsetY = 0.5f; // Center vertically

            // Adjust textOffsetX to center the text within the block
            poseStack.translate(textOffsetX, textOffsetY, 11);

            // Calculate the actual position of the text based on its length
            float actualTextOffsetX = -textWidth / 2.0f;

            fontRenderer.drawInBatch(text,
                    actualTextOffsetX, 0, 0xFFFFFF, false, poseStack.last().pose(),
                    buffer, Font.DisplayMode.NORMAL, 0, 0xFFFFFF );




            // Reset transformations
            poseStack.scale(1.0f / scaleX, -1.0f / scaleY, 1.0f / scaleZ);
            poseStack.translate(0.0, -textOffsetY, 0.0);
        }

    }



}
