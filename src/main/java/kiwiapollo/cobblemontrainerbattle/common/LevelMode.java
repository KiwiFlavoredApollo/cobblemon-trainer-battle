package kiwiapollo.cobblemontrainerbattle.common;

public enum LevelMode {
    NORMAL(),
    RELATIVE(),
    FLAT();

    LevelMode() {

    }

    public static class Factory {
        private final String levelMode;

        public Factory(String levelMode) {
            this.levelMode = levelMode;
        }

        public LevelMode create() {
            return switch (levelMode) {
                case "normal" -> LevelMode.NORMAL;
                case "relative" -> LevelMode.RELATIVE;
                case "flat" -> LevelMode.FLAT;
                default -> LevelMode.NORMAL;
            };
        }
    }
}
