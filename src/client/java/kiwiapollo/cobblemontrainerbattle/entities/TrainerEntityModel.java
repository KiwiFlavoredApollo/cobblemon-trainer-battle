package kiwiapollo.cobblemontrainerbattle.entities;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class TrainerEntityModel extends BipedEntityModel<TrainerEntity> {
    public TrainerEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
    @Override
    public void setAngles(TrainerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        this.rightArm.pitch = MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI) * 2.0F * limbDistance * 0.5F;
        this.leftArm.pitch = MathHelper.cos(limbAngle * 0.6662F) * 2.0F * limbDistance * 0.5F;
    }

    @Override
    public void render(MatrixStack matrices, net.minecraft.client.render.VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F), 0.0F);
        return TexturedModelData.of(modelData, 64, 64);
    }
}
