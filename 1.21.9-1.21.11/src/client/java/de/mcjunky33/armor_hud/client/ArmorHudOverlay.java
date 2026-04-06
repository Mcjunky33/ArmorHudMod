package de.mcjunky33.armor_hud.client;

import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;
import de.mcjunky33.armor_hud.client.mixin.InventoryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class ArmorHudOverlay {

    private final ArmorHudConfig config = ArmorHudConfig.getInstance();

    private static final ResourceLocation HOTBAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/hotbar_texture.png");
    private static final ResourceLocation HOTBAR_TEXTURE_DARK =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/hotbar_texture_dark.png");
    private static final ResourceLocation SLOT_1 =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/1_slot.png");
    private static final ResourceLocation SLOT_2 =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/2_slots.png");
    private static final ResourceLocation SLOT_3 =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/3_slots.png");
    private static final ResourceLocation SLOT_4 =
            ResourceLocation.fromNamespaceAndPath("armor_hud", "textures/gui/4_slots.png");

    private static final int[] ARMOR_ORDER = {39, 38, 37, 36};

    public void renderArmorUI(GuiGraphics context, int mouseX, int mouseY) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        if (client.options.hideGui) return;

        if (client.player.isSpectator()) return;

        int screenWidth = context.guiWidth();
        int screenHeight = context.guiHeight();

        if (config.isVisible()) {
            ItemStack[] armorItems = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                armorItems[i] = client.player.getInventory().getItem(39 - i);
            }

            if (config.isUseHotbarTexture()) {
                renderInternalHotbar(context, screenWidth / 2, screenHeight, armorItems, config);
            } else {

                renderInternal(context, screenWidth / 2, screenHeight, armorItems, config);
            }
        }

        if (config.isShowHandDurability()) {
            renderHotbarDurability(context, screenWidth, screenHeight);
        }
    }

    public static void appendTooltipDurability(net.minecraft.world.item.ItemStack stack, java.util.List<net.minecraft.network.chat.Component> lines) {
        de.mcjunky33.armor_hud.client.config.ArmorHudConfig config = de.mcjunky33.armor_hud.client.config.ArmorHudConfig.getInstance();

        if (config.isAlwaysShowTooltipDurability() && stack.isDamageableItem()) {

            lines.removeIf(component -> {
                String raw = component.getString().toLowerCase();
                return raw.contains("durability") || raw.contains("haltbarkeit") || raw.contains("%s");
            });

            int maxDamage = stack.getMaxDamage();
            int durability = maxDamage - stack.getDamageValue();
            float ratio = durability / (float) maxDamage;
            int percent = Math.round(ratio * 100);

            int colorRGB = ratio > 0.7f ? 0xFF00FF00 :
                    ratio > 0.4f ? 0xFFFFFF00 :
                            ratio > 0.2f ? 0xFFFFA500 :
                                    ratio > 0.05f ? 0xFFFF0000 :
                                            0xFFAA0000;

            String label = "de".equals(config.getLanguage()) ? "Haltbarkeit: " : "Durability: ";
            String infoText = String.format("%d / %d (%d%%)", durability, maxDamage, percent);

            net.minecraft.network.chat.TextColor guiColor = net.minecraft.network.chat.TextColor.fromRgb(colorRGB & 0xFFFFFF);

            net.minecraft.network.chat.MutableComponent durabilityLine = net.minecraft.network.chat.Component.literal(label)
                    .append(net.minecraft.network.chat.Component.literal(infoText).withStyle(style -> style.withColor(guiColor)));

            lines.add(durabilityLine);
        }
    }


    private void renderHotbarDurability(GuiGraphics context, int sw, int sh) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        int mode = config.getDurabilityDisplayMode();
        if (mode == 0) return;

        int hotbarStartX = sw / 2 - 91;
        int textY = sh - 28;

        // --- MAINHAND ---
        int currentSlot = ((InventoryAccessor) client.player.getInventory()).getSelectedSlot();
        ItemStack main = client.player.getMainHandItem();
        if (!main.isEmpty() && main.isDamageableItem()) {
            int x = hotbarStartX + (currentSlot * 20) + 2;
            drawDurabilityTextSimple(context, x, textY, 16, main, 0, 0, mode == 2);
        }

        // --- OFFHAND ---
        ItemStack off = client.player.getOffhandItem();
        if (!off.isEmpty() && off.isDamageableItem()) {

            int offhandX = hotbarStartX - 25;

            if (client.player.getMainArm() == net.minecraft.world.entity.HumanoidArm.LEFT) {
                offhandX = hotbarStartX + 182;
            }

            drawDurabilityTextSimple(context, offhandX, textY, 16, off, 0, 0, mode == 2);
        }
    }

    // ================= PREVIEW =================

    public static void renderPreview(
            GuiGraphics context,
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

        for (int i = 0; i < previewArmor.length; i++) {
            ItemStack item = previewArmor[i];
            int max = item.getMaxDamage();
            int damage;
            switch (i) {
                case 0 -> damage = (int) (max * 0.9);
                case 1 -> damage = max / 4;
                case 2 -> damage = (int) (max * 0.75);
                case 3 -> damage = max / 2;
                default -> damage = 0;
            }
            item.setDamageValue(damage);
        }

        ArmorHudOverlay hud = new ArmorHudOverlay();

        if (config.isUseHotbarTexture()) {

            hud.renderInternalHotbar(
                    context,
                    screenWidth / 2,
                    screenHeight,
                    previewArmor,
                    config
            );
        } else {
            hud.renderInternal(
                    context,
                    screenWidth / 2,
                    screenHeight,
                    previewArmor,
                    config
            );
        }
    }

    // ================= GEMEINSAME RENDER-LOGIK =================

    private void renderInternal(
            GuiGraphics context,
            int baseX,
            int baseY,
            ItemStack[] armorItems,
            ArmorHudConfig config
    ) {
        int boxSize = config.getBoxSize();
        int spacing = config.getSpacing();

        if (config.isVertical()) {
            if (config.isSplitMode()) {
                renderGroup(context, new ItemStack[]{armorItems[0], armorItems[1]},
                        baseX + config.getXOffsetLeft2(), baseY + config.getYOffsetLeft2(),
                        boxSize, spacing, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft(), "TO_END");


                renderGroup(context, new ItemStack[]{armorItems[2], armorItems[3]},
                        baseX + config.getXOffsetRight(), baseY + config.getYOffsetRight(),
                        boxSize, spacing, true, config.getNumberOffsetXRight(), config.getNumberOffsetYRight(), "TO_END");
            } else {

                renderGroup(context, armorItems,
                        baseX + config.getXOffsetLeftVertical(), baseY + config.getYOffsetLeftVertical(),
                        boxSize, spacing, true, config.getNumberOffsetXLeftVertical(), config.getNumberOffsetYLeftVertical(), "TO_END");
            }
        } else {
            if (config.isSplitMode()) {
                renderGroup(context, new ItemStack[]{armorItems[0], armorItems[1]},
                        baseX + config.getXOffsetLeft2(), baseY + config.getYOffsetLeft2(),
                        boxSize, spacing, false, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft(), "TO_END");

                renderGroup(context, new ItemStack[]{armorItems[2], armorItems[3]},
                        baseX + config.getXOffsetRight(), baseY + config.getYOffsetRight(),
                        boxSize, spacing, false, config.getNumberOffsetXRight(), config.getNumberOffsetYRight(), "TO_END");
            } else {
                renderGroup(context, armorItems,
                        baseX + config.getXOffset(), baseY + config.getYOffset(),
                        boxSize, spacing, false, config.getNumberOffsetX(), config.getNumberOffsetY(), "TO_END");
            }
        }
    }

    private void renderGroup(
            GuiGraphics context,
            ItemStack[] items,
            int startX,
            int startY,
            int boxSize,
            int spacing,
            boolean vertical,
            int offNumX,
            int offNumY,
            String mode
    ) {

        java.util.List<ItemStack> active = new java.util.ArrayList<>();
        for (ItemStack s : items) {
            if (s != null && !s.isEmpty()) {
                active.add(s);
            }
        }

        if (active.isEmpty()) return;

        for (int i = 0; i < active.size(); i++) {
            int idx;
            if (mode.equals("TO_END")) {

                idx = (items.length - active.size()) + i;
            } else {

                idx = i;
            }


            drawBoxAndArmor(context, active.get(i), startX, startY, boxSize, spacing, idx, vertical, offNumX, offNumY);
        }
    }


    private void renderInternalHotbar(
            GuiGraphics context,
            int baseX,
            int baseY,
            ItemStack[] armorItems,
            ArmorHudConfig config
    ) {

        int boxSize = 22;

        if (config.isVertical()) {
            if (config.isSplitMode()) {

                renderGroupHotbar(context, new ItemStack[]{armorItems[0], armorItems[1]},
                        baseX + config.getXOffsetLeft2(), baseY + config.getYOffsetLeft2(),
                        boxSize, true, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());


                renderGroupHotbar(context, new ItemStack[]{armorItems[2], armorItems[3]},
                        baseX + config.getXOffsetRight(), baseY + config.getYOffsetRight(),
                        boxSize, true, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
            } else {

                renderGroupHotbar(context, armorItems,
                        baseX + config.getXOffsetLeftVertical(), baseY + config.getYOffsetLeftVertical(),
                        boxSize, true, config.getNumberOffsetXLeftVertical(), config.getNumberOffsetYLeftVertical());
            }
        } else {
            if (config.isSplitMode()) {

                renderGroupHotbar(context, new ItemStack[]{armorItems[0], armorItems[1]},
                        baseX + config.getXOffsetLeft2(), baseY + config.getYOffsetLeft2(),
                        boxSize, false, config.getNumberOffsetXLeft(), config.getNumberOffsetYLeft());


                renderGroupHotbar(context, new ItemStack[]{armorItems[2], armorItems[3]},
                        baseX + config.getXOffsetRight(), baseY + config.getYOffsetRight(),
                        boxSize, false, config.getNumberOffsetXRight(), config.getNumberOffsetYRight());
            } else {

                renderGroupHotbar(context, armorItems,
                        baseX + config.getXOffset(), baseY + config.getYOffset(),
                        boxSize, false, config.getNumberOffsetX(), config.getNumberOffsetY());
            }
        }
    }


    private void renderGroupHotbar(
            GuiGraphics context,
            ItemStack[] items,
            int startX,
            int startY,
            int boxSize,
            boolean vertical,
            int offNumX,
            int offNumY
    ) {
        java.util.List<ItemStack> active = new java.util.ArrayList<>();
        for (ItemStack s : items) if (s != null && !s.isEmpty()) active.add(s);
        if (active.isEmpty()) return;

        int count = active.size();
        int offsetIdx = items.length - count;


        ResourceLocation texture = switch (count) {
            case 2 -> SLOT_2;
            case 3 -> SLOT_3;
            case 4 -> SLOT_4;
            default -> SLOT_1;
        };


        int texW = 22 + (count - 1) * 20;
        int texH = 22;

        int drawX = startX + (!vertical ? offsetIdx * 20 : 0);
        int drawY = startY + (vertical ? offsetIdx * 20 : 0);

        if (vertical) {
            context.pose().pushMatrix();
            context.pose().translate((float)drawX + 11.0f, (float)drawY + 11.0f);
            context.pose().rotate((float) Math.toRadians(90));
            context.pose().translate(-11.0f, -11.0f);
            context.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0, 0, texW, texH, texW, texH);
            context.pose().popMatrix();
        } else {
            context.blit(RenderPipelines.GUI_TEXTURED, texture, drawX, drawY, 0, 0, texW, texH, texW, texH);
        }


        for (int i = 0; i < active.size(); i++) {
            int idx = offsetIdx + i;
            drawBoxAndArmor(context, active.get(i), startX, startY, 22, -2, idx, vertical, offNumX, offNumY);
        }
    }

    // ================= DRAW =================

    private void drawBoxAndArmor(
            GuiGraphics context,
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

            if (!config.isUseHotbarTexture()) {
                ResourceLocation texture = config.isDarkMode() ? HOTBAR_TEXTURE_DARK : HOTBAR_TEXTURE;
                context.blit(RenderPipelines.GUI_TEXTURED, texture, boxX, boxY, 0, 0, boxSize, boxSize, boxSize, boxSize);
            }
        }



        context.renderItem(armorItem, boxX + 3, boxY + 3);


        if (config.isShowDurabilityBar()) {

            drawDurabilityBar(context, boxX + 3, boxY + 16, 16, armorItem);
        }


        int mode = config.getDurabilityDisplayMode();
        if (mode == 1) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, false);
        } else if (mode == 2) {
            drawDurabilityTextSimple(context, boxX, boxY, boxSize, armorItem, offsetXNumber, offsetYNumber, true);
        }
    }

    // ================= DURABILITY =================

    public void drawDurabilityBar(GuiGraphics context, int x, int y, int width, ItemStack item) {
        int maxDamage = item.getMaxDamage();
        if (maxDamage <= 0) return;

        int damage = item.getDamageValue();


        if (damage <= 0) return;

        float ratio = (maxDamage - damage) / (float) maxDamage;
        int barWidth = 13;
        int barX = x + (width - barWidth) / 2 + 1;


        fill(context, barX, y, barX + barWidth, y + 2, 0xFF000000);


        fill(context, barX, y,
                barX + Math.round(ratio * barWidth),
                y + 1,
                convertHSVtoARGB((ratio / 3f) * 360, 1, 1));
    }


    public void drawDurabilityTextSimple(
            GuiGraphics context,
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

        int durability = maxDamage - item.getDamageValue();
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

        var tr = Minecraft.getInstance().font;
        int textX = boxX + (boxSize - tr.width(text)) / 2 + offsetX;
        int textY = boxY + boxSize - tr.lineHeight + offsetY;

        context.drawString(tr, text, textX, textY, color);
    }

    private void fill(GuiGraphics context, int x1, int y1, int x2, int y2, int color) {
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
