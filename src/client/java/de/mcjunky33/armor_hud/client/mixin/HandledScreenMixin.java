package de.mcjunky33.armor_hud.client.mixin;

import de.mcjunky33.armor_hud.client.ArmorHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow protected int x; // Startpunkt des Inventars (links)
    @Shadow protected int y; // Startpunkt des Inventars (oben)

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Slot focusedSlot = ((HandledScreenAccessor) this).getFocusedSlot();

        if (focusedSlot != null && focusedSlot.hasStack()) {
            if (focusedSlot.getStack().isDamageable()) {
                // Wir übergeben die Shadow-Variablen x und y
                new ArmorHudOverlay().renderInventoryDurability(context, focusedSlot, this.x, this.y);
            }
        }
    }
}