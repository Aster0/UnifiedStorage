package me.astero.unifiedstoragemod.renderer.items.wings;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class StorageWingsModel<T extends LivingEntity> extends AgeableListModel<T> {

    private final ModelPart rightWing;
    private final ModelPart leftWing;
    public StorageWingsModel(ModelPart part) {



        this.leftWing = part.getChild("left_wing");
        this.rightWing = part.getChild("right_wing");
    }



    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition leftWing =
                partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create()
                        .texOffs(16, 0)
                        .addBox(-10.0F, 4.05F, 7.4F, 10.0F, 1.25F, 1.25F),
                        PartPose.offsetAndRotation(0, -3.7F, -1.6F, 0,0,-45 ));


        partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 0, 0, 0), PartPose.offsetAndRotation(0, 0.0F, 0.0F, 0, 0.0F, 0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing);
    }

    public void setupAnim(T p_102544_, float p_102545_, float p_102546_, float p_102547_, float p_102548_, float p_102549_) {

    }

}
