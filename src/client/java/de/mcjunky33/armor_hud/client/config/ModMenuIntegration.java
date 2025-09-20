package de.mcjunky33.armor_hud.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Set;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SimpleConfigScreen::new;
    }
}

class SimpleConfigScreen extends Screen {
    private final Screen parent;
    private final ArmorHudConfig config;
    private TextFieldWidget xOffsetField;
    private TextFieldWidget yOffsetField;

    // Sprachcodes für Deutsch
    private static final Set<String> GERMAN_LANGS = Set.of("de_de", "de_at", "de_ch", "de_li", "de_lu", "de");

    // Caches die aktuelle Sprache für das Menü
    private String currentLanguage;

    protected SimpleConfigScreen(Screen parent) {
        super(Text.literal("Armor HUD Configuration"));
        this.parent = parent;
        this.config = ArmorHudConfig.getInstance();
        this.currentLanguage = detectLanguage();
    }

    /**
     * Automatische Spracherkennung anhand der Client-Sprache,
     * sofern config.language auf "auto" steht.
     */
    private String detectLanguage() {
        String configLang = config.getLanguage();
        if (!"auto".equals(configLang)) {
            return configLang;
        }
        // Minecraft-Client-Sprache auslesen
        String mcLang = MinecraftClient.getInstance().options.language;
        if (mcLang == null) mcLang = "en_us";
        mcLang = mcLang.toLowerCase();
        if (GERMAN_LANGS.contains(mcLang)) {
            return "de";
        }
        return "en";
    }

    /**
     * Kurze Hilfsmethode für Übersetzungen.
     */
    private String tr(String key) {
        // DEUTSCHE TEXTE
        if ("de".equals(currentLanguage)) {
            return switch (key) {
                case "title" -> "Armor HUD Konfiguration";
                case "hud_visible" -> "HUD Sichtbar";
                case "durability_number" -> "Durability-Zahl";
                case "durability_bar" -> "Durability-Balken";
                case "darkmode" -> "Darkmode";
                case "orientation" -> "Ausrichtung";
                case "vertical" -> "Vertikal (untereinander)";
                case "horizontal" -> "Horizontal (nebeneinander)";
                case "x_minus" -> "X -10";
                case "x_plus" -> "X +10";
                case "y_minus" -> "Y -10";
                case "y_plus" -> "Y +10";
                case "defaults" -> "Standardwerte";
                case "done" -> "Fertig";
                case "offset_hint" -> "X = nach rechts, Y = nach unten";
                case "lang_button_de" -> "Sprache: Deutsch";
                case "lang_button_en" -> "Language: English";
                default -> key;
            };
        }
        // ENGLISCHE TEXTE (Standard)
        return switch (key) {
            case "title" -> "Armor HUD Configuration";
            case "hud_visible" -> "HUD Visible";
            case "durability_number" -> "Durability Number";
            case "durability_bar" -> "Durability Bar";
            case "darkmode" -> "Darkmode";
            case "orientation" -> "Orientation";
            case "vertical" -> "Vertical (stacked)";
            case "horizontal" -> "Horizontal (side by side)";
            case "x_minus" -> "X -10";
            case "x_plus" -> "X +10";
            case "y_minus" -> "Y -10";
            case "y_plus" -> "Y +10";
            case "defaults" -> "Defaults";
            case "done" -> "Done";
            case "offset_hint" -> "X = move right, Y = move down";
            case "lang_button_de" -> "Sprache: Deutsch";
            case "lang_button_en" -> "Language: English";
            default -> key;
        };
    }

    @Override
    protected void init() {
        int buttonWidth = 180;
        int smallButtonWidth = 50;
        int buttonHeight = 20;
        int fieldWidth = 60;
        int centerX = this.width / 2 - buttonWidth / 2;
        int currentY = 35;

        // SPRACH-UMSCHALTER (oben rechts) zeigt immer die aktuelle Sprache!
        int langBtnWidth = 120;
        String langBtnText = "de".equals(currentLanguage) ? tr("lang_button_de") : tr("lang_button_en");
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(langBtnText),
                        button -> {
                            if ("de".equals(currentLanguage)) {
                                config.setLanguage("en");
                                currentLanguage = "en";
                            } else {
                                config.setLanguage("de");
                                currentLanguage = "de";
                            }
                            // Menü komplett neu laden (inkl. neue Button-Callbacks!)
                            this.client.setScreen(new SimpleConfigScreen(this.parent));
                        })
                .dimensions(this.width - langBtnWidth - 8, 8, langBtnWidth, buttonHeight)
                .build());

        // Sichtbarkeit
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))),
                        button -> {
                            config.setVisible(!config.isVisible());
                            button.setMessage(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        // Durability-Zahl anzeigen
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                        button -> {
                            config.setShowDurabilityAsNumber(!config.isShowDurabilityAsNumber());
                            button.setMessage(Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        // Durability-Bar anzeigen
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                        button -> {
                            config.setShowDurabilityBar(!config.isShowDurabilityBar());
                            button.setMessage(Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        // Darkmode-Schalter
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                        button -> {
                            config.setDarkMode(!config.isDarkMode());
                            button.setMessage(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        // Ausrichtung Horizontal/Vertikal
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))),
                        button -> {
                            config.setVertical(!config.isVertical());
                            button.setMessage(Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        // X Offset mit -10 und +10 Buttons, klar beschriftet
        int fieldX = this.width / 2 - fieldWidth / 2;
        xOffsetField = new TextFieldWidget(this.textRenderer, fieldX, currentY, fieldWidth, buttonHeight, Text.literal(""));
        xOffsetField.setText(String.valueOf(config.getXOffset()));
        xOffsetField.setChangedListener(text -> {
            try {
                int value = Integer.parseInt(text);
                config.setXOffset(value);
            } catch (NumberFormatException ignored) {}
        });
        int labelX = fieldX - smallButtonWidth - 30;
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("x_minus")),
                        button -> {
                            config.setXOffset(config.getXOffset() - 10);
                            xOffsetField.setText(String.valueOf(config.getXOffset()));
                        })
                .dimensions(labelX, currentY, smallButtonWidth + 10, buttonHeight)
                .build());
        this.addDrawableChild(xOffsetField);
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("x_plus")),
                        button -> {
                            config.setXOffset(config.getXOffset() + 10);
                            xOffsetField.setText(String.valueOf(config.getXOffset()));
                        })
                .dimensions(fieldX + fieldWidth + 5, currentY, smallButtonWidth + 10, buttonHeight)
                .build());
        currentY += 30;

        // Y Offset mit -10 und +10 Buttons, klar beschriftet
        int yFieldX = this.width / 2 - fieldWidth / 2;
        yOffsetField = new TextFieldWidget(this.textRenderer, yFieldX, currentY, fieldWidth, buttonHeight, Text.literal(""));
        yOffsetField.setText(String.valueOf(config.getYOffset()));
        yOffsetField.setChangedListener(text -> {
            try {
                int value = Integer.parseInt(text);
                config.setYOffset(value);
            } catch (NumberFormatException ignored) {}
        });
        int yLabelX = yFieldX - smallButtonWidth - 30;
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("y_minus")),
                        button -> {
                            config.setYOffset(config.getYOffset() - 10);
                            yOffsetField.setText(String.valueOf(config.getYOffset()));
                        })
                .dimensions(yLabelX, currentY, smallButtonWidth + 10, buttonHeight)
                .build());
        this.addDrawableChild(yOffsetField);
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("y_plus")),
                        button -> {
                            config.setYOffset(config.getYOffset() + 10);
                            yOffsetField.setText(String.valueOf(config.getYOffset()));
                        })
                .dimensions(yFieldX + fieldWidth + 5, currentY, smallButtonWidth + 10, buttonHeight)
                .build());
        currentY += 30;

        // Done & Defaults
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("defaults")),
                        button -> {
                            config.resetToDefaults();
                            this.client.setScreen(new SimpleConfigScreen(this.parent));
                        })
                .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                .build());
        currentY += 30;

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(tr("done")),
                        button -> {
                            assert this.client != null;
                            this.client.setScreen(this.parent);
                        })
                .dimensions(centerX, this.height - 40, buttonWidth, buttonHeight)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawCenteredTextWithShadow(this.textRenderer, tr("title"), this.width / 2, 12, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(tr("offset_hint")), this.width / 2, 110, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);

        // "by Mcjunky33" unten links anzeigen
        context.drawTextWithShadow(this.textRenderer, "by Mcjunky33", 8, this.height - 18, 0x777777);
    }
}