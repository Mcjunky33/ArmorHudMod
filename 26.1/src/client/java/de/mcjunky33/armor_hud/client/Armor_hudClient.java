package de.mcjunky33.armor_hud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import org.lwjgl.glfw.GLFW;
import de.mcjunky33.armor_hud.client.config.ModMenuIntegration;
import de.mcjunky33.armor_hud.client.config.ArmorHudConfig;


public class Armor_hudClient implements ClientModInitializer {

    public static KeyMapping keyBindOpenConfig;
    public static KeyMapping keyBindToggleDurabilityMode;
    public static KeyMapping keyBindToggleHandDurability;
    net.minecraft.resources.Identifier armorhudid = net.minecraft.resources.Identifier.fromNamespaceAndPath("armor_hud", "armor_overlay");

    @Override
    public void onInitializeClient() {
        Category armorHudCategory = Category.register(Identifier.fromNamespaceAndPath("armor_hud", "armor_hud"));
        HudElementRegistry.addLast(armorhudid, new ArmorHudOverlay());



        keyBindOpenConfig = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.armorhud.open_config",
                GLFW.GLFW_KEY_F10,
                armorHudCategory
        ));

        keyBindToggleDurabilityMode = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.armorhud.toggle_durability_mode",
                GLFW.GLFW_KEY_UNKNOWN,
                armorHudCategory
        ));

        keyBindToggleHandDurability = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.armorhud.toggle_hand_durability",
                GLFW.GLFW_KEY_UNKNOWN,
                armorHudCategory
        ));

        net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            de.mcjunky33.armor_hud.client.ArmorHudOverlay.appendTooltipDurability(stack, lines);
        });

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {


            if (keyBindOpenConfig.consumeClick()) {

                client.setScreen(new ModMenuIntegration.SimpleConfigScreen(client.screen));
            }

            if (keyBindToggleDurabilityMode.consumeClick()) {
                ArmorHudConfig config = ArmorHudConfig.getInstance();
                int newMode = (config.getDurabilityDisplayMode() + 1) % 3;
                config.setDurabilityDisplayMode(newMode);
            }

            if (keyBindToggleHandDurability.consumeClick()) {
                ArmorHudConfig config = ArmorHudConfig.getInstance();
                boolean newState = !config.isShowHandDurability();
                config.setShowHandDurability(newState);
            }
        });
    }
}