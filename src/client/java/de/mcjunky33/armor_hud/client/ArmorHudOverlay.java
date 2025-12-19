package de.mcjunky33.armor_hud.client;

import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ArmorHudOverlay {

    private final ArmorHudConfig config = ArmorHudConfig.getInstance();

    private static final Identifier HOTBAR_TEXTURE =
            Identifier.of("armor_hud", "textures/gui/hotbar_texture.png");
    private static final Identifier HOTBAR_TEXTURE_DARK =
            Identifier.of("armor_hud", "textures/gui/hotbar_texture_dark.png");

    // ARMOR SLOTS (versionssicher)
    // 39 = Helmet, 38 = Chestplate, 37 = Leggings, 36 = Boots
    private static final int[] ARMOR_ORDER = {39, 38, 37, 36};

    // ================= INGAME =================

    public void renderArmorUI(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!config.isVisible()
                || client.options.hudHidden
                || client.player == null
                || client.world == null) {
            return;
        }

        // ================= ARMOR =================
        ItemStack[] armorItems = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armorItems[i] = client.player.getInventory().getStack(ARMOR_ORDER[i]);
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        renderInternal(context, screenWidth / 2, screenHeight, armorItems, config);

        // ================= HAND DURABILITY =================
        if (!config.isShowHandDurability()) return;

        ItemStack mainHand = client.player.getMainHandStack();
        ItemStack offHand = client.player.getOffHandStack();

        int hotbarStartX = screenWidth / 2 - 91;
        int hotbarY = screenHeight - 22;

        // ===== MAINHAND =====
        int selectedSlot = client.player.getInventory().getSelectedSlot();
        int mainSlotX = hotbarStartX + selectedSlot * 20;
        int mainSlotCenterX = mainSlotX + 12;

        // ===== OFFHAND (links neben Slot 0) =====
        int offhandSlotX = hotbarStartX - 20;
        int offhandSlotCenterX = offhandSlotX + 2;

        int barWidth = 16;
        int barY = hotbarY + 14;
        int textY = hotbarY - 6;

        boolean showBar = config.isShowDurabilityBar();
        int durabilityMode = config.getDurabilityDisplayMode(); // 0 = aus, 1 = number, 2 = percent

        // ===== MAINHAND RENDER =====
        if (!mainHand.isEmpty() && mainHand.getMaxDamage() > 0) {

            if (showBar) {
                drawDurabilityBar(
                        context,
                        mainSlotCenterX - barWidth / 2,
                        barY,
                        barWidth,
                        mainHand
                );
            }

            if (durabilityMode != 0) {
                drawDurabilityTextSimple(
                        context,
                        mainSlotCenterX - barWidth / 2,
                        textY,
                        barWidth,
                        mainHand,
                        0,
                        0,
                        durabilityMode == 2
                );
            }
        }

        // ===== OFFHAND RENDER =====
        if (!offHand.isEmpty() && offHand.getMaxDamage() > 0) {

            if (showBar) {
                drawDurabilityBar(
                        context,
                        offhandSlotCenterX - barWidth / 2,
                        barY,
                        barWidth,
                        offHand
                );
            }

            if (durabilityMode != 0) {
                drawDurabilityTextSimple(
                        context,
                        offhandSlotCenterX - barWidth / 2,
                        textY,
                        barWidth,
                        offHand,
                        0,
                        0,
                        durabilityMode == 2
                );
            }
        }
    }



    // ================= PREVIEW =================

    public static void renderPreview(
            DrawContext context,
            int screenWidth,
            int screenHeight,
            ArmorHudConfig config
    ) {
        ItemStack[] previewArmor = {
                new ItemStack(Items.DIAMOND_HELMET),
                new ItemStack(Items.GOLDEN_CHESTPLATE),
                new ItemStack(Items.LEATHER_LEGGINGS),
                new ItemStack(Items.CHAINMAIL_BOOTS)
        };

        // FAKE DURABILITY (nur Preview)
        for (int i = 0; i < previewArmor.length; i++) {
            ItemStack item = previewArmor[i];
            int max = item.getMaxDamage();
            int damage;
            switch (i) {
                case 0 -> damage = max * 9 / 10; // 10 %
                case 1 -> damage = max / 4;      // 75 %
                case 2 -> damage = max * 3 / 4;  // 25 %
                case 3 -> damage = max / 2;      // 50 %
                default -> damage = 0;
            }
            item.setDamage(damage);
        }

        new ArmorHudOverlay().renderInternal(
                context,
                screenWidth / 2,
                screenHeight,
                previewArmor,
                config
        );
    }

    // ================= GEMEINSAME RENDER-LOGIK =================

    private void renderInternal(
            DrawContext context,
            int baseX,
            int baseY,
            ItemStack[] armorItems,
            ArmorHudConfig config
    ) {
        int boxSize = config.getBoxSize();
        int spacing = config.getSpacing();

        if (config.isVertical()) {
            if (config.isSplitMode()) {

                // LEFT (0–1)
                for (int i = 0; i < 2; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffsetLeft2(),
                            baseY + config.getYOffsetLeft2(),
                            boxSize, spacing, i, true,
                            config.getNumberOffsetXLeft(),
                            config.getNumberOffsetYLeft()
                    );
                }

                // RIGHT (2–3)
                for (int i = 2; i < 4; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffsetRight(),
                            baseY + config.getYOffsetRight(),
                            boxSize, spacing, i - 2, true,
                            config.getNumberOffsetXRight(),
                            config.getNumberOffsetYRight()
                    );
                }

            } else {
                // VERTICAL NORMAL → hier die neuen Vertical-Felder benutzen
                for (int i = 0; i < 4; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffsetLeftVertical(),
                            baseY + config.getYOffsetLeftVertical(),
                            boxSize, spacing, i, true,
                            config.getNumberOffsetXLeftVertical(),
                            config.getNumberOffsetYLeftVertical()
                    );
                }
            }
        } else {
            if (config.isSplitMode()) {

                // HORIZONTAL SPLIT
                for (int i = 0; i < 2; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffsetLeft2(),
                            baseY + config.getYOffsetLeft2(),
                            boxSize, spacing, i, false,
                            config.getNumberOffsetXLeft(),
                            config.getNumberOffsetYLeft()
                    );
                }

                for (int i = 2; i < 4; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffsetRight(),
                            baseY + config.getYOffsetRight(),
                            boxSize, spacing, i - 2, false,
                            config.getNumberOffsetXRight(),
                            config.getNumberOffsetYRight()
                    );
                }

            } else {
                // HORIZONTAL NORMAL
                for (int i = 0; i < 4; i++) {
                    drawBoxAndArmor(
                            context,
                            armorItems[i],
                            baseX + config.getXOffset(),
                            baseY + config.getYOffset(),
                            boxSize, spacing, i, false,
                            config.getNumberOffsetX(),
                            config.getNumberOffsetY()
                    );
                }
            }
        }
    }


    // ================= DRAW =================

    private void drawBoxAndArmor(
            DrawContext context,
            ItemStack armorItem,
            int baseX,
            int baseY,
            int boxSize,
            int spacing,
            int idx,
            boolean vertical,
            int offsetXNumber,
            int offsetYNumber
    ) {
        if (armorItem.isEmpty()) return;

        int boxX = baseX + (!vertical ? idx * (boxSize + spacing) : 0);
        int boxY = baseY + (vertical ? idx * (boxSize + spacing) : 0);

        if (config.isShowBoxTexture()) {
            Identifier texture = config.isDarkMode()
                    ? HOTBAR_TEXTURE_DARK
                    : HOTBAR_TEXTURE;

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    texture,
                    boxX,
                    boxY,
                    0, 0,
                    boxSize, boxSize,
                    boxSize, boxSize
            );
        }

        // Item (16x16, zentriert)
        int itemX = boxX + (boxSize - 16) / 2;
        int itemY = boxY + (boxSize - 16) / 2;
        context.drawItem(armorItem, itemX, itemY);

        int mode = config.getDurabilityDisplayMode();

// Durability Bar (falls aktiviert)
        if (config.isShowDurabilityBar()) {
            drawDurabilityBar(context, boxX, boxY + boxSize - 6, boxSize, armorItem);
        }

// Durability Text / Percent (falls mode != 0)
        if (mode == 1) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, false);
        } else if (mode == 2) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, true);
        }

    }

    // ================= DURABILITY =================

    public void drawDurabilityBar(DrawContext context, int x, int y, int width, ItemStack item) {
        int maxDamage = item.getMaxDamage();
        if (maxDamage <= 0) return;

        int damage = item.getDamage();

        // 🔹 NEU: Wenn Haltbarkeit voll ist, nichts zeichnen
        if (damage <= 0) return;

        float ratio = (maxDamage - damage) / (float) maxDamage;
        int barWidth = 13;
        int barX = x + (width - barWidth) / 2 + 1;

        // Hintergrund der Bar
        fill(context, barX, y, barX + barWidth, y + 2, 0xFF000000);

        // Farbliche Anzeige der Haltbarkeit
        fill(context, barX, y,
                barX + Math.round(ratio * barWidth),
                y + 1,
                convertHSVtoARGB((ratio / 3f) * 360, 1, 1));
    }


    public void drawDurabilityTextSimple(
            DrawContext context,
            int boxX,
            int boxY,
            int boxSize,
            ItemStack item,
            int offsetX,
            int offsetY,
            boolean showPercent
    ) {
        int maxDamage = item.getMaxDamage();
        if (maxDamage <= 0) return;

        int durability = maxDamage - item.getDamage();
        float ratio = durability / (float) maxDamage;

        String text = showPercent
                ? Math.round(ratio * 100) + "%"
                : String.valueOf(durability);

        int color =
                ratio > 0.7f ? 0xFF00FF00 :
                        ratio > 0.4f ? 0xFFFFFF00 :
                                ratio > 0.2f ? 0xFFFFA500 :
                                        ratio > 0.05f ? 0xFFFF0000 :
                                                0xFF000000;

        var tr = MinecraftClient.getInstance().textRenderer;
        int textX = boxX + (boxSize - tr.getWidth(text)) / 2 + offsetX;
        int textY = boxY + boxSize - tr.fontHeight + offsetY;

        context.drawTextWithShadow(tr, text, textX, textY, color);
    }

    private void fill(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
    }

    private int convertHSVtoARGB(float h, float s, float v) {
        h = (h % 360 + 360) % 360;

        float hh = h / 60f;
        int i = (int) hh;
        float f = hh - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        float r = switch (i) {
            case 0 -> v;
            case 1 -> q;
            case 2 -> p;
            case 3 -> p;
            case 4 -> t;
            default -> v;
        };
        float g = switch (i) {
            case 0 -> t;
            case 1 -> v;
            case 2 -> v;
            case 3 -> q;
            case 4 -> p;
            default -> p;
        };
        float b = switch (i) {
            case 0 -> p;
            case 1 -> p;
            case 2 -> t;
            case 3 -> v;
            case 4 -> v;
            default -> q;
        };

        return (255 << 24)
                | ((int) (r * 255) << 16)
                | ((int) (g * 255) << 8)
                | (int) (b * 255);
    }
}
