# Growthcraft Versioning

## Current Targets

- **Minecraft platform version:** `1.21.1` (`minecraft_version` in `gradle.properties`)
- **NeoForge version:** `21.1.209` (`neo_version` in `gradle.properties`)
- **Growthcraft mod version:** `1.21.1.6` (`mod_version` in `gradle.properties`)

## Semantics

- `minecraft_version`  
  The version of the Minecraft **game** that Growthcraft is built for.

- `mod_version`  
  The version of the **Growthcraft mod artifact**. This is what Gradle and NeoForge use as the mod’s version.

- `version` (Gradle project version)  
  In `build.gradle` / `build.gradle.kts`, this is set from `mod_version`, e.g.:

  ```text
  version = project.mod_version
  ```
