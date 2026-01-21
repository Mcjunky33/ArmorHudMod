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

    // ==================== Default-Koordinaten ====================
    // Horizontal Mode Defaults
    public static final int DEFAULT_X_OFFSET_NORM_HOR = -220;
    public static final int DEFAULT_Y_OFFSET_NORM_HOR = -22;
    public static final int DEFAULT_X_OFFSET_LEFT2_HOR = -180;
    public static final int DEFAULT_Y_OFFSET_LEFT2_HOR = -22;
    public static final int DEFAULT_X_OFFSET_RIGHT_HOR = 100;
    public static final int DEFAULT_Y_OFFSET_RIGHT_HOR = -22;

    // Vertical Normal (stacked=false) Defaults
    public static final int DEFAULT_X_OFFSET_LEFT_VERTICAL_1 = -950;
    public static final int DEFAULT_Y_OFFSET_LEFT_VERTICAL_1 = -112;
    public static final int DEFAULT_X_OFFSET_LEFT_VERTICAL_2 = -470;
    public static final int DEFAULT_Y_OFFSET_LEFT_VERTICAL_2 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT_VERTICAL_3 = -310;
    public static final int DEFAULT_Y_OFFSET_LEFT_VERTICAL_3 = -102;
    public static final int DEFAULT_X_OFFSET_LEFT_VERTICAL_4 = -230;
    public static final int DEFAULT_Y_OFFSET_LEFT_VERTICAL_4 = -102;

    // Stacked Vertical (stacked=true) Defaults
    public static final int DEFAULT_X_OFFSET_LEFT2_1 = -950;
    public static final int DEFAULT_Y_OFFSET_LEFT2_1 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_1 = 930;
    public static final int DEFAULT_Y_OFFSET_RIGHT_1 = -52;

    public static final int DEFAULT_X_OFFSET_LEFT2_2 = -470;
    public static final int DEFAULT_Y_OFFSET_LEFT2_2 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_2 = 450;
    public static final int DEFAULT_Y_OFFSET_RIGHT_2 = -52;

    public static final int DEFAULT_X_OFFSET_LEFT2_3 = -310;
    public static final int DEFAULT_Y_OFFSET_LEFT2_3 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_3 = 290;
    public static final int DEFAULT_Y_OFFSET_RIGHT_3 = -52;

    public static final int DEFAULT_X_OFFSET_LEFT2_4 = -230;
    public static final int DEFAULT_Y_OFFSET_LEFT2_4 = -52;
    public static final int DEFAULT_X_OFFSET_RIGHT_4 = -210;
    public static final int DEFAULT_Y_OFFSET_RIGHT_4 = -52;

    // Box Defaults
    public static final int DEFAULT_BOX_SIZE = 22;
    public static final int DEFAULT_SPACING = 2;

    // ==================== Fields ====================
    private int xOffset;
    private int yOffset;
    private int xOffsetLeft1;
    private int yOffsetLeft1;
    private int xOffsetLeft2;
    private int yOffsetLeft2;
    private int xOffsetRight;
    private int yOffsetRight;
    private boolean splitMode = false;
    private boolean vertical = false;

    private float durabilityWarningThreshold;
    private int boxSize;
    private int spacing;
    private boolean visible;

    private int durabilityDisplayMode = 0;
    private boolean darkMode;
    private boolean showBoxTexture = true;
    private String language = "auto";
    private int lastGuiScaleChecked = -1;

    private int numberOffsetX = 0;
    private int numberOffsetY = -12;
    private int numberOffsetXLeft = 0;
    private int numberOffsetYLeft = -12;
    private int numberOffsetXRight = 0;
    private int numberOffsetYRight = -12;

    // Vertical Offsets
    private int xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_1;
    private int yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1;
    private int numberOffsetXLeftVertical = 0;
    private int numberOffsetYLeftVertical = 0;

    // ==================== Durability Options ====================
    private boolean showDurabilityBar = true;      // Bar an/aus
    private boolean showDurabilityText = true;     // Zahl/Prozent an/aus
    private boolean showDurabilityPercent = false; // false = Zahl, true = Prozent

    // ==================== Hand Item ====================
    private boolean showHandDurability = false;

    public ArmorHudConfig() {}

    // ==================== Singleton ====================
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
        config.durabilityDisplayMode = 0;
        config.darkMode = false;
        config.showBoxTexture = true;
        config.language = "auto";
        config.splitMode = false;
        config.vertical = false;
        config.xOffset = DEFAULT_X_OFFSET_NORM_HOR;
        config.yOffset = DEFAULT_Y_OFFSET_NORM_HOR;
        config.xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT_VERTICAL_1;
        config.yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1;
        config.xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1;
        config.yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1;
        config.xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1;
        config.yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1;
        config.xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_1;
        config.yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1;
        config.numberOffsetXLeftVertical = 0;
        config.numberOffsetYLeftVertical = 0;
        config.lastGuiScaleChecked = -1;
        config.showHandDurability = false;

        // Durability defaults
        config.showDurabilityBar = true;
        config.showDurabilityText = true;
        config.showDurabilityPercent = false;

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

    public void resetToDefaults() {
        visible = true;
        language = "en";
        durabilityDisplayMode = 0;
        darkMode = false;
        showBoxTexture = true;
        vertical = false;
        splitMode = false;
        boxSize = DEFAULT_BOX_SIZE;
        spacing = DEFAULT_SPACING;
        durabilityWarningThreshold = 0.20f;

        numberOffsetX = 0;
        numberOffsetY = -12;
        numberOffsetXLeft = 0;
        numberOffsetYLeft = -12;
        numberOffsetXRight = 0;
        numberOffsetYRight = -12;

        xOffset = DEFAULT_X_OFFSET_NORM_HOR;
        yOffset = DEFAULT_Y_OFFSET_NORM_HOR;

        xOffsetLeft1 = DEFAULT_X_OFFSET_LEFT_VERTICAL_1;
        yOffsetLeft1 = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1;

        xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1;
        yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1;

        xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1;
        yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1;

        xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_1;
        yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1;
        numberOffsetXLeftVertical = 0;
        numberOffsetYLeftVertical = -12;

        lastGuiScaleChecked = -1;
        showHandDurability = false;

        showDurabilityBar = true;
        showDurabilityText = true;
        showDurabilityPercent = false;

        applyCurrentModeDefaults();
        saveConfig();
    }

    public int getCurrentGuiScale() {
        try {
            return MinecraftClient.getInstance().options.getGuiScale().getValue();
        } catch (Exception e) {
            return 2;
        }
    }

    // In ArmorHudConfig.java ergänzen/prüfen:
    public void checkGuiScale() {
        int currentScale = getCurrentGuiScale();
        if (this.lastGuiScaleChecked != currentScale) {
            // Wenn sich die Skalierung geändert hat, Offsets neu berechnen oder anpassen
            applyCurrentModeDefaults();
            this.lastGuiScaleChecked = currentScale;
            saveConfig();
        }
    }


    // ==================== Getter / Setter ====================
    public boolean isShowDurabilityBar() { return showDurabilityBar; }
    public void setShowDurabilityBar(boolean value) { this.showDurabilityBar = value; saveConfig(); }

    public boolean isShowDurabilityText() { return showDurabilityText; }
    public void setShowDurabilityText(boolean value) { this.showDurabilityText = value; saveConfig(); }

    public boolean isShowDurabilityPercent() { return showDurabilityPercent; }
    public void setShowDurabilityPercent(boolean value) { this.showDurabilityPercent = value; saveConfig(); }

    public boolean isShowHandDurability() { return showHandDurability; }
    public void setShowHandDurability(boolean show) { this.showHandDurability = show; saveConfig(); }

    public int getLastGuiScaleChecked() { return lastGuiScaleChecked; }
    public void setLastGuiScaleChecked(int value) { lastGuiScaleChecked = value; saveConfig(); }

    // ==================== Getter/Setter für Position & Style ====================
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

    public int getXOffsetLeftVertical() { return xOffsetLeftVertical; }
    public void setXOffsetLeftVertical(int value) { this.xOffsetLeftVertical = value; saveConfig(); }
    public int getYOffsetLeftVertical() { return yOffsetLeftVertical; }
    public void setYOffsetLeftVertical(int value) { this.yOffsetLeftVertical = value; saveConfig(); }
    public int getNumberOffsetXLeftVertical() { return numberOffsetXLeftVertical; }
    public void setNumberOffsetXLeftVertical(int value) { this.numberOffsetXLeftVertical = value; saveConfig(); }
    public int getNumberOffsetYLeftVertical() { return numberOffsetYLeftVertical; }
    public void setNumberOffsetYLeftVertical(int value) { this.numberOffsetYLeftVertical = value; saveConfig(); }

    public int getNumberOffsetX() { return numberOffsetX; }
    public void setNumberOffsetX(int numberOffsetX) { this.numberOffsetX = numberOffsetX; saveConfig(); }
    public int getNumberOffsetY() { return numberOffsetY; }
    public void setNumberOffsetY(int numberOffsetY) { this.numberOffsetY = numberOffsetY; saveConfig(); }
    public int getNumberOffsetXLeft() { return numberOffsetXLeft; }
    public void setNumberOffsetXLeft(int numberOffsetXLeft) { this.numberOffsetXLeft = numberOffsetXLeft; saveConfig(); }
    public int getNumberOffsetYLeft() { return numberOffsetYLeft; }
    public void setNumberOffsetYLeft(int numberOffsetYLeft) { this.numberOffsetYLeft = numberOffsetYLeft; saveConfig(); }
    public int getNumberOffsetXRight() { return numberOffsetXRight; }
    public void setNumberOffsetXRight(int numberOffsetXRight) { this.numberOffsetXRight = numberOffsetXRight; saveConfig(); }
    public int getNumberOffsetYRight() { return numberOffsetYRight; }
    public void setNumberOffsetYRight(int numberOffsetYRight) { this.numberOffsetYRight = numberOffsetYRight; saveConfig(); }

    public boolean isSplitMode() { return splitMode; }
    public void setSplitMode(boolean splitMode) { this.splitMode = splitMode; applyCurrentModeDefaults(); saveConfig(); }

    public boolean isVertical() { return vertical; }
    public void setVertical(boolean vertical) { this.vertical = vertical; applyCurrentModeDefaults(); saveConfig(); }

    public float getDurabilityWarningThreshold() { return durabilityWarningThreshold; }
    public void setDurabilityWarningThreshold(float durabilityWarningThreshold) { this.durabilityWarningThreshold = durabilityWarningThreshold; saveConfig(); }
    public int getBoxSize() { return boxSize; }
    public void setBoxSize(int boxSize) { this.boxSize = boxSize; saveConfig(); }
    public int getSpacing() { return spacing; }
    public void setSpacing(int spacing) { this.spacing = spacing; saveConfig(); }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; saveConfig(); }
    public boolean isShowBoxTexture() { return showBoxTexture; }
    public void setShowBoxTexture(boolean showBoxTexture) { this.showBoxTexture = showBoxTexture; saveConfig(); }
    public int getDurabilityDisplayMode() { return durabilityDisplayMode; }
    public void setDurabilityDisplayMode(int durabilityDisplayMode) { this.durabilityDisplayMode = durabilityDisplayMode; saveConfig(); }
    public boolean isDarkMode() { return darkMode && showBoxTexture; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; saveConfig(); }
    public String getLanguage() { return language == null ? "auto" : language; }
    public void setLanguage(String language) { this.language = language == null ? "auto" : language; saveConfig(); }

    // ==================== Defaults anwenden ====================
    public void applyCurrentModeDefaults() {
        int guiScale = getCurrentGuiScale();
        if (guiScale > 4) guiScale = 4;

        if (vertical && !splitMode) {
            switch (guiScale) {
                case 1 -> { xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_1; yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_1; }
                case 2 -> { xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_2; yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_2; }
                case 3 -> { xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_3; yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_3; }
                case 4 -> { xOffsetLeftVertical = DEFAULT_X_OFFSET_LEFT_VERTICAL_4; yOffsetLeftVertical = DEFAULT_Y_OFFSET_LEFT_VERTICAL_4; }
            }
        } else if (vertical && splitMode) {
            switch (guiScale) {
                case 1 -> { xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_1; yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_1; xOffsetRight = DEFAULT_X_OFFSET_RIGHT_1; yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_1; }
                case 2 -> { xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_2; yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_2; xOffsetRight = DEFAULT_X_OFFSET_RIGHT_2; yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_2; }
                case 3 -> { xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_3; yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_3; xOffsetRight = DEFAULT_X_OFFSET_RIGHT_3; yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_3; }
                case 4 -> { xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_4; yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_4; xOffsetRight = DEFAULT_X_OFFSET_RIGHT_4; yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_4; }
            }
        } else if (splitMode) {
            xOffsetLeft2 = DEFAULT_X_OFFSET_LEFT2_HOR;
            yOffsetLeft2 = DEFAULT_Y_OFFSET_LEFT2_HOR;
            xOffsetRight = DEFAULT_X_OFFSET_RIGHT_HOR;
            yOffsetRight = DEFAULT_Y_OFFSET_RIGHT_HOR;
        } else {
            xOffset = DEFAULT_X_OFFSET_NORM_HOR;
            yOffset = DEFAULT_Y_OFFSET_NORM_HOR;
        }

        saveConfig();
    }
}
