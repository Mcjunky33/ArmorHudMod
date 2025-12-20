package de.mcjunky33.armor_hud.client.mixins.accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("x")
    int armorHud$getX();

    @Accessor("y")
    int armorHud$getY();
}
