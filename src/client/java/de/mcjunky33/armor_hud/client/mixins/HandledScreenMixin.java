package de.mcjunky33.armor_hud.client.mixins;

import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;
import de.mcjunky33.armor_hud.client.mixins.accessor.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void armorHud$renderDurabilityOnHover(DrawContext context, Slot slot, CallbackInfo ci) {
        ArmorHudConfig config = ArmorHudConfig.getInstance();
        if (!config.isShowHandDurability()) return;

        ItemStack stack = slot.getStack();
        if (stack.isEmpty() || stack.getMaxDamage() <= 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;
        HandledScreenAccessor acc = (HandledScreenAccessor) screen;

        // ----------------------
        // Mausposition im GUI
        // ----------------------
        double mouseX = client.mouse.getX() * client.getWindow().getScaledWidth() / client.getWindow().getWidth();
        double mouseY = client.mouse.getY() * client.getWindow().getScaledHeight() / client.getWindow().getHeight();

        int guiLeft = acc.armorHud$getX();
        int guiTop = acc.armorHud$getY();

        // ----------------------
        // Hover prüfen
        // ----------------------
        if (mouseX < slot.x + guiLeft || mouseX >= slot.x + guiLeft + 16 ||
                mouseY < slot.y + guiTop || mouseY >= slot.y + guiTop + 16) return;

        // ----------------------
        // Durability rendern
        // ----------------------
        drawDurabilityText(context, slot.x, slot.y, stack, config);
    }

    private void drawDurabilityText(DrawContext context, int slotX, int slotY, ItemStack item, ArmorHudConfig config) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        int max = item.getMaxDamage();
        int current = max - item.getDamage();
        float ratio = current / (float) max;

        String text = config.getDurabilityDisplayMode() == 2
                ? Math.round(ratio * 100) + "%"
                : String.valueOf(current);

        int color =
                ratio > 0.7f ? 0xFF00FF00 :
                        ratio > 0.4f ? 0xFFFFFF00 :
                                ratio > 0.2f ? 0xFFFFA500 :
                                        0xFFFF0000;

        // ----------------------
        // Direkt über dem Slot zeichnen
        // ----------------------
        int textX = slotX + 8 - tr.getWidth(text) / 2;
        int textY = slotY - 2;

        context.drawTextWithShadow(tr, text, textX, textY, color);
    }
}
