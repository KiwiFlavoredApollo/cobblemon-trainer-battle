package kiwiapollo.cobblemontrainerbattle.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IdentifierArgumentType.class)
public class IdentifierArgumentTypeMixin {
    @Inject(method = "parse", at = @At("HEAD"), cancellable = true)
    public void parseTrainerBattleCommandIdentifierArgument(StringReader reader, CallbackInfoReturnable<Identifier> info) throws CommandSyntaxException {
        if (!isTrainerBattleCommand(reader)) {
            return;
        }

        Identifier identifier = parseDefaultedIdentifierArgument(reader);
        info.setReturnValue(identifier);
        info.cancel();
    }

    private boolean isTrainerBattleCommand(StringReader reader) {
        return reader.getString().matches("^(trainerbattle|rentalbattle) .+");
    }

    private Identifier parseDefaultedIdentifierArgument(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead() && Identifier.isCharValid(reader.peek())) {
            reader.skip();
        }

        String string = reader.getString().substring(i, reader.getCursor());

        try {
            return toDefaultedIdentifier(string);

        } catch (InvalidIdentifierException e) {
            reader.setCursor(i);
            throw new SimpleCommandExceptionType(Text.translatable("argument.id.invalid")).createWithContext(reader);
        }
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }
}
