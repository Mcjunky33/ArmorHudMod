package de.mcjunky33.armor_hud.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArmorHudConfig {
    private static volatile ArmorHudConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("armor_hud.json");

    // Offsets & Layout
    private int xOffset;
    private int yOffset;
    private int xOffsetLeft;
    private int yOffsetLeft;
    private int xOffsetRight;
    private int yOffsetRight;
    private boolean splitMode = false;
    private boolean vertical = false;

    // Display
    private float durabilityWarningThreshold;
    private int boxSize;
    private int spacing;
    private boolean visible;
    private boolean showDurabilityBar;
    private boolean showDurabilityAsNumber;
    private boolean darkMode;

    private String language = "auto";

    // Only one keybind, for opening config menu. This is set by Minecraft's keybind system!
    // No longer stored here; handled by KeyBinding in your mod init.
    // Example: KeyBinding keyBindOpenConfig = new KeyBinding("key.armorhud.open_config", GLFW.GLFW_KEY_F10, "key.categories.armorhud");

    // --- Default-Koordinaten ---
    public static final int DEFAULT_X_OFFSET_NORM_HOR = -220;
    public static final int DEFAULT_Y_OFFSET_NORM_HOR = -22;

    public static final int DEFAULT_X_OFFSET_NORM_VERT = -470;
    public static final int DEFAULT_Y_OFFSET_NORM_VERT = -102;

    public static final int DEFAULT_X_OFFSET_LEFT_SPLIT_HOR = -170;
    public static final int DEFAULT_Y_OFFSET_LEFT_SPLIT_HOR = -22;
    public static final int DEFAULT_X_OFFSET_RIGHT_SPLIT_HOR = 100;
    public static final int DEFAULT_Y_OFFSET_RIGHT_SPLIT_HOR = -22;

    public static final int DEFAULT_X_OFFSET_LEFT_SPLIT_VERT = -470;
    public static final int DEFAULT_Y_OFFSET_LEFT_SPLIT_VERT = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_SPLIT_VERT = 450;
    public static final int DEFAULT_Y_OFFSET_RIGHT_SPLIT_VERT = -52;

    private static final int DEFAULT_BOX_SIZE = 22;
    private static final int DEFAULT_SPACING = 2;

    public ArmorHudConfig() {}

    public static ArmorHudConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (ArmorHudConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = loadOrCreate();
                }
            }
        }
        return INSTANCE;
    }

    private static ArmorHudConfig loadOrCreate() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, ArmorHudConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to load config: " + e.getMessage());
            }
        }
        return createDefaultConfig();
    }

    private static ArmorHudConfig createDefaultConfig() {
        ArmorHudConfig config = new ArmorHudConfig();
        config.boxSize = DEFAULT_BOX_SIZE;
        config.spacing = DEFAULT_SPACING;
        config.durabilityWarningThreshold = 0.20f;
        config.visible = true;
        config.showDurabilityBar = true;
        config.showDurabilityAsNumber = false;
        config.darkMode = false;
        config.language = "auto";
        config.splitMode = false;
        config.vertical = false;
        config.xOffset = DEFAULT_X_OFFSET_NORM_HOR;
        config.yOffset = DEFAULT_Y_OFFSET_NORM_HOR;
        config.xOffsetLeft = DEFAULT_X_OFFSET_LEFT_SPLIT_HOR;
        config.yOffsetLeft = DEFAULT_Y_OFFSET_LEFT_SPLIT_HOR;
        config.xOffsetRight = DEFAULT_X_OFFSET_RIGHT_SPLIT_HOR;
        config.yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_SPLIT_HOR;
        config.saveConfig();
        return config;
    }

    public void saveConfig() {
        try {
            if (!Files.exists(CONFIG_PATH.getParent())) {
                Files.createDirectories(CONFIG_PATH.getParent());
            }
            String json = GSON.toJson(this);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    private void copyFrom(ArmorHudConfig other) {
        if (other != null) {
            this.xOffset = other.xOffset;
            this.yOffset = other.yOffset;
            this.xOffsetLeft = other.xOffsetLeft;
            this.yOffsetLeft = other.yOffsetLeft;
            this.xOffsetRight = other.xOffsetRight;
            this.yOffsetRight = other.yOffsetRight;
            this.splitMode = other.splitMode;
            this.vertical = other.vertical;
            this.durabilityWarningThreshold = other.durabilityWarningThreshold;
            this.boxSize = other.boxSize;
            this.spacing = other.spacing;
            this.visible = other.visible;
            this.showDurabilityBar = other.showDurabilityBar;
            this.showDurabilityAsNumber = other.showDurabilityAsNumber;
            this.darkMode = other.darkMode;
            this.language = other.language;
        }
    }

    // Set defaults for current mode
    public void setDefaultsForCurrentMode() {
        if (splitMode) {
            if (vertical) {
                xOffsetLeft = DEFAULT_X_OFFSET_LEFT_SPLIT_VERT;
                yOffsetLeft = DEFAULT_Y_OFFSET_LEFT_SPLIT_VERT;
                xOffsetRight = DEFAULT_X_OFFSET_RIGHT_SPLIT_VERT;
                yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_SPLIT_VERT;
            } else {
                xOffsetLeft = DEFAULT_X_OFFSET_LEFT_SPLIT_HOR;
                yOffsetLeft = DEFAULT_Y_OFFSET_LEFT_SPLIT_HOR;
                xOffsetRight = DEFAULT_X_OFFSET_RIGHT_SPLIT_HOR;
                yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_SPLIT_HOR;
            }
        } else {
            if (vertical) {
                xOffset = DEFAULT_X_OFFSET_NORM_VERT;
                yOffset = DEFAULT_Y_OFFSET_NORM_VERT;
            } else {
                xOffset = DEFAULT_X_OFFSET_NORM_HOR;
                yOffset = DEFAULT_Y_OFFSET_NORM_HOR;
            }
        }
    }

    public void resetToDefaults() {
        ArmorHudConfig defaults = createDefaultConfig();
        copyFrom(defaults);
        saveConfig();
    }

    public void onModeChanged(boolean newSplit, boolean newVertical) {
        this.splitMode = newSplit;
        this.vertical = newVertical;
        setDefaultsForCurrentMode();
        saveConfig();
    }

    // Getter/Setter für alles
    public int getXOffset() { return xOffset; }
    public void setXOffset(int xOffset) { this.xOffset = xOffset; saveConfig(); }
    public int getYOffset() { return yOffset; }
    public void setYOffset(int yOffset) { this.yOffset = yOffset; saveConfig(); }
    public boolean isSplitMode() { return splitMode; }
    public void setSplitMode(boolean splitMode) { onModeChanged(splitMode, this.vertical); }
    public boolean isVertical() { return vertical; }
    public void setVertical(boolean vertical) { onModeChanged(this.splitMode, vertical); }
    public int getXOffsetLeft() { return xOffsetLeft; }
    public void setXOffsetLeft(int xOffsetLeft) { this.xOffsetLeft = xOffsetLeft; saveConfig(); }
    public int getYOffsetLeft() { return yOffsetLeft; }
    public void setYOffsetLeft(int yOffsetLeft) { this.yOffsetLeft = yOffsetLeft; saveConfig(); }
    public int getXOffsetRight() { return xOffsetRight; }
    public void setXOffsetRight(int xOffsetRight) { this.xOffsetRight = xOffsetRight; saveConfig(); }
    public int getYOffsetRight() { return yOffsetRight; }
    public void setYOffsetRight(int yOffsetRight) { this.yOffsetRight = yOffsetRight; saveConfig(); }

    public float getDurabilityWarningThreshold() { return durabilityWarningThreshold; }
    public void setDurabilityWarningThreshold(float threshold) { this.durabilityWarningThreshold = threshold; saveConfig(); }
    public int getBoxSize() { return boxSize; }
    public void setBoxSize(int size) { this.boxSize = size; saveConfig(); }
    public int getSpacing() { return spacing; }
    public void setSpacing(int spacing) { this.spacing = spacing; saveConfig(); }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; saveConfig(); }
    public boolean isShowDurabilityBar() { return showDurabilityBar; }
    public void setShowDurabilityBar(boolean show) { this.showDurabilityBar = show; saveConfig(); }
    public boolean isShowDurabilityAsNumber() { return showDurabilityAsNumber; }
    public void setShowDurabilityAsNumber(boolean show) { this.showDurabilityAsNumber = show; saveConfig(); }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; saveConfig(); }
    public String getLanguage() { return language == null ? "auto" : language; }
    public void setLanguage(String lang) { this.language = lang == null ? "auto" : lang; saveConfig(); }
}