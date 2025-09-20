package de.mcjunky33.armor_hud.client;

import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

public class ArmorHudOverlay {
    private final ArmorHudConfig config = ArmorHudConfig.getInstance();
    private static final Identifier HOTBAR_TEXTURE = Identifier.of("armor_hud", "textures/gui/hotbar_texture.png");
    private static final Identifier HOTBAR_TEXTURE_DARK = Identifier.of("armor_hud", "textures/gui/hotbar_texture_dark.png");

    // Display order: Helmet, Chestplate, Leggings, Boots
    private static final int[] ARMOR_ORDER = {39, 38, 37, 36};

    public void renderArmorUI(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!config.isVisible() || client.options.hudHidden || client.player == null || client.world == null) {
            return;
        }
        ItemStack[] armorItems = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armorItems[i] = client.player.getInventory().getStack(ARMOR_ORDER[i]);
        }
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int boxSize = config.getBoxSize();
        int spacing = config.getSpacing();

        if (config.isVertical()) {
            if (config.isSplitMode()) {
                int xOffsetLeft2 = screenWidth / 2 + config.getXOffsetLeft2();
                int yOffsetLeft2 = screenHeight + config.getYOffsetLeft2();
                for (int i = 0; i < 2; i++) {
                    if (!armorItems[i].isEmpty()) {
                        drawBoxAndArmor(context, armorItems[i], xOffsetLeft2, yOffsetLeft2, boxSize, spacing, i, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                    }
                }
                int xOffsetRight = screenWidth / 2 + config.getXOffsetRight();
                int yOffsetRight = screenHeight + config.getYOffsetRight();
                for (int i = 2; i < 4; i++) {
                    if (!armorItems[i].isEmpty()) {
                        drawBoxAndArmor(context, armorItems[i], xOffsetRight, yOffsetRight, boxSize, spacing, i - 2, true, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
                    }
                }
            } else {
                int xOffsetLeft1 = screenWidth / 2 + config.getXOffsetLeft1();
                int yOffsetLeft1 = screenHeight + config.getYOffsetLeft1();
                for (int i = 0; i < 4; i++) {
                    if (!armorItems[i].isEmpty()) {
                        drawBoxAndArmor(context, armorItems[i], xOffsetLeft1, yOffsetLeft1, boxSize, spacing, i, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                    }
                }
            }
        } else if (config.isSplitMode()) {
            int xOffsetLeft2 = screenWidth / 2 + config.getXOffsetLeft2();
            int yOffsetLeft2 = screenHeight + config.getYOffsetLeft2();
            for (int i = 0; i < 2; i++) {
                if (!armorItems[i].isEmpty()) {
                    drawBoxAndArmor(context, armorItems[i], xOffsetLeft2, yOffsetLeft2, boxSize, spacing, i, false, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                }
            }
            int xOffsetRight = screenWidth / 2 + config.getXOffsetRight();
            int yOffsetRight = screenHeight + config.getYOffsetRight();
            for (int i = 2; i < 4; i++) {
                if (!armorItems[i].isEmpty()) {
                    drawBoxAndArmor(context, armorItems[i], xOffsetRight, yOffsetRight, boxSize, spacing, i - 2, false, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
                }
            }
        } else {
            int xOffset = screenWidth / 2 + config.getXOffset();
            int yOffset = screenHeight + config.getYOffset();
            for (int i = 0; i < 4; i++) {
                if (!armorItems[i].isEmpty()) {
                    drawBoxAndArmor(context, armorItems[i], xOffset, yOffset, boxSize, spacing, i, false, config.getNumberOffsetX(), config.getNumberOffsetY());
                }
            }
        }
    }

    // Reihenfolge: Box > Item > Bar/Text (Bar/Text **zuletzt** für Vordergrund!)
    private void drawBoxAndArmor(DrawContext context, ItemStack armorItem, int baseX, int baseY, int boxSize, int spacing, int idx, boolean vertical, int offsetXNumber, int offsetYNumber) {
        int boxX = baseX + (!vertical ? idx * (boxSize + spacing) : 0);
        int boxY = baseY + (vertical ? idx * (boxSize + spacing) : 0);

        // 1. Box-Textur
        if (config.isShowBoxTexture()) {
            Identifier texture = config.isDarkMode() ? HOTBAR_TEXTURE_DARK : HOTBAR_TEXTURE;
            context.drawTexture(
                    texture,
                    boxX, boxY,
                    0, 0,
                    boxSize, boxSize,
                    boxSize, boxSize
            );
        }

        // 2. Item-Icon
        int itemSize = 16;
        int itemX = boxX + Math.round((boxSize - itemSize) / 2f);
        int itemY = boxY + Math.round((boxSize - itemSize) / 2f);
        context.drawItem(armorItem, itemX, itemY);

        // 3. Bar und Text _nach_ dem Icon, aber: Z-Buffer/Depth-Test _deaktivieren_
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        GL11.glDepthFunc(GL11.GL_ALWAYS);

        int mode = config.getDurabilityDisplayMode();
        if (mode == 0) {
            drawDurabilityBar(context, boxX, boxY + boxSize - 6, boxSize, armorItem);
        } else if (mode == 1) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, false);
        } else if (mode == 2) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, true);
        }

        // Depth-Test wieder an!
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    public void drawDurabilityBar(DrawContext context, int x, int y, int width, ItemStack item) {
        int maxDamage = item.getMaxDamage();
        int damage = item.getDamage();
        if (maxDamage <= 0) return;

        int barWidth = 13;
        int barX = x + (width - barWidth) / 2 + 1;
        int barHeight = 2;

        float durabilityRatio = ((maxDamage - damage) / (float) maxDamage);
        int remainingWidth = (int) Math.round(durabilityRatio * 13);
        int barColor = convertHSVtoARGB((durabilityRatio / 3f) * 360, 1, 1);

        fill(context, barX, y, barX + barWidth, y + barHeight, 0xFF000000);
        fill(context, barX, y, barX + remainingWidth, y + barHeight / 2, barColor);
    }

    public void drawDurabilityTextSimple(DrawContext context, int boxX, int boxY, int boxSize, ItemStack item, int offsetXNumber, int offsetYNumber, boolean showAsPercent) {
        int maxDamage = item.getMaxDamage();
        int damage = item.getDamage();
        if (maxDamage <= 0) return;

        int durability = maxDamage - damage;
        float ratio = durability / (float) maxDamage;

        String durabilityText = showAsPercent ? (Math.round(ratio * 100) + "%") : String.valueOf(durability);

        int color;
        if (ratio > 0.7f) {
            color = 0xFF00FF00;
        } else if (ratio > 0.4f) {
            color = 0xFFFFFF00;
        } else if (ratio > 0.2f) {
            color = 0xFFFFA500;
        } else if (ratio > 0.05f) {
            color = 0xFFFF0000;
        } else {
            color = 0xFF000000;
        }

        var textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(durabilityText);
        int textHeight = textRenderer.fontHeight;

        int textX = boxX + (boxSize - textWidth) / 2 + offsetXNumber;
        int textY = boxY + boxSize - textHeight + offsetYNumber;

        context.drawTextWithShadow(textRenderer, durabilityText, textX, textY, color);
    }

    private void fill(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, color);
    }

    private int convertHSVtoARGB(float h, float s, float v) {
        h = (h % 360 + 360) % 360;
        float hh = h / 60.0f;
        int i = (int) hh % 6;
        float f = hh - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        int r = 0, g = 0, b = 0;
        switch (i) {
            case 0: r = Math.round(v * 255); g = Math.round(t * 255); b = Math.round(p * 255); break;
            case 1: r = Math.round(q * 255); g = Math.round(v * 255); b = Math.round(p * 255); break;
            case 2: r = Math.round(p * 255); g = Math.round(v * 255); b = Math.round(t * 255); break;
            case 3: r = Math.round(p * 255); g = Math.round(q * 255); b = Math.round(v * 255); break;
            case 4: r = Math.round(t * 255); g = Math.round(p * 255); b = Math.round(v * 255); break;
            case 5: r = Math.round(v * 255); g = Math.round(p * 255); b = Math.round(q * 255); break;
        }
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    // ----------- PREVIEW FÜR CONFIG-SCREEN -----------
    public static void renderPreview(DrawContext context, int screenWidth, int screenHeight, ArmorHudConfig config) {
        ItemStack[] previewArmor = new ItemStack[] {
                new ItemStack(Items.DIAMOND_HELMET),
                new ItemStack(Items.GOLDEN_CHESTPLATE),
                new ItemStack(Items.LEATHER_LEGGINGS),
                new ItemStack(Items.CHAINMAIL_BOOTS)
        };
        for (int i = 0; i < previewArmor.length; i++) {
            ItemStack item = previewArmor[i];
            int max = item.getMaxDamage();
            int val;
            switch (i) {
                case 0: val = max*9/10; break;    // Helmet 10%
                case 1: val = max/4; break;       // Chestplate 25%
                case 2: val = max*3/4; break;     // Leggings 75%
                case 3: val = max/2; break;       // Boots 50%
                default: val = 0;
            }
            item.setDamage(val);
        }

        int boxSize = config.getBoxSize();
        int spacing = config.getSpacing();

        if (config.isVertical()) {
            if (config.isSplitMode()) {
                int xOffsetLeft2 = screenWidth / 2 + config.getXOffsetLeft2();
                int yOffsetLeft2 = screenHeight + config.getYOffsetLeft2();
                for (int i = 0; i < 2; i++) {
                    if (!previewArmor[i].isEmpty()) {
                        (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffsetLeft2, yOffsetLeft2, boxSize, spacing, i, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                    }
                }
                int xOffsetRight = screenWidth / 2 + config.getXOffsetRight();
                int yOffsetRight = screenHeight + config.getYOffsetRight();
                for (int i = 2; i < 4; i++) {
                    if (!previewArmor[i].isEmpty()) {
                        (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffsetRight, yOffsetRight, boxSize, spacing, i - 2, true, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
                    }
                }
            } else {
                int xOffsetLeft1 = screenWidth / 2 + config.getXOffsetLeft1();
                int yOffsetLeft1 = screenHeight + config.getYOffsetLeft1();
                for (int i = 0; i < 4; i++) {
                    if (!previewArmor[i].isEmpty()) {
                        (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffsetLeft1, yOffsetLeft1, boxSize, spacing, i, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                    }
                }
            }
        } else if (config.isSplitMode()) {
            int xOffsetLeft2 = screenWidth / 2 + config.getXOffsetLeft2();
            int yOffsetLeft2 = screenHeight + config.getYOffsetLeft2();
            for (int i = 0; i < 2; i++) {
                if (!previewArmor[i].isEmpty()) {
                    (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffsetLeft2, yOffsetLeft2, boxSize, spacing, i, false, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());
                }
            }
            int xOffsetRight = screenWidth / 2 + config.getXOffsetRight();
            int yOffsetRight = screenHeight + config.getYOffsetRight();
            for (int i = 2; i < 4; i++) {
                if (!previewArmor[i].isEmpty()) {
                    (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffsetRight, yOffsetRight, boxSize, spacing, i - 2, false, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
                }
            }
        } else {
            int xOffset = screenWidth / 2 + config.getXOffset();
            int yOffset = screenHeight + config.getYOffset();
            for (int i = 0; i < 4; i++) {
                if (!previewArmor[i].isEmpty()) {
                    (new ArmorHudOverlay()).drawBoxAndArmor(context, previewArmor[i], xOffset, yOffset, boxSize, spacing, i, false, config.getNumberOffsetX(), config.getNumberOffsetY());
                }
            }
        }
    }
}