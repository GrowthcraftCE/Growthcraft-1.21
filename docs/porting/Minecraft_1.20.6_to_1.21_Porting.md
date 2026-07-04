
# Minecraft 1.20.6 → 1.21 Porting Summary (NeoForge Primer)

This document summarizes key vanilla changes relevant for mod developers migrating
from **Minecraft 1.20.6 to Minecraft 1.21**, based on the NeoForge migration primer.
This focuses on meaningful API and behavioral shifts impacting mods rather than
end‑user changelog features.

---

## Overview
Minecraft 1.21 removes multiple experimental flags, finalizes related features, and
introduces important API structural changes. Most notably, **ResourceLocation** no
longer supports public constructors and is now created through static factories.

---

## Experimental Features Finalization

Experimental features previously behind the `update_1_21` flag have now been either:

- Fully implemented, or
- Removed entirely

Removed features can be retrieved programmatically via:

```
WorldData#getRemovedFeatureFlags
```

If your mod depended on experimental feature gating logic, review assumptions carefully.

---

## ResourceLocation API Changes

### Public constructors are now **private**

Old (no longer valid):
```
new ResourceLocation("example:my_id")
new ResourceLocation("example", "my_id")
```

### New creation methods:

```
ResourceLocation.parse(String)
ResourceLocation.fromNamespaceAndPath(String namespace, String path)
ResourceLocation.withDefaultNamespace(String path)
```

### Safer Variants
Some APIs expose `tryParse` / `tryBuild`, which return `null` instead of throwing
on invalid identifiers. Use these when dealing with potentially invalid input.

#### Motivation
- input validation
- clearer semantics
- crash protection
- future compatibility

---

## Impact to Mods

### Required Refactor Areas
- [ ] Any direct use of `new ResourceLocation(...)`
- [ ] Registry lookups
- [ ] Network packet identifiers
- [ ] Loot tables & JSON references
- [ ] Any stored or dynamically constructed identifiers

Failure to update these will result in compile failures.

---

## Migration Examples

### Before (1.20.6)
```java
ResourceLocation id = new ResourceLocation("growthcraft:barrel");
```

### After (1.21)
```java
ResourceLocation id = ResourceLocation.parse("growthcraft:barrel");
```

---

## Behavior Stability
Aside from constructor removal, no major behavioral breaking changes are highlighted
in the vanilla primer affecting mod mechanics directly, but downstream API effects
(in loaders such as NeoForge) should also be considered when applying changes.

---

## Growthcraft Notes
For Growthcraft, ensure:
- Registry systems are updated
- Configuration / data identifiers use new factories
- Networking packet IDs are modernized
- Any text‑constructed identifiers use `tryParse` when appropriate

---

This file is intended for internal developer guidance as part of the Growthcraft
1.20.x → 1.21.x migration documentation set.
