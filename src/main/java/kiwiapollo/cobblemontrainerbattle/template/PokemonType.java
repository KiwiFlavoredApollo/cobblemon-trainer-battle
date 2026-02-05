package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;

import java.util.*;
import java.util.stream.Collectors;

public class PokemonType {
    private final String first;
    private final String second;

    public PokemonType(String first, String second) {
        if (first == null && second == null) {
            throw new IllegalArgumentException();
        }

        if (first != null && !isWildcard(first) && !isElementalType(first)) {
            throw new IllegalArgumentException();
        }

        if (second != null && !isWildcard(second) && !isElementalType(second)) {
            throw new IllegalArgumentException();
        }

        this.first = first;
        this.second = second;
    }

    public PokemonType(String type) {
        this(type, null);
    }

    private boolean isWildcard(String string) {
        return isAsterisk(string) || isPlusSign(string);
    }

    private boolean isAsterisk(String string) {
        return Objects.equals(string, "*");
    }

    private boolean isPlusSign(String string) {
        return Objects.equals(string, "+");
    }

    private boolean isElementalType(String type) {
        return ElementalTypes.INSTANCE.all().contains(ElementalTypes.INSTANCE.get(type));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof PokemonType other)) {
            return false;
        }

        return this.contains(other) || other.contains(this);
    }

    private boolean contains(PokemonType other) {
        return this.createTypeGroup().containsAll(other.createTypeGroup());
    }

    private Set<Set<String>> createTypeGroup() {
        if (first != null && second != null) {
            return createDualTypeGroup(first, second);
        }

        if (first != null && second == null) {
            return createSingleTypeGroup(first);
        }

        if (first == null && second != null) {
            return createSingleTypeGroup(second);
        }

        throw new IllegalStateException();
    }

    private Set<Set<String>> createDualTypeGroup(String first, String second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }

        if (isWildcard(first) && isWildcard(second)) {
            return new DualTypeGroupFactory.All().create();
        }

        if (!isWildcard(first) && !isWildcard(second)) {
            return new DualTypeGroupFactory.Literal(first, second).create();
        }

        if (!isWildcard(first) && isPlusSign(second)) {
            return new DualTypeGroupFactory.PlusSign(first).create();
        }

        if (!isWildcard(first) && isAsterisk(second)) {
            return new DualTypeGroupFactory.Asterisk(first).create();
        }

        if (isPlusSign(first) && !isWildcard(second)) {
            return new DualTypeGroupFactory.PlusSign(second).create();
        }

        if (isAsterisk(first) && !isWildcard(second)) {
            return new DualTypeGroupFactory.Asterisk(second).create();
        }

        throw new IllegalStateException();
    }

    private Set<Set<String>> createSingleTypeGroup(String type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        if (isWildcard(type)) {
            return new SingleTypeGroupFactory.All().create();
        }

        if (!isWildcard(type)) {
            return new SingleTypeGroupFactory.Literal(type).create();
        }

        throw new IllegalStateException();
    }

    public String getString() {
        if (first != null && second != null) {
            return String.format("%s/%s", getDisplayName(first), getDisplayName(second));
        }

        if (first != null && second == null) {
            return String.format("%s", getDisplayName(first));
        }

        if (first == null && second != null) {
            return String.format("%s", getDisplayName(second));
        }

        throw new IllegalStateException();
    }

    private String getDisplayName(String type) {
        if (Objects.equals(type, "*")) {
            return "ANY*";
        }

        if (Objects.equals(type, "+")) {
            return "ANY+";
        }

        if (ElementalTypes.INSTANCE.all().contains(ElementalTypes.INSTANCE.get(type))) {
            return ElementalTypes.INSTANCE.get(type).getDisplayName().getString();
        }

        throw new IllegalArgumentException();
    }

    private static class SingleTypeGroupFactory {
        private static class All {
            private Set<Set<String>> create() {
                return ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).map(Set::of).collect(Collectors.toSet());
            }
        }

        private static class Literal {
            private final String type;

            private Literal(String type) {
                this.type = type;
            }

            private Set<Set<String>> create() {
                return Set.of(Set.of(type));
            }
        }
    }

    private static class DualTypeGroupFactory {
        private static class All {
            private Set<Set<String>> create() {
                Set<Set<String>> group = new HashSet<>();

                for (ElementalType first : ElementalTypes.INSTANCE.all()) {
                    for (ElementalType second : ElementalTypes.INSTANCE.all()) {
                        if (Objects.equals(first, second)) {
                            continue;
                        }

                        group.add(Set.of(first.getName(), second.getName()));
                    }
                }

                return group;
            }
        }

        private static class Literal {
            private final String first;
            private final String second;

            private Literal(String first, String second) {
                this.first = first;
                this.second = second;
            }

            private Set<Set<String>> create() {
                return Set.of(Set.of(first, second));
            }
        }

        private static class PlusSign {
            private final String type;

            private PlusSign(String type) {
                this.type = type;
            }

            private Set<Set<String>> create() {
                Set<Set<String>> group = new HashSet<>();

                ElementalType first = ElementalTypes.INSTANCE.getOrException(type);

                for (ElementalType second : ElementalTypes.INSTANCE.all()) {
                    if (Objects.equals(first, second)) {
                        continue;
                    }

                    group.add(Set.of(first.getName(), second.getName()));
                }

                return group;
            }
        }

        private static class Asterisk {
            private final String type;

            private Asterisk(String type) {
                this.type = type;
            }

            private Set<Set<String>> create() {
                Set<Set<String>> group = new HashSet<>();

                group.addAll(new DualTypeGroupFactory.PlusSign(type).create());
                group.addAll(new SingleTypeGroupFactory.Literal(type).create());

                return group;
            }
        }
    }
}
