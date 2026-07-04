
# Minecraft 1.20.5 → 1.20.6 Porting Summary (NeoForge Primer)

This document summarizes the key vanilla changes relevant to mod developers migrating from **Minecraft 1.20.5 to Minecraft 1.20.6**, based on the NeoForge Primer documentation.

---

## Overview
This update is comparatively minor. It primarily introduces utility improvements rather than major behavioral rewrites. Mods compatible with **1.20.5** should largely work in **1.20.6** with little or no modification unless they interact with specific areas touched by this update.

---

## Notable Additions

### `BlockEntity#parseCustomNameSafe`
A new method has been added to `net.minecraft.world.level.block.entity.BlockEntity`:

```
parseCustomNameSafe(String) → @Nullable Component
```

This method:
- Attempts to safely parse a string into a `Component`
- Returns `null` if parsing fails
- Prevents potential crashes due to malformed text input

#### Recommended Usage
If your mod:
- Handles custom block entity names
- Accepts user input
- Parses raw strings into components

Then you may benefit from replacing manual parsing logic with this method to improve stability.

---

## Breaking Changes
No significant breaking removals or deprecations are highlighted for this version jump in the vanilla primer.

---

## Suggested Migration Action Items

- [ ] Review block entity name handling logic
- [ ] Use `parseCustomNameSafe` where appropriate
- [ ] Ensure naming logic gracefully handles `null` values

---

## Notes for NeoForge Mods
The NeoForge primer documentation for this update is focused on vanilla behavior. Loader‑specific engine or lifecycle adjustments are handled in subsequent migration guides (particularly for **1.20.6 → 1.21** transitions).

---

## Growthcraft Considerations
If Growthcraft modules:
- Accept custom names
- Store block‑entity labels
- Sync named block entities across networking

Then evaluate whether adopting this safer parser improves reliability or avoids edge‑case crashes.

---

This file is intended for internal developer use as part of the Minecraft 1.20.x → 1.21.x migration series.
