package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TrainerRegexPredicate implements Predicate<String> {
    private final String regex;

    private TrainerRegexPredicate(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean test(String trainer) {
        return trainer.matches(regex);
    }

    public static class Builder {
        private final static String WILDCARD = ".*";
        private final static String RADICAL_RED = "radicalred";
        private final static String INCLEMENT_EMERALD = "inclementemerald";
        private final static String SMOGON = "smogon";

        private List<String> patterns;

        public Builder() {
            this.patterns = new ArrayList<>();
        }

        public TrainerRegexPredicate build() {
            return new TrainerRegexPredicate(toRegex(patterns));
        }

        public Builder addWildcard() {
            patterns.add(WILDCARD);
            return this;
        }

        public Builder addRadicalRed() {
            patterns.add(RADICAL_RED);
            return this;
        }

        public Builder addInclementEmerald() {
            patterns.add(INCLEMENT_EMERALD);
            return this;
        }

        public Builder addSmogon() {
            patterns.add(SMOGON);
            return this;
        }

        private static String toRegex(List<String> patterns) {
            if (patterns.contains(WILDCARD) || patterns.isEmpty()) {
                return WILDCARD;
            } else {
                return String.format("^(%s)/.+", String.join("|", patterns));
            }
        }
    }
}
