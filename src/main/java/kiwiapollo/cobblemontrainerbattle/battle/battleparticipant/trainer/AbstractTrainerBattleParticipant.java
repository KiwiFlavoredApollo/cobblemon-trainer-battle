package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleAIFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleFormatFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerPreset;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class AbstractTrainerBattleParticipant implements TrainerBattleParticipant {
    private static final int MINIMUM_ENTITY_DISTANCE = 10;

    private final String id;
    private final UUID uuid;
    private final String name;
    private final PartyStore party;
    private final BattleFormat battleFormat;
    private final BattleAI battleAI;
    private final SoundEvent battleTheme;
    private final UUID entityUuid;
    private final Identifier texture;
    private final boolean isSpawningAllowed;
    private final List<String> onVictoryCommands;
    private final List<String> onDefeatCommands;

    public AbstractTrainerBattleParticipant(String id, TrainerPreset preset, PartyStore party) {
        this.id = id;
        this.uuid = UUID.randomUUID();
        this.name = Text.translatable(Optional.ofNullable(preset.displayName).orElse(id)).getString();
        this.party = party;
        this.battleFormat = new BattleFormatFactory(preset.battleFormat).create();
        this.battleAI = new BattleAIFactory(preset.battleFormat, preset.battleAI).create();
        this.battleTheme = SoundEvent.of(Identifier.tryParse(preset.battleTheme));
        this.entityUuid = toUUID(preset.entityUuid);
        this.texture = Identifier.tryParse(preset.texture);
        this.isSpawningAllowed = preset.isSpawningAllowed;
        this.onVictoryCommands = preset.onVictoryCommands;
        this.onDefeatCommands = preset.onDefeatCommands;
    }

    private static UUID toUUID(String uuid) {
        try {
            return UUID.fromString(Objects.requireNonNull(uuid));

        } catch (NullPointerException | IllegalArgumentException e) {
            return UUID.randomUUID();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BattleFormat getBattleFormat() {
        return battleFormat;
    }

    @Override
    public BattleAI getBattleAI() {
        return battleAI;
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return Optional.ofNullable(battleTheme);
    }

    @Override
    public boolean isSpawningAllowed() {
        return isSpawningAllowed;
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        return new EntityBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(player),
                getBattleAI(),
                getEntityOrPlayer(player)
        );
    }

    private LivingEntity getEntityOrPlayer(ServerPlayerEntity player) {
        try {
            return getEntity(player);

        } catch (NullPointerException e) {
            return player;
        }
    }

    @Override
    public LivingEntity getEntity(ServerPlayerEntity player) {
        try {
            LivingEntity entity = (LivingEntity) player.getServerWorld().getEntity(entityUuid);

            if (entity.distanceTo(player) > MINIMUM_ENTITY_DISTANCE) {
                throw new NullPointerException();
            }

            return entity;

        } catch (ClassCastException e) {
            throw new NullPointerException();
        }
    }

    @Override
    public void onPlayerVictory(ServerPlayerEntity player) {
        onVictoryCommands.forEach(command -> executeCommand(command, player));
    }

    @Override
    public void onPlayerDefeat(ServerPlayerEntity player) {
        onDefeatCommands.forEach(command -> executeCommand(command, player));
    }

    @Override
    public PartyStore getParty() {
        return party;
    }

    @Override
    public List<BattlePokemon> getBattleTeam(ServerPlayerEntity player) {
        List<BattlePokemon> team = getParty().toGappyList().stream().filter(Objects::nonNull).map(new SafeCopyBattlePokemonFactory()).toList();
        team.forEach(pokemon -> pokemon.getEffectedPokemon().heal());
        return team;
    }

    private void executeCommand(String command, ServerPlayerEntity player) {
        command = command.replace("%player%", player.getGameProfile().getName());

        MinecraftServer server = player.getCommandSource().getServer();
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
    }

    @Override
    public Identifier getTexture() {
        return texture;
    }
}
