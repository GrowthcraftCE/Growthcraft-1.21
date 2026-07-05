# Growthcraft Porting Notes

This file is a handoff for the next major Minecraft port. It captures lessons from the Minecraft 1.21.1 / NeoForge
port so the next repo does not have to rediscover the same traps.

## Starting Point

- Treat the in-game Patchouli manual as the source of truth for gameplay documentation. Avoid reviving the old GitHub
  wiki workflow.
- Keep GitHub issues as the work ledger. Commit frequently and include the issue number in each commit message.
- Expect the next port to live in a separate repo folder. Use this repo as the reference implementation, not as a place
  to do broad speculative refactors.
- Start by getting registry, data, recipes, and generated resources loading cleanly before deep gameplay polish.

## Versioning

- `gradle.properties` owns the publish version through `mod_version`.
- Update the README badge and README History section whenever `mod_version` changes.
- The 1.21.1 line uses Minecraft-version-prefixed versions, such as `1.21.1.6`.
- `VERSION.md` exists to explain the distinction between Minecraft platform version, Growthcraft artifact version, and
  Gradle project version.

## NeoForge And Build Setup

- This repo uses the NeoForge ModDev plugin.
- IntelliJ run configs come from `genIntellijRuns`; `createLaunchScripts` is an alias to that task in this repo.
- VS Code run configs come from `genVSCodeRuns`.
- Java target is 21 for Minecraft 1.21.1.
- JEI is compile-only plus local runtime. Patchouli is an implementation dependency and local runtime dependency.
- AppleSkin is local runtime only, useful for confirming food properties in the dev client.

## Resource And Recipe Migration

- Prefer generated data where a provider exists, but inspect generated output for 1.21 schema changes.
- NeoForge conditional recipes use the `neoforge:conditions` wrapper. Legacy `conditions` / `forge:mod_loaded` entries
  are suspicious during a new port.
- Patchouli no longer provides the old `patchouli:shapeless_book_recipe` serializer in this setup. Use a normal
  `minecraft:crafting_shapeless` recipe that returns `patchouli:guide_book` with the `patchouli:book` component.
- Patchouli item strings use current item component syntax. Old inline NBT such as `item{color:-7114959}` can break book
  loading.
- Missing registry ids in Patchouli pages can make the whole book compile as empty. Check `run/logs/latest.log` for the
  exact entry and page number.

## Patchouli Manual Lessons

- Book definition lives under `data/growthcraft/patchouli_books/growthcraft/book.json`.
- Localized content lives under `assets/growthcraft/patchouli_books/growthcraft/en_us` because the book uses
  `use_resource_pack`.
- Keep Patchouli text pages short. Long text on image, spotlight, or recipe pages may render as tiny/pixelated text.
- Split crowded pages into multiple `patchouli:text` pages instead of forcing long captions.
- Image pages should use real 256x256 PNG assets. Missing screenshots render as black/magenta checker pages.
- Use the Developer Notes category for changelog, migration notes, and maintainer/admin content.
- The 1.21.1 manual reset the changelog to `1.21.1.6`, the first production release of this port line.
- Apiary bee boxes are not part of the 1.21.1 gameplay path. Recommend vanilla bee mechanics or Productivity Bees.

## Rendering And Client Patterns

- Shared fluid rendering helpers are worth keeping configurable. The fruit press needed custom bounds, low floor pooling,
  faster expiry, and gravity-aware drip behavior.
- Multi-tank machines need explicit tank-region rendering. Mixing Vat and Pancheon split visible fluid areas by tank.
- Transparent containers, such as the Culture Jar, need rendered fluid sides and bottom, not only the top face.
- Item and block colorization needs both block and item paths. Curds/drained curds exposed transparency bugs when item
  tint layers did not match the expected texture layers.
- Validate in-game visuals early with screenshots. Several issues only became obvious from the dev client.

## Machine And Interaction Patterns

- Bucket interaction must consume the action on both client and server paths to avoid a brief vanilla fluid-place flash.
- Multi-tank bucket draining should prefer output tanks first, then input tanks only when not processing.
- Machines with a useful tank state should have a GUI. Pancheon gained a GUI because it has three tanks and no clear
  external state otherwise.
- When a block entity stores visible inventory or fluid state, confirm both direct interaction and break/drop behavior.

## Content Decisions From 1.21.1

- Butter has a gameplay use through higher-yield cake and cookie recipes.
- Shop sign behavior extends vanilla hanging signs through transformed Growthcraft sign blocks, not a completely
  separate custom sign workflow.
- Rope is a custom item and block. The item needs its own `item.growthcraft.rope_linen` translation in addition to the
  block translation.
- Cheese slices are food items. AppleSkin is useful in local runtime to verify hover food properties.
- Bee box assets and lang entries may exist from older versions even when the blocks/items are not registered.

## Useful Verification Commands

```text
./gradlew.bat compileJava -x createMinecraftArtifacts
./gradlew.bat processResources -x createMinecraftArtifacts
./gradlew.bat runData
git diff --check
gh issue list --state open --limit 20
```

For Patchouli loader problems, inspect:

```text
run/logs/latest.log
```

Look for `Error loading and compiling book`, then follow the entry id and page number.

## Before Release

- Confirm `mod_version`, README badge, README History, and `VERSION.md` all agree.
- Open the in-game manual and spot-check each category for missing textures, raw translation keys, and tiny text.
- Check startup logs for recipe parse errors and Patchouli compile errors.
- Keep release changelog content scoped to the new version line instead of copying old manual changelogs forward.
