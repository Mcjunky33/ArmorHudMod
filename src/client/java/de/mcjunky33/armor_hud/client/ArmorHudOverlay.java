package de.mcjunky33.armor_hud.client;

import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ArmorHudOverlay {
    private final ArmorHudConfig config = ArmorHudConfig.getInstance();
    private static final Identifier HOTBAR_TEXTURE = Identifier.of("armor_hud", "textures/gui/hotbar_texture.png");
    private static final Identifier HOTBAR_TEXTURE_DARK = Identifier.of("armor_hud", "textures/gui/hotbar_texture_dark.png");

    public void renderArmorUI(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!config.isVisible() || client.options.hudHidden || client.player == null || client.world == null) {
            return;
        }

        // Get armor items
        ItemStack[] armorItems = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armorItems[i] = client.player.getInventory().getStack(36 + i);
        }

        // Get screen width and height
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Use config values
        int boxSize = config.getBoxSize();
        int spacing = config.getSpacing();
        int xOffset = screenWidth / 2 + config.getXOffset();
        int yOffset = screenHeight + config.getYOffset();

        // Draw armor boxes and icons
        for (int i = armorItems.length - 1; i >= 0; i--) {
            ItemStack armorItem = armorItems[i];

            if (!armorItem.isEmpty()) {
                int armorSpacing = (armorItems.length - 1 - i) * (boxSize + spacing);
                int boxX = xOffset;
                int boxY = yOffset;

                // NEU: Vertikal/Horizontal
                if (config.isVertical()) {
                    boxY += armorSpacing;
                } else {
                    boxX += armorSpacing;
                }

                // Draw box background (dark mode logic included)
                drawTexture(context, boxX, boxY, boxSize, boxSize);

                // Draw armor icon
                context.drawItem(armorItem, boxX + (boxSize - 16) / 2, boxY + (boxSize - 16) / 2);

                // Umschalt-Logik für Bar/Text
                if (config.isShowDurabilityBar()) {
                    drawDurabilityBar(context, boxX, boxY + boxSize - 6, boxSize, armorItem);
                }
                if (config.isShowDurabilityAsNumber()) {
                    drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem);
                }
            }
        }
    }

    private void drawTexture(DrawContext context, int x, int y, int width, int height) {
        Identifier texture = config.isDarkMode() ? HOTBAR_TEXTURE_DARK : HOTBAR_TEXTURE;
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x, y,
                0, 0,
                width, height,
                width, height
        );
    }

    private void drawDurabilityBar(DrawContext context, int x, int y, int width, ItemStack item) {
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

    private void drawDurabilityTextSimple(DrawContext context, int boxX, int boxY, int boxSize, ItemStack item) {
        int maxDamage = item.getMaxDamage();
        int damage = item.getDamage();
        if (maxDamage <= 0) return;

        int durability = maxDamage - damage;
        String durabilityText = String.valueOf(durability);

        float ratio = durability / (float) maxDamage;
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

        int textX = boxX + (boxSize - textWidth) / 2;
        int textY = boxY + boxSize - textHeight - 0;

        context.drawTextWithShadow(
                textRenderer,
                durabilityText,
                textX,
                textY,
                color
        );
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
}