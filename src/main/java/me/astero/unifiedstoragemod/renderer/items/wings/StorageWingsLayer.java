package me.astero.unifiedstoragemod.renderer.items.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.astero.unifiedstoragemod.client.CustomModelLayer;
import me.astero.unifiedstoragemod.client.RegisterBlockEntities;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StorageWingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M>  {

    private static final ResourceLocation WINGS_TEXTURE = new ResourceLocation(ModUtils.MODID,
            "textures/item/wings.png");

    private final StorageWingsModel storageWingsModel;

    public StorageWingsLayer(RenderLayerParent renderLayerParent) {
        super(renderLayerParent);

        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        storageWingsModel = new StorageWingsModel(entityModels.bakeLayer(CustomModelLayer.WINGS));


    }


    public void render(PoseStack poseStack, MultiBufferSource buffer, int p_116953_, T p_116954_, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
        ItemStack itemstack = p_116954_.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, p_116954_)) {
            ResourceLocation resourcelocation;

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();


            poseStack.pushPose();


            poseStack.translate(0F, -0.1f, 0.1F);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            poseStack.scale(1F, 1.1F, 1F);

            itemRenderer.renderStatic(new ItemStack(ItemRegistry.STORAGE_WINGS.get()), ItemDisplayContext.FIXED,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY, poseStack, buffer, Minecraft.getInstance().level, 1);

            poseStack.popPose();


        }
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == ItemRegistry.STORAGE_WINGS.get();
    }


    public ResourceLocation getWingsTexture(ItemStack stack, T entity) {
        return WINGS_TEXTURE;
    }
}
