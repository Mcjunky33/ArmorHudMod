package de.mcjunky33.armor_hud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import de.mcjunky33.armor_hud.client.config.ModMenuIntegration; // <--- Wichtig für den Zugriff auf die innere Klasse

public class Armor_hudClient implements ClientModInitializer {
    // Die KeyBind-Variable für das Config-Menü
    public static KeyBinding keyBindOpenConfig;

    @Override
    public void onInitializeClient() {
        // Registriere die Keybind (F10, Armor HUD Kategorie)
        keyBindOpenConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.armorhud.open_config",      // Übersetzungs-Key für Sprachdatei
                GLFW.GLFW_KEY_F10,               // Standardtaste: F10
                "key.categories.armorhud"        // Kategorie im Controls-Menü
        ));

        // HUD-Rendering wie gehabt
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            new ArmorHudOverlay().renderArmorUI(context);
        });

        // Tick-Event zum Öffnen des Config-Menüs
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBindOpenConfig.wasPressed()) {
                // Nutze jetzt die innere Klasse von ModMenuIntegration!
                client.setScreen(new ModMenuIntegration.SimpleConfigScreen(client.currentScreen));
            }
        });
    }
}