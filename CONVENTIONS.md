# AI Conventions for Growthcraft (NeoForge 1.21.1)

This document defines how AI tools (aider, OpenWebUI, etc.) should interpret
this repository. It is authoritative when there is any ambiguity.

---

## 1. Version Semantics

**Single source of truth:** `gradle.properties`

- `minecraft_version`  
  The version of the Minecraft **game platform** that Growthcraft targets.  
  Example: `1.21.1`

- `minecraft_version_short`  
  A short form of the game version for use in paths or artifact naming.  
  Example: `1.21`

- `minecraft_version_range`  
  The version range Growthcraft is compatible with for NeoForge loader constraints.  
  Example: `[1.21.1]`

- `neo_version`  
  The NeoForge version that must be compatible with `minecraft_version`.  
  Example: `21.1.209`

- `loader_version_range`  
  The FML/NeoForge loader version range.  
  Example: `[1,)`

- `mod_id`  
  The unique, lowercase identifier for this mod.  
  Example: `growthcraft`

- `mod_name`  
  The human-readable display name for the mod.  
  Example: `Growthcraft`

- `mod_license`  
  License string for the mod.  
  Example: `All Rights Reserved`

- `mod_version`  
  The **version of the Growthcraft mod artifact itself**, following semver where possible.  
  Example: `1.21.1.2`

**Important AI rule:**  
When asked any of the following:

- "What is the version of our Minecraft mod?"
- "What version is Growthcraft?"
- "What version are we on?"
- Any similar question about "the mod version"

→ **Always answer using `mod_version` from `gradle.properties`, NOT `minecraft_version`.**

When asked:

- "What Minecraft version do we target?"
- "What game version is this built for?"

→ Use `minecraft_version`.

Gradle is configured so that:

- `version` (the Gradle project version) = `mod_version`
- `group` = `mod_group_id` (or equivalent group property)

The NeoForge mods descriptor (`META-INF/neoforge.mods.toml` or similar) uses:

- `modId = ${mod_id}`
- `version = ${mod_version}`
- `displayName = ${mod_name}`
- `license = ${mod_license}`
- Loader constraints from `loader_version_range` and `minecraft_version_range`.

---

## 2. Project Type and Targets

- This is a **NeoForge** mod, not Forge or Fabric.
- Primary target:
  - **Minecraft:** `minecraft_version` from `gradle.properties`
  - **NeoForge:** `neo_version` from `gradle.properties`
- Java toolchain version should follow the NeoForge + MC 1.21.x recommendations.

Whenever an AI proposes changes that assume Forge or Fabric APIs, it should:

1. Prefer NeoForge equivalents.
2. If uncertain, explicitly flag the uncertainty instead of silently guessing.

---

## 3. Source Layout and Naming

General assumptions for AI:

- Java package base: `net.alatyami.growthcraft` (or equivalent configured group).
- Main mod class:
  - Annotated with `@Mod(modid)` where `modid` matches `mod_id` from `gradle.properties`.
- Registry classes:
  - Follow a pattern such as `GrowthcraftBlocks`, `GrowthcraftItems`, etc., or
    a modular registry naming. When editing, preserve the existing naming scheme.

When adding new registries or content:

- Keep naming consistent with existing registry classes.
- Avoid introducing new patterns unless explicitly requested.

---

## 4. Porting Context: 1.20.x → 1.21.x

This repository represents the **1.21.1 NeoForge** version of Growthcraft.
The legacy branch for reference is the **1.20.x** version (Forge or earlier
NeoForge, depending on the branch).

For AI-assisted porting work:

- Treat 1.20.x code as the **source** branch.
- Treat this 1.21.1 NeoForge branch as the **target** branch.
- Assume that **API differences** between 1.20 and 1.21, and Forge → NeoForge
  differences, may require:
  - Registry initialization changes
  - Event bus / lifecycle changes
  - Data generator API updates
  - Potential file path/layout adjustments

When transforming old code:

1. Preserve behavior first (gameplay parity).
2. Then align with current NeoForge best practices present in this repo.
3. Do not change `mod_id` or core namespace unless explicitly instructed.

---

## 5. AI Editing Rules

When making edits:

1. **Do not change `mod_id`.**
2. **Do not hard-code the mod version** anywhere; always rely on `mod_version`
   and related properties from `gradle.properties` and mods.toml.
3. If creating new features, configuration, or data files:
   - Reuse and follow existing patterns in this repo.
   - If no clear pattern exists, prefer small, minimal changes and leave
     a comment describing intent.

When in doubt about semantic intent (e.g., what a block/item is supposed to do):

- Prefer asking for clarification or surfacing options,
  instead of guessing and silently changing behavior.

---

## 6. Questions to Prefer Asking the User

AI tools should **ask or flag uncertainty** instead of silently guessing when:

- Changing worldgen, structure placement, or loot behavior.
- Removing or replacing APIs that are no longer available in 1.21.1.
- Introducing new configuration options that may affect save compatibility.
