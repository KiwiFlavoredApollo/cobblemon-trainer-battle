package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TrainerEntityRenderer extends MobEntityRenderer<TrainerEntity, PlayerEntityModel<TrainerEntity>> {
    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    @Override
    public Identifier getTexture(TrainerEntity entity) {
        Identifier texture = Optional.ofNullable(entity.getTexture()).orElse(Identifier.tryParse(entity.getFallbackTexture()));

        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        if (manager.getResource(texture).isEmpty()) {
            return Identifier.tryParse(entity.getFallbackTexture());
        }

        if (texture.getPath().isEmpty()) {
            return Identifier.tryParse(entity.getFallbackTexture());
        }

        return texture;
    }
}
