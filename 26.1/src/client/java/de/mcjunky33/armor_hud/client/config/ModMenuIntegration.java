package de.mcjunky33.armor_hud.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.mcjunky33.armor_hud.client.ArmorHudOverlay;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font;

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
        private EditBox boxSizeField, spacingField;
        private EditBox xOffsetLeft2Field, yOffsetLeft2Field, numberOffsetXLeftField, numberOffsetYLeftField;
        private EditBox xOffsetRightField, yOffsetRightField, numberOffsetXRightField, numberOffsetYRightField;
        private EditBox xOffsetField, yOffsetField, numberOffsetXField, numberOffsetYField;

        // --- Vertical (stacked) offsets ---
        private transient EditBox xOffsetVerticalField;
        private transient EditBox yOffsetVerticalField;
        private transient EditBox numberOffsetXVerticalField;
        private transient EditBox numberOffsetYVerticalField;



        public SimpleConfigScreen(Screen parent) {
            super(Component.literal("Armor HUD Configuration"));
            this.parent = parent;
            this.config = ArmorHudConfig.getInstance();
            this.currentLanguage = detectLanguage();
        }

        private String detectLanguage() {
            String configLang = config.getLanguage();
            if (!"auto".equals(configLang)) return configLang;
            String mcLang = Minecraft.getInstance().options.languageCode;
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
                    case "hand_durability_on" -> "Hand Item Durability: An";
                    case "hand_durability_off" -> "Hand Item Durability: Aus";
                    case "style_label" -> "Box textur";
                    case "style_hotbar" -> "Hotbar Textur";
                    case "style_offhand" -> "Zweithand Slots";
                    case "tooltip_lang" -> "Ändert die Sprache zu Englisch";
                    case "tooltip_hud_visible" -> "Schaltet die gesamte HUD-Anzeige im Spiel ein oder aus";
                    case "tooltip_durability_mode" -> "Wechselt zwischen: Aus, Haltbarkeit als Zahl oder als Prozent";
                    case "tooltip_durability_bar" -> "Zeigt oder versteckt den farbigen Haltbarkeitsbalken unter den Items";
                    case "tooltip_box_texture" -> "Zeigt einen Hintergrundkasten hinter den Rüstungsteilen an";
                    case "tooltip_darkmode" -> "Macht die Offhandbox Textur dunkel (statt hell)";
                    case "tooltip_orientation" -> "Wechselt zwischen vertikaler (gestapelt) und horizontaler Liste";
                    case "tooltip_splitmode" -> "Trennt die Anzeige nach links und rechts wenn aktiviert";
                    case "tooltip_hand_durability" -> "Zeigt auch die Haltbarkeit der Items in der Haupt- und Nebenhand";
                    case "tooltip_defaults" -> "Setzt alle Einstellungen auf die Standardwerte zurück";
                    case "tooltip_style_mode" -> "Wähle zwischen keinem Hintergrund, Zweithand Slots oder dem Hotbar-Look";
                    case "tooltip_durability_on" -> "Haltbarkeit im Tooltip: An";
                    case "tooltip_durability_off" -> "Haltbarkeit im Tooltip: Aus";
                    case "tooltip_always_show_tooltip" -> "Zeigt die Haltbarkeit immer im Item-Tooltip an";
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
                case "hand_durability_on" -> "Hand Item Durability: On";
                case "hand_durability_off" -> "Hand Item Durability: Off";
                case "style_label" -> "Box Texture";
                case "style_hotbar" -> "Hotbar Texture";
                case "style_offhand" -> "Offhand Slots";
                case "tooltip_lang" -> "Changes the language to German";
                case "tooltip_hud_visible" -> "Toggles the entire HUD display in-game";
                case "tooltip_durability_mode" -> "Switches between: Off, Durability as a number, or as a percentage";
                case "tooltip_durability_bar" -> "Shows or hides the colored durability bar under items";
                case "tooltip_box_texture" -> "Displays a background box behind the armor pieces";
                case "tooltip_darkmode" -> "Makes the offhandbox texture dark (instead of light)";
                case "tooltip_orientation" -> "Switches between vertical (stacked) and horizontal list";
                case "tooltip_splitmode" -> "Splits the display into left and right when this option is enabled";
                case "tooltip_hand_durability" -> "Also shows durability for main hand and offhand items";
                case "tooltip_defaults" -> "Resets all settings to their default values";
                case "tooltip_style_mode" -> "Choose between no background, offhand boxes, or the hotbar look";
                case "tooltip_durability_on" -> "Tooltip Durability: On";
                case "tooltip_durability_off" -> "Tooltip Durability: Off";
                case "tooltip_always_show_tooltip" -> "Always shows durability in the item tooltip";
                default -> key;
            };
        }

        @Override
        protected void init() {
            this.clearWidgets();

            int winW = this.width;
            int winH = this.height;
            int btnW = Math.max(90, Math.min(140, winW / 7));
            int btnH = Math.max(18, Math.min(24, winH / 32));
            int smallBtnW = Math.max(40, btnW / 2);
            int fieldW = Math.max(40, Math.min(58, winW / 14));
            int arrowBtn = Math.max(22, btnH);
            int marginY = Math.max(6, btnH / 2);
            int marginX = Math.max(8, winW / 64);

            int langBtnW = Math.max(110, btnW);
            int langBtnX = winW - langBtnW - marginX;
            int langBtnY = marginY;
            String langBtnText = "de".equals(currentLanguage) ? tr("lang_button_de") : tr("lang_button_en");
            this.addRenderableWidget(Button.builder(
                            Component.literal(langBtnText),

                            btn -> {
                                config.setLanguage("de".equals(currentLanguage) ? "en" : "de");
                                this.minecraft.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_lang"))))
                    .bounds(langBtnX, langBtnY, langBtnW, btnH)
                    .build());

            int toggleStartX = langBtnX;
            int toggleY = langBtnY + btnH + marginY * 4;
            int toggleStepY = btnH + 2;

            this.addRenderableWidget(Button.builder(
                            Component.literal(tr("hud_visible") + ": " + (config.isVisible() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                            btn -> {
                                config.setVisible(!config.isVisible());
                                this.minecraft.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_hud_visible"))))
                    .build());
            toggleY += toggleStepY;

            String[] styleModes = {tr("durability_off"), tr("style_offhand"), tr("style_hotbar")};
            int currentStyleIndex = 0;
            if (config.isShowBoxTexture()) {
                currentStyleIndex = config.isUseHotbarTexture() ? 2 : 1;
            }

            this.addRenderableWidget(Button.builder(
                            Component.literal(tr("style_label") + ": " + styleModes[currentStyleIndex]),
                            btn -> {
                                if (!config.isShowBoxTexture()) {
                                    config.setShowBoxTexture(true);
                                    config.setUseHotbarTexture(false);
                                } else if (!config.isUseHotbarTexture()) {
                                    config.setShowBoxTexture(true);
                                    config.setUseHotbarTexture(true);
                                } else {
                                    config.setShowBoxTexture(false);
                                    config.setUseHotbarTexture(false);
                                }
                                this.minecraft.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_style_mode"))))
                    .build());

            toggleY += toggleStepY;

            if (config.isShowBoxTexture() && !config.isUseHotbarTexture()) {
                this.addRenderableWidget(Button.builder(
                                Component.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))),
                                btn -> {
                                    config.setDarkMode(!config.isDarkMode());
                                    btn.setMessage(Component.literal(tr("darkmode") + ": " + (config.isDarkMode() ? ("de".equals(currentLanguage) ? "An" : "On") : ("de".equals(currentLanguage) ? "Aus" : "Off"))));
                                })
                        .bounds(toggleStartX, toggleY, btnW, btnH)
                        .tooltip(Tooltip.create(Component.literal(tr("tooltip_darkmode"))))
                        .build());

                toggleY += toggleStepY;
            }



            String[] durabilityModes = {tr("durability_off"), tr("durability_number"), tr("durability_percent")};
            this.addRenderableWidget(Button.builder(
                            Component.literal(tr("durability_mode") + ": " + durabilityModes[config.getDurabilityDisplayMode()]),
                            btn -> {
                                int newMode = (config.getDurabilityDisplayMode() + 1) % 3;
                                config.setDurabilityDisplayMode(newMode);
                                btn.setMessage(Component.literal(tr("durability_mode") + ": " + durabilityModes[newMode]));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_durability_mode"))))
                    .build());
            toggleY += toggleStepY;


            this.addRenderableWidget(Button.builder(
                            Component.literal(config.isShowDurabilityBar() ? tr("durability_bar_on") : tr("durability_bar_off")),
                            btn -> {
                                config.setShowDurabilityBar(!config.isShowDurabilityBar());
                                btn.setMessage(Component.literal(config.isShowDurabilityBar() ? tr("durability_bar_on") : tr("durability_bar_off")));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_durability_bar"))))
                    .build());
            toggleY += toggleStepY;


// ORIENTATION
            this.addRenderableWidget(Button.builder(
                            Component.literal(tr("orientation") + ": " + (config.isVertical() ? tr("vertical") : tr("horizontal"))),
                            btn -> {
                                config.setVertical(!config.isVertical());
                                this.init();
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_orientation"))))
                    .build());
            toggleY += toggleStepY;

// SPLITMODE
            this.addRenderableWidget(Button.builder(
                            Component.literal(config.isSplitMode() ? tr("splitmode_on") : tr("splitmode_off")),
                            btn -> {
                                config.setSplitMode(!config.isSplitMode());
                                this.minecraft.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_splitmode"))))
                    .build());
            toggleY += toggleStepY;

// SHOW HAND DURABILITY
            this.addRenderableWidget(Button.builder(
                            Component.literal(config.isShowHandDurability() ? tr("hand_durability_on") : tr("hand_durability_off")),
                            btn -> {
                                config.setShowHandDurability(!config.isShowHandDurability());
                                btn.setMessage(Component.literal(config.isShowHandDurability() ? tr("hand_durability_on") : tr("hand_durability_off")));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_hand_durability"))))
                    .build());

            toggleY += toggleStepY;

            this.addRenderableWidget(Button.builder(
                            Component.literal(config.isAlwaysShowTooltipDurability() ? tr("tooltip_durability_on") : tr("tooltip_durability_off")),
                            btn -> {
                                config.setAlwaysShowTooltipDurability(!config.isAlwaysShowTooltipDurability());
                                btn.setMessage(Component.literal(config.isAlwaysShowTooltipDurability() ? tr("tooltip_durability_on") : tr("tooltip_durability_off")));
                            })
                    .bounds(toggleStartX, toggleY, btnW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_always_show_tooltip"))))
                    .build());


            //Box/Spacing/Koordinaten links
            int adjustStartY = langBtnY;
            int adjustX = marginX;
            int adjustStepY = btnH + 8;
            int boxY = adjustStartY;
            this.addRenderableWidget(Button.builder(Component.literal(tr("box_size_minus")),
                    btn -> {
                        config.setBoxSize(Math.max(16, config.getBoxSize() - 2));
                        boxSizeField.setValue(String.valueOf(config.getBoxSize()));
                    }).bounds(adjustX, boxY, smallBtnW, btnH).build());
            boxSizeField = new EditBox(this.font, adjustX + smallBtnW + 4, boxY, fieldW, btnH, Component.literal(""));
            boxSizeField.setValue(String.valueOf(config.getBoxSize()));
            boxSizeField.setResponder(txt -> {
                try { config.setBoxSize(Math.max(16, Integer.parseInt(txt))); } catch (NumberFormatException ignored) {}
            });
            this.addRenderableWidget(boxSizeField);
            this.addRenderableWidget(Button.builder(Component.literal(tr("box_size_plus")),
                    btn -> {
                        config.setBoxSize(config.getBoxSize() + 2);
                        boxSizeField.setValue(String.valueOf(config.getBoxSize()));
                    }).bounds(adjustX + smallBtnW + 4 + fieldW + 4, boxY, smallBtnW, btnH).build());

            int spacingY = boxY + adjustStepY;
            this.addRenderableWidget(Button.builder(Component.literal(tr("spacing_minus")),
                    btn -> {
                        config.setSpacing(Math.max(0, config.getSpacing() - 1));
                        spacingField.setValue(String.valueOf(config.getSpacing()));
                    }).bounds(adjustX, spacingY, smallBtnW, btnH).build());
            spacingField = new EditBox(this.font, adjustX + smallBtnW + 4, spacingY, fieldW, btnH, Component.literal(""));
            spacingField.setValue(String.valueOf(config.getSpacing()));
            spacingField.setResponder(txt -> {
                try { config.setSpacing(Math.max(0, Integer.parseInt(txt))); } catch (NumberFormatException ignored) {}
            });
            this.addRenderableWidget(spacingField);
            this.addRenderableWidget(Button.builder(Component.literal(tr("spacing_plus")),
                    btn -> {
                        config.setSpacing(config.getSpacing() + 1);
                        spacingField.setValue(String.valueOf(config.getSpacing()));
                    }).bounds(adjustX + smallBtnW + 4 + fieldW + 4, spacingY, smallBtnW, btnH).build());

            int coordY = spacingY + adjustStepY + 4;
            int rightFieldStartX = adjustX + arrowBtn + 4 + fieldW + 4 + arrowBtn + marginX;

            if (config.isSplitMode()) {
                // Helmet+Chestplate (links oben)
                this.addRenderableWidget(Button.builder(Component.literal("←"),
                        btn -> {
                            config.setXOffsetLeft2(config.getXOffsetLeft2() - 10);
                            xOffsetLeft2Field.setValue(String.valueOf(config.getXOffsetLeft2()));
                        }).bounds(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetLeft2Field = new EditBox(this.font, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                xOffsetLeft2Field.setValue(String.valueOf(config.getXOffsetLeft2()));
                xOffsetLeft2Field.setResponder(txt -> {
                    try { config.setXOffsetLeft2(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(xOffsetLeft2Field);
                this.addRenderableWidget(Button.builder(Component.literal("→"),
                        btn -> {
                            config.setXOffsetLeft2(config.getXOffsetLeft2() + 10);
                            xOffsetLeft2Field.setValue(String.valueOf(config.getXOffsetLeft2()));
                        }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal("↑"),
                        btn -> {
                            config.setYOffsetLeft2(config.getYOffsetLeft2() - 10);
                            yOffsetLeft2Field.setValue(String.valueOf(config.getYOffsetLeft2()));
                        }).bounds(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetLeft2Field = new EditBox(this.font, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                yOffsetLeft2Field.setValue(String.valueOf(config.getYOffsetLeft2()));
                yOffsetLeft2Field.setResponder(txt -> {
                    try { config.setYOffsetLeft2(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(yOffsetLeft2Field);
                this.addRenderableWidget(Button.builder(Component.literal("↓"),
                        btn -> {
                            config.setYOffsetLeft2(config.getYOffsetLeft2() + 10);
                            yOffsetLeft2Field.setValue(String.valueOf(config.getYOffsetLeft2()));
                        }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Nummern-Offset Helm+Brust
                this.addRenderableWidget(Button.builder(Component.literal("←"),
                        btn -> {
                            config.setNumberOffsetXLeft(config.getNumberOffsetXLeft() - 2);
                            numberOffsetXLeftField.setValue(String.valueOf(config.getNumberOffsetXLeft()));
                        }).bounds(rightFieldStartX, coordY, arrowBtn, btnH).build());
                numberOffsetXLeftField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                numberOffsetXLeftField.setValue(String.valueOf(config.getNumberOffsetXLeft()));
                numberOffsetXLeftField.setResponder(txt -> {
                    try { config.setNumberOffsetXLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(numberOffsetXLeftField);
                this.addRenderableWidget(Button.builder(Component.literal("→"),
                        btn -> {
                            config.setNumberOffsetXLeft(config.getNumberOffsetXLeft() + 2);
                            numberOffsetXLeftField.setValue(String.valueOf(config.getNumberOffsetXLeft()));
                        }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal("↑"),
                        btn -> {
                            config.setNumberOffsetYLeft(config.getNumberOffsetYLeft() - 2);
                            numberOffsetYLeftField.setValue(String.valueOf(config.getNumberOffsetYLeft()));
                        }).bounds(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build());
                numberOffsetYLeftField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                numberOffsetYLeftField.setValue(String.valueOf(config.getNumberOffsetYLeft()));
                numberOffsetYLeftField.setResponder(txt -> {
                    try { config.setNumberOffsetYLeft(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(numberOffsetYLeftField);
                this.addRenderableWidget(Button.builder(Component.literal("↓"),
                        btn -> {
                            config.setNumberOffsetYLeft(config.getNumberOffsetYLeft() + 2);
                            numberOffsetYLeftField.setValue(String.valueOf(config.getNumberOffsetYLeft()));
                        }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Beschriftung links
                coordY += btnH * 2 + 8;
                this.addRenderableWidget(Button.builder(Component.literal(tr("left2_coords")),
                        btn -> {}).bounds(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal(tr("number_offset_left")),
                        btn -> {}).bounds(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());

                // Right (Leggings+Boots)
                coordY += btnH + 6;
                this.addRenderableWidget(Button.builder(Component.literal("←"),
                        btn -> {
                            config.setXOffsetRight(config.getXOffsetRight() - 10);
                            xOffsetRightField.setValue(String.valueOf(config.getXOffsetRight()));
                        }).bounds(adjustX, coordY, arrowBtn, btnH).build());
                xOffsetRightField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                xOffsetRightField.setValue(String.valueOf(config.getXOffsetRight()));
                xOffsetRightField.setResponder(txt -> {
                    try { config.setXOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(xOffsetRightField);
                this.addRenderableWidget(Button.builder(Component.literal("→"),
                        btn -> {
                            config.setXOffsetRight(config.getXOffsetRight() + 10);
                            xOffsetRightField.setValue(String.valueOf(config.getXOffsetRight()));
                        }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal("↑"),
                        btn -> {
                            config.setYOffsetRight(config.getYOffsetRight() - 10);
                            yOffsetRightField.setValue(String.valueOf(config.getYOffsetRight()));
                        }).bounds(adjustX, coordY + btnH + 2, arrowBtn, btnH).build());
                yOffsetRightField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                yOffsetRightField.setValue(String.valueOf(config.getYOffsetRight()));
                yOffsetRightField.setResponder(txt -> {
                    try { config.setYOffsetRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(yOffsetRightField);
                this.addRenderableWidget(Button.builder(Component.literal("↓"),
                        btn -> {
                            config.setYOffsetRight(config.getYOffsetRight() + 10);
                            yOffsetRightField.setValue(String.valueOf(config.getYOffsetRight()));
                        }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Nummern-Offset Right
                this.addRenderableWidget(Button.builder(Component.literal("←"),
                        btn -> {
                            config.setNumberOffsetXRight(config.getNumberOffsetXRight() - 2);
                            numberOffsetXRightField.setValue(String.valueOf(config.getNumberOffsetXRight()));
                        }).bounds(rightFieldStartX, coordY, arrowBtn, btnH).build());
                numberOffsetXRightField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                numberOffsetXRightField.setValue(String.valueOf(config.getNumberOffsetXRight()));
                numberOffsetXRightField.setResponder(txt -> {
                    try { config.setNumberOffsetXRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(numberOffsetXRightField);
                this.addRenderableWidget(Button.builder(Component.literal("→"),
                        btn -> {
                            config.setNumberOffsetXRight(config.getNumberOffsetXRight() + 2);
                            numberOffsetXRightField.setValue(String.valueOf(config.getNumberOffsetXRight()));
                        }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal("↑"),
                        btn -> {
                            config.setNumberOffsetYRight(config.getNumberOffsetYRight() - 2);
                            numberOffsetYRightField.setValue(String.valueOf(config.getNumberOffsetYRight()));
                        }).bounds(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build());
                numberOffsetYRightField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                numberOffsetYRightField.setValue(String.valueOf(config.getNumberOffsetYRight()));
                numberOffsetYRightField.setResponder(txt -> {
                    try { config.setNumberOffsetYRight(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                });
                this.addRenderableWidget(numberOffsetYRightField);
                this.addRenderableWidget(Button.builder(Component.literal("↓"),
                        btn -> {
                            config.setNumberOffsetYRight(config.getNumberOffsetYRight() + 2);
                            numberOffsetYRightField.setValue(String.valueOf(config.getNumberOffsetYRight()));
                        }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build());

                // Beschriftung rechts
                coordY += btnH * 2 + 8;
                this.addRenderableWidget(Button.builder(Component.literal(tr("right_coords")),
                        btn -> {}).bounds(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal(tr("number_offset_right")),
                        btn -> {}).bounds(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
            } else {

                // --- OFFSET CONTROLS (NON-SPLIT MODE, HORIZONTAL) ---
                if (!config.isVertical()) {
                    // X Offset Field
                    xOffsetField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                    xOffsetField.setValue(String.valueOf(config.getXOffset()));
                    xOffsetField.setResponder(txt -> {
                        try { config.setXOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(xOffsetField);

                    // X Offset Buttons
                    Button xDecBtn = Button.builder(Component.literal(""), btn -> {
                        config.setXOffset(config.getXOffset() - 10);
                        xOffsetField.setValue(String.valueOf(config.getXOffset()));
                    }).bounds(adjustX, coordY, arrowBtn, btnH).build();

                    Button xIncBtn = Button.builder(Component.literal(""), btn -> {
                        config.setXOffset(config.getXOffset() + 10);
                        xOffsetField.setValue(String.valueOf(config.getXOffset()));
                    }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addRenderableWidget(xDecBtn);
                    this.addRenderableWidget(xIncBtn);

                    // Y Offset Field
                    yOffsetField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                    yOffsetField.setValue(String.valueOf(config.getYOffset()));
                    yOffsetField.setResponder(txt -> {
                        try { config.setYOffset(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(yOffsetField);

                    // Y Offset Buttons
                    Button yDecBtn = Button.builder(Component.literal(""), btn -> {
                        config.setYOffset(config.getYOffset() - 10);
                        yOffsetField.setValue(String.valueOf(config.getYOffset()));
                    }).bounds(adjustX, coordY + btnH + 2, arrowBtn, btnH).build();

                    Button yIncBtn = Button.builder(Component.literal(""), btn -> {
                        config.setYOffset(config.getYOffset() + 10);
                        yOffsetField.setValue(String.valueOf(config.getYOffset()));
                    }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addRenderableWidget(yDecBtn);
                    this.addRenderableWidget(yIncBtn);

                    // Number X Offset Field + Buttons
                    numberOffsetXField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                    numberOffsetXField.setValue(String.valueOf(config.getNumberOffsetX()));
                    numberOffsetXField.setResponder(txt -> {
                        try { config.setNumberOffsetX(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(numberOffsetXField);

                    Button numXDecBtn = Button.builder(Component.literal(""), btn -> {
                        config.setNumberOffsetX(config.getNumberOffsetX() - 2);
                        numberOffsetXField.setValue(String.valueOf(config.getNumberOffsetX()));
                    }).bounds(rightFieldStartX, coordY, arrowBtn, btnH).build();

                    Button numXIncBtn = Button.builder(Component.literal(""), btn -> {
                        config.setNumberOffsetX(config.getNumberOffsetX() + 2);
                        numberOffsetXField.setValue(String.valueOf(config.getNumberOffsetX()));
                    }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addRenderableWidget(numXDecBtn);
                    this.addRenderableWidget(numXIncBtn);

                    // Number Y Offset Field + Buttons
                    numberOffsetYField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                    numberOffsetYField.setValue(String.valueOf(config.getNumberOffsetY()));
                    numberOffsetYField.setResponder(txt -> {
                        try { config.setNumberOffsetY(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(numberOffsetYField);

                    Button numYDecBtn = Button.builder(Component.literal(""), btn -> {
                        config.setNumberOffsetY(config.getNumberOffsetY() - 2);
                        numberOffsetYField.setValue(String.valueOf(config.getNumberOffsetY()));
                    }).bounds(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build();

                    Button numYIncBtn = Button.builder(Component.literal(""), btn -> {
                        config.setNumberOffsetY(config.getNumberOffsetY() + 2);
                        numberOffsetYField.setValue(String.valueOf(config.getNumberOffsetY()));
                    }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addRenderableWidget(numYDecBtn);
                    this.addRenderableWidget(numYIncBtn);

                    // Update Arrow Buttons
                    updateArrowButtons(xDecBtn, xIncBtn, yDecBtn, yIncBtn, numXDecBtn, numXIncBtn, numYDecBtn, numYIncBtn);
                }

                if (config.isVertical()) {
                    // --- OFFSET CONTROLS (NON-SPLIT MODE, VERTICAL / STACKED) ---

                    // X Offset Field
                    xOffsetVerticalField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                    xOffsetVerticalField.setValue(String.valueOf(config.getXOffsetLeftVertical()));
                    xOffsetVerticalField.setResponder(txt -> {
                        try { config.setXOffsetLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(xOffsetVerticalField);

                    // X Offset Buttons (Horizontal Movement: ← / →)
                    Button xDecVBtn = Button.builder(Component.literal("←"), btn -> {
                        config.setXOffsetLeftVertical(config.getXOffsetLeftVertical() - 10);
                        xOffsetVerticalField.setValue(String.valueOf(config.getXOffsetLeftVertical()));
                    }).bounds(adjustX, coordY, arrowBtn, btnH).build();

                    Button xIncVBtn = Button.builder(Component.literal("→"), btn -> {
                        config.setXOffsetLeftVertical(config.getXOffsetLeftVertical() + 10);
                        xOffsetVerticalField.setValue(String.valueOf(config.getXOffsetLeftVertical()));
                    }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addRenderableWidget(xDecVBtn);
                    this.addRenderableWidget(xIncVBtn);

                    // Y Offset Field
                    yOffsetVerticalField = new EditBox(this.font, adjustX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                    yOffsetVerticalField.setValue(String.valueOf(config.getYOffsetLeftVertical()));
                    yOffsetVerticalField.setResponder(txt -> {
                        try { config.setYOffsetLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(yOffsetVerticalField);

                    // Y Offset Buttons (Vertical Movement: ↑ / ↓)
                    Button yDecVBtn = Button.builder(Component.literal("↑"), btn -> {
                        config.setYOffsetLeftVertical(config.getYOffsetLeftVertical() - 10);
                        yOffsetVerticalField.setValue(String.valueOf(config.getYOffsetLeftVertical()));
                    }).bounds(adjustX, coordY + btnH + 2, arrowBtn, btnH).build();

                    Button yIncVBtn = Button.builder(Component.literal("↓"), btn -> {
                        config.setYOffsetLeftVertical(config.getYOffsetLeftVertical() + 10);
                        yOffsetVerticalField.setValue(String.valueOf(config.getYOffsetLeftVertical()));
                    }).bounds(adjustX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addRenderableWidget(yDecVBtn);
                    this.addRenderableWidget(yIncVBtn);

                    // Number X Offset Field + Buttons
                    numberOffsetXVerticalField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY, fieldW, btnH, Component.literal(""));
                    numberOffsetXVerticalField.setValue(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    numberOffsetXVerticalField.setResponder(txt -> {
                        try { config.setNumberOffsetXLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(numberOffsetXVerticalField);

                    Button numXDecVBtn = Button.builder(Component.literal("←"), btn -> {
                        config.setNumberOffsetXLeftVertical(config.getNumberOffsetXLeftVertical() - 2);
                        numberOffsetXVerticalField.setValue(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    }).bounds(rightFieldStartX, coordY, arrowBtn, btnH).build();

                    Button numXIncVBtn = Button.builder(Component.literal("→"), btn -> {
                        config.setNumberOffsetXLeftVertical(config.getNumberOffsetXLeftVertical() + 2);
                        numberOffsetXVerticalField.setValue(String.valueOf(config.getNumberOffsetXLeftVertical()));
                    }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY, arrowBtn, btnH).build();

                    this.addRenderableWidget(numXDecVBtn);
                    this.addRenderableWidget(numXIncVBtn);

                    // Number Y Offset Field + Buttons
                    numberOffsetYVerticalField = new EditBox(this.font, rightFieldStartX + arrowBtn + 4, coordY + btnH + 2, fieldW, btnH, Component.literal(""));
                    numberOffsetYVerticalField.setValue(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    numberOffsetYVerticalField.setResponder(txt -> {
                        try { config.setNumberOffsetYLeftVertical(Integer.parseInt(txt)); } catch (NumberFormatException ignored) {}
                    });
                    this.addRenderableWidget(numberOffsetYVerticalField);

                    Button numYDecVBtn = Button.builder(Component.literal("↑"), btn -> {
                        config.setNumberOffsetYLeftVertical(config.getNumberOffsetYLeftVertical() - 2);
                        numberOffsetYVerticalField.setValue(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    }).bounds(rightFieldStartX, coordY + btnH + 2, arrowBtn, btnH).build();

                    Button numYIncVBtn = Button.builder(Component.literal("↓"), btn -> {
                        config.setNumberOffsetYLeftVertical(config.getNumberOffsetYLeftVertical() + 2);
                        numberOffsetYVerticalField.setValue(String.valueOf(config.getNumberOffsetYLeftVertical()));
                    }).bounds(rightFieldStartX + arrowBtn + 4 + fieldW + 4, coordY + btnH + 2, arrowBtn, btnH).build();

                    this.addRenderableWidget(numYDecVBtn);
                    this.addRenderableWidget(numYIncVBtn);
                }

                coordY += btnH * 2 + 8;
                this.addRenderableWidget(Button.builder(Component.literal(tr("left2_coords")),
                        btn -> {}).bounds(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal(tr("number_offset_left")),
                        btn -> {}).bounds(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                coordY += btnH + 6;
                this.addRenderableWidget(Button.builder(Component.literal(tr("right_coords")),
                        btn -> {}).bounds(adjustX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
                this.addRenderableWidget(Button.builder(Component.literal(tr("number_offset_right")),
                        btn -> {}).bounds(rightFieldStartX, coordY, fieldW + arrowBtn*2 + 8, btnH).build());
            }

            int midW = Math.max(btnW, 160);
            int midX = winW / 2 - midW / 2;
            int defY = winH - btnH * 2 - marginY * 3;
            int doneY = winH - btnH - marginY * 2;

            this.addRenderableWidget(Button.builder(
                            Component.literal(tr("defaults")),
                            btn -> {
                                config.resetToDefaults();
                                this.minecraft.setScreen(new SimpleConfigScreen(this.parent));
                            })
                    .bounds(midX, defY, midW, btnH)
                    .tooltip(Tooltip.create(Component.literal(tr("tooltip_defaults"))))
                    .build());

            this.addRenderableWidget(Button.builder(Component.literal(tr("done")),
                    btn -> {
                        this.minecraft.setScreen(this.parent);
                    }).bounds(midX, doneY, midW, btnH).build());
        }


        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            super.extractRenderState(graphics, mouseX, mouseY, delta);

            String titleText = tr("title");
            int titleWidth = this.font.width(titleText);
            graphics.textWithBackdrop(this.font, Component.literal(titleText), (this.width / 2) - (titleWidth / 2), 10, titleWidth, 0xFFFFFFFF);

            String authorText = "by Mcjunky33";
            int authorWidth = this.font.width(authorText);
            graphics.textWithBackdrop(this.font, Component.literal(authorText), this.width - authorWidth - 10, this.height - this.font.lineHeight - 10, authorWidth, 0xFFFFFFFF);

            if (config.isVisible()) {
                graphics.nextStratum();

                graphics.pose().pushMatrix();
                ArmorHudOverlay.renderPreview(graphics, this.width, this.height, config);
                graphics.pose().popMatrix();
            }
        }


        private void updateArrowButtons(Button xDec, Button xInc, Button yDec, Button yInc,
                                        Button numXDec, Button numXInc, Button numYDec, Button numYInc) {
            if (config.isVertical()) {
                // Vertical / Stacked Mode
                // X = horizontal Verschiebung (links/rechts)
                xDec.setMessage(Component.literal("←"));
                xInc.setMessage(Component.literal("→"));
                numXDec.setMessage(Component.literal("←"));
                numXInc.setMessage(Component.literal("→"));

                // Y = vertikale Verschiebung (oben/unten)
                yDec.setMessage(Component.literal("↑"));
                yInc.setMessage(Component.literal("↓"));
                numYDec.setMessage(Component.literal("↑"));
                numYInc.setMessage(Component.literal("↓"));
            } else {
                // Horizontal Mode
                // X = horizontal Verschiebung (links/rechts)
                xDec.setMessage(Component.literal("←"));
                xInc.setMessage(Component.literal("→"));
                numXDec.setMessage(Component.literal("←"));
                numXInc.setMessage(Component.literal("→"));

                // Y = vertikale Verschiebung (oben/unten)
                yDec.setMessage(Component.literal("↑"));
                yInc.setMessage(Component.literal("↓"));
                numYDec.setMessage(Component.literal("↑"));
                numYInc.setMessage(Component.literal("↓"));
            }
        }
    }
}