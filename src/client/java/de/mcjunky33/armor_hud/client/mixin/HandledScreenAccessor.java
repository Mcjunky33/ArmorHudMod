package de.mcjunky33.armor_hud.client.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("focusedSlot")
    Slot getFocusedSlot();

    // Ein Invoker erlaubt uns, die private Methode 'getSlotAt' aufzurufen
    @Invoker("getSlotAt")
    Slot invokeGetSlotAt(double x, double y);
}