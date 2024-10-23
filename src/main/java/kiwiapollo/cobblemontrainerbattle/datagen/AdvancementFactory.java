package kiwiapollo.cobblemontrainerbattle.datagen;

import net.minecraft.advancement.Advancement;

import java.util.List;

public interface AdvancementFactory {
    List<Advancement> create();
}
