package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class AnchorEntityRenderer extends MobEntityRenderer<AnchorEntity, PlayerEntityModel<AnchorEntity>> {
    private static final String FALLBACK_TEXTURE = "cobblemontrainerbattle:textures/entity/trainer/slim/leaf_piikapiika.png";

    public AnchorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    @Override
    public Identifier getTexture(AnchorEntity entity) {
        Identifier texture = entity.getTexture();

        ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
        if (manager.getResource(texture).isEmpty()) {
            return Identifier.tryParse(FALLBACK_TEXTURE);
        }

        if (texture.getPath().isEmpty()) {
            return Identifier.tryParse(FALLBACK_TEXTURE);
        }

        return texture;
    }
}
