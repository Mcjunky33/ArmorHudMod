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

    public static class SimpleConfigScreen extends Screen {
        private final Screen parent;
        private final ArmorHudConfig config;

        private static final Set<String> GERMAN_LANGS = Set.of("de_de", "de_at", "de_ch", "de_li", "de_lu", "de");
        private String currentLanguage;

        // Felder für TextFieldWidgets
        private TextFieldWidget xOffsetField, yOffsetField, boxSizeField, spacingField;
        private TextFieldWidget xOffsetLeft1Field, yOffsetLeft1Field;
        private TextFieldWidget xOffsetLeft2Field, yOffsetLeft2Field;
        private TextFieldWidget xOffsetRightField, yOffsetRightField;

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
                    case "left1_coords" -> "Links (Stacked)";
                    case "left2_coords" -> "Links (Helm+Brust)";
                    case "right_coords" -> "Rechts (Hose+Schuhe)";
                    default -> key;
                };
            }
            return switch (key) {
                case "title" -> "Armor HUD Configuration";
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
                case "left1_coords" -> "Left (Stacked)";
                case "left2_coords" -> "Left (Helmet+Chestplate)";
                case "right_coords" -> "Right (Leggings+Boots)";
                default -> key;
            };
        }

        @Override
        protected void init() {
            this.clearChildren();

            int winW = this.width;
            int winH = this.height;
            int btnW = Math.max(90, Math.min(140, winW / 7));
            int btnH = Math.max(18, Math.min(24, winH / 32));
            int smallBtnW = Math.max(40, btnW / 2);
            int fieldW = Math.max(40, Math.min(58, winW / 14));
            int arrowBtn = Math.max(22, btnH);
            int marginY = Math.max(6, btnH / 2);
            int marginX = Math.max(8, winW / 64);

            // Language-Button ganz oben rechts
            int langBtnW = Math.max(110, btnW);
            int langBtnX = winW - langBtnW - marginX;
            int langBtnY = marginY;
            String langBtnText = "de".equals(currentLanguage) ? tr("lang_button_de") : tr("lang_button_en");
            this.addDrawableChild(ButtonWidget.builder(
                            Text.literal(langBtnText),
                            btn -> {
                                config.setLanguage("de".equals(currentLanguage) ? "en" : "de");
                                this.client.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .dimensions(langBtnX, langBtnY, langBtnW, btnH)
                    .build());

            // Toggle-Buttons: weiter nach unten, Abstand zum Language-Button
            int toggleStartX = langBtnX;
            int toggleY = langBtnY + btnH + marginY * 4;
            int toggleStepY = btnH + 2;

            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))),
                    btn -> {
                        config.setVisible(!config.isVisible());
                        btn.setMessage(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "Ja" : "Yes") : ("de".equals(currentLanguage) ? "Nein" : "No"))));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                    btn -> {
                        config.setShowDurabilityAsNumber(!config.isShowDurabilityAsNumber());
                        btn.setMessage(Text.literal(tr("durability_number") + ": " + (config.isShowDurabilityAsNumber() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                    btn -> {
                        config.setShowDurabilityBar(!config.isShowDurabilityBar());
                        btn.setMessage(Text.literal(tr("durability_bar") + ": " + (config.isShowDurabilityBar() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                    btn -> {
                        config.setDarkMode(!config.isDarkMode());
                        btn.setMessage(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))),
                    btn -> {
                        config.setVertical(!config.isVertical());
                        btn.setMessage(Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")),
                    btn -> {
                        config.setSplitMode(!config.isSplitMode());
                        btn.setMessage(Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")));
                        this.client.setScreen(new SimpleConfigScreen(this.parent));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());

            // Anpassung: Box/Spacing/Koordinaten links
            int adjustStartY = langBtnY;
            int adjustX = marginX;
            int adjustStepY = btnH + 8;
            int boxY = adjustStartY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("box_size_minus")),
                    btn -> {
                        config.setBoxSize(Math.max(16, config.getBoxSize() - 2));
                        boxSizeField.setText(String.valueOf(config.getBoxSize()));
                    }).dimensions(adjustX, boxY, smallBtnW, btnH).build());
            boxSizeField = new TextFieldWidget(this.textRenderer, adjustX + smallBtnW + 4, boxY, fieldW, btnH, Text.literal(""));
            boxSizeField.setText(String.valueOf(config.getBoxSize()));
            boxSizeField.setChangedListener(txt -> {
                try { config.setBoxSize(Math.max(16, Integer.parseInt(txt))); } catch (NumberFormatException ignored) {}
            });
            this.addDrawableChild(boxSizeField);
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("box_size_plus")),
                    btn -> {
                        config.setBoxSize(config.getBoxSize() + 2);
                        boxSizeField.setText(String.valueOf(config.getBoxSize()));
                    }).dimensions(adjustX + smallBtnW + 4 + fieldW + 4, boxY, smallBtnW, btnH).build());

            int spacingY = boxY + adjustStepY;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("spacing_minus")),
                    btn -> {
                        config.setSpacing(Math.max(0, config.getSpacing() - 1));
                        spacingField.setText(String.valueOf(config.getSpacing()));
                    }).dimensions(adjustX, spacingY, smallBtnW, btnH).build());
            spacingField = new TextFieldWidget(this.textRenderer, adjustX + smallBtnW + 4, spacingY, fieldW, btnH, Text.literal(""));
            spacingField.setText(String.valueOf(config.getSpacing()));
            spacingField.setChangedListener(txt -> {
                try { config.setSpacing(Math.max(0, Integer.parseInt(txt))); } catch (NumberFormatException ignored) {}
            });
            this.addDrawableChild(spacingField);
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("spacing_plus")),
                    btn -> {
                        config.setSpacing(config.getSpacing() + 1);
                        spacingField.setText(String.valueOf(config.getSpacing()));
                    }).dimensions(adjustX + smallBtnW + 4 + fieldW + 4, spacingY, smallBtnW, btnH).build());

            int coordY = spacingY + adjustStepY + 4;
            if (config.isVertical() && !config.isSplitMode()) {
                // Nur Stacked: xOffsetLeft1/yOffsetLeft1 für alle vier Armor-Items
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setXOffsetLeft1(config.getXOffsetLeft1() - 10);
                            xOffsetLeft1Field.setText(String.valueOf(config.getXOffsetLeft1()));
                        }).dimensions(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetLeft1Field = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                xOffsetLeft1Field.setText(String.valueOf(config.getXOffsetLeft1()));
                xOffsetLeft1Field.setChangedListener(txt -> {
                    try { config.setXOffsetLeft1(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(xOffsetLeft1Field);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setXOffsetLeft1(config.getXOffsetLeft1() + 10);
                            xOffsetLeft1Field.setText(String.valueOf(config.getXOffsetLeft1()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setYOffsetLeft1(config.getYOffsetLeft1() - 10);
                            yOffsetLeft1Field.setText(String.valueOf(config.getYOffsetLeft1()));
                        }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetLeft1Field = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                yOffsetLeft1Field.setText(String.valueOf(config.getYOffsetLeft1()));
                yOffsetLeft1Field.setChangedListener(txt -> {
                    try { config.setYOffsetLeft1(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(yOffsetLeft1Field);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setYOffsetLeft1(config.getYOffsetLeft1() + 10);
                            yOffsetLeft1Field.setText(String.valueOf(config.getYOffsetLeft1()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("left1_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
            } else if (config.isSplitMode()) {
                // Splitmode (egal ob vertical oder horizontal!)
                // Helm+Chestplate = xOffsetLeft2/yOffsetLeft2, Leggings+Boots = xOffsetRight/yOffsetRight
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setXOffsetLeft2(config.getXOffsetLeft2() - 10);
                            xOffsetLeft2Field.setText(String.valueOf(config.getXOffsetLeft2()));
                        }).dimensions(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetLeft2Field = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                xOffsetLeft2Field.setText(String.valueOf(config.getXOffsetLeft2()));
                xOffsetLeft2Field.setChangedListener(txt -> {
                    try { config.setXOffsetLeft2(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(xOffsetLeft2Field);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setXOffsetLeft2(config.getXOffsetLeft2() + 10);
                            xOffsetLeft2Field.setText(String.valueOf(config.getXOffsetLeft2()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setYOffsetLeft2(config.getYOffsetLeft2() - 10);
                            yOffsetLeft2Field.setText(String.valueOf(config.getYOffsetLeft2()));
                        }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetLeft2Field = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                yOffsetLeft2Field.setText(String.valueOf(config.getYOffsetLeft2()));
                yOffsetLeft2Field.setChangedListener(txt -> {
                    try { config.setYOffsetLeft2(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(yOffsetLeft2Field);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setYOffsetLeft2(config.getYOffsetLeft2() + 10);
                            yOffsetLeft2Field.setText(String.valueOf(config.getYOffsetLeft2()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("left2_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());

                coordY += btnH + 6;
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setXOffsetRight(config.getXOffsetRight() - 10);
                            xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                        }).dimensions(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetRightField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                xOffsetRightField.setChangedListener(txt -> {
                    try { config.setXOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(xOffsetRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setXOffsetRight(config.getXOffsetRight() + 10);
                            xOffsetRightField.setText(String.valueOf(config.getXOffsetRight()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setYOffsetRight(config.getYOffsetRight() - 10);
                            yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                        }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetRightField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                yOffsetRightField.setChangedListener(txt -> {
                    try { config.setYOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(yOffsetRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setYOffsetRight(config.getYOffsetRight() + 10);
                            yOffsetRightField.setText(String.valueOf(config.getYOffsetRight()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("right_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
            } else {
                // Normalmode horizontal
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setXOffset(config.getXOffset() - 10);
                            xOffsetField.setText(String.valueOf(config.getXOffset()));
                        }).dimensions(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                xOffsetField.setText(String.valueOf(config.getXOffset()));
                xOffsetField.setChangedListener(txt -> {
                    try { config.setXOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(xOffsetField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setXOffset(config.getXOffset() + 10);
                            xOffsetField.setText(String.valueOf(config.getXOffset()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setYOffset(config.getYOffset() - 10);
                            yOffsetField.setText(String.valueOf(config.getYOffset()));
                        }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                yOffsetField.setText(String.valueOf(config.getYOffset()));
                yOffsetField.setChangedListener(txt -> {
                    try { config.setYOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(yOffsetField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setYOffset(config.getYOffset() + 10);
                            yOffsetField.setText(String.valueOf(config.getYOffset()));
                        }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());
            }

            int midW = Math.max(btnW, 160);
            int midX = winW / 2 - midW / 2;
            int defY = winH - btnH * 2 - marginY * 3;
            int doneY = winH - btnH - marginY * 2;

            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("defaults")),
                    btn -> {
                        config.resetToDefaults();
                        this.client.setScreen(new SimpleConfigScreen(this.parent));
                    }).dimensions(midX, defY, midW, btnH).build());

            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("done")),
                    btn -> {
                        this.client.setScreen(this.parent);
                    }).dimensions(midX, doneY, midW, btnH).build());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawCenteredTextWithShadow(this.textRenderer, tr("title"), this.width / 2, 12, 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(tr("offset_hint")), this.width / 2, 110, 0xAAAAAA);

            if (config.isVisible()) {
                ArmorHudOverlay.renderPreview(context, this.width, this.height, config);
            }

            context.drawTextWithShadow(this.textRenderer, "by Mcjunky33", 8, this.height - 18, 0x777777);
            super.render(context, mouseX, mouseY, delta);
        }
    }
}