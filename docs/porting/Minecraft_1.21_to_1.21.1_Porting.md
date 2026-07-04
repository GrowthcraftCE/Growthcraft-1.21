
# Minecraft 1.21 → 1.21.1 Porting Summary (NeoForge Primer)

This document summarizes the key vanilla API and behavioral differences between
**Minecraft 1.21 and Minecraft 1.21.1**, based on the NeoForge migration primer.
This focuses on developer-impacting changes rather than gameplay changelog content.

---

## Overview
This is a relatively small update. It introduces a handful of additions
and removes one previously available API surface related to entity selector parsing.

---

## Additions

### `EntitySelectorParser#allowSelectors`
Indicates whether entity selector providers are permitted, usually meaning
whether creative-mode level permissions are available. Useful for mods
that inspect or manipulate selector behavior.

### `BlockEntity#isValidBlockState`
Returns whether the current block state is valid for a block entity instance.
Useful for mods that validate block entity state transitions or integrity.

---

## Removals

### `EntitySelectorParser(StringReader)` constructor removed
The raw constructor taking a `StringReader` has been removed.
Mods relying on it must migrate to supported selector creation APIs.

---

## Suggested Migration Steps

- [ ] If your mod parses entity selectors:
      - Stop constructing `EntitySelectorParser` directly
      - Use approved parsing entry points
      - Leverage `allowSelectors` where relevant

- [ ] If your mod deals with block entity validation:
      - Adopt `isValidBlockState` for correctness checks

---

## Notes for NeoForge Mods
These changes originate at the vanilla layer. Ensure loader-specific expectations
are still met, especially if interacting with NeoForge command or entity systems.

---

## Growthcraft Context
Most Growthcraft modules likely unaffected unless:
- Performing selector parsing
- Validating block entity state logic

Still recommended to verify compilation and run-time sanity.

---

This file is part of the Minecraft 1.20.x → 1.21.x internal developer migration guidance.
