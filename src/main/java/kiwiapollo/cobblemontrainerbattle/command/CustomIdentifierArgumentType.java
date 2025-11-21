package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

/**
 * @see Identifier#fromCommandInput(StringReader)
 */
public class CustomIdentifierArgumentType extends IdentifierArgumentType {
    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
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
