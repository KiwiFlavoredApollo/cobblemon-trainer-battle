package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.NoSuchElementException;

public class TrainerEntityRenderer extends MobEntityRenderer<TrainerEntity, PlayerEntityModel<TrainerEntity>> {
    private static final String FALLBACK_TEXTURE = "minecraft:textures/entity/player/slim/steve.png";

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    @Override
    public Identifier getTexture(TrainerEntity entity) {
        try {
            Identifier texture = entity.getTexture();

            ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
            manager.getResource(texture).orElseThrow();

            return texture;

        } catch (NullPointerException | NoSuchElementException e) {
            return Identifier.tryParse(FALLBACK_TEXTURE);
        }
    }
}
