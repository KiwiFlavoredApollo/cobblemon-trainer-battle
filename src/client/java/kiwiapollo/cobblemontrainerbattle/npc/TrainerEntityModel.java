package kiwiapollo.cobblemontrainerbattle.npc;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class TrainerEntityModel extends BipedEntityModel<TrainerEntity> {
    public TrainerEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    // Override the method that handles animation and posing
    @Override
    public void setAngles(TrainerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        // Call the super method to maintain basic biped behavior
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        // Example: Customize the arm movement for the "Trainer"
        this.rightArm.pitch = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 2.0F * limbDistance * 0.5F;
        this.leftArm.pitch = MathHelper.cos(limbAngle * 0.6662F) * 2.0F * limbDistance * 0.5F;
    }

    // Optionally, you can modify how the model is positioned relative to the world here
    @Override
    public void render(MatrixStack matrices, net.minecraft.client.render.VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        // Call the superclass method to preserve basic rendering
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);

        // Add any additional rendering code here if needed (like custom overlays or animations)
    }

    // Example method to define model data for this entity (head, body, arms, legs, etc.)
    public static TexturedModelData getTexturedModelData() {
        // Copy the same model parts as the player model (BipedEntityModel) and return them
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F), 0.0F);
        return TexturedModelData.of(modelData, 64, 64);
    }
}
