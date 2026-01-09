package de.mcjunky33.armor_hud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import de.mcjunky33.armor_hud.client.config.ModMenuIntegration;
import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;

public class Armor_hudClient implements ClientModInitializer {
    public static KeyBinding keyBindOpenConfig;
    public static KeyBinding keyBindToggleDurabilityMode;
    public static KeyBinding keyBindToggleHandDurability;


    @Override
    public void onInitializeClient() {
        // Richtige Methode ab 1.21.9!
        Category armorHudCategory = Category.create(Identifier.of("armor_hud", "armor_hud"));

        keyBindOpenConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armorhud.open_config",
                GLFW.GLFW_KEY_F10,
                armorHudCategory
        ));

        keyBindToggleDurabilityMode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armorhud.toggle_durability_mode",
                GLFW.GLFW_KEY_UNKNOWN,
                armorHudCategory
        ));

        keyBindToggleHandDurability = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armorhud.toggle_hand_durability",
                GLFW.GLFW_KEY_UNKNOWN, // frei wählbar
                armorHudCategory
        ));

        // In Armor_hudClient.java
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            new ArmorHudOverlay().renderArmorUI(context, -1, -1);
        });

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBindOpenConfig.wasPressed()) {
                client.setScreen(new ModMenuIntegration.SimpleConfigScreen(client.currentScreen));
            }

            if (keyBindToggleDurabilityMode.wasPressed()) {
                ArmorHudConfig config = ArmorHudConfig.getInstance();
                int newMode = (config.getDurabilityDisplayMode() + 1) % 3;
                config.setDurabilityDisplayMode(newMode);
            }

            if (keyBindToggleHandDurability.wasPressed()) {
                ArmorHudConfig config = ArmorHudConfig.getInstance();
                boolean newState = !config.isShowHandDurability();
                config.setShowHandDurability(newState);
            }

        });
    }
}