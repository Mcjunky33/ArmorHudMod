# Changelog

## [0.10-26.2] – Port to Minecraft 26.2

### English

- **Build:** Bumped Fabric Loader 0.18.5 → 0.19.3, Fabric Loom 1.15-SNAPSHOT → 1.17.11, Gradle 9.2.1 → 9.5.1, `minecraft` dependency 26.1 → 26.2, `fabric.mod.json` minecraft/fabricloader dependency ranges updated to match.
- **API Fix:** `Minecraft.setScreen(...)` and the `Minecraft.screen` field were removed in 26.2 in favor of `Minecraft.gui.setScreen(...)` / `Minecraft.gui.screen()`. Updated all call sites in `Armor_hudClient.java` and `ModMenuIntegration.java`.
- **API Fix:** `Options.hideGui` was removed entirely in 26.2 (the F1 hide-GUI state moved to a private `GuiRenderState.isHudHidden` field not exposed through `GuiGraphicsExtractor`/`Options`). The explicit check in `ArmorHudOverlay.renderArmorUI` was dropped on the assumption that Fabric's `HudElementRegistry` now gates registered HUD elements centrally when the GUI is hidden, mirroring vanilla render layers. **Needs in-game confirmation that F1 still hides this HUD.**
- **Note:** Builds clean (`./gradlew build`) and produces a runnable jar. Not yet launch-tested in a real world.

---

### Deutsch

- **Build:** Fabric Loader 0.18.5 → 0.19.3, Fabric Loom 1.15-SNAPSHOT → 1.17.11, Gradle 9.2.1 → 9.5.1, `minecraft`-Abhängigkeit 26.1 → 26.2, Abhängigkeitsbereiche in `fabric.mod.json` entsprechend angepasst.
- **API-Fix:** `Minecraft.setScreen(...)` und das Feld `Minecraft.screen` wurden in 26.2 entfernt, stattdessen `Minecraft.gui.setScreen(...)` / `Minecraft.gui.screen()`. Alle Aufrufstellen in `Armor_hudClient.java` und `ModMenuIntegration.java` angepasst.
- **API-Fix:** `Options.hideGui` wurde in 26.2 komplett entfernt (der F1-Status liegt jetzt in einem privaten Feld `GuiRenderState.isHudHidden`, das über `GuiGraphicsExtractor`/`Options` nicht mehr zugänglich ist). Die explizite Prüfung in `ArmorHudOverlay.renderArmorUI` wurde entfernt, in der Annahme, dass Fabrics `HudElementRegistry` registrierte HUD-Elemente nun zentral ausblendet, wenn die GUI versteckt ist. **Muss im Spiel noch geprüft werden, ob F1 dieses HUD weiterhin ausblendet.**
- **Hinweis:** Baut fehlerfrei (`./gradlew build`) und erzeugt eine lauffähige Jar. Noch nicht in einer echten Welt getestet.

---

## [0.9] – Final Port to 26.1 and 1.21.9- 1.21.11 compatiblity with new features.

### English

- **Info:**
  Starting with version 26.1, the preview in the config file is only loaded in one world or server. Reason: Otherwise it crashes, I don't know why.
- **Changes:** Durability in the inventory is now displayed via a tooltip in the inventory and no longer in the slot when active.
- **Bug Fixes:** The Durability text color will no longer be black.


---

### Deutsch

- **Info:** Ab der Version 26.1 wird die Preview in der Config nur noch in einer Welt oder Server geladen. Grund: Es crashed sonst weis nicht warum.
- **Changes:** Die Durability im inventar wird nun per Tooltip im inv angezeigt und nicht mehr im slot wenn aktiv.
- **Bugfixes:** Die Durability text farbe wird nicht mehr schwarz.

---
