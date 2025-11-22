package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.global.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
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

    private final Identifier identifier;
    private final UUID uuid;
    private final Text name;
    private final PartyStore party;
    private final BattleFormat battleFormat;
    private final BattleAI battleAI;
    private final SoundEvent battleTheme;
    private final UUID entityUuid;
    private final Identifier texture;
    private final boolean isSpawningAllowed;
    private final List<String> onVictoryCommands;
    private final List<String> onDefeatCommands;

    public AbstractTrainerBattleParticipant(TrainerTemplate template, PartyStore party) {
        this.identifier = template.getIdentifier();
        this.uuid = UUID.randomUUID();
        this.name = template.getDisplayName();
        this.party = party;
        this.battleFormat = template.getBattleFormat();
        this.battleAI = template.getBattleAI();
        this.battleTheme = template.getBattleTheme();
        this.entityUuid = template.getEntityUuid();
        this.texture = template.getTexture();
        this.isSpawningAllowed = template.isSpawningAllowed();
        this.onVictoryCommands = template.getOnVictoryCommands();
        this.onDefeatCommands = template.getOnDefeatCommands();
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
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
        return new CustomTrainerBattleActor(
                getName(),
                getBattleTeam(player),
                getBattleAI(),
                getEntityOrPlayer(player),
                () -> {
                    onVictoryCommands.forEach(command -> executeCommand(command, player));
                    getBattleRecord(player).setVictoryCount(getBattleRecord(player).getVictoryCount() + 1);
                    CustomCriteria.DEFEAT_TRAINER_CRITERION.trigger(player);
                },
                () -> {
                    onDefeatCommands.forEach(command -> executeCommand(command, player));
                    getBattleRecord(player).setDefeatCount(getBattleRecord(player).getDefeatCount() + 1);
                }
        );
    }

    private BattleRecord getBattleRecord(ServerPlayerEntity player) {
        return PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid()).getOrCreate(identifier);
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
