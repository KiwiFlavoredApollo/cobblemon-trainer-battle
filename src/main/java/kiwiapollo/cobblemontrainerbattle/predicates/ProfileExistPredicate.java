package kiwiapollo.cobblemontrainerbattle.predicates;

import kiwiapollo.cobblemontrainerbattle.parser.profile.ProfileRegistry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProfileExistPredicate implements MessagePredicate<Identifier> {
    private final ProfileRegistry<?> registry;

    public ProfileExistPredicate(ProfileRegistry<?> registry) {
        this.registry = registry;
    }

    @Override
    public MutableText getMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.profile_exist");
    }

    @Override
    public boolean test(Identifier identifier) {
        return registry.containsKey(identifier);
    }
}
