package me.astero.mechanicaldrawersmod.registry.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.astero.mechanicaldrawersmod.registry.blocks.DrawerBlock;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;

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


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();


//        itemRenderer.render(itemStack, ItemDisplayContext.FIXED,
//                false, poseStack, buffer, combinedOverlay, packedLight,null);




        poseStack.pushPose();
        poseStack.translate(0.5f, 2, 0.5f);




        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                getLightLevel(drawerBlockEntity.getLevel(), drawerBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, poseStack, buffer, drawerBlockEntity.getLevel(), 1);


//        itemRenderer.renderStatic(Minecraft.getInstance().player, itemStack, ItemDisplayContext.FIXED,
//                false, poseStack, buffer, Minecraft.getInstance().level, packedLight, packedOverlay, 0);

        poseStack.popPose();



    }

    public int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(blockLight, skyLight);
    }

}
