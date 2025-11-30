package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;

import java.util.*;

public class PokemonType {
    private final String first;
    private final String second;

    public PokemonType(String first, String second) {
        if (first == null && second == null) {
            throw new IllegalArgumentException();
        }

        if (!isValidType(first)) {
            throw new IllegalArgumentException();
        }

        if (!isValidType(second)) {
            throw new IllegalArgumentException();
        }

        this.first = first;
        this.second = second;
    }

    public PokemonType(String type) {
        this(type, null);
    }

    private boolean isValidType(String type) {
        return type == null || List.of("*", "+").contains(type) || ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).toList().contains(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PokemonType other)) {
            return false;
        }

        List<Set<String>> thisGroup = this.createTypeGroup();
        List<Set<String>> otherGroup = other.createTypeGroup();

        if (thisGroup.size() != 1 && otherGroup.size() != 1) {
            return false;
        }

        if (thisGroup.size() == 1 && otherGroup.size() == 1) {
            return thisGroup.get(0).equals(otherGroup.get(0));
        }

        if (thisGroup.size() == 1 && otherGroup.size() != 1) {
            return otherGroup.contains(thisGroup.get(0));
        }

        if (thisGroup.size() != 1 && otherGroup.size() == 1) {
            return thisGroup.contains(otherGroup.get(0));
        }

        throw new IllegalStateException();
    }

    private List<Set<String>> createTypeGroup() {
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

    private List<Set<String>> createSingleTypeGroup(String type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        if (isWildcard(type)) {
            return createAllSingleTypeGroup();
        }

        if (!isWildcard(type)) {
            return createLiteralSingleTypeGroup(type);
        }

        throw new IllegalStateException();
    }

    private List<Set<String>> createAllSingleTypeGroup() {
        return ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).map(Set::of).toList();
    }

    private List<Set<String>> createLiteralSingleTypeGroup(String type) {
        return List.of(Set.of(type));
    }

    private List<Set<String>> createDualTypeGroup(String first, String second) {
        if (first == null) {
            throw new IllegalArgumentException();
        }

        if (second == null) {
            throw new IllegalArgumentException();
        }

        if (isWildcard(first) && isWildcard(second)) {
            return createAllDualTypeGroup();
        }

        if (!isWildcard(first) && !isWildcard(second)) {
            return createLiteralDualTypeGroup(first, second);
        }

        if (!isWildcard(first) && isAsterisk(second)) {
            List<Set<String>> dual = createDualTypeGroupIncluding(first);
            List<Set<String>> single = createLiteralSingleTypeGroup(first);

            List<Set<String>> group = new ArrayList<>();
            group.addAll(dual);
            group.addAll(single);

            return group;
        }

        if (!isWildcard(first) && isPlusSign(second)) {
            return createDualTypeGroupIncluding(first);
        }

        if (isAsterisk(first) && !isWildcard(second)) {
            List<Set<String>> dual = createDualTypeGroupIncluding(second);
            List<Set<String>> single = createLiteralSingleTypeGroup(second);

            List<Set<String>> group = new ArrayList<>();
            group.addAll(dual);
            group.addAll(single);

            return group;
        }

        if (isPlusSign(first) && !isWildcard(second)) {
            return createDualTypeGroupIncluding(second);
        }

        throw new IllegalStateException();
    }

    private List<Set<String>> createAllDualTypeGroup() {
        List<Set<String>> types = new ArrayList<>();

        for (ElementalType first : ElementalTypes.INSTANCE.all()) {
            for (ElementalType second : ElementalTypes.INSTANCE.all()) {
                if (first.equals(second)) {
                    continue;
                }

                types.add(Set.of(first.getName(), second.getName()));
            }
        }

        return types;
    }

    private List<Set<String>> createLiteralDualTypeGroup(String first, String second) {
        return List.of(Set.of(first, second));
    }

    private List<Set<String>> createDualTypeGroupIncluding(String first) {
        List<Set<String>> types = new ArrayList<>();

        for (String second : ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).toList()) {
            if (first.equals(second)) {
                continue;
            }

            types.add(Set.of(first, second));
        }

        return types;
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

    public String getString() {
        if (first != null && second != null) {
            return String.format("%s/%s", getDisplayName(first), getDisplayName(second));
            // Water / +
            // Water, +
            // Fire, Rock
            // Electric, Flying
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
//        if (List.of("*", "+").contains(type)) {
//            return type;
//        }

        if (Objects.equals(type, "*")) {
            return "ANY*";
        }

        if (Objects.equals(type, "+")) {
            return "ANY+";
        }

        if (ElementalTypes.INSTANCE.all().stream().map(ElementalType::getName).toList().contains(type)) {
            return ElementalTypes.INSTANCE.get(type).getDisplayName().getString();
        }

        throw new IllegalArgumentException();
    }
}
