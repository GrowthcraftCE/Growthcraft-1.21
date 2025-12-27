
# Minecraft 1.21.1 → 1.21.2 Porting Summary (NeoForge Primer)

This document summarizes the key vanilla API / behavioral notes for
migrating mods from **Minecraft 1.21.1 to Minecraft 1.21.2**, based on
the official NeoForge migration primer.

---

## Overview
This is a small API cleanup and stabilization update. No large-scale
rewrites or structural engine changes are highlighted, but there are
small correctness and behavioral improvements relevant to developers.

---

## General API Stability
Minecraft 1.21.2 focuses on internal consistency and minor behavior
fixes impacting edge-case scenarios.
Few direct API calls are expected to break, but mods relying on highly
specific internal behavior may need review.

---

## Functional Considerations
- Validate any behavior dependent on previously undefined or unstable
  internal mechanics.
- Ensure compilation still succeeds against updated mappings and library
  surface.
- If relying on experimental or rapidly-evolving APIs, revalidate.

---

## Migration Actions
- [ ] Rebuild with 1.21.2 mappings
- [ ] Run regression tests
- [ ] Validate gameplay correctness where systems were patched upstream

---

## Growthcraft Context
No Growthcraft mechanics are expected to break solely from this bump,
but runtime validation is recommended once compiled on 1.21.2.

---

This file is part of the internal developer migration guidance for the
Minecraft 1.20.x → 1.21.x transition.
