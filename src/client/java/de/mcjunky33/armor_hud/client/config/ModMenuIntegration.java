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
        private TextFieldWidget boxSizeField, spacingField;
        private TextFieldWidget xOffsetLeft2Field, yOffsetLeft2Field, numberOffsetXLeftField, numberOffsetYLeftField;
        private TextFieldWidget xOffsetRightField, yOffsetRightField, numberOffsetXRightField, numberOffsetYRightField;
        private TextFieldWidget xOffsetField, yOffsetField, numberOffsetXField, numberOffsetYField;

        // --- Vertical (stacked) offsets ---
        private transient TextFieldWidget xOffsetVerticalField;
        private transient TextFieldWidget yOffsetVerticalField;
        private transient TextFieldWidget numberOffsetXVerticalField;
        private transient TextFieldWidget numberOffsetYVerticalField;



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
                    case "durability_number" -> "Zahl";
                    case "durability_off" -> "Aus";
                    case "durability_percent" -> "Prozent";
                    case "durability_mode" -> "Durability Nummern";
                    case "durability_bar_on" -> "Durability Bar: An";
                    case "durability_bar_off" -> "Durability Bar: Aus";
                    case "darkmode" -> "Darkmode";
                    case "orientation" -> "Ausrichtung";
                    case "vertical" -> "Vertikal (untereinander)";
                    case "horizontal" -> "Horizontal (nebeneinander)";
                    case "splitmode_on" -> "Split-Modus: An";
                    case "splitmode_off" -> "Split-Modus: Aus";
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
                    case "left2_coords" -> "Helm+Brust";
                    case "right_coords" -> "Hose+Schuhe";
                    case "number_offset_left" -> "Nummern Helm+Brust";
                    case "number_offset_right" -> "Nummern Hose+Schuhe";
                    case "show_box_texture_on" -> "Box-Textur: An";
                    case "show_box_texture_off" -> "Box-Textur: Aus";
                    case "hand_durability_on" -> "Zeige Hand Item Durability: An";
                    case "hand_durability_off" -> "Zeige Hand Item Durability: Aus";
                    default -> key;
                };
            }
            return switch (key) {
                case "title" -> "Armor HUD Configuration";
                case "hud_visible" -> "HUD Visible";
                case "durability_number" -> "Number";
                case "durability_off" -> "Off";
                case "durability_percent" -> "Percent";
                case "durability_mode" -> "Durability Numbers";
                case "durability_bar_on" -> "Durability Bar: On";
                case "durability_bar_off" -> "Durability Bar: Off";
                case "darkmode" -> "Darkmode";
                case "orientation" -> "Orientation";
                case "vertical" -> "Vertical (stacked)";
                case "horizontal" -> "Horizontal (side by side)";
                case "splitmode_on" -> "Split Mode: On";
                case "splitmode_off" -> "Split Mode: Off";
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
                case "left2_coords" -> "Helmet+Chestplate";
                case "right_coords" -> "Leggings+Boots";
                case "number_offset_left" -> "Numbers Helmet+Chestplate";
                case "number_offset_right" -> "Numbers Leggings+Boots";
                case "show_box_texture_on" -> "Box Texture: On";
                case "show_box_texture_off" -> "Box Texture: Off";
                case "hand_durability_on" -> "Show Hand Item Durability: On";
                case "hand_durability_off" -> "Show Hand Item Durability: Off";
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

// SHOW HUD BUTTON
            this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                    btn -> {
                        config.setVisible(!config.isVisible());
                        btn.setMessage(Text.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                        this.client.setScreen(new SimpleConfigScreen(this.parent));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;

// DURABILITY NUMBER / PERCENT / OFF BUTTON
            String[] durabilityModes = {tr("durability_off"), tr("durability_number"), tr("durability_percent")};
            int mode = config.getDurabilityDisplayMode();
            ButtonWidget durabilityModeButton = ButtonWidget.builder(
                    Text.literal(tr("durability_mode") + ": " + durabilityModes[mode]),
                    btn -> {
                        int newMode = (config.getDurabilityDisplayMode() + 1) % 3; // 0 = off, 1 = number, 2 = percent
                        config.setDurabilityDisplayMode(newMode);
                        btn.setMessage(Text.literal(tr("durability_mode") + ": " + durabilityModes[newMode]));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build();
            this.addDrawableChild(durabilityModeButton);
            toggleY += toggleStepY;

// DURABILITY BAR ON/OFF BUTTON
            ButtonWidget durabilityBarButton = ButtonWidget.builder(
                    Text.literal(config.isShowDurabilityBar() ? tr("durability_bar_on") : tr("durability_bar_off")),
                    btn -> {
                        boolean newState = !config.isShowDurabilityBar();
                        config.setShowDurabilityBar(newState);
                        btn.setMessage(Text.literal(newState ? tr("durability_bar_on") : tr("durability_bar_off")));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build();
            this.addDrawableChild(durabilityBarButton);
            toggleY += toggleStepY;


// SHOW BOX TEXTURE
            String showBoxTextMsg = config.isShowBoxTexture() ? tr("show_box_texture_on") : tr("show_box_texture_off");
            this.addDrawableChild(ButtonWidget.builder(Text.literal(showBoxTextMsg),
                    btn -> {
                        config.setShowBoxTexture(!config.isShowBoxTexture());
                        btn.setMessage(Text.literal(config.isShowBoxTexture() ? tr("show_box_texture_on") : tr("show_box_texture_off")));
                        this.client.setScreen(new SimpleConfigScreen(this.parent));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;

// DARKMODE nur anzeigen wenn showBoxTexture aktiv
            if (config.isShowBoxTexture()) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                        btn -> {
                            config.setDarkMode(!config.isDarkMode());
                            btn.setMessage(Text.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                        }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
                toggleY += toggleStepY;
            }

// ORIENTATION
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))),
                    btn -> {
                        boolean newVertical = !config.isVertical();
                        config.setVertical(newVertical);
                        btn.setMessage(Text.literal(tr("orientation") + ": " + (newVertical ? tr("vertical") : tr("horizontal"))));

                        // Alle alten Offset-Widgets entfernen und neu init
                        this.children().removeIf(child ->
                                child instanceof TextFieldWidget || child instanceof ButtonWidget &&
                                        ((ButtonWidget) child).getMessage().getString().isEmpty()
                        );
                        this.init();
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;

// SPLITMODE
            this.addDrawableChild(ButtonWidget.builder(Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")),
                    btn -> {
                        config.setSplitMode(!config.isSplitMode());
                        btn.setMessage(Text.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")));
                        this.client.setScreen(new SimpleConfigScreen(this.parent));
                    }).dimensions(toggleStartX, toggleY, btnW, btnH).build());
            toggleY += toggleStepY;

// SHOW HAND DURABILITY
            this.addDrawableChild(ButtonWidget.builder(Text.literal(config.isShowHandDurability() ? tr("hand_durability_on") : tr("hand_durability_off")),
                    btn -> {
                        boolean newState = !config.isShowHandDurability();
                        config.setShowHandDurability(newState);
                        btn.setMessage(Text.literal(newState ? tr("hand_durability_on") : tr("hand_durability_off")));
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
            int rightFieldStartX = adjustX + arrowBtn + 4 + fieldW + 4 + arrowBtn + marginX;

            if (config.isSplitMode()) {
                // Helmet+Chestplate (links oben)
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

                // Nummern-Offset Helm+Brust
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setNumberOffsetXLeft(config.getNumberOffsetXLeft() - 2);
                            numberOffsetXLeftField.setText(String.valueOf(config.getNumberOffsetXLeft()));
                        }).dimensions(rightFieldStartX, coordY, arrowBtn, btnH).build());
                numberOffsetXLeftField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                numberOffsetXLeftField.setText(String.valueOf(config.getNumberOffsetXLeft()));
                numberOffsetXLeftField.setChangedListener(txt -> {
                    try { config.setNumberOffsetXLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(numberOffsetXLeftField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setNumberOffsetXLeft(config.getNumberOffsetXLeft() + 2);
                            numberOffsetXLeftField.setText(String.valueOf(config.getNumberOffsetXLeft()));
                        }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setNumberOffsetYLeft(config.getNumberOffsetYLeft() - 2);
                            numberOffsetYLeftField.setText(String.valueOf(config.getNumberOffsetYLeft()));
                        }).dimensions(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build());
                numberOffsetYLeftField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                numberOffsetYLeftField.setText(String.valueOf(config.getNumberOffsetYLeft()));
                numberOffsetYLeftField.setChangedListener(txt -> {
                    try { config.setNumberOffsetYLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(numberOffsetYLeftField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setNumberOffsetYLeft(config.getNumberOffsetYLeft() + 2);
                            numberOffsetYLeftField.setText(String.valueOf(config.getNumberOffsetYLeft()));
                        }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Beschriftung links
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("left2_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("number_offset_left")),
                        btn -> {}).dimensions(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());

                // Right (Leggings+Boots)
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

                // Nummern-Offset Right
                this.addDrawableChild(ButtonWidget.builder(Text.literal("←"),
                        btn -> {
                            config.setNumberOffsetXRight(config.getNumberOffsetXRight() - 2);
                            numberOffsetXRightField.setText(String.valueOf(config.getNumberOffsetXRight()));
                        }).dimensions(rightFieldStartX, coordY, arrowBtn, btnH).build());
                numberOffsetXRightField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                numberOffsetXRightField.setText(String.valueOf(config.getNumberOffsetXRight()));
                numberOffsetXRightField.setChangedListener(txt -> {
                    try { config.setNumberOffsetXRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(numberOffsetXRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("→"),
                        btn -> {
                            config.setNumberOffsetXRight(config.getNumberOffsetXRight() + 2);
                            numberOffsetXRightField.setText(String.valueOf(config.getNumberOffsetXRight()));
                        }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↑"),
                        btn -> {
                            config.setNumberOffsetYRight(config.getNumberOffsetYRight() - 2);
                            numberOffsetYRightField.setText(String.valueOf(config.getNumberOffsetYRight()));
                        }).dimensions(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build());
                numberOffsetYRightField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                numberOffsetYRightField.setText(String.valueOf(config.getNumberOffsetYRight()));
                numberOffsetYRightField.setChangedListener(txt -> {
                    try { config.setNumberOffsetYRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addDrawableChild(numberOffsetYRightField);
                this.addDrawableChild(ButtonWidget.builder(Text.literal("↓"),
                        btn -> {
                            config.setNumberOffsetYRight(config.getNumberOffsetYRight() + 2);
                            numberOffsetYRightField.setText(String.valueOf(config.getNumberOffsetYRight()));
                        }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Beschriftung rechts
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("right_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("number_offset_right")),
                        btn -> {}).dimensions(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
            } else {

                // --- OFFSET CONTROLS (NON-SPLIT MODE, HORIZONTAL) ---
                if (!config.isVertical()) {
                    // X Offset Field
                    xOffsetField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                    xOffsetField.setText(String.valueOf(config.getXOffset()));
                    xOffsetField.setChangedListener(txt -> {
                        try { config.setXOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(xOffsetField);

                    // X Offset Buttons
                    ButtonWidget xDecBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setXOffset(config.getXOffset() - 10);
                        xOffsetField.setText(String.valueOf(config.getXOffset()));
                    }).dimensions(adjustX, coordY, arrowBtn, btnH).build();

                    ButtonWidget xIncBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setXOffset(config.getXOffset() + 10);
                        xOffsetField.setText(String.valueOf(config.getXOffset()));
                    }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addDrawableChild(xDecBtn);
                    this.addDrawableChild(xIncBtn);

                    // Y Offset Field
                    yOffsetField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                    yOffsetField.setText(String.valueOf(config.getYOffset()));
                    yOffsetField.setChangedListener(txt -> {
                        try { config.setYOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(yOffsetField);

                    // Y Offset Buttons
                    ButtonWidget yDecBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setYOffset(config.getYOffset() - 10);
                        yOffsetField.setText(String.valueOf(config.getYOffset()));
                    }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build();

                    ButtonWidget yIncBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setYOffset(config.getYOffset() + 10);
                        yOffsetField.setText(String.valueOf(config.getYOffset()));
                    }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addDrawableChild(yDecBtn);
                    this.addDrawableChild(yIncBtn);

                    // Number X Offset Field + Buttons
                    numberOffsetXField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                    numberOffsetXField.setText(String.valueOf(config.getNumberOffsetX()));
                    numberOffsetXField.setChangedListener(txt -> {
                        try { config.setNumberOffsetX(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(numberOffsetXField);

                    ButtonWidget numXDecBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setNumberOffsetX(config.getNumberOffsetX() - 2);
                        numberOffsetXField.setText(String.valueOf(config.getNumberOffsetX()));
                    }).dimensions(rightFieldStartX, coordY, arrowBtn, btnH).build();

                    ButtonWidget numXIncBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setNumberOffsetX(config.getNumberOffsetX() + 2);
                        numberOffsetXField.setText(String.valueOf(config.getNumberOffsetX()));
                    }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addDrawableChild(numXDecBtn);
                    this.addDrawableChild(numXIncBtn);

                    // Number Y Offset Field + Buttons
                    numberOffsetYField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                    numberOffsetYField.setText(String.valueOf(config.getNumberOffsetY()));
                    numberOffsetYField.setChangedListener(txt -> {
                        try { config.setNumberOffsetY(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(numberOffsetYField);

                    ButtonWidget numYDecBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setNumberOffsetY(config.getNumberOffsetY() - 2);
                        numberOffsetYField.setText(String.valueOf(config.getNumberOffsetY()));
                    }).dimensions(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build();

                    ButtonWidget numYIncBtn = ButtonWidget.builder(Text.literal(""), btn -> {
                        config.setNumberOffsetY(config.getNumberOffsetY() + 2);
                        numberOffsetYField.setText(String.valueOf(config.getNumberOffsetY()));
                    }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addDrawableChild(numYDecBtn);
                    this.addDrawableChild(numYIncBtn);

                    // Update Arrow Buttons
                    updateArrowButtons(xDecBtn, xIncBtn, yDecBtn, yIncBtn, numXDecBtn, numXIncBtn, numYDecBtn, numYIncBtn);
                }

                if (config.isVertical()) {
                    // --- OFFSET CONTROLS (NON-SPLIT MODE, VERTICAL / STACKED) ---

                    // X Offset Field
                    xOffsetVerticalField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                    xOffsetVerticalField.setText(String.valueOf(config.getXOffsetLeftVertical()));
                    xOffsetVerticalField.setChangedListener(txt -> {
                        try { config.setXOffsetLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(xOffsetVerticalField);

                    // X Offset Buttons (Horizontal Movement: ← / →)
                    ButtonWidget xDecVBtn = ButtonWidget.builder(Text.literal("←"), btn -> {
                        config.setXOffsetLeftVertical(config.getXOffsetLeftVertical() - 10);
                        xOffsetVerticalField.setText(String.valueOf(config.getXOffsetLeftVertical()));
                    }).dimensions(adjustX, coordY, arrowBtn, btnH).build();

                    ButtonWidget xIncVBtn = ButtonWidget.builder(Text.literal("→"), btn -> {
                        config.setXOffsetLeftVertical(config.getXOffsetLeftVertical() + 10);
                        xOffsetVerticalField.setText(String.valueOf(config.getXOffsetLeftVertical()));
                    }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addDrawableChild(xDecVBtn);
                    this.addDrawableChild(xIncVBtn);

                    // Y Offset Field
                    yOffsetVerticalField = new TextFieldWidget(this.textRenderer, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                    yOffsetVerticalField.setText(String.valueOf(config.getYOffsetLeftVertical()));
                    yOffsetVerticalField.setChangedListener(txt -> {
                        try { config.setYOffsetLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(yOffsetVerticalField);

                    // Y Offset Buttons (Vertical Movement: ↑ / ↓)
                    ButtonWidget yDecVBtn = ButtonWidget.builder(Text.literal("↑"), btn -> {
                        config.setYOffsetLeftVertical(config.getYOffsetLeftVertical() - 10);
                        yOffsetVerticalField.setText(String.valueOf(config.getYOffsetLeftVertical()));
                    }).dimensions(adjustX, coordY + btnH + 2, arrowBtn, btnH).build();

                    ButtonWidget yIncVBtn = ButtonWidget.builder(Text.literal("↓"), btn -> {
                        config.setYOffsetLeftVertical(config.getYOffsetLeftVertical() + 10);
                        yOffsetVerticalField.setText(String.valueOf(config.getYOffsetLeftVertical()));
                    }).dimensions(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addDrawableChild(yDecVBtn);
                    this.addDrawableChild(yIncVBtn);

                    // Number X Offset Field + Buttons
                    numberOffsetXVerticalField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Text.literal(""));
                    numberOffsetXVerticalField.setText(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    numberOffsetXVerticalField.setChangedListener(txt -> {
                        try { config.setNumberOffsetXLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(numberOffsetXVerticalField);

                    ButtonWidget numXDecVBtn = ButtonWidget.builder(Text.literal("←"), btn -> {
                        config.setNumberOffsetXLeftVertical(config.getNumberOffsetXLeftVertical() - 2);
                        numberOffsetXVerticalField.setText(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    }).dimensions(rightFieldStartX, coordY, arrowBtn, btnH).build();

                    ButtonWidget numXIncVBtn = ButtonWidget.builder(Text.literal("→"), btn -> {
                        config.setNumberOffsetXLeftVertical(config.getNumberOffsetXLeftVertical() + 2);
                        numberOffsetXVerticalField.setText(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addDrawableChild(numXDecVBtn);
                    this.addDrawableChild(numXIncVBtn);

                    // Number Y Offset Field + Buttons
                    numberOffsetYVerticalField = new TextFieldWidget(this.textRenderer, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Text.literal(""));
                    numberOffsetYVerticalField.setText(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    numberOffsetYVerticalField.setChangedListener(txt -> {
                        try { config.setNumberOffsetYLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addDrawableChild(numberOffsetYVerticalField);

                    ButtonWidget numYDecVBtn = ButtonWidget.builder(Text.literal("↑"), btn -> {
                        config.setNumberOffsetYLeftVertical(config.getNumberOffsetYLeftVertical() - 2);
                        numberOffsetYVerticalField.setText(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    }).dimensions(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build();

                    ButtonWidget numYIncVBtn = ButtonWidget.builder(Text.literal("↓"), btn -> {
                        config.setNumberOffsetYLeftVertical(config.getNumberOffsetYLeftVertical() + 2);
                        numberOffsetYVerticalField.setText(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    }).dimensions(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addDrawableChild(numYDecVBtn);
                    this.addDrawableChild(numYIncVBtn);
                }

                // Beschriftungsbuttons (auch wenn Splitmode aus!)
                coordY += btnH * 2 + 8;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("left2_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("number_offset_left")),
                        btn -> {}).dimensions(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                coordY += btnH + 6;
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("right_coords")),
                        btn -> {}).dimensions(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addDrawableChild(ButtonWidget.builder(Text.literal(tr("number_offset_right")),
                        btn -> {}).dimensions(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
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
            // Blur zuerst
            super.render(context, mouseX, mouseY, delta);
            // Preview nun im Vordergrund!
            if (config.isVisible()) {
                ArmorHudOverlay.renderPreview(context, this.width, this.height, config);
            }
            context.drawCenteredTextWithShadow(this.textRenderer, tr(""), this.width / 2, 12, 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(tr("")), this.width / 2, 110, 0xAAAAAA);
            context.drawTextWithShadow(this.textRenderer, "", 8, this.height - 18, 0x777777);
        }
        private void updateArrowButtons(ButtonWidget xDec, ButtonWidget xInc, ButtonWidget yDec, ButtonWidget yInc,
                                        ButtonWidget numXDec, ButtonWidget numXInc, ButtonWidget numYDec, ButtonWidget numYInc) {
            if (config.isVertical()) {
                // Vertical / Stacked Mode
                // X = horizontal Verschiebung (links/rechts)
                xDec.setMessage(Text.literal("←"));
                xInc.setMessage(Text.literal("→"));
                numXDec.setMessage(Text.literal("←"));
                numXInc.setMessage(Text.literal("→"));

                // Y = vertikale Verschiebung (oben/unten)
                yDec.setMessage(Text.literal("↑"));
                yInc.setMessage(Text.literal("↓"));
                numYDec.setMessage(Text.literal("↑"));
                numYInc.setMessage(Text.literal("↓"));
            } else {
                // Horizontal Mode
                // X = horizontal Verschiebung (links/rechts)
                xDec.setMessage(Text.literal("←"));
                xInc.setMessage(Text.literal("→"));
                numXDec.setMessage(Text.literal("←"));
                numXInc.setMessage(Text.literal("→"));

                // Y = vertikale Verschiebung (oben/unten)
                yDec.setMessage(Text.literal("↑"));
                yInc.setMessage(Text.literal("↓"));
                numYDec.setMessage(Text.literal("↑"));
                numYInc.setMessage(Text.literal("↓"));
            }
        }
    }
}