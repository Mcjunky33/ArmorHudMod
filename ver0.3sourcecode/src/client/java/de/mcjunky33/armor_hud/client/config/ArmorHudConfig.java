package de.mcjunky33.armor_hud.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

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
    // Stacked modes
    private int xOffsetLeft1;
    private int yOffsetLeft1;
    private int xOffsetLeft2;
    private int yOffsetLeft2;
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

    // --- NEU: Zum automatischen Umschalten bei GUI-Scale-Wechsel ---
    private int lastGuiScaleChecked = -1;

    // ---- Default-Koordinaten für STACKED/VERTICAL Mode ----
    // GUI SCALE 1
    public static final int DEFAULT_X_OFFSET_LEFT1_1 = -950;
    public static final int DEFAULT_Y_OFFSET_LEFT1_1 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT2_1 = -950;
    public static final int DEFAULT_Y_OFFSET_LEFT2_1 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_1 = 930;
    public static final int DEFAULT_Y_OFFSET_RIGHT_1 = -52;

    // GUI SCALE 2
    public static final int DEFAULT_X_OFFSET_LEFT1_2 = -470;
    public static final int DEFAULT_Y_OFFSET_LEFT1_2 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT2_2 = -470;
    public static final int DEFAULT_Y_OFFSET_LEFT2_2 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_2 = 450;
    public static final int DEFAULT_Y_OFFSET_RIGHT_2 = -52;

    // GUI SCALE 3
    public static final int DEFAULT_X_OFFSET_LEFT1_3 = -310;
    public static final int DEFAULT_Y_OFFSET_LEFT1_3 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT2_3 = -310;
    public static final int DEFAULT_Y_OFFSET_LEFT2_3 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_3 = 290;
    public static final int DEFAULT_Y_OFFSET_RIGHT_3 = -52;

    // GUI SCALE 4
    public static final int DEFAULT_X_OFFSET_LEFT1_4 = -230;
    public static final int DEFAULT_Y_OFFSET_LEFT1_4 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT2_4 = -230;
    public static final int DEFAULT_Y_OFFSET_LEFT2_4 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_4 = -210;
    public static final int DEFAULT_Y_OFFSET_RIGHT_4 = -52;

    // ---- Default-Koordinaten für horizontal (Hotbar, wie gehabt) ----
    public static final int DEFAULT_X_OFFSET_NORM_HOR = -220;
    public static final int DEFAULT_Y_OFFSET_NORM_HOR = -22;

    // ---- Default-Koordinaten für SPLITMODE horizontal ----
    // Diese Werte sind für JEDEN GUI-Scale gleich!
    public static final int DEFAULT_X_OFFSET_LEFT2_HOR = -180;
    public static final int DEFAULT_Y_OFFSET_LEFT2_HOR = -22;
    public static final int DEFAULT_X_OFFSET_RIGHT_HOR = 100;
    public static final int DEFAULT_Y_OFFSET_RIGHT_HOR = -22;

    public static final int DEFAULT_BOX_SIZE = 22;
    public static final int DEFAULT_SPACING = 2;

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
        config.xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_1;
        config.yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_1;
        config.xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1;
        config.yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1;
        config.xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1;
        config.yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1;
        config.lastGuiScaleChecked = -1; // NEU
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

    public int getCurrentGuiScale() {
        try {
            return MinecraftClient.getInstance().options.getGuiScale().getValue();
        } catch (Exception e) {
            return 2;
        }
    }

    // NEU: Getter/Setter für lastGuiScaleChecked
    public int getLastGuiScaleChecked() { return lastGuiScaleChecked; }
    public void setLastGuiScaleChecked(int value) { lastGuiScaleChecked = value; saveConfig(); }

    public void applyCurrentModeDefaults() {
        int guiScale = getCurrentGuiScale();
        if (vertical) {
            setDefaultsForStackedMode(guiScale, splitMode);
        } else if (splitMode) {
            setDefaultsForSplitModeHorizontal();
        } else {
            setDefaultsForHorizontalMode();
        }
        saveConfig();
    }

    /**
     * Set defaults for stacked mode (vertical) according to GUI scale and splitMode.
     * - Wenn splitMode=false: xOffsetLeft1/yOffsetLeft1 für alle 4 Boxen.
     * - Wenn splitMode=true: xOffsetLeft2/yOffsetLeft2 für Helm & Chestplate (Box 0 & 1),
     *                        xOffsetRight/yOffsetRight für Leggings & Boots (Box 2 & 3).
     */
    public void setDefaultsForStackedMode(int guiScale, boolean splitMode) {
        if (splitMode) {
            switch (guiScale) {
                case 1:
                    xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1;
                    yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1;
                    xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1;
                    yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1;
                    break;
                case 2:
                    xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_2;
                    yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_2;
                    xOffsetRight = DEFAULT_X_OFFSET_RIGHT_2;
                    yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_2;
                    break;
                case 3:
                    xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_3;
                    yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_3;
                    xOffsetRight = DEFAULT_X_OFFSET_RIGHT_3;
                    yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_3;
                    break;
                case 4:
                    xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_4;
                    yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_4;
                    xOffsetRight = DEFAULT_X_OFFSET_RIGHT_4;
                    yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_4;
                    break;
                default:
                    xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_4 / 2;
                    yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_4 / 2;
                    xOffsetRight = DEFAULT_X_OFFSET_RIGHT_4 / 2;
                    yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_4 / 2;
                    break;
            }
        } else {
            switch (guiScale) {
                case 1:
                    xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_1;
                    yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_1;
                    break;
                case 2:
                    xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_2;
                    yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_2;
                    break;
                case 3:
                    xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_3;
                    yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_3;
                    break;
                case 4:
                    xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_4;
                    yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_4;
                    break;
                default:
                    xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_4 / 2;
                    yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_4 / 2;
                    break;
            }
        }
    }

    // NEU: für Split horizontal, Werte immer gleich!
    public void setDefaultsForSplitModeHorizontal() {
        xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_HOR;
        yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_HOR;
        xOffsetRight = DEFAULT_X_OFFSET_RIGHT_HOR;
        yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_HOR;
    }

    public void setDefaultsForHorizontalMode() {
        xOffset = DEFAULT_X_OFFSET_NORM_HOR;
        yOffset = DEFAULT_Y_OFFSET_NORM_HOR;
    }

    // --- Hier die neue resetToDefaults Methode ---
    public void resetToDefaults() {
        // Set all values to their intended "full reset" defaults
        visible = true;
        language = "en";
        showDurabilityAsNumber = true;
        showDurabilityBar = false;
        darkMode = false;
        vertical = false;
        splitMode = false;
        boxSize = DEFAULT_BOX_SIZE;
        spacing = DEFAULT_SPACING;
        durabilityWarningThreshold = 0.20f;

        xOffset = DEFAULT_X_OFFSET_NORM_HOR;
        yOffset = DEFAULT_Y_OFFSET_NORM_HOR;

        xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT1_1;
        yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT1_1;
        xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1;
        yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1;
        xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1;
        yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1;

        lastGuiScaleChecked = -1; // damit beim nächsten rendern geprüft wird

        saveConfig();
    }

    // Getter/Setter für alle Koordinaten
    public int getXOffset() { return xOffset; }
    public void setXOffset(int xOffset) { this.xOffset = xOffset; saveConfig(); }
    public int getYOffset() { return yOffset; }
    public void setYOffset(int yOffset) { this.yOffset = yOffset; saveConfig(); }
    public int getXOffsetLeft1() { return xOffsetLeft1; }
    public void setXOffsetLeft1(int xOffsetLeft1) { this.xOffsetLeft1 = xOffsetLeft1; saveConfig(); }
    public int getYOffsetLeft1() { return yOffsetLeft1; }
    public void setYOffsetLeft1(int yOffsetLeft1) { this.yOffsetLeft1 = yOffsetLeft1; saveConfig(); }
    public int getXOffsetLeft2() { return xOffsetLeft2; }
    public void setXOffsetLeft2(int xOffsetLeft2) { this.xOffsetLeft2 = xOffsetLeft2; saveConfig(); }
    public int getYOffsetLeft2() { return yOffsetLeft2; }
    public void setYOffsetLeft2(int yOffsetLeft2) { this.yOffsetLeft2 = yOffsetLeft2; saveConfig(); }
    public int getXOffsetRight() { return xOffsetRight; }
    public void setXOffsetRight(int xOffsetRight) { this.xOffsetRight = xOffsetRight; saveConfig(); }
    public int getYOffsetRight() { return yOffsetRight; }
    public void setYOffsetRight(int yOffsetRight) { this.yOffsetRight = yOffsetRight; saveConfig(); }

    public boolean isSplitMode() { return splitMode; }
    public void setSplitMode(boolean splitMode) {
        this.splitMode = splitMode;
        applyCurrentModeDefaults();
    }

    public boolean isVertical() { return vertical; }
    public void setVertical(boolean vertical) {
        boolean previousVertical = this.vertical;
        this.vertical = vertical;

        // Wenn SplitMode aktiv ist und von vertical -> horizontal gewechselt wird:
        if (splitMode && !vertical && previousVertical) {
            setDefaultsForSplitModeHorizontal();
        } else {
            applyCurrentModeDefaults();
        }
        saveConfig();
    }

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