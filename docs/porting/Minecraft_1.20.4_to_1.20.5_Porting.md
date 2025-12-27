
# Minecraft 1.20.4 → 1.20.5 Porting Summary (NeoForge Primer)

This document summarizes the key changes relevant to mod developers migrating from **Minecraft 1.20.4 to Minecraft 1.20.5**, based on the NeoForge Primer documentation.

---

## Overview
Minecraft 1.20.5 introduces structural internal changes and new APIs which mods must adapt to. These changes are mostly **vanilla behavioral and structural**, not loader‑specific, but they directly affect NeoForge mod compatibility.

---

## Java Requirement
Minecraft now requires **Java 21**.
- Ensure Gradle toolchain targets Java 21
- Update development environment accordingly

---

## Data Components
Minecraft introduces **Data Components** as a replacement for some direct NBT usage:
- Many APIs now use `DataComponentMap` and `DataComponents`
- `DataComponentHolder` replaces some legacy NBT interactions
- Direct `CompoundTag` manipulation may break

### Action:
Review all NBT handling in your mod and migrate to component APIs when necessary.

---

## Networking / Codec Changes
Custom networking shifts toward **codec‑driven serialization**:
- Use `StreamCodec` where applicable
- Reduce reliance on raw `FriendlyByteBuf` read/write sequences

### Action:
Refactor custom packet logic to use codec‑style serialization where required.

---

## Registry / Holder Changes
In many areas, Minecraft now prefers working with **Holders** rather than raw registry objects.

### Action:
Replace direct object usage (Item, Block, etc.) with:
- `Holder<T>`
- Proper registry APIs

---

## ItemStack / NBT Changes
Some APIs:
- No longer accept raw `CompoundTag`
- Expect structured component access instead

### Action:
Review ItemStack storage, migration logic, and persistent custom data usage.

---

## General Behavioral & Method Changes
The 1.20.5 update introduces many internal refactors affecting:
- Block methods & behavior visibility
- GUI / client‑side rendering internals
- Entity logic adjustments
- Loot & advancements handling
- Recipe/data provider pipelines

### Action:
Expect refactoring effort — compare API surfaces and validate overrides carefully.

---

## Impact on Growthcraft
For the Growthcraft project:
- Ensure Java 21 compatibility
- Review networking code
- Audit registry access
- Modernize NBT usage into DataComponents
- Validate gameplay parity after porting

---

## Suggested Documentation Entry Example

### 1.20.4 → 1.20.5 Migration Notes

**Java**
- Requires Java 21

**Data Components**
- Replace NBT manipulation with `DataComponents`

**Networking**
- Prefer `StreamCodec` to manual byte buffer IO

**Registry**
- Move toward `Holder<T>` registry patterns

**API / Method Changes**
- Review block/client/entity/loot overrides and behavior

---

This document is intended as a developer reference for assisting the porting process. Further NeoForge API‑specific changes may also apply outside vanilla changes.
