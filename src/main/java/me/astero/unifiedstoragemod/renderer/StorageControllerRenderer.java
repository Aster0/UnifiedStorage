package me.astero.unifiedstoragemod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayers;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public class StorageControllerRenderer implements BlockEntityRenderer<StorageControllerEntity> {

    private BlockEntityRendererProvider.Context context;


    private float posY = 1.29F;

    public StorageControllerRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;


    }

    @Override
    public void render(StorageControllerEntity storageControllerEntity,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {





        ItemStack renderItem = storageControllerEntity.getVisualItemInventory().getStackInSlot(0);


        if(!renderItem.equals(ItemStack.EMPTY, false)) {

            ItemRenderer itemRenderer = context.getItemRenderer();

            boolean item = !(renderItem.getItem() instanceof BlockItem);







            if(!item) {

                int count = renderItem.getCount();


                if(count < 2) {


                    renderBlock1(itemRenderer, renderItem, poseStack, buffer, storageControllerEntity, partialTicks);

                }
                else if(count < 3) {
                    renderBlock2(itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);
                }
                else {
                    renderBlock3(itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);
                }



            }
            else {
                renderItem(itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);
            }




        }



    }


    private void renderItem(ItemRenderer itemRenderer, ItemStack renderItem, PoseStack poseStack,
                             MultiBufferSource buffer, StorageControllerEntity storageControllerEntity) {

        poseStack.pushPose();

        float scale = 0.5f;

        poseStack.scale(scale, scale, scale);

        poseStack.translate(0.5 / scale, 1.05 / scale, 0.5 / scale);

        // Get the block's rotation
        Direction blockFacing = storageControllerEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        float rotationYaw = blockFacing.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationYaw));

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));




        itemRenderer.renderStatic(renderItem, ItemDisplayContext.FIXED,
                LightTexture.FULL_SKY,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, storageControllerEntity.getLevel(), 1);

        poseStack.popPose();


    }


    private void renderBlock1(ItemRenderer itemRenderer, ItemStack renderItem, PoseStack poseStack,
                              MultiBufferSource buffer, StorageControllerEntity storageControllerEntity, float partialTicks) {

        poseStack.pushPose();


        // Get the original block's direction
        Direction blockDirection = storageControllerEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        // Determine the rotation angle based on the block's direction
        float rotationAngle = switch (blockDirection) {
            case NORTH -> 0.0f;
            case SOUTH -> 180.0f;
            case WEST -> 90.0f;
            case EAST -> -90.0f;
            default -> 0.0f;

        };


        float scale = 0.5f;

        poseStack.scale(scale, scale, scale);


        // Adjust the multiplier to control the speed of the hover effect
        float timeMultiplier = 0.1f;

        // Calculate the vertical offset based on time
        float yOffset = (float) Math.sin((storageControllerEntity.getLevel().getGameTime() + partialTicks) * timeMultiplier) * 0.1f;

        poseStack.translate(0.5 / scale, (posY + yOffset) / scale, 0.5 / scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));

        itemRenderer.renderStatic(renderItem, ItemDisplayContext.FIXED,
                LightTexture.FULL_SKY,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, storageControllerEntity.getLevel(), 1);


        poseStack.popPose();






    }


    private void renderBlock2(ItemRenderer itemRenderer, ItemStack renderItem, PoseStack poseStack,
                              MultiBufferSource buffer, StorageControllerEntity storageControllerEntity) {


        // 0.1 = 1 block
        placeBlocks(0.2f, 0.40f, 1.05f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.40f, 1.15f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.40f, 1.25f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.50f, 1.25f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.60f, 1.25f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.60f, 1.15f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.2f, 0.60f, 1.05f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.24f, 0.50f, 1.10f, 0.5f,
                itemRenderer, new ItemStack(Items.ACACIA_DOOR), poseStack, buffer, storageControllerEntity);

    }

    private void renderBlock3(ItemRenderer itemRenderer, ItemStack renderItem, PoseStack poseStack,
                              MultiBufferSource buffer, StorageControllerEntity storageControllerEntity) {



        placeBlocks(0.4f, 0.5f, 1.05f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);


        placeBlocks(0.25f, 0.25f, 1.05f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);

        placeBlocks(0.25f, 0.75f, 1.05f, 0.5f,
                itemRenderer, renderItem, poseStack, buffer, storageControllerEntity);
    }

    private void placeBlocks(float scale, float x, float y, float z,
                             ItemRenderer itemRenderer, ItemStack renderItem, PoseStack poseStack,
                             MultiBufferSource buffer, StorageControllerEntity storageControllerEntity) {


        poseStack.pushPose();

        poseStack.scale(scale, scale, scale);

        poseStack.translate(x / scale, y / scale, z / scale);


        itemRenderer.renderStatic(renderItem, ItemDisplayContext.FIXED,
                LightTexture.FULL_SKY,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, storageControllerEntity.getLevel(), 1);

        poseStack.popPose();
    }





}
