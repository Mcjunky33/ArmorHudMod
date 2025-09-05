package de.mcjunky33.armor_hud.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import de.mcjunky33.armor_hud.client.ArmorHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import java.util.Set;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new SimpleConfigScreen(parent);
    }

    // ---- Hier beginnt die SimpleConfigScreen Klasse ----
    public static class SimpleConfigScreen extends Screen {
        private final Screen parent;
        private final ArmorHudConfig config;

        private TextFieldWidget xOffsetField, yOffsetField, boxSizeField, spacingField;
        private TextFieldWidget xOffsetLeftField, yOffsetLeftField, xOffsetRightField, yOffsetRightField;

        private static final Set<String> GERMAN_LANGS = Set.of("de_de", "de_at", "de_ch", "de_li", "de_lu", "de");
        private String currentLanguage;

        public SimpleConfigScreen(Screen parent) {
            super(Text.literal("Armor HUD Configuration"));
            this.parent = parent;
            this.config = ArmorHudConfig.getInstance();
            this.currentLanguage = detectLanguage();
        }

        private String detectLanguage() {
            String configLang = config.getLanguage();
            if (!"auto".equals(configLang)) return configLang;
            String mcLang = MinecraftClient.getInstance().options.language;
            if (mcLang == null) mcLang = "en_us";
            mcLang = mcLang.toLowerCase();
            return GERMAN_LANGS.contains(mcLang) ? "de" : "en";
        }

        private String tr(String key) {
            if ("de".equals(currentLanguage)) {
                return switch (key) {
                    case "title" -> "Armor HUD Konfiguration";
                    case "config_tab" -> "Konfiguration";
                    case "hud_visible" -> "HUD Sichtbar";
                    case "durability_number" -> "Durability-Zahl";
                    case "durability_bar" -> "Durability-Balken";
                    case "darkmode" -> "Darkmode";
                    case "orientation" -> "Ausrichtung";
                    case "vertical" -> "Vertikal (untereinander)";
                    case "horizontal" -> "Horizontal (nebeneinander)";
                    case "splitmode_on" -> "Split-Modus: AN";
                    case "splitmode_off" -> "Split-Modus: AUS";
                    case "box_size" -> "Box-Größe";
                    case "box_size_minus" -> "Box -2";
                    case "box_size_plus" -> "Box +2";
                    case "spacing" -> "Abstand";
                    case "spacing_minus" -> "Abstand -1";
                    case "spacing_plus" -> "Abstand +1";
                    case "defaults" -> "Standardwerte";
                    case "done" -> "Fertig";
                    case "offset_hint" -> "X = nach rechts, Y = nach unten";
                    case "lang_button_de" -> "Sprache: Deutsch";
                    case "lang_button_en" -> "Language: English";
                    case "left_coords" -> "Links";
                    case "right_coords" -> "Rechts";
                    default -> key;
                };
            }
            return switch (key) {
                case "title" -> "Armor HUD Configuration";
                case "config_tab" -> "Configuration";
                case "hud_visible" -> "HUD Visible";
                case "durability_number" -> "Durability Number";
                case "durability_bar" -> "Durability Bar";
                case "darkmode" -> "Darkmode";
                case "orientation" -> "Orientation";
                case "vertical" -> "Vertical (stacked)";
                case "horizontal" -> "Horizontal (side by side)";
                case "splitmode_on" -> "Split Mode: ON";
                case "splitmode_off" -> "Split Mode: OFF";
                case "box_size" -> "Box Size";
                case "box_size_minus" -> "Box -2";
                case "box_size_plus" -> "Box +2";
                case "spacing" -> "Spacing";
                case "spacing_minus" -> "Spacing -1";
                case "spacing_plus" -> "Spacing +1";
                case "defaults" -> "Defaults";
                case "done" -> "Done";
                case "offset_hint" -> "X = move right, Y = move down";
                case "lang_button_de" -> "Sprache: Deutsch";
                case "lang_button_en" -> "Language: English";
                case "left_coords" -> "Left";
                case "right_coords" -> "Right";
                default -> key;
            };
        }

        @Override
        protected void init() {
            int buttonWidth = 180;
            int smallButtonWidth = 50;
            int buttonHeight = 20;
            int fieldWidth = 40;
            int arrowBtn = 22;
            int centerX = this.width / 2 - buttonWidth / 2;
            int currentY = 35;

            // Sprachumschalter
            int langBtnWidth = 120;
            String langBtnText = "de".equals(currentLanguage) ? tr("lang_button_de") : tr("lang_button_en");
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(langBtnText),
                            button -> {
                                config.setLanguage("de".equals(currentLanguage) ? "en" : "de");
                                this.client.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .dimensions(this.width - langBtnWidth - 20, 8, langBtnWidth, buttonHeight)
                    .build());

            // -------- CONFIG TAB --------
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))),
                            button -> {
                                config.setVisible(!config.isVisible());
                                button.setMessage(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                            button -> {
                                config.setShowDurabilityAsNumber(!config.isShowDurabilityAsNumber());
                                button.setMessage(Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                            button -> {
                                config.setShowDurabilityBar(!config.isShowDurabilityBar());
                                button.setMessage(Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                            button -> {
                                config.setDarkMode(!config.isDarkMode());
                                button.setMessage(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))),
                            button -> {
                                config.setVertical(!config.isVertical());
                                button.setMessage(Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")),
                            button -> {
                                config.setSplitMode(!config.isSplitMode());
                                button.setMessage(Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")));
                                this.client.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;

            // BoxSize Block (-2/+2)
            int boxSizeBlockWidth = smallButtonWidth + fieldWidth + smallButtonWidth + 20;
            int boxSizeX = this.width / 2 - boxSizeBlockWidth / 2;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("box_size_minus")), button -> {
                config.setBoxSize(Math.max(16, config.getBoxSize() - 2));
                boxSizeField.setText(String.valueOf(config.getBoxSize()));
            }).dimensions(boxSizeX, currentY, smallButtonWidth, buttonHeight).build());
            boxSizeField = new TextFieldWidget(this.textRenderer, boxSizeX + smallButtonWidth + 10, currentY, fieldWidth, buttonHeight, Text.literal(""));
            boxSizeField.setText(String.valueOf(config.getBoxSize()));
            boxSizeField.setChangedListener(text -> {try {config.setBoxSize(Math.max(16, Integer.parseInt(text)));} catch (NumberFormatException ignored) {}} );
            this.addDrawableChild(boxSizeField);
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("box_size_plus")), button -> {
                config.setBoxSize(config.getBoxSize() + 2);
                boxSizeField.setText(String.valueOf(config.getBoxSize()));
            }).dimensions(boxSizeX + smallButtonWidth + 10 + fieldWidth + 10, currentY, smallButtonWidth, buttonHeight).build());
            currentY += 30;

            // Spacing Block (-1/+1)
            int spacingBlockWidth = smallButtonWidth + fieldWidth + smallButtonWidth + 20;
            int spacingX = this.width / 2 - spacingBlockWidth / 2;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("spacing_minus")), button -> {
                config.setSpacing(Math.max(0, config.getSpacing() - 1));
                spacingField.setText(String.valueOf(config.getSpacing()));
            }).dimensions(spacingX, currentY, smallButtonWidth, buttonHeight).build());
            spacingField = new TextFieldWidget(this.textRenderer, spacingX + smallButtonWidth + 10, currentY, fieldWidth, buttonHeight, Text.literal(""));
            spacingField.setText(String.valueOf(config.getSpacing()));
            spacingField.setChangedListener(text -> {try {config.setSpacing(Math.max(0, Integer.parseInt(text)));} catch (NumberFormatException ignored) {}} );
            this.addDrawableChild(spacingField);
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("spacing_plus")), button -> {
                config.setSpacing(config.getSpacing() + 1);
                spacingField.setText(String.valueOf(config.getSpacing()));
            }).dimensions(spacingX + smallButtonWidth + 10 + fieldWidth + 10, currentY, smallButtonWidth, buttonHeight).build());
            currentY += 30;

            // Koordinaten-Felder: Split oder Einzel-Modus
            if (config.isSplitMode()) {
                // Links
                int leftBlockWidth = arrowBtn * 2 + fieldWidth + 20;
                int leftBlockX = this.width / 2 - leftBlockWidth - 10;
                int fieldY = currentY;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"), btn -> {
                    config.setXOffsetLeft(config.getXOffsetLeft() - 10);
                    xOffsetLeftField.setText(String.valueOf(config.getXOffsetLeft()));
                }).dimensions(leftBlockX, fieldY, arrowBtn, buttonHeight).build());
                xOffsetLeftField = new TextFieldWidget(this.textRenderer, leftBlockX + arrowBtn + 5, fieldY, fieldWidth, buttonHeight, Text.literal(""));
                xOffsetLeftField.setText(String.valueOf(config.getXOffsetLeft()));
                xOffsetLeftField.setChangedListener(txt -> { try { config.setXOffsetLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {} });
                this.addDrawableChild(xOffsetLeftField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"), btn -> {
                    config.setXOffsetLeft(config.getXOffsetLeft() + 10);
                    xOffsetLeftField.setText(String.valueOf(config.getXOffsetLeft()));
                }).dimensions(leftBlockX + arrowBtn + 5 + fieldWidth + 5, fieldY, arrowBtn, buttonHeight).build());
                // Y Links
                fieldY += buttonHeight + 4;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"), btn -> {
                    config.setYOffsetLeft(config.getYOffsetLeft() - 10);
                    yOffsetLeftField.setText(String.valueOf(config.getYOffsetLeft()));
                }).dimensions(leftBlockX, fieldY, arrowBtn, buttonHeight).build());
                yOffsetLeftField = new TextFieldWidget(this.textRenderer, leftBlockX + arrowBtn + 5, fieldY, fieldWidth, buttonHeight, Text.literal(""));
                yOffsetLeftField.setText(String.valueOf(config.getYOffsetLeft()));
                yOffsetLeftField.setChangedListener(txt -> { try { config.setYOffsetLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {} });
                this.addDrawableChild(yOffsetLeftField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"), btn -> {
                    config.setYOffsetLeft(config.getYOffsetLeft() + 10);
                    yOffsetLeftField.setText(String.valueOf(config.getYOffsetLeft()));
                }).dimensions(leftBlockX + arrowBtn + 5 + fieldWidth + 5, fieldY, arrowBtn, buttonHeight).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("left_coords")), btn -> {}).dimensions(leftBlockX, fieldY + buttonHeight + 4, fieldWidth + arrowBtn*2 + 10, buttonHeight).build());

                // Rechts
                int rightBlockWidth = arrowBtn * 2 + fieldWidth + 20;
                int rightBlockX = this.width / 2 + 10;
                fieldY = currentY;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"), btn -> {
                    config.setXOffsetRight(config.getXOffsetRight() - 10);
                    xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                }).dimensions(rightBlockX, fieldY, arrowBtn, buttonHeight).build());
                xOffsetRightField = new TextFieldWidget(this.textRenderer, rightBlockX + arrowBtn + 5, fieldY, fieldWidth, buttonHeight, Text.literal(""));
                xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                xOffsetRightField.setChangedListener(txt -> { try { config.setXOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {} });
                this.addDrawableChild(xOffsetRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"), btn -> {
                    config.setXOffsetRight(config.getXOffsetRight() + 10);
                    xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                }).dimensions(rightBlockX + arrowBtn + 5 + fieldWidth + 5, fieldY, arrowBtn, buttonHeight).build());
                // Y Rechts
                fieldY += buttonHeight + 4;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"), btn -> {
                    config.setYOffsetRight(config.getYOffsetRight() - 10);
                    yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                }).dimensions(rightBlockX, fieldY, arrowBtn, buttonHeight).build());
                yOffsetRightField = new TextFieldWidget(this.textRenderer, rightBlockX + arrowBtn + 5, fieldY, fieldWidth, buttonHeight, Text.literal(""));
                yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                yOffsetRightField.setChangedListener(txt -> { try { config.setYOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {} });
                this.addDrawableChild(yOffsetRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"), btn -> {
                    config.setYOffsetRight(config.getYOffsetRight() + 10);
                    yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                }).dimensions(rightBlockX + arrowBtn + 5 + fieldWidth + 5, fieldY, arrowBtn, buttonHeight).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("right_coords")), btn -> {}).dimensions(rightBlockX, fieldY + buttonHeight + 4, fieldWidth + arrowBtn*2 + 10, buttonHeight).build());
                currentY += (buttonHeight + 4) * 2 + buttonHeight + 8;
            } else {
                // Zentral: X mit ←/→
                int xBlockWidth = arrowBtn*2 + fieldWidth + 20;
                int xBlockX = this.width / 2 - xBlockWidth / 2;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"), button -> {
                    config.setXOffset(config.getXOffset() - 10);
                    xOffsetField.setText(String.valueOf(config.getXOffset()));
                }).dimensions(xBlockX, currentY, arrowBtn, buttonHeight).build());
                xOffsetField = new TextFieldWidget(this.textRenderer, xBlockX + arrowBtn + 5, currentY, fieldWidth, buttonHeight, Text.literal(""));
                xOffsetField.setText(String.valueOf(config.getXOffset()));
                xOffsetField.setChangedListener(text -> {try {config.setXOffset(Integer.parseInt(text));} catch (NumberFormatException ignored) {} });
                this.addDrawableChild(xOffsetField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"), button -> {
                    config.setXOffset(config.getXOffset() + 10);
                    xOffsetField.setText(String.valueOf(config.getXOffset()));
                }).dimensions(xBlockX + arrowBtn + 5 + fieldWidth + 5, currentY, arrowBtn, buttonHeight).build());
                currentY += buttonHeight + 4;

                // Zentral: Y mit ↑/↓
                int yBlockWidth = arrowBtn*2 + fieldWidth + 20;
                int yBlockX = this.width / 2 - yBlockWidth / 2;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"), button -> {
                    config.setYOffset(config.getYOffset() - 10);
                    yOffsetField.setText(String.valueOf(config.getYOffset()));
                }).dimensions(yBlockX, currentY, arrowBtn, buttonHeight).build());
                yOffsetField = new TextFieldWidget(this.textRenderer, yBlockX + arrowBtn + 5, currentY, fieldWidth, buttonHeight, Text.literal(""));
                yOffsetField.setText(String.valueOf(config.getYOffset()));
                yOffsetField.setChangedListener(text -> {try {config.setYOffset(Integer.parseInt(text));} catch (NumberFormatException ignored) {} });
                this.addDrawableChild(yOffsetField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"), button -> {
                    config.setYOffset(config.getYOffset() + 10);
                    yOffsetField.setText(String.valueOf(config.getYOffset()));
                }).dimensions(yBlockX + arrowBtn + 5 + fieldWidth + 5, currentY, arrowBtn, buttonHeight).build());
                currentY += buttonHeight + 4;
            }

            // Defaults
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("defaults")),
                            button -> {
                                config.resetToDefaults();
                                this.client.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .dimensions(centerX, currentY, buttonWidth, buttonHeight)
                    .build());
            currentY += 30;

            // Done Button
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(tr("done")),
                            button -> {
                                assert this.client != null;
                                this.client.setScreen(this.parent);
                            })
                    .dimensions(this.width / 2 - buttonWidth / 2, this.height - 40, buttonWidth, buttonHeight)
                    .build());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawCenteredTextWithShadow(this.textRenderer, tr("title"), this.width / 2, 12, 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(tr("offset_hint")), this.width / 2, 110, 0xAAAAAA);

            super.render(context, mouseX, mouseY, delta);

            ArmorHudOverlay.renderPreview(context, this.width, this.height, config);

            context.drawTextWithShadow(this.textRenderer, "by Mcjunky33", 8, this.height - 18, 0x777777);
        }
    }
}