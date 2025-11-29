package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;

import java.util.*;

public class PokemonType {
    private final Set<Set<String>> types;
    private final String first;
    private final String second;

    public PokemonType(List<String> type) {
        this.first = toFirst(type);
        this.second = toSecond(type);
        this.types = createTypes(first, second);
    }

    private String toFirst(List<String> type) {
        if (type.size() < 1) {
            return null;
        } else {
            return type.get(0);
        }
    }

    private String toSecond(List<String> type) {
        if (type.size() < 2) {
            return null;
        } else {
            return type.get(1);
        }
    }

    private Set<Set<String>> createTypes(String first, String second) {
        if (!isWildcard(first) && !isWildcard(second)) {
            return createLiteralType(first, second);

        } else if (isWildcard(first) && isWildcard(second)) {
            return createAllTypes();

        } else if (isAsterisk(first)) {
            return createSingleAndDualTypes(second);

        } else if (isPlusSign(first)) {
            return createDualTypes(second);

        } else if (isAsterisk(second)) {
            return createSingleAndDualTypes(first);

        } else if (isPlusSign(second)) {
            return createDualTypes(first);

        } else {
            throw new IllegalStateException();
        }
    }

    private boolean isPlusSign(String string) {
        return Objects.equals(string, "+");
    }

    private boolean isAsterisk(String string) {
        return Objects.equals(string, "*");
    }

    private boolean isWildcard(String string) {
        return isAsterisk(string) || isPlusSign(string);
    }

    private Set<Set<String>> createLiteralType(String first, String second) {
        if (first != null && second != null) {
            return Set.of(Set.of(first, second));

        } else if (first == null) {
            return Set.of(Set.of(second));

        } else if (second == null) {
            return Set.of(Set.of(first));
        }

        throw new IllegalStateException();
    }

    private Set<Set<String>> createSingleAndDualTypes(String pivot) {
        Set<Set<String>> types = new HashSet<>();

        for (ElementalType t : ElementalTypes.INSTANCE.all()) {
            if (t.getName().equals(pivot)) {
                continue;
            }

            types.add(Set.of(pivot, t.getName()));
        }

        types.add(Set.of(pivot));

        return types;
    }

    private Set<Set<String>> createDualTypes(String pivot) {
        Set<Set<String>> types = new HashSet<>();

        for (ElementalType t : ElementalTypes.INSTANCE.all()) {
            if (t.getName().equals(pivot)) {
                continue;
            }

            types.add(Set.of(pivot, t.getName()));
        }

        return types;
    }

    private Set<Set<String>> createAllTypes() {
        Set<Set<String>> types = new HashSet<>();

        for (ElementalType first : ElementalTypes.INSTANCE.all()) {
            for (ElementalType second : ElementalTypes.INSTANCE.all()) {
                if (first.getName().equals(second.getName())) {
                    continue;
                }

                types.add(Set.of(first.getName(), second.getName()));
            }
        }

        for (ElementalType single : ElementalTypes.INSTANCE.all()) {
            types.add(Set.of(single.getName()));
        }

        return types;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PokemonType other)) {
            return false;
        }

        if (this.types.size() != 1 && other.types.size() != 1) {
            return false;

        } else if (this.types.size() == 1 && other.types.size() == 1) {
            Set<String> thisType = new ArrayList<>(this.types).get(0);
            Set<String> otherType = new ArrayList<>(other.types).get(0);
            return thisType.equals(otherType);

        } else if (this.types.size() == 1) {
            return other.types.contains(new ArrayList<>(this.types).get(0));

        } else if (other.types.size() == 1) {
            return this.types.contains(new ArrayList<>(other.types).get(0));
        }

        throw new IllegalStateException();

    }

    public String getString() {
        if (second == null) {
            return String.format("%s", ElementalTypes.INSTANCE.get(first).getDisplayName().getString());

        } else {
            return String.format("%s/%s", ElementalTypes.INSTANCE.get(first).getDisplayName().getString(), ElementalTypes.INSTANCE.get(second).getDisplayName().getString());
        }
    }
}
